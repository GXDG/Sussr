package com.example.hzg.mysussr.features.config

import android.arch.persistence.room.*
import io.reactivex.Single


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
    fun insert(config: ConfigBean):Long

    @Query("SELECT uid,name FROM config")
    fun getConfigNameList(): List<SimpleConfig>

    @Query("select * from config where uid = :uid")
    fun getConfigById(uid: Int): Single<ConfigBean>

    @Query("select * from config ORDER BY uid DESC LIMIT 1")
    fun getConfigByRowId(): ConfigBean

    @Query(" DELETE FROM config where uid = :uid")
    fun deleteConfigById(uid: Int)

    @Delete
    fun delete(user: ConfigBean)

    @Update
    fun update(vararg users: ConfigBean)
}
