package com.example.hzg.mysussr.widget

import com.example.hzg.mysussr.R
import com.example.hzg.mysussr.base.BaseDialog
import com.example.hzg.mysussr.databinding.DialogEditBinding
import com.example.hzg.mysussr.features.AdapterListener

/**
 * Created by hzg on 2018/4/6.
 *
 */

class ConfigEditDialog : BaseDialog() {

    var message: String? = null
    var header: String? = null
    var listener: AdapterListener.DialogCallback? = null
    var canEdit = true

    override fun getLayoutId(): Int {
        return R.layout.dialog_edit
    }

    override fun initDialog() {
        setFullScreen(true)
    }

    override fun init() {
        with(getDataBinding<DialogEditBinding>()) {
            tvHeader.text = header
            tvContent.setText(message)
            tvContent.isEnabled = canEdit
            tvYes.setOnClickListener {
                listener?.onClickYes(tvContent.text.toString())
                dismiss()
            }
            tvNo.setOnClickListener {
                dismiss()
            }
        }

    }

    companion object {

        fun newInstance(header: String, message: String, listener: AdapterListener.DialogCallback): ConfigEditDialog {
            val dialog = ConfigEditDialog()
            dialog.message = message
            dialog.header = header
            dialog.listener = listener
            return dialog
        }

        fun newInstanceForText(header: String, message: String): ConfigEditDialog {
            val dialog = ConfigEditDialog()
            dialog.message = message
            dialog.header = header
            dialog.canEdit = false
            return dialog
        }
    }


}