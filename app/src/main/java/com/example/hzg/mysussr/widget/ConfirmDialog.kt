package com.example.hzg.mysussr.widget

import com.example.hzg.mysussr.R
import com.example.hzg.mysussr.base.BaseDialog
import com.example.hzg.mysussr.databinding.DialogConfrimBinding

/**
 * Created by hzg on 2018/4/11 14:22
 * mail:1039766856@qq.com
 *Sussr
 */
class ConfirmDialog() : BaseDialog() {
    companion object {
        fun newInstance(header: String, message: String, listener: Listener): ConfirmDialog {
            val dialog = ConfirmDialog()
            dialog.listener = listener
            dialog.header = header
            dialog.message = message
            return dialog
        }
    }

    var header = "确认对话框"
    var message = "确认操作?"
    var listener: Listener? = null
    override fun getLayoutId(): Int {
        return R.layout.dialog_confrim
    }

    override fun initDialog() {
        setFullScreen(true)
    }

    override fun init() {
        with(getDataBinding<DialogConfrimBinding>()) {
            tvHeader.text = header
            tvContent.text = message
            tvNo.setOnClickListener {
                listener?.confirm(false)
                dismiss()
            }
            tvYes.setOnClickListener {
                listener?.confirm(true)
                dismiss()
            }
        }
    }

    interface Listener {
        fun confirm(confirm: Boolean)
    }
}