package com.example.hzg.mysussr.features.config

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters

/**
 * Created by hzg on 2018/4/6.
 */

@Database(entities = arrayOf(ConfigBean::class), version = 5)
@TypeConverters(Converters::class)
abstract class AppDataBase : RoomDatabase() {
    abstract fun configDao(): ConfigDao
}
