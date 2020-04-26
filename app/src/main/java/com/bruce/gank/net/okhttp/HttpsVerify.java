package com.bruce.gank.net.okhttp;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bruce.gank.util.LogUtils;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.SignatureException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateFactory;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

/**
 * Https
 *
 * CA_public_Key CA_private_Key
 * Server_public_Key Server_private_Key
 * Server 找 CA 机构生成自己的证书 把 Server_public_Key与Server的基本信息作为内容 进行 数字签名（将内容进行Hash算法（不可逆）形成摘要，使用 CA_private_Key 非对称加密） 生成数字证书
 * Client 安装根证书 拥有 CA_public_Key
 *
 * Https 传输过程：
 * - Client Request 只是用来验证的
 * - Server Response with 数字证书
 * - Client 验证 CA 服务器数字证书 （CA_public_Key 验证） 验证通过后确认是 Server ，并得到 Server_public_Key  （如果验证未通过，就是中间人劫持了，返回不了真实的 server 证书，Client 会发现访问的可能不是 Server 了）
 * - Client 随机生成一个对称加密串Key+key的摘要 使用 Server_public_Key 加密
 * - Server 使用 Server_private_Key 解密，得到 Key+key的摘要
 * - Client 与 Server 接下来使用 key进行通信 开始Key加密传输通信
 *
 * 本质：就是通过非对称加密来得到对称加密秘钥，但是非对称加密可能也会被中间人劫持，所以使用第三方CA的数字证书。
 * 中间人劫持：如果没有 CA，那么 Client 和 Server 有自己的秘钥对，并公钥互换，那么中间人可以获取到 Client 与 Server 的公钥，然后自己生成一对秘钥对，在中间同时与双方通信，达到劫持的目的。
 *
 * 过程：
 （1） 客户端向服务器端索要并验证公钥。
 （2） 双方协商生成"对话密钥"。
 （3） 双方采用"对话密钥"进行加密通信。
 *
 * 数字证书
 数字证书的格式普遍采用的是X.509V3 国际标准，一个标准的X.509 数字证书包含以下一些内容：
 证书的版本信息；version
 证书的序列号，每个证书都有一个唯一的证书序列号； serial num
 证书所使用的签名算法； sign RSA
 证书的发行机构名称，命名规则一般采用X.500 格式；CA name
 证书的有效期，通用的证书一般采用UTC 时间格式，它的计时范围为1950-2049； certificate valid time
 证书所有人的名称，命名规则一般采用X.500 格式； server name
 证书所有人的公开密钥； server_public_key
 证书发行者对证书的签名。 sign 数字签名

 根证书：就是安装在客户端，用来验证服务端CA证书或者服务端自签名证书（例如12306证书）。
 *
 */
public class HttpsVerify {

    private static final String TAG = HttpsVerify.class.getSimpleName();

    public static class SSLParams {
        public SSLSocketFactory sSSLSocketFactory;
        public X509TrustManager sTrustManager;
    }

    /**
     * @param trustManager X509TrustManager 包含该系统可信任的根证书链
     * @return 根据信任的证书来创建 SSLSocket 工厂
     * @throw 该系统没有任何可信任的证书，也就是不支持TLS协议，不支持 HTTPS
     */
    public static SSLParams getSslSocketFactory(X509TrustManager trustManager){
        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[] { trustManager }, null);
            SSLSocketFactory socketFactory = sslContext.getSocketFactory();
            SSLParams sslParams = new SSLParams();
            sslParams.sSSLSocketFactory = socketFactory;
            sslParams.sTrustManager = trustManager;
            return sslParams;
        } catch (GeneralSecurityException e) {
            throw new AssertionError(); // The system has no TLS. Just give up.
        }
    }

    /**
     * @param certificates 根证书流，可以是本地 assert 读取的文件流，也可以说是根证书 String 流
     * @param bksFile
     * @param password
     * @return
     */
    public static SSLParams getSslSocketFactory(InputStream[] certificates, InputStream bksFile, String password) {
        try {
            KeyManager[] keyManagers = prepareKeyManager(bksFile, password);
            TrustManager[] trustManagers = prepareTrustManager(certificates);
            SSLContext sslContext = SSLContext.getInstance("TLS");
            X509TrustManager trustManager;
            if (trustManagers != null) {
                trustManager = new MyTrustManager(chooseTrustManager(trustManagers));
            } else {
                trustManager = new UnSafeTrustManager();
            }
            sslContext.init(keyManagers, new TrustManager[]{trustManager}, new SecureRandom());
            SSLParams sslParams = new SSLParams();
            sslParams.sSSLSocketFactory = sslContext.getSocketFactory();
            sslParams.sTrustManager = trustManager;
            return sslParams;
        } catch (NoSuchAlgorithmException e) {
            throw new AssertionError(e);
        } catch (KeyManagementException e) {
            throw new AssertionError(e);
        } catch (KeyStoreException e) {
            throw new AssertionError(e);
        }
    }

    private static KeyManager[] prepareKeyManager(@Nullable InputStream bksFile, @Nullable String password) {
        try {
            if (bksFile == null || password == null) return null;

            KeyStore clientKeyStore = KeyStore.getInstance("BKS");
            clientKeyStore.load(bksFile, password.toCharArray());
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(clientKeyStore, password.toCharArray());
            return keyManagerFactory.getKeyManagers();

        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static TrustManager[] prepareTrustManager(@Nullable InputStream... certificates) {
        try {
            if (certificates == null || certificates.length <= 0) return null;

            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null);
            int index = 0;
            for (InputStream certificate : certificates) {
                String certificateAlias = Integer.toString(index++);
                keyStore.setCertificateEntry(certificateAlias, certificateFactory.generateCertificate(certificate));
                if (certificate != null) {
                    certificate.close();
                }
            }
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);
            return trustManagerFactory.getTrustManagers();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static X509TrustManager chooseTrustManager(@NonNull TrustManager[] trustManagers) {
        for (TrustManager trustManager : trustManagers) {
            if (trustManager instanceof X509TrustManager) {
                return (X509TrustManager) trustManager;
            }
        }
        return null;
    }

    /**
     * CA 证书过期，可继续访问 (服务端单向验证)
     */
    public static class PastDueCATrustManager implements X509TrustManager {

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            // Client Certificate Varify
        }

        // 服务端验证
        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            // Server Certificate Varify
            if (chain != null) {
                for (X509Certificate cert : chain) {

                    try {
                        // Make sure that it hasn't expired.
                        cert.checkValidity();
                    } catch (CertificateExpiredException e) {
                        //此异常是证书已经过期异常，在手机调到证书生效时间之后会捕捉到此异常
                        LogUtils.w(TAG, "checkServerTrusted: CertificateExpiredException:" + e.getLocalizedMessage());
                    } catch (CertificateNotYetValidException e) {
                        //此异常是证书未生效异常，在手机调到证书生效时间之前会捕捉到此异常
                        LogUtils.w(TAG, "checkServerTrusted: CertificateNotYetValidException:" + e.getLocalizedMessage());
                    }

                    try {
                        // Verify the certificate's public key chain.
                        cert.verify(cert.getPublicKey());
                    } catch (CertificateExpiredException e) {
                        //此异常是证书已经过期异常，在手机调到证书生效时间之后会捕捉到此异常
                        LogUtils.w(TAG, "checkServerTrusted: CertificateExpiredException:" + e.getLocalizedMessage());
                    } catch (CertificateNotYetValidException e) {
                        //此异常是证书未生效异常，在手机调到证书生效时间之前会捕捉到此异常
                        LogUtils.w(TAG, "checkServerTrusted: CertificateNotYetValidException:" + e.getLocalizedMessage());
                    } catch (CertificateException ex) {
                        //其他异常正常报错
                        LogUtils.w(TAG, "Throw checkClientTrusted: " + ex.getLocalizedMessage());
                        throw ex;
                    } catch (NoSuchAlgorithmException e) {
                        LogUtils.w(TAG, "checkServerTrusted: NoSuchAlgorithmException:" + e.getLocalizedMessage());
                    } catch (InvalidKeyException e) {
                        LogUtils.w(TAG, "checkServerTrusted: InvalidKeyException:" + e.getLocalizedMessage());
                    } catch (NoSuchProviderException e) {
                        LogUtils.w(TAG, "checkServerTrusted: NoSuchProviderException:" + e.getLocalizedMessage());
                    } catch (SignatureException e) {
                        LogUtils.w(TAG, "checkServerTrusted: SignatureException:" + e.getLocalizedMessage());
                    }
                }
            }
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }

    private static class MyTrustManager implements X509TrustManager {

        @Nullable
        private X509TrustManager defaultTrustManager;
        private X509TrustManager localTrustManager;

        public MyTrustManager(X509TrustManager localTrustManager) throws NoSuchAlgorithmException, KeyStoreException {
            TrustManagerFactory var4 = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            var4.init((KeyStore) null);
            defaultTrustManager = chooseTrustManager(var4.getTrustManagers());
            this.localTrustManager = localTrustManager;
        }


        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            try {
                defaultTrustManager.checkServerTrusted(chain, authType);
            } catch (CertificateException ce) {
                localTrustManager.checkServerTrusted(chain, authType);
            }
        }

        @NonNull
        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }

    private static class UnSafeTrustManager implements X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
        }

        @NonNull
        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }

}
