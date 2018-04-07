package com.example.hzg.mysussr.features.config

import android.arch.persistence.room.TypeConverter

import com.example.hzg.mysussr.util.StringUtil

/**
 * Created by hzg on 2018/4/3 18:02
 * mail:1039766856@qq.com
 * Sussr
 */

class Converters {
    @TypeConverter
    fun stringToList(s: String): List<KeyBean> {
        return StringUtil.json2List(s, Array<KeyBean>::class.java)
    }

    @TypeConverter
    fun listToString(list: List<KeyBean>): String {
        return StringUtil.obj2Json(list)
    }
}
