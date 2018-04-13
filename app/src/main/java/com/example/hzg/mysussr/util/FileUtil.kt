package com.example.hzg.mysussr.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.support.v4.content.FileProvider
import android.util.Log
import java.io.File
import java.io.FileOutputStream


/**
 * Created by hzg on 2017/8/20.
 *文件工具类
 */
object FileUtil {
    fun isFileExist(fileName: String): Boolean {
        val file = File(fileName)
        return file.exists()
    }

    fun copyFileFromAssets(context: Context, dstPath: String) {

        context.assets.list("")
                .forEach {
                    Log.d("accests path:", it)
                    if (it.contains("apk") || it.contains("zip") || it.contains(".tar"))
                        copyFileInAssets(context, it, dstPath + "/" + it)
                }
    }

    fun copyFile(src: String, dst: String) {
        val srcFile = File(src)
        val dstFile = File(dst)
        dstFile.writeBytes(srcFile.readBytes())

    }

    fun initDir(path: String): Boolean {

        val file = File(path)
        if (file.exists()) {
            return false
        } else {
            file.mkdir()
            Log.d("initDir", "create path:$path")
            return true
        }
    }

    private fun copyFileInAssets(context: Context, src: String, dst: String) {
        Log.d("copyFileInAssets", "src:$src,dst:$dst")
        val input = context.assets.open(src)
        val output = FileOutputStream(dst)
        output.write(input.readBytes())
        output.close()
    }


     fun writeFileForText( content: String, dst: String) {

        val output = FileOutputStream(dst)
        output.write(content.toByteArray())
        output.close()
    }

    fun installApk(context: Context, apkPath: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(Uri.fromFile(File(apkPath)), "application/vnd.android.package-archive")

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    fun installApk(context: Context, apkPath: String, providerAuthority: String?) {
        val intent = Intent(Intent.ACTION_VIEW)
        if (Build.VERSION.SDK_INT > 23) {
            if (providerAuthority == null) return
            val apkUri = FileProvider.getUriForFile(context, providerAuthority, File(apkPath))
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive")
        } else intent.setDataAndType(Uri.fromFile(File(apkPath)), "application/vnd.android.package-archive")

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }
}