package com.example.hzg.mysussr.features.config

import android.content.Context
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.ListPopupWindow
import android.text.InputType
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.TextView
import com.example.hzg.mysussr.R
import com.example.hzg.mysussr.base.BaseAdapter
import com.example.hzg.mysussr.databinding.ItemConfigBinding
import com.example.hzg.mysussr.databinding.ItemConfigHeaderBinding
import com.example.hzg.mysussr.databinding.ItemConfigSwitchBinding
import com.hzg.mysussr.weight.dialog.ConfigShareDialog

/**
 * Created by hzg on 2018/4/6.
 *
 */

class ConfigAdapter(context: Context, mData: MutableList<Data>) : BaseAdapter<ConfigAdapter.Data>(context, mData) {

    override fun getItemViewType(position: Int): Int {
        return getItem(position).type
    }

    override fun getLayoutId(viewType: Int): Int {

        when (viewType) {
            Data.TYPE_SWITCH -> return R.layout.item_config_switch
            Data.TYPE_EDIT, Data.TYPE_SELECT -> return R.layout.item_config
            Data.TYPE_HEADER -> return R.layout.item_config_header
            else -> return R.layout.item_config
        }

    }

    override fun convert(holder: BaseAdapter.BaseVH, position: Int, bean: Data) {
        when (holder.itemViewType) {
            Data.TYPE_SWITCH -> covertSwitch(holder, position, bean)
            Data.TYPE_EDIT -> covertEdit(holder, position, bean)
            Data.TYPE_SELECT -> covertSelect(holder, position, bean)
            Data.TYPE_HEADER -> covertHeader(holder, position, bean)
        }
//        val mBinding = holder.getDataBinding<ItemConfigBinding>()
//        mBinding.tvHeader.text = bean.data.key
//        mBinding.tvContent.text = bean.data.value
    }

    private fun covertHeader(holder: BaseAdapter.BaseVH, position: Int, bean: ConfigAdapter.Data) {
        with(holder.getDataBinding<ItemConfigHeaderBinding>()) {
            tvHeader.text = bean.data.value
            ivShare.setOnClickListener { v ->
                if (context is AppCompatActivity) {
                    val list = ArrayList<String>()
                    mData.indices.mapTo(list) { getItem(it).data.value }
                    ConfigShareDialog.newInstance(list)
                            .show(context.supportFragmentManager)
                }
            }
            ivEdit.setOnClickListener {
                showEditDialog(position, bean.data.value)
            }
        }
    }

    private fun covertEdit(holder: BaseAdapter.BaseVH, position: Int, bean: Data) {
        with(holder.getDataBinding<ItemConfigBinding>()) {
            tvHeader.text = bean.data.key
            tvContent.setText(bean.data.value)
            if (!bean.contentHide) {
                tvContent.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            } else {
                tvContent.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
            tvContent.setOnClickListener({
                showEditDialog(position, bean.data.value)
            })
        }
    }

    private fun covertSelect(holder: BaseAdapter.BaseVH, position: Int, bean: Data) {
        with(holder.getDataBinding<ItemConfigBinding>()) {
            tvHeader.text = bean.data.key
            tvContent.setText(bean.data.value)
            tvContent.setOnClickListener({
                showListPopUpWindow(tvContent, position, listableIndexs.indexOf(position))
            })
        }
    }

    private fun covertSwitch(holder: BaseAdapter.BaseVH, position: Int, bean: Data) {
        with(holder.getDataBinding<ItemConfigSwitchBinding>()) {
            tvHeader.text = bean.data.key
            switchContent.setOnCheckedChangeListener(null)
            switchContent.isChecked = bean.data.value.equals("1")
            switchContent.setOnCheckedChangeListener({ v, isChecked ->
                getItem(holder.layoutPosition).data.value = if (isChecked) "1" else "0"
            })
        }
    }

    private fun showEditDialog(position: Int, content: String) {
        val builder = AlertDialog.Builder(context)
        val editText = EditText(context)
        editText.setText(content)
        builder.setTitle(getItem(position).data.key)
                .setView(editText)
                .setPositiveButton("确定", { dialog, which ->
                    dataChange(position, editText.text.toString())
                })
                .setNegativeButton("取消", null)
                .show()
    }

    var listableIndexs: MutableList<Int>
    var listDatas: MutableList<Array<String>>

    init {
        listableIndexs = mutableListOf(4, 5, 6, 9)
        listDatas = mutableListOf(
                context.resources.getStringArray(R.array.value_method),
                context.resources.getStringArray(R.array.value_protocol),
                context.resources.getStringArray(R.array.value_obscure),
                context.resources.getStringArray(R.array.value_dludp)
        )
    }

    private fun showListPopUpWindow(anchor: View, position: Int, index: Int) {
        if (!listableIndexs.contains(position)) return
        val listPopupWindow = ListPopupWindow(anchor.context)
        listPopupWindow.width = ViewGroup.LayoutParams.MATCH_PARENT
        listPopupWindow.height = ViewGroup.LayoutParams.WRAP_CONTENT
        listPopupWindow.isModal = true
        val data = listDatas.get(index)
        val listPopupAdapter = ArrayAdapter<String>(anchor.context, android.R.layout.simple_list_item_1, data)
        listPopupWindow.setAdapter(listPopupAdapter)
        listPopupWindow.setOnItemClickListener { parent, view, position1, id ->
            if (anchor is TextView) {
                if ("手动输入".equals(data[position1])) {
                    showEditDialog(position, "")
                } else {
                    anchor.setText(data[position1])
                    dataChange(position, data[position1])
                    anchor.getRootView().isFocusableInTouchMode = true
                    anchor.getRootView().requestFocus()
                }
            }
            listPopupWindow.dismiss()
        }
        listPopupWindow.anchorView = anchor
        listPopupWindow.show()
    }

    private fun dataChange(position: Int, s: String) {
        mData[position].data.value = s
        notifyItemChanged(position)
    }

    class Data(var data: KeyBean, var type: Int = TYPE_EDIT, var contentHide: Boolean = false) {
        companion object {
            val TYPE_SWITCH = 0x1
            val TYPE_EDIT = 0x2
            val TYPE_SELECT = 0x3
            val TYPE_HEADER = 0x4
        }
    }
}
