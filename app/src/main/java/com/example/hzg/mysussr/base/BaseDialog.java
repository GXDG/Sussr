package com.example.hzg.mysussr.base;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.example.hzg.mysussr.R;


/**
 * Created by DELL on 2018年1月3日 003.
 * E-Mail:n.zjx@163.com
 * PrivyM8
 * BaseDialog: Dialog 基类
 * 可以很好解决其他弹框的问题：例如popupWindow不能使用EditText,Dialog生命周期管理等
 */

public abstract class BaseDialog extends DialogFragment {

    protected Context mContext;
    protected View mView;

    private boolean isFullScreen;//是否全屏
    private int mGravity = Gravity.NO_GRAVITY;//位置
    private int mWidth = WindowManager.LayoutParams.WRAP_CONTENT;//宽比例(0-1]
    private int mTheme = android.R.style.Theme_Holo_Light_Dialog_MinWidth;//dialog样式


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        int layoutId = getLayoutId();
        if (layoutId == 0) {
            return super.onCreateView(inflater, container, savedInstanceState);
        }
        mView = inflater.inflate(getLayoutId(), container, false);
        return mView;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        initDialog();
        if (isFullScreen) {
            mTheme = R.style.dialog_full_screen;
        }
        Dialog dialog = new Dialog(mContext, mTheme);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = dialog.getWindow();
        assert window != null;
        window.setGravity(mGravity);
        return dialog;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (!isFullScreen) {//可设置宽度，高度不设置
            getDialog().getWindow().setLayout(mWidth, WindowManager.LayoutParams.WRAP_CONTENT);
        }
        init();
    }

    protected <B extends ViewDataBinding> B getDataBinding() {
        return DataBindingUtil.bind(mView);
    }

    /**
     * @param theme 默认系统最小宽度dialog
     */
    public void setTheme(int theme) {
        if (theme != 0) {
            this.mTheme = theme;
        }
        setStyle(STYLE_NO_TITLE, theme);
    }

    /**
     * 重写设置layoutId
     *
     * @return 布局id
     */
    @LayoutRes
    public int getLayoutId() {
        return 0;
    }

    /**
     * 设置是否全屏，默认不全屏
     */
    public void setFullScreen(boolean fullScreen) {
        isFullScreen = fullScreen;
    }

    /**
     * 设置位置
     *
     * @param gravity 位置{@link Gravity}
     */
    public void setGravity(int gravity) {
        this.mGravity = gravity;
    }

    /**
     * 设置宽度是否为matchParent
     */
    public void setMatchParentWidth() {
        this.mWidth = WindowManager.LayoutParams.MATCH_PARENT;
    }

    /**
     * tag:this.getClass().getSimpleName()
     */
    public void show(FragmentManager manager) {
        if (!isAdded()) {
            super.show(manager, this.getClass().getSimpleName());
        }
    }

    /**
     * 初始化Dialog的样式,比如设置全屏，位置等.比{@link #init()}先执行
     */
    protected abstract void initDialog();

    /**
     * 初始化，比如初始化view，设置监听，设置数据.
     */
    protected void init() {

    }

    /**
     * 需要在初始化dialog后调用，例如在init()方法里调用
     * Sets dialog background color.
     * reference {@link Color}
     *
     * @param color the color {@link Color} or other color int
     */
    protected void setBackgroundColor(@ColorInt int color) {
        if (getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(color));
        }
    }

}
