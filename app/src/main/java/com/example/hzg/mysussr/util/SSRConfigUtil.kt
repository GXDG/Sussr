package com.example.hzg.mysussr.util

import android.util.Base64
import android.util.Log

/**
 * Created by hzg on 2018/4/8 17:21
 * mail:1039766856@qq.com
 *Sussr
 */
object SSRConfigUtil {
    fun getConfigItemFromSSR(ssr: String): Array<String>? {
        Log.e("ssr链接:", ssr)
        val string = ssr.replace("ssr://", "")
        Log.e("ssr链接解码部分", string)
        val s = string.replace('_', '/')
        Log.e("ssr链接最终解码部分", string)
        try {
            val bytes = Base64.decode(s, Base64.DEFAULT)
            val decodeString = String(bytes)
            Log.e("完整的解析", decodeString)
            /**
             * 解析的数据parms有6个
             * parms[0] ip
             * parms[1] port
             * parms[2] 协议
             * prams[3] 加密方法
             * parms[4] 混淆方式
             * parms[5] 密码
             */
            val parmArray = decodeString.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray();
            val parms = parmArray[0].split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

            val other = Array<String>(2, { "" })
            if (parmArray.size > 1) {
                val parms1 = parmArray[1].split("&".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                parms1.forEach {
                    Log.e("解析第2部分", it)
                    if (it.contains("obfsparam")) {
                        other[0] = it.substring(it.indexOf("=") + 1, it.length)
                        other[0] = String(Base64.decode(other[0], Base64.DEFAULT))
                    }

                    if (it.contains("remarks")) {
                        other[1] = it.substring(it.indexOf("=") + 1, it.length)
                        other[1] = String(Base64.decode(other[1], Base64.DEFAULT))
                    }
                }
            }

            parms.forEach {
                Log.e("解析第1部分", it)
            }





            parms[5] = String(Base64.decode(parms[5], Base64.DEFAULT))
            val result = parms.toMutableList()
            for (s in other) {
                result.add(s)
            }
            result.forEach {
                Log.e("最终解析的数据", it)
            }

            return result.toTypedArray()
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }


    }

    //                * parms[0] ip
//                * parms[1] port
//                * parms[2] 协议
//                * prams[3] 加密方法
//                * parms[4] 混淆方式
//                * parms[5] 密码
//    config.set(0, ssr[0])
//    config.set(1, ssr[1])
//    config.set(2, ssr[5])
//    config.set(3, ssr[3])
//    config.set(4, ssr[2])
//    config.set(5, ssr[4])
//    if (ssr[6] != "")
//    config.set(6, ssr[6])
//    val configName = if (ssr[7] == "") ssr[0] else ssr[7]
    fun shareBySSR(value: Array<String>): String {
        if (value.size != 8)
            return "错误的数据"
        val builder = StringBuilder()
        for (i in value.indices) {
            if (i < 6) {
                val s = if (i == 5) String(Base64.encode(value[5].toByteArray(), Base64.DEFAULT)) else value[i]
                builder.append(s)
                if (i == 5) builder.append("/?")
                else builder.append(":")
            }
            if (i == 6)
                builder.append("obfsparam=").append(String(Base64.encode(value[6].toByteArray(), Base64.DEFAULT))).append("&")
            if (i == 7)
                builder.append("remarks=").append(String(Base64.encode(value[7].toByteArray(), Base64.DEFAULT)))
        }
        val ssr = String(Base64.encode(builder.toString().toByteArray(), Base64.DEFAULT))
        Log.d("ssr分享", ssr)
        return "ssr://" + ssr.replace("/", "_")
    }

    fun getConfigItemFromSuSSR(sussr: String): MutableList<String> {
        val string = sussr.replace("sussr://", "")
        Log.d("sussr:", string)
        val sussr = ArrayList<String>()
        try {
            val decode = String(Base64.decode(string, Base64.DEFAULT))
            sussr.addAll(decode.split(":").toMutableList())
        } catch (e: Exception) {
            Log.e("Decode Sussr", e.message)
        }
        return sussr
    }

    fun shareBySussr(value: MutableList<String>): String {
        val build = StringBuilder()
        var size = value.size
        value.forEach {
            size--
            build.append(it)
            if (size > 0)
                build.append(":")
        }
        var sussr: String
        try {
            sussr = "sussr://" + String(Base64.encode(build.toString().toByteArray(), Base64.DEFAULT))
        } catch (e: Exception) {
            sussr = "解析错误"
            Log.e("Decode Sussr", e.message)
        }
        return sussr
    }
}