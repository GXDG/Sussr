package com.example.hzg.mysussr;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by hzg on 2018/4/4 09:58
 * mail:1039766856@qq.com
 * Sussr
 */

public abstract class BaseFragment extends Fragment {
    protected Context mContext;
    protected View mView;
    private boolean isCreateView = false;
    protected boolean isFirstLoad = false;
    protected boolean isVisible = false;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(getLayoutId(), container, false);
        }
        ViewGroup viewGroup = (ViewGroup) mView.getParent();
        if (viewGroup != null) {
            viewGroup.removeView(mView);
        }

        return mView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        if (!isCreateView) {
            initView();
            isCreateView = true;
            initData();
            initListeners();
            checkLazyLoadData();
        }

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser) {
            isVisible = true;
            checkLazyLoadData();
        } else {
            isVisible = false;
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    private void checkLazyLoadData() {
        if (!isCreateView || !isFirstLoad || !isVisible) {
            return;
        }
        isFirstLoad = false;
        lazyLoadData();
    }

    protected void lazyLoadData() {

    }


    abstract int getLayoutId();

    protected void initView() {

    }

    protected void initData() {

    }

    protected void initListeners() {

    }
}
