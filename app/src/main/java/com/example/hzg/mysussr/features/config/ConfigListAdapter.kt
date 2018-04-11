package com.example.hzg.mysussr.features.config

import android.content.Context
import android.support.v7.widget.RecyclerView
import com.example.hzg.mysussr.R
import com.example.hzg.mysussr.base.BaseAdapter
import com.example.hzg.mysussr.databinding.ItemConfigListBinding

/**
 * Created by hzg on 2018/4/9 17:04
 * mail:1039766856@qq.com
 *Sussr
 */
class ConfigListAdapter(context: Context, mData: MutableList<SimpleConfig>, var selectUid: Int) : BaseAdapter<SimpleConfig>(context, mData) {
    var listener: Listener? = null
    lateinit var recyclerView: RecyclerView
    var selectIndex = 0
    var lastSelectIndex = 0

    override fun getLayoutId(viewType: Int): Int {
        return R.layout.item_config_list
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        this.recyclerView = recyclerView
        super.onAttachedToRecyclerView(recyclerView)

    }

    override fun convert(holder: BaseVH, position: Int, bean: SimpleConfig) {
        with(holder.getDataBinding<ItemConfigListBinding>()) {
            tvName.text = bean.name
            cbSelect.isChecked = selectUid == bean.uid

            if (selectUid == bean.uid) {
                selectIndex = position
            }
            ivDelete.setOnClickListener {
                if (mData.size > 1) {
                    listener?.delete(bean, position)
                }

            }
            cbSelect.setOnCheckedChangeListener({ _, isChecked ->
                if (isChecked) {
                    selectUid = getItem(holder.layoutPosition).uid
                    lastSelectIndex = selectIndex
                    selectIndex = position;
                    if (lastSelectIndex != selectIndex) {
                        recyclerView.post({
                            notifyItemChanged(lastSelectIndex)
                        })
                    }


                }
            })
        }
    }

    fun removeConfig(position: Int) {
        if (selectUid == mData.get(position).uid) {
            selectUid = mData.get(position - 1).uid
        }
        mData.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(0, mData.size)
    }

    interface Listener {
        fun delete(item: SimpleConfig, position: Int)
    }
}