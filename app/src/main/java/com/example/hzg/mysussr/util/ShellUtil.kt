package com.hzg.mysussr.utils

import android.util.Log
import java.io.BufferedReader
import java.io.InputStreamReader

/**
 * Created by hzg on 2017/8/20.
 *执行shell命令的工具类
 */
object ShellUtil {
    fun execShell(cmds: Array<String>, isRoot: Boolean, isGetError: Boolean): Array<String> {
        val tag = "shell"
        val p: Process?
        var startIndex = 0
        if (isRoot)
            p = Runtime.getRuntime().exec(("su"))
        else {
            p = Runtime.getRuntime().exec((cmds[0]))
            startIndex = 1
            Log.d(tag, cmds[0]);
        }
        val inbuilder = StringBuilder()
        val errbuilder = StringBuilder()
        val outputStream = p.outputStream
        val inputStream = p.inputStream
        val errStream = p.errorStream
//      写入命令 isRoot为真时 从0开始，为假时从1开始（创建 Process时消耗了cmds【0】）
        for (index in startIndex..cmds.lastIndex) {
            outputStream.write((cmds[index] + "\n").toByteArray())
        }
        outputStream.close()

//        读取输出信息
        val reader = BufferedReader(InputStreamReader(inputStream))
        for (line in reader.readLines()) {
            inbuilder.append(line)
            inbuilder.append("\n")
        }
        inputStream.close()
//读取错误信息
        if (isGetError) {
            val errreader = BufferedReader(InputStreamReader(errStream))
            for (line in errreader.readLines()) {
                errbuilder.append(line)
                errbuilder.append("\n")
            }

        }
        errStream.close()
        //关闭进程
        p.destroy()
        //返回String 数组，[0]表示输出信息 [1]表示错误信息
        return arrayOf(inbuilder.toString(), errbuilder.toString())
    }
}