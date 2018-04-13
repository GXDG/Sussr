package com.example.hzg.mysussr.widget

import android.content.ClipboardManager
import android.content.Context
import android.support.design.widget.Snackbar
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.ListPopupWindow
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.example.hzg.mysussr.R
import com.example.hzg.mysussr.base.BaseDialog
import com.example.hzg.mysussr.databinding.DialogConfigListBinding
import com.example.hzg.mysussr.features.config.ConfigListAdapter
import com.example.hzg.mysussr.features.config.SimpleConfig
import com.example.hzg.mysussr.util.SSRConfigUtil
import com.hzg.mysussr.weight.dialog.ConfigAddDialog

/**
 * Created by hzg on 2018/4/9 17:19
 * mail:1039766856@qq.com
 *Sussr
 */
class ConfigListDialog : BaseDialog() {

    companion object {
        fun newInstance(configList: MutableList<SimpleConfig>, selectUid: Int = 0, listener: AddConfigListener): ConfigListDialog {
            val dialog = ConfigListDialog()
            dialog.configList = configList
            dialog.selectUid = selectUid
            dialog.mListener = listener
            return dialog
        }
    }

    lateinit var configList: MutableList<SimpleConfig>
    var mListener: AddConfigListener? = null
    var selectUid: Int = 0
    lateinit var mBinding: DialogConfigListBinding
    override fun getLayoutId(): Int {
        return R.layout.dialog_config_list
    }

    override fun initDialog() {
        setFullScreen(true)
    }

    override fun init() {
        mBinding = getDataBinding()
        with(mBinding) {
            val adapter = ConfigListAdapter(mContext, configList, selectUid)
            adapter.listener = object : ConfigListAdapter.Listener {
                override fun delete(item: SimpleConfig, position: Int) {
                    ConfirmDialog.newInstance("删除配置", "确认删除配置${item.name}?(删除后不可恢复)", object : ConfirmDialog.Listener {
                        override fun confirm(confirm: Boolean) {
                            if (confirm) {
                                mListener?.deleteConfig(item.uid)
                                adapter.removeConfig(position)
//                                adapter.mData.removeAt(position)
//                                adapter.notifyItemRemoved(position)
                            }
                        }

                    }).show(childFragmentManager)
                }

            }
            rvContent.layoutManager = LinearLayoutManager(mContext)
            rvContent.addItemDecoration(DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL))
            rvContent.adapter = adapter
            tvNo.setOnClickListener { dismiss() }
            tvYes.setOnClickListener {
                mListener?.selectConfig(adapter.selectUid)
                dismiss()
            }
            ivAdd.setOnClickListener { openAddMenu(ivAdd) }

        }
    }

    private fun showListMenu(anchor: View, data: MutableList<String>, listener: (position: Int) -> Unit) {
        val listPopupWindow = ListPopupWindow(anchor.context)
        listPopupWindow.width = resources.displayMetrics.widthPixels / 2
        listPopupWindow.height = ViewGroup.LayoutParams.WRAP_CONTENT
        listPopupWindow.isModal = true

        val listPopupAdapter = ArrayAdapter<String>(anchor.context, android.R.layout.simple_list_item_1, data)
        listPopupWindow.setAdapter(listPopupAdapter)
        listPopupWindow.setOnItemClickListener { parent, view, position1, id ->
            listener(position1)
            listPopupWindow.dismiss()
        }
        listPopupWindow.anchorView = anchor
        listPopupWindow.show()
    }

    fun openAddMenu(view: View) {
        showListMenu(view, mutableListOf("从剪贴板导入ssr", "从剪贴板导入sussr", "新建"), {
            when (it) {
                0 -> {
                    val clipboard = context!!.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    val ssr = clipboard.text?.toString()

                    if (ssr != null && ssr.startsWith("ssr://")) {
                        val ssrResult = SSRConfigUtil.getConfigItemFromSSR(ssr)
                        if (ssrResult != null) {
                            mListener?.addConfigSsr(ssrResult)
                            dismiss()
                        } else toastS("ssr链接解析错误")
                    } else toastS("请输入正确的ssr链接")
                }
                1 -> {
                    val clipboard = context!!.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    val sussr = clipboard.text?.toString()
                    if (sussr != null && sussr.startsWith("sussr://")) {
                        val ssrResult = SSRConfigUtil.getConfigItemFromSuSSR(sussr)
                        if (ssrResult != null) {
                            mListener?.addConfigSussr(ssrResult)
                            dismiss()
                        } else toastS("sussr链接解析错误")
                    } else toastS("请输入正确的sussr链接")
                }
                2 -> {
                    showAddDialog()
                }
            }
        })
    }

    fun toastS(message: String) {
        val s = Snackbar.make(mBinding.rvContent, message, Snackbar.LENGTH_SHORT).show()
    }

    fun showAddDialog() {
        val list = ArrayList<String>()
        configList.mapTo(list) { it.name }
        val dialog = ConfigAddDialog.newInstance(list, object : ConfigAddDialog.OnClickListener {
            override fun sussr(sussr: MutableList<String>) {
                mListener?.addConfigSussr(sussr)
                dismiss()
            }

            override fun ssr(ssr: Array<String>) {
                mListener?.addConfigSsr(ssr)
                dismiss()
            }

            override fun ok(name: String) {
                mListener?.addConfigName(name)
                dismiss()
            }
        })

        dialog.show(childFragmentManager)
    }

    interface AddConfigListener {
        fun selectConfig(uid: Int)
        fun deleteConfig(uid: Int)
        fun addConfigSussr(sussr: MutableList<String>)
        fun addConfigSsr(ssr: Array<String>)
        fun addConfigName(name: String)
    }
}