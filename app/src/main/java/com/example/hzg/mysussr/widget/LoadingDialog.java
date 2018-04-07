package com.example.hzg.mysussr.widget;

import android.view.Gravity;

import com.example.hzg.mysussr.R;
import com.example.hzg.mysussr.base.BaseDialog;
import com.example.hzg.mysussr.databinding.DialogLoadingBinding;

/**
 * Created by hzg on 2018/4/6.
 */

public class LoadingDialog extends BaseDialog {
    private DialogLoadingBinding mBinding;
    private String message;

    public static LoadingDialog newInstance(String message, Boolean cancelable) {
        LoadingDialog dialog = new LoadingDialog();
        dialog.setMessage(message);
        dialog.setCancelable(cancelable);
        return dialog;
    }

    @Override
    public int getLayoutId() {
        return R.layout.dialog_loading;
    }

//    @Override
//    protected int getLayoutResId() {
//        return R.layout.dialog_loading;
//    }

    @Override
    protected void initDialog() {
      setGravity(Gravity.CENTER);
        setFullScreen(true);
    }

    @Override
    protected void init() {
        mBinding = getDataBinding();
        mBinding.tvContent.setText(message);
    }

    public void setMessage(String message) {
        this.message = message;
    }


}
