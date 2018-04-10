package com.example.hzg.mysussr.features.config

import android.arch.persistence.room.*


/**
 * Created by hzg on 2018/4/6.
 */

@Dao
interface ConfigDao {

    @Query("SELECT * FROM config")
    fun getAllConfig(): List<ConfigBean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg users: ConfigBean)

    @Insert
    fun insert(config: ConfigBean)

    @Query("select uid,name FROM config")
    fun getConfigNameList(): List<SimpleConfig>

    @Query("select * from config where uid = :uid")
    fun getConfigById(uid: Array<Int>): ConfigBean

    @Delete
    fun delete(user: ConfigBean)

    @Update
    fun update(vararg users: ConfigBean)
}
