package com.example.hzg.mysussr.features.uid


import android.content.Context
import android.support.v4.util.ArraySet
import com.example.hzg.mysussr.R
import com.example.hzg.mysussr.base.BaseAdapter
import com.example.hzg.mysussr.databinding.ItemUidBinding
import com.example.hzg.mysussr.features.AdapterListener
import com.example.hzg.mysussr.features.uid.bean.AppUidBean
import com.example.hzg.mysussr.util.ImageLoader


/**
 * Created by hzg on 2017/8/30.
 *
 */
class UidAdapter(context: Context, data: MutableList<AppUidBean>, val selectList: ArrayList<String>) : BaseAdapter<AppUidBean>(context, data) {
    var checkedSet: ArraySet<Int> = ArraySet()

    init {
        data.indices
                .filter { selectList.contains(data.get(it).uid) }
                .forEach { checkedSet.add(it) }
    }


    var listener: AdapterListener.StringCallback? = null

    override fun getLayoutId(viewType: Int): Int {
        return R.layout.item_uid
    }

    override fun convert(holder: BaseAdapter.BaseVH, position: Int, bean: AppUidBean) {
        with(holder.getDataBinding<ItemUidBinding>())
        {
            checkbox.setOnCheckedChangeListener(null)
            checkbox.isChecked = checkedSet.contains(position)
            checkbox.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    checkedSet.add(position)
                } else {
                    checkedSet.remove(position)
                }
            }


            ImageLoader.getInstance().loadAppIcon(context, bean.logo, R.mipmap.ic_launcher, ivIcon)
            //     GlideApp.with(context).load(bean.packageName).centerCrop().into(ivIcon)
            // val pm = App.instance.getPackageManager()
            // ivIcon.setImageDrawable(pm.getApplicationIcon(bean.packageName))
            tvUid.text = bean.uid
            tvLabel.text = bean.label
        }
    }


     fun getSelectUidSting(): String {
        val builder = StringBuilder();
        checkedSet.forEach {
            if (builder.isNotEmpty()) builder.append(",")
            builder.append(mData.get(it).uid)
        }
        return builder.toString()
    }

    fun refresh() {
        val checkedItem: ArrayList<AppUidBean> = ArrayList()
        checkedSet.forEach { checkedItem.add(mData.get(it)) }
        mData.removeAll(checkedItem)
        mData.addAll(0, checkedItem)
        val newCheckSet: ArraySet<Int> = ArraySet()
        for (i in checkedSet.indices) {
            newCheckSet.add(i)
        }
        checkedSet = newCheckSet
    }


}