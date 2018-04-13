package com.hzg.mysussr.weight.dialog

import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import com.example.hzg.mysussr.R
import com.example.hzg.mysussr.base.BaseDialog
import com.example.hzg.mysussr.util.SSRConfigUtil
import kotlinx.android.synthetic.main.dialog_config_share.*


/**
 * Created by hzg on 2017/8/28.
 * 分享窗口
 */
class ConfigShareDialog : BaseDialog() {
    companion object {
        fun newInstance(list: List<String>): ConfigShareDialog {
            val dialog = ConfigShareDialog()
            dialog.valueList = list
            return dialog
        }
    }

    var valueList: List<String>? = null
    override fun getLayoutId(): Int {
        return R.layout.dialog_config_share
    }

    override fun initDialog() {
        setFullScreen(true)
    }

    override fun init() {

        tv_mysussr.setOnClickListener {
            val result = SSRConfigUtil.shareBySussr(valueList as MutableList<String>)
            println(result)
            et_content.setText(result)
            copyToClipboard(result)
        }
        tv_ssr.setOnClickListener {
            if (valueList != null) {
                val value = Array<String>(8, {
                    when (it) {
                        0 -> valueList!![1]
                        1 -> valueList!![2]
                        2 -> valueList!![5]
                        3 -> valueList!![4]
                        4 -> valueList!![6]
                        5 -> valueList!![3]
                        6 -> valueList!![7]
                        7 -> valueList!![0]
                        else -> ""
                    }

                })
                val ssr = SSRConfigUtil.shareBySSR(value)
                et_content.setText(ssr)
                copyToClipboard(ssr)
            }


        }
        tv_no.setOnClickListener { dismiss() }
    }

    private fun copyToClipboard(message: String) {
        val clipboard = context!!.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboard.text = et_content.text.toString()
        Toast.makeText(context, "已复制到剪贴板", Toast.LENGTH_SHORT).show()
    }


}