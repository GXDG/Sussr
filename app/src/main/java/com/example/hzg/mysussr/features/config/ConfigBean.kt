package com.example.hzg.mysussr.features.config

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * Created by hzg on 2018/4/3 17:34
 * mail:1039766856@qq.com
 * Sussr
 */
@Entity(tableName = "config")
class ConfigBean {

    var configName: String? = null
    @PrimaryKey
    var uid: Int = 0


    var data: List<KeyBean>? = null

}
