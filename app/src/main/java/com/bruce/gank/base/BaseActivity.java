package com.bruce.gank.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModelProvider;

import com.bruce.gank.util.ActivityCollector;

/**
 * Created time：2020/4/22
 * Author：Bruce
 * Function Description：View 层
 */
public abstract class BaseActivity<DB extends ViewDataBinding> extends AppCompatActivity {

    protected Context mContext;
    protected BaseActivity mActivity;
    protected String TAG;
    protected Intent mIntent;
    protected Bundle mExtras;
    protected DB mDataBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        mActivity = this;
        TAG = this.getClass().getSimpleName();
        ActivityCollector.getInsance().addActivity(this);
        mIntent = getIntent();
        mExtras = mIntent.getExtras();
        initArgument();
        mDataBinding = DataBindingUtil.setContentView(this, getLayoutId());
        initView();
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.getInsance().removeActivity(this);
        mContext = null;
        mActivity = null;
    }

    protected abstract int getLayoutId();
    protected void initArgument(){};
    protected abstract void initView();
    protected abstract void initData();

    public DB getDataBinding() {
        return mDataBinding;
    }

    public ViewModelProvider getViewModelProvider() {
        // 其实还是类似于单例：ViewModelStoreOwner.getDefaultViewModelProviderFactory()
        return new ViewModelProvider(this);
    }

}
