package com.example.hzg.mysussr.features

import android.content.Context
import com.example.hzg.mysussr.R
import com.example.hzg.mysussr.base.BaseAdapter
import com.example.hzg.mysussr.databinding.ItemMenuBinding

/**
 * Created by hzg on 2018/4/12 10:54
 * mail:1039766856@qq.com
 *Sussr
 */
class MenuAdapter(context: Context, mData: MutableList<Data>) : BaseAdapter<MenuAdapter.Data>(context, mData) {
    var listener: AdapterListener.ItemOnClickLister<Data>? = null
    override fun getLayoutId(viewType: Int): Int {
        return R.layout.item_menu
    }


    override fun convert(holder: BaseVH, position: Int, bean: Data) {
        with(holder.getDataBinding<ItemMenuBinding>()) {
            tvContent.text = bean.content

        }
        holder.itemView.setOnClickListener {
            listener?.onClick(position, bean)
        }
    }


    class Data(var content: String)
}