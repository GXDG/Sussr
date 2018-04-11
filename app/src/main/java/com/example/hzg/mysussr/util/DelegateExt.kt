package com.example.hzg.mysussr.util

import android.content.Context

/**
 * Created by hzg on 2017/8/22.
 * 委托属性
 * 重大问题:不能应用到热更新
 */
object DelegateExt {
    fun <T : Any> sPreference( name: String, default: T,context: Context = Utils.getApp()) = SPreference(context, name, default)
}