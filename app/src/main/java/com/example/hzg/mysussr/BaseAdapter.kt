package com.example.hzg.mysussr

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Created by hzg on 2018/4/4 11:48
 * mail:1039766856@qq.com
 *Sussr
 */
abstract class BaseAdapter<T>(val context: Context, var mData: MutableList<T>) : RecyclerView.Adapter<BaseAdapter.BaseVH>() {

    fun setData(mData: MutableList<T>) {
        this.mData = mData
    }

    fun addData(list: List<T>) {
        if (list.isEmpty()) {
            this.mData.addAll(list)
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseVH {
        val view = LayoutInflater.from(parent.context).inflate(getLayoutId(viewType), parent, false)
        return createVH(view, viewType)
    }

    override fun onBindViewHolder(holder: BaseVH, position: Int) {
        convert(holder, position, mData.get(position))
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    //获取viewTyp类型获取对应的布局id
    protected abstract fun getLayoutId(viewType: Int): Int

    abstract fun createVH(view: View, viewType: Int): BaseVH
    //     布局内容绑定
    protected abstract fun convert(holder: BaseVH, position: Int, bean: T)

    open class BaseVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
    }
}