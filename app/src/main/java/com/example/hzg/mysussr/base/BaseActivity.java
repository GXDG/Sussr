package com.example.hzg.mysussr.base;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by hzg on 2018/4/6.
 */

public abstract class BaseActivity extends AppCompatActivity {
    protected View mView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mView = LayoutInflater.from(this).inflate(getLayoutResId(), null);
        setContentView(mView);
        initView();
        initData();
        initListener();
    }

    protected <T extends ViewDataBinding> T getDataBinding() {
        return DataBindingUtil.bind(mView);
    }

    protected abstract int getLayoutResId();

    protected abstract void initView();

    protected abstract void initData();

    protected abstract void initListener();
}
