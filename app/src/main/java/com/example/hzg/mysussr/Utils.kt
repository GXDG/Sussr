package com.example.hzg.mysussr

import android.content.Context
import android.content.pm.ApplicationInfo

/**
 * Created by hzg on 2018/4/4 10:49
 * mail:1039766856@qq.com
 *Sussr
 */
object Utils {

    fun getApp(): Context {
        return App.instance
    }

    /**
     * 从PackageManage获取已安装软件的UID信息（不包括系统软件）
     *
     * @param context context
     * @return 装载应用UID信息的列表
     */
    fun getAppUidList(): ArrayList<AppUidBean> {
        val pm = getApp().packageManager
        val packageInfos = pm.getInstalledPackages(0)
        val appUidBeanList = ArrayList<AppUidBean>()
        for (i in packageInfos.indices) {
            if (packageInfos[i].applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM <= 0) {
                with(packageInfos[i].applicationInfo)
                {
                    val label = loadLabel(pm) as String
                    val uid = uid.toString()
                    appUidBeanList.add(AppUidBean(label, uid, packageName))
                }

            }
        }
        return appUidBeanList
    }
}