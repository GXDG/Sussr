package com.example.hzg.mysussr.util

import android.content.Context
import com.example.hzg.mysussr.AppConfig
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * SharedPreferences 属性委托
 * 重大问题:不能应用到热更新
 */
class SPreference<T>(val context: Context, val name: String, val default: T) : ReadWriteProperty<Any?, T> {
    val prefs by lazy { context.getSharedPreferences(AppConfig.NAME_SP, Context.MODE_PRIVATE) }



    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return findPreference(name, default)
    }

    private fun findPreference(name: String, default: T): T {
        with(prefs)
        {
            val res: Any = when (default) {
                is Long -> getLong(name, default)
                is String -> getString(name, default)
                is Boolean -> getBoolean(name, default)
                is Int -> getInt(name, default)
                is Float -> getFloat(name, default)
                else -> throw  IllegalArgumentException("this type can be saved into Preferences")
            }
            return res as T
        }
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        putPreference(name, value)
    }

    private fun putPreference(name: String, value: T) {
        with(prefs.edit())
        {
            when (value) {
                is Long -> putLong(name, value)
                is String -> putString(name, value)
                is Boolean -> putBoolean(name, value)
                is Int -> putInt(name, value)
                is Float -> putFloat(name, value)
                else -> throw  IllegalArgumentException("this type can be saved into Preferences")
            }
            apply()
        }
    }

}