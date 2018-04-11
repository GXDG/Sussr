package com.hzg.mysussr

import com.example.hzg.mysussr.util.Utils

/**
 * Created by hzg on 2017/8/20.
 * 常量
 */
object AppConfig {
    val SUSSR_VERSION="5.6";
    val NAME_SP = "mysussr"    //sharePre 名称
    val provider_Path = "com.hzg.mysussr.fileprovider"
    val KEY_IS_INIT = "isInit"//是否初始化
    val KEY_Config = "config"//配置
    val KEY_BOOT = "boot"//开机自启
    val KEY_AUTO_UPDATE = "autoUpdate"//启动时检测更新
    val KEY_UFX = "ufx"//udp放行
    val KEY_UJW = "ujw"//udp禁网
    val KEY_TFX = "tfx"//tcp放行
    val KEY_Config_Index = "configIndex"//选择的配置
    val KEY_IS_AGREE = "isAgree"//是否同意软件协议
    val KEY_SHOW_ERROR = "showError"//是否输出错误信息

    val FILE_PATH = Utils.getApp().filesDir.getPath()//app // 附件存放路径
    val SUSSR_SRC_PATH = "$FILE_PATH/sussr$SUSSR_VERSION.zip";   //sussr安装文件路径
    val BUSYBOX_SRC_PATH = FILE_PATH + "/busybox.apk";//busybox安装文件路径
    var PATH_SUSSR_DIR = "/data/sussr/"               //sussr  根目录
    var PATH_config = PATH_SUSSR_DIR + "setting.ini" //sussr文件配置 路径
    var PATH_Help = PATH_SUSSR_DIR + "说明.txt" //sussr文件配置 路径

    val Shell_mkdir_sussr = "mkdir $PATH_SUSSR_DIR"
    val Shell_install_sussr = "unzip -o $SUSSR_SRC_PATH -d $PATH_SUSSR_DIR"
    val Shell_chmod_sussr = "chmod -R 777 $PATH_SUSSR_DIR"
    val Shell_remove_sussr = "rm -R $PATH_SUSSR_DIR"
    val Shell_sussr_start = PATH_SUSSR_DIR + "start.sh"
    val Shell_sussr_stop = PATH_SUSSR_DIR + "stop.sh"
    val Shell_sussr_check = PATH_SUSSR_DIR + "check.sh"
    val Shell_sussr_edit = "cat $PATH_config"
    val Shell_sussr_edit_help = "cat $PATH_Help"


    val Remove_SUSSR = arrayOf(Shell_remove_sussr)
    val Inatall_SUSSR = arrayOf(Shell_mkdir_sussr, Shell_install_sussr, Shell_chmod_sussr)
    val ReInstall_Sussr = arrayOf(Shell_remove_sussr, Shell_mkdir_sussr, Shell_install_sussr, Shell_chmod_sussr)
    val StopSussr = arrayOf(Shell_sussr_stop)
    val CheckSussr = arrayOf(Shell_sussr_check)
    val StartSussr = arrayOf(Shell_sussr_start)
    val EditSussr = arrayOf(Shell_sussr_edit)


    val BASE_URL = "http://baidu.com"


}