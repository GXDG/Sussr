package com.example.hzg.mysussr.features

/**
 * Created by hzg on 2018/4/12 11:04
 * mail:1039766856@qq.com
 *Sussr
 */
interface AdapterListener {
    interface ItemOnClickLister<T> {
        fun onClick(position: Int, item: T)
    }

    interface StringCallback {
        fun onChange(s: String)
    }

    interface DialogCallback {
        fun onClickYes(s: String)
    }
}