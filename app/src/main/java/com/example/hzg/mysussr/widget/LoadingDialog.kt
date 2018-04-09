package com.example.hzg.mysussr.widget

import android.view.Gravity

import com.example.hzg.mysussr.R
import com.example.hzg.mysussr.base.BaseDialog
import com.example.hzg.mysussr.databinding.DialogLoadingBinding

/**
 * Created by hzg on 2018/4/6.
 *
 */

class LoadingDialog : BaseDialog() {

    private var message: String? = null

    override fun getLayoutId(): Int {
        return R.layout.dialog_loading
    }

    override fun initDialog() {
        setGravity(Gravity.CENTER)
        setFullScreen(true)
    }

    override fun init() {
        with(getDataBinding<DialogLoadingBinding>()) {
            tvContent.text = message
        }

    }

    fun setMessage(message: String) {
        this.message = message
    }

    companion object {

        fun newInstance(message: String, cancelable: Boolean?): LoadingDialog {
            val dialog = LoadingDialog()
            dialog.setMessage(message)
            dialog.isCancelable = cancelable!!
            return dialog
        }
    }


}
