package com.hzg.mysussr.weight.dialog

import android.text.TextUtils
import com.example.hzg.mysussr.R
import com.example.hzg.mysussr.base.BaseDialog
import com.example.hzg.mysussr.databinding.DialogConfigAddBinding
import com.example.hzg.mysussr.util.SSRConfigUtil

/**
 * Created by hzg on 2017/8/28.
 *
 */
class ConfigAddDialog() : BaseDialog() {
    companion object {
        fun newInstance(listener: OnClickListener): ConfigAddDialog {
            val dialog = ConfigAddDialog();
            dialog.listener = listener
            return dialog
        }
    }

    var listener: OnClickListener? = null
    override fun getLayoutId(): Int {
        return R.layout.dialog_config_add
    }

    override fun initDialog() {
        setFullScreen(true)
    }

    init {
        with(getDataBinding<DialogConfigAddBinding>()) {
            tvOk.setOnClickListener {
                val configName = etContent.text.toString()
                if (TextUtils.isEmpty(configName))
                    etContent.error = "请输入配置名称"
                else {
                    listener?.ok(configName)
                    dismiss()
                }
            }
            tvSsr.setOnClickListener {
                val configName = etContent.text.toString()
                if (!configName.startsWith("ssr://"))
                    etContent.error = "请输入正确的ssr链接"
                else {
                    val ssr = SSRConfigUtil.getConfigItemFromSSR(configName)
                    if (ssr == null) {
                        etContent.error = "请输入正确的ssr链接"
                    } else {
                        listener?.ssr(ssr)
                        dismiss()
                    }
                }
            }
            tvSussr.setOnClickListener {
                val configName = etContent.text.toString()
                if (!configName.startsWith("sussr://"))
                    etContent.error = "请输入正确的sussr链接"
                else {
                    val sussr = SSRConfigUtil.getConfigItemFromSuSSR(configName)
                    if (sussr.size == 0 || sussr.size != 19) {
                        etContent.error = "请输入正确的sussr链接"
                    } else {
                        listener?.sussr(sussr)
                        dismiss()
                    }
                }
            }
            tvNo.setOnClickListener { dismiss() }
        }

    }


    interface OnClickListener {
        fun ok(name: String)
        fun ssr(ssr: Array<String>)
        fun sussr(sussr: MutableList<String>)
    }
}