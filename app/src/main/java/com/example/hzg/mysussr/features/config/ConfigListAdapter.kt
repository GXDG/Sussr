package com.example.hzg.mysussr.features.config

import android.content.Context
import com.example.hzg.mysussr.R
import com.example.hzg.mysussr.base.BaseAdapter
import com.example.hzg.mysussr.databinding.ItemConfigListBinding

/**
 * Created by hzg on 2018/4/9 17:04
 * mail:1039766856@qq.com
 *Sussr
 */
class ConfigListAdapter(context: Context, mData: MutableList<SimpleConfig>, var selectUid: Int) : BaseAdapter<SimpleConfig>(context, mData) {
    override fun getLayoutId(viewType: Int): Int {
        return R.layout.item_config_list
    }

    override fun convert(holder: BaseVH, position: Int, bean: SimpleConfig) {
        with(holder.getDataBinding<ItemConfigListBinding>()) {
            tvName.text = bean.name
            cbSelect.isChecked = selectUid == bean.uid
            cbSelect.setOnCheckedChangeListener({ _, isChecked ->
                if (isChecked) {
                    selectUid = getItem(holder.layoutPosition).uid
                    notifyDataSetChanged()
                }
            })
        }
    }
}