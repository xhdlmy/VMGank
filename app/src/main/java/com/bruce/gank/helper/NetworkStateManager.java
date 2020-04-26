/*
 * Copyright 2018-2019 KunMinX
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.bruce.gank.helper;

import android.content.IntentFilter;
import android.net.ConnectivityManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;

import static java.util.Objects.requireNonNull;

/**
 * 监听网络状态变化
 */
public class NetworkStateManager implements DefaultLifecycleObserver {

    private static final NetworkStateManager S_MANAGER = new NetworkStateManager();

    private NetworkStateManager() {
        mNetworkStateReceive = new NetworkStateReceive();
    }

    public static NetworkStateManager getInstance() {
        return S_MANAGER;
    }

    private NetworkStateReceive mNetworkStateReceive;

    @Override
    public void onResume(@NonNull LifecycleOwner owner) {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        if (owner instanceof AppCompatActivity) {
            ((AppCompatActivity) owner).registerReceiver(mNetworkStateReceive, filter);
        }
        if (owner instanceof Fragment) {
            requireNonNull(((Fragment) owner).getActivity())
                    .registerReceiver(mNetworkStateReceive, filter);
        }
    }

    @Override
    public void onPause(@NonNull LifecycleOwner owner) {
        if (owner instanceof AppCompatActivity) {
            ((AppCompatActivity) owner).unregisterReceiver(mNetworkStateReceive);
        }
        if (owner instanceof Fragment) {
            requireNonNull(((Fragment) owner).getActivity())
                    .unregisterReceiver(mNetworkStateReceive);
        }
    }
}
