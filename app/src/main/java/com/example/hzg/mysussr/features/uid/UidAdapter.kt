package com.example.hzg.mysussr.features.uid


import android.content.Context
import android.support.v4.util.ArraySet
import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import com.example.hzg.mysussr.features.uid.bean.AppUidBean
import com.example.hzg.mysussr.R
import com.example.hzg.mysussr.base.BaseAdapter
import com.example.hzg.mysussr.util.ImageLoader


/**
 * Created by hzg on 2017/8/30.
 *
 */
class UidAdapter(context: Context, data: MutableList<AppUidBean>, selectList: ArrayList<String>) : BaseAdapter<AppUidBean>(context, data) {


    init {

    }

    override fun createVH(view: View, viewType: Int): BaseAdapter.BaseVH {
        return MyVH(view)
    }

    class MyVH(view: View) : BaseAdapter.BaseVH(view) {
        val tvUid: TextView
        val ivIcon: ImageView
        val tvLable: TextView
        val checkbox: CheckBox

        init {
            tvUid = view.findViewById<TextView>(R.id.tv_uid)
            tvLable = view.findViewById<TextView>(R.id.tv_lable)
            ivIcon = view.findViewById<ImageView>(R.id.iv_icon)
            checkbox = view.findViewById<CheckBox>(R.id.checkbox)
        }
    }

    override fun convert(holder: BaseAdapter.BaseVH, position: Int, bean: AppUidBean) {
        with(holder as MyVH)
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
            tvLable.text = bean.label
        }
    }

    var checkedSet: ArraySet<Int> = ArraySet()

    init {
        for (i in data.indices) {
            if (selectList.contains(data.get(i).uid))
                checkedSet.add(i)
        }
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

    override fun getLayoutId(viewType: Int): Int {
        return R.layout.item_uid
    }
}