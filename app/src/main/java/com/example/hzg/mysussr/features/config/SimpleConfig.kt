package com.example.hzg.mysussr.features.config

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity

/**
 * Created by hzg on 2018/4/9 16:59
 * mail:1039766856@qq.com
 *Sussr
 */
@Entity
data class SimpleConfig(
        @ColumnInfo(name = "uid")
        var uid: Int,
        @ColumnInfo(name = "name")
        var name: String) {
    constructor() : this(0, "")
}