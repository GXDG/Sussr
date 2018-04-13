package com.example.hzg.mysussr.features.config

import com.example.hzg.mysussr.App
import com.example.hzg.mysussr.AppConfig
import com.example.hzg.mysussr.R
import com.example.hzg.mysussr.SingleResultObserver
import com.example.hzg.mysussr.util.DelegateExt
import com.example.hzg.mysussr.util.FileUtil
import com.example.hzg.mysussr.util.ShellUtil
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by hzg on 2018/4/12 16:46
 * mail:1039766856@qq.com
 *Sussr
 */
class SussrConfigRepository {
    val formatString: String

    init {
        val builder = StringBuilder()
        val conifName = App.instance.resources.getStringArray(R.array.configName)
        for (i in conifName.indices) {
            builder.append(conifName[i] + "=%s")
            if (i < conifName.size - 1) {
                builder.append("\\\n")
            }
        }
        formatString = builder.toString()
    }

    fun executeShell(shell: Array<String>, observer: SingleResultObserver<Array<String>>) {
        Single.create<Array<String>>({ it.onSuccess(ShellUtil.execShell(shell, true, true)) })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer)
    }

    fun getStartShell(config: Array<String>): Array<String> {
        val dludp = when (config[8]) {
            "直接发送UDP" -> "0"
            "通过UDP转发" -> "1"
            "通过TCP转发" -> "2"
            else -> "1"
        }
        val TFX by DelegateExt.sPreference(AppConfig.KEY_TFX, "")
        val UFX by DelegateExt.sPreference(AppConfig.KEY_UFX, "")
        val UJW by DelegateExt.sPreference(AppConfig.KEY_UJW, "")
        val ULW by DelegateExt.sPreference(AppConfig.KEY_ULW, "")
        val string = String.format(formatString,
                config[0], config[1], config[2], config[9], config[3]
                , config[4], config[5], config[6], config[7], dludp
                , config[10], config[11], config[12], config[13], config[14]
                , config[15], config[16], config[17], "\"$TFX\"", "\"$UFX\""
                , "\"$UJW\"", "\"$ULW\"")
        return arrayOf("sed -i '2,${config.size + 5}d' /data/sussr/setting.ini", "sed -i '1a $string' /data/sussr/setting.ini", AppConfig.Shell_sussr_start)

    }

    fun installSussr(observer: SingleResultObserver<Array<String>>) {
        executeShell(AppConfig.ReInstall_Sussr1, observer)
    }

    fun startSussr(config: Array<String>, observer: SingleResultObserver<Array<String>>) {
        executeShell(getStartShell(config), observer)
    }

    fun stopSussr(observer: SingleResultObserver<Array<String>>) {
        executeShell(AppConfig.StopSussr, observer)
    }

    fun checkSussr(observer: SingleResultObserver<Array<String>>) {
        executeShell(AppConfig.CheckSussr, observer)
    }

    fun editSussr(observer: SingleResultObserver<Array<String>>) {
        executeShell(AppConfig.EditSussr, observer)
    }

    fun saveEditSussr(s: String, observer: SingleResultObserver<Array<String>>) {
        Single.create<Array<String>>({
            FileUtil.writeFileForText(s, AppConfig.FILE_PATH + "/temp")
            it.onSuccess(ShellUtil.execShell(arrayOf("cat  ${AppConfig.FILE_PATH}/temp > /data/sussr/setting.ini"), true, true))
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer)


    }

    fun removeSussr(observer: SingleResultObserver<Array<String>>) {
        executeShell(AppConfig.Remove_SUSSR, observer)
    }
}