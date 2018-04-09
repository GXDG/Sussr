package com.example.hzg.mysussr.widget

import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import com.example.hzg.mysussr.R
import com.example.hzg.mysussr.base.BaseDialog
import com.example.hzg.mysussr.databinding.DialogConfigListBinding
import com.example.hzg.mysussr.features.config.ConfigListAdapter
import com.example.hzg.mysussr.features.config.SimpleConfig

/**
 * Created by hzg on 2018/4/9 17:19
 * mail:1039766856@qq.com
 *Sussr
 */
class ConfigListDialog : BaseDialog() {

    companion object {
        fun newInstance(configList: MutableList<SimpleConfig>, selectUid: Int = 0): ConfigListDialog {
            val dialog = ConfigListDialog()
            dialog.configList = configList
            dialog.selectUid = selectUid
            return dialog
        }
    }

    lateinit var configList: MutableList<SimpleConfig>
    var selectUid: Int = 0
    override fun getLayoutId(): Int {
        return R.layout.dialog_config_list
    }

    override fun initDialog() {
        setFullScreen(true)
    }

    override fun init() {
        with(getDataBinding<DialogConfigListBinding>()) {
            val adapter = ConfigListAdapter(mContext, configList, selectUid)
            rvContent.layoutManager = LinearLayoutManager(mContext)
            rvContent.addItemDecoration(DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL))
            rvContent.adapter = adapter
        }
    }

}