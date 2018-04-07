package com.example.hzg.mysussr

import android.app.Application
import com.squareup.leakcanary.LeakCanary
import kotlin.properties.Delegates

/**
 * Created by hzg on 2018/4/4 10:52
 * mail:1039766856@qq.com
 *Sussr
 */
class App : Application() {
    companion object {
        var instance: App by Delegates.notNull()
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        LeakCanary.install(this)
    }
}