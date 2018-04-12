package com.example.hzg.mysussr.features

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.arch.persistence.room.Room
import android.content.Intent
import android.support.design.widget.Snackbar
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.BounceInterpolator
import com.example.hzg.mysussr.AppConfig
import com.example.hzg.mysussr.R
import com.example.hzg.mysussr.ResultObserver
import com.example.hzg.mysussr.base.BaseActivity
import com.example.hzg.mysussr.databinding.ActivityMainBinding
import com.example.hzg.mysussr.features.config.*
import com.example.hzg.mysussr.features.uid.UidActivity
import com.example.hzg.mysussr.util.DelegateExt
import com.example.hzg.mysussr.util.FileUtil
import com.example.hzg.mysussr.widget.ConfigListDialog
import com.example.hzg.mysussr.widget.LoadingDialog

class MainActivity : BaseActivity() {
    private lateinit var mBinding: ActivityMainBinding
    private lateinit var mViewModel: MainViewModel
    private var mConfigBean: ConfigBean? = null
    private lateinit var dataList: ArrayList<ConfigAdapter.Data>
    var loadingDialog: LoadingDialog? = null
    var selectId by DelegateExt.sPreference("selectId", 0)
    lateinit var adapter: ConfigAdapter
    lateinit var quickMenuList: Array<View>
    var isQuickMenuOpen = false
    override fun getLayoutResId(): Int {
        return R.layout.activity_main;
    }

    override fun initView() {
        mBinding = getDataBinding()
        initQuickMenu()
    }

    private fun initQuickMenu() {
        quickMenuList = arrayOf(mBinding.btnCheck, mBinding.btnStart, mBinding.btnStop, mBinding.btnIp)
        mBinding.btnCheck.setOnClickListener { mViewModel.checkSussr() }
        mBinding.btnStart.setOnClickListener { mViewModel.startSussr(mConfigBean) }
        mBinding.btnStop.setOnClickListener { mViewModel.stopSussr() }
        mBinding.btnIp.setOnClickListener {
            //todo:查询ip
        }

        val dimen = resources.getDimension(R.dimen.dp70)
        val valueAnimator = ValueAnimator()
        valueAnimator.addUpdateListener {
            val radio = it.getAnimatedValue() as Float
            quickMenuList[0].translationX = dimen * radio
            quickMenuList[1].translationY = dimen * radio
            quickMenuList[2].translationX = -dimen * radio
            quickMenuList[3].translationY = -dimen * radio

            quickMenuList[0].alpha = radio
            quickMenuList[1].alpha = radio
            quickMenuList[2].alpha = radio
            quickMenuList[3].alpha = radio

            mBinding.btnMenu.alpha = 1 - radio * 0.5f
        }
        valueAnimator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator?) {
                super.onAnimationStart(animation)
                if (isQuickMenuOpen) {
                    quickMenuList.forEach {
                        it.visibility = View.VISIBLE
                        it.isClickable = true
                    }
                }

            }

            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                mBinding.btnMenu.isClickable = true
                if (!isQuickMenuOpen) {
                    quickMenuList.forEach {
                        it.visibility = View.GONE
                        it.isClickable = false
                    }
                }
            }
        })

        mBinding.btnMenu.setOnClickListener {
            if (isQuickMenuOpen) {
                isQuickMenuOpen = false
                mBinding.btnMenu.isClickable = false
                valueAnimator.setFloatValues(1f, 0f)
                valueAnimator.interpolator = AccelerateInterpolator()
                valueAnimator.duration = 300
                valueAnimator.start()
            } else {
                isQuickMenuOpen = true
                mBinding.btnMenu.isClickable = false
                valueAnimator.setFloatValues(0f, 1f)
                valueAnimator.interpolator = BounceInterpolator()
                valueAnimator.duration = 500
                valueAnimator.start()
            }
        }
    }

    override fun initData() {
        val db = Room.databaseBuilder(getApplicationContext(),
                AppDataBase::class.java, "sussr1")
                .fallbackToDestructiveMigration()
                .build()

        mViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        dataList = ArrayList<ConfigAdapter.Data>()

        adapter = ConfigAdapter(this, dataList)

        mBinding.rvConfig.layoutManager = LinearLayoutManager(this)
        mBinding.rvConfig.adapter = adapter
        mBinding.rvConfig.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        mViewModel.setRepository(ConfigRepository(db.configDao()))
        mViewModel.configList.observe(this, Observer {
            if (it != null && it.size > 0) {
                Log.d("", "有数据,选中第一条数据")
                refreshRv(it[0])
            } else {
                Log.d("", "自动插入默认数据")
                addDefaultConfig("默认配置")
            }
        })

        mViewModel.message.observe(this, Observer {
            if (it != null) {
                Snackbar.make(mBinding.btnMenu, it, Snackbar.LENGTH_SHORT).show()
            }

        })
        mViewModel.selectConfig.observe(this, Observer {
            refreshRv(it!!)
        })
        mViewModel.isLoading.observe(this, Observer {
            if (it!!) {
                showDialog()
            } else {
                dismissDialog()
            }
        })
        mViewModel.reInstallApk.observe(this, Observer {
            if (it != null && it) {
                try {
                    FileUtil.installApk(this, AppConfig.BUSYBOX_SRC_PATH, AppConfig.provider_Path)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        })
        mViewModel.loadSelectConfig(selectId)


        val menuList = ArrayList<MenuAdapter.Data>()
        menuList.add(MenuAdapter.Data("UID放行"))
        menuList.add(MenuAdapter.Data("配置选择"))
        menuList.add(MenuAdapter.Data("附件检测"))
        menuList.add(MenuAdapter.Data("安装Busybox(APK)"))
        menuList.add(MenuAdapter.Data("附件重置"))
        //   menuList.add(MenuAdapter.Data("附件权限更改"))
        //   menuList.add(MenuAdapter.Data("root权限检测"))
        menuList.add(MenuAdapter.Data("安装sussr"))
        menuList.add(MenuAdapter.Data("编辑sussr配置"))
        val menuAdapter = MenuAdapter(this, menuList)
        menuAdapter.listener = object : AdapterListener.ItemOnClickLister<MenuAdapter.Data> {
            override fun onClick(position: Int, item: MenuAdapter.Data) {
                when (item.content) {
                    "UID放行" -> {
                        startActivity(Intent(this@MainActivity, UidActivity::class.java))
                    }
                    "配置选择" -> {
                        mViewModel.repository.loadSimpleConfigList(object : ResultObserver<List<SimpleConfig>>() {
                            override fun onSuccess(t: List<SimpleConfig>) {
                                ConfigListDialog.newInstance(t as MutableList<SimpleConfig>, selectId, object : ConfigListDialog.AddConfigListener {
                                    override fun deleteConfig(uid: Int) {
                                        mViewModel.deleteConfig(uid)
                                    }

                                    override fun selectConfig(uid: Int) {
                                        mViewModel.loadSelectConfig(uid)
                                    }

                                    override fun addConfigSussr(sussr: MutableList<String>) {
                                        addConfigBySUSSR(sussr)
                                    }

                                    override fun addConfigSsr(ssr: Array<String>) {
                                        addConfigBySSR(ssr)
                                    }

                                    override fun addConfigName(name: String) {
                                        addDefaultConfig(name)
                                    }
                                }).show(supportFragmentManager)
                            }

                            override fun onFailure(e: Throwable) {

                            }

                        })
                    }
                    "附件检测" -> {
                        mViewModel.checkFile()
                    }
                    "安装Busybox(APK)" -> {
                        try {
                            FileUtil.installApk(this@MainActivity, AppConfig.BUSYBOX_SRC_PATH, AppConfig.provider_Path)
                        } catch (e: Exception) {
                            e.printStackTrace()
                            mViewModel.changeFileChmod()
                        }
                    }
                    "附件重置" -> {
                        mViewModel.copyFile()
                    }
                    "附件权限更改" -> {
                        mViewModel.changeFileChmod()
                    }
                    "root权限检测" -> {
                        mViewModel.checkSu()
                    }
                    "安装sussr" -> {
                        mViewModel.installSussr()
                    }
                    "编辑sussr配置" -> {
                        mViewModel.editSussr()
                    }
                }
            }

        }
        mBinding.rvMenu.layoutManager = GridLayoutManager(this, 2)
        mBinding.rvMenu.adapter = menuAdapter
    }


    private fun refreshRv(bean: ConfigBean) {
        //先保存当前配置
        if (mConfigBean != null) {
            saveConfig()
        }
        mConfigBean = bean
        selectId = mConfigBean!!.uid
        initData(mConfigBean!!)
        adapter.notifyDataSetChanged()
    }

    private fun initData(bean: ConfigBean) {
        val headerSize = 1
        if (dataList.size == 0) {
            dataList.add(ConfigAdapter.Data(KeyBean("配置名称", bean.configName), ConfigAdapter.Data.TYPE_HEADER))
            bean.data?.forEach {
                dataList.add(ConfigAdapter.Data(it))
            }
        } else {
            for (i in dataList.indices) {
                if (i == 0) {
                    dataList[0].data.value = bean.configName
                } else {
                    dataList[i].data = bean.data!![i - headerSize]
                }
            }
        }

        dataList[3 + headerSize].type = ConfigAdapter.Data.TYPE_SELECT
        dataList[4 + headerSize].type = ConfigAdapter.Data.TYPE_SELECT
        dataList[5 + headerSize].type = ConfigAdapter.Data.TYPE_SELECT
        dataList[8 + headerSize].type = ConfigAdapter.Data.TYPE_SELECT

        dataList[10 + headerSize].type = ConfigAdapter.Data.TYPE_SWITCH
        dataList[11 + headerSize].type = ConfigAdapter.Data.TYPE_SWITCH
        dataList[12 + headerSize].type = ConfigAdapter.Data.TYPE_SWITCH
        dataList[13 + headerSize].type = ConfigAdapter.Data.TYPE_SWITCH
        dataList[14 + headerSize].type = ConfigAdapter.Data.TYPE_SWITCH
        dataList[15 + headerSize].type = ConfigAdapter.Data.TYPE_SWITCH
        dataList[16 + headerSize].type = ConfigAdapter.Data.TYPE_SWITCH
        dataList[17 + headerSize].type = ConfigAdapter.Data.TYPE_SWITCH
    }

    fun addConfigBySUSSR(sussr: MutableList<String>) {
        val data = ConfigBean()
        data.configName = sussr[0]
        val keyArray = applicationContext.resources.getStringArray(R.array.main_titles)
        sussr.removeAt(0)
        val list = ArrayList<KeyBean>()
        keyArray.indices.mapTo(list) { KeyBean(keyArray[it], sussr[it]) }
        data.data = list
        mViewModel.insertConfig(data)
    }

    fun addConfigBySSR(ssr: Array<String>) {
        val data = ConfigBean()
        data.configName = if (ssr[7] == "") ssr[0] else ssr[7]
        val keyArray = applicationContext.resources.getStringArray(R.array.main_titles)
        val valueArray = applicationContext.resources.getStringArray(R.array.configValues)
        //                * parms[0] ip
//                * parms[1] port
//                * parms[2] 协议
//                * prams[3] 加密方法
//                * parms[4] 混淆方式
//                * parms[5] 密码
        valueArray.set(0, ssr[0])
        valueArray.set(1, ssr[1])
        valueArray.set(2, ssr[5])
        valueArray.set(3, ssr[3])
        valueArray.set(4, ssr[2])
        valueArray.set(5, ssr[4])
        if (ssr[6] != "")
            valueArray.set(6, ssr[6])
        val list = ArrayList<KeyBean>()
        keyArray.indices.mapTo(list) { KeyBean(keyArray[it], valueArray[it]) }
        data.data = list
        mViewModel.insertConfig(data)
    }

    fun addDefaultConfig(configName: String) {
        val data = ConfigBean()
        data.configName = configName
        val keyArray = applicationContext.resources.getStringArray(R.array.main_titles)
        val valueArray = applicationContext.resources.getStringArray(R.array.configValues)
        val list = ArrayList<KeyBean>()
        keyArray.indices.mapTo(list) { KeyBean(keyArray[it], valueArray[it]) }
        data.data = list
        mViewModel.insertConfig(data)
    }

    override fun initListener() {
//        mBinding.btnSave.setOnClickListener { v -> startActivity(Intent(this, UidActivity::class.java)) }
//        mBinding.btnDialog.setOnClickListener { v ->
//            mViewModel.repository.loadSimpleConfigList(object : ResultObserver<List<SimpleConfig>>() {
//                override fun onSuccess(t: List<SimpleConfig>) {
//                    ConfigListDialog.newInstance(t as MutableList<SimpleConfig>, selectId, object : ConfigListDialog.AddConfigListener {
//                        override fun deleteConfig(uid: Int) {
//                            mViewModel.deleteConfig(uid)
//                        }
//
//                        override fun selectConfig(uid: Int) {
//                            mViewModel.loadSelectConfig(uid)
//                        }
//
//                        override fun addConfigSussr(sussr: MutableList<String>) {
//                            addConfigBySUSSR(sussr)
//                        }
//
//                        override fun addConfigSsr(ssr: Array<String>) {
//                            addConfigBySSR(ssr)
//                        }
//
//                        override fun addConfigName(name: String) {
//                            addDefaultConfig(name)
//                        }
//                    }).show(supportFragmentManager)
//                }
//
//                override fun onFailure(e: Throwable) {
//
//                }
//
//            })
//        }
//        mBinding.btnInsert.setOnClickListener {
//
//        }
//        mBinding.btnLoad.setOnClickListener {
//
//
//        }

    }


    fun showDialog() {
        if (loadingDialog == null) {
            loadingDialog = LoadingDialog.newInstance("应用信息读取中", true)
        }

        loadingDialog?.show(supportFragmentManager)
    }


    fun dismissDialog() {
        loadingDialog?.dismiss()
    }

    fun saveConfig() {
        if (mConfigBean != null) {
            mConfigBean?.configName = dataList[0].data.value
            mViewModel.saveConfig(mConfigBean)
        }

    }

    override fun onPause() {
        super.onPause()
    }

    override fun onStop() {
        saveConfig()
        super.onStop()
    }

}