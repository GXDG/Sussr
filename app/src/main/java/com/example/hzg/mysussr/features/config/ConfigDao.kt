package com.example.hzg.mysussr.features.config

import android.arch.persistence.room.*


/**
 * Created by hzg on 2018/4/6.
 */

@Dao
interface ConfigDao {

    @get:Query("SELECT * FROM config")
    val all: List<ConfigBean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg users: ConfigBean)

    @Query("SELECT uid,configName FROM config")
    fun getConfigNameList(): List<ConfigBean>

    @Delete
    fun delete(user: ConfigBean)

    @Update
    fun update(vararg users: ConfigBean)
}
