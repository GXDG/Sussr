package com.example.hzg.mysussr.features.config

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query

/**
 * Created by hzg on 2018/4/6.
 */

@Dao
interface ConfigDao {

    @get:Query("SELECT * FROM config")
    val all: List<ConfigBean>

    @Insert
    fun insertAll(vararg users: ConfigBean)

    @Delete
    fun delete(user: ConfigBean)
}
