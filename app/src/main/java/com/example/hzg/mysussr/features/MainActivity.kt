package com.example.hzg.mysussr.features

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.arch.persistence.room.Room
import android.content.Intent
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.example.hzg.mysussr.R
import com.example.hzg.mysussr.ResultObserver
import com.example.hzg.mysussr.base.BaseActivity
import com.example.hzg.mysussr.databinding.ActivityMainBinding
import com.example.hzg.mysussr.features.config.*
import com.example.hzg.mysussr.features.uid.UidActivity
import com.example.hzg.mysussr.widget.ConfigListDialog
import com.example.hzg.mysussr.widget.LoadingDialog

class MainActivity : BaseActivity() {
    private lateinit var mBinding: ActivityMainBinding
    private lateinit var mViewModel: MainViewModel
    private var mConfigBean: ConfigBean? = null
    private lateinit var dataList: ArrayList<ConfigAdapter.Data>
    var loadingDialog: LoadingDialog? = null
    var selectId = 0
    override fun getLayoutResId(): Int {
        return R.layout.activity_main;
    }

    override fun initView() {
        mBinding = getDataBinding();
    }

    override fun initData() {
        val db = Room.databaseBuilder(getApplicationContext(),
                AppDataBase::class.java, "sussr1")
                .fallbackToDestructiveMigration()
                .build()


        mViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        dataList = ArrayList<ConfigAdapter.Data>()

        val adapter = ConfigAdapter(this, dataList)

        mBinding.rvConfig.layoutManager = LinearLayoutManager(this)
        mBinding.rvConfig.adapter = adapter
        mBinding.rvConfig.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        mViewModel.setRepository(ConfigRepository(db.configDao()))
        mViewModel.configList.observe(this, Observer {
            if (it != null && it.size > 0) {
                Log.d("", "有数据")

            } else {
                Log.d("", "自动插入默认数据")
                addDefaultConfig("默认配置")
            }
        })

        mViewModel.getSelectConfig(selectId).observe(this, Observer {
            mConfigBean = it
            selectId = mConfigBean!!.uid
            initData(mConfigBean!!)
            adapter.notifyDataSetChanged()
        })
        mViewModel.isLoading.observe(this, Observer {
            if (it!!) {
                showDialog()
            } else {
                dismissDialog()
            }
        })

        mBinding.btnLoad.setOnClickListener {
            mViewModel.repository.loadConfig(object : ResultObserver<List<ConfigBean>>() {
                override fun onSuccess(t: List<ConfigBean>) {
                    t.forEach {
                        Log.d("xxx", it.configName)
                        it.data?.forEach {
                            Log.d(it.key, it.value)
                        }
                    }
                }

                override fun onFailure(e: Throwable) {
                    Log.d("xxx", "没有数据")
                }
            })
        }
        mBinding.btnInsert.setOnClickListener {
            val data = ConfigBean()
            data.configName = "默认配置"
            //data.uid = 1
            val keyArray = applicationContext.resources.getStringArray(R.array.main_titles)
            val valueArray = applicationContext.resources.getStringArray(R.array.configValues)
            val list = ArrayList<KeyBean>()
            keyArray.indices.mapTo(list) { KeyBean(keyArray[it], valueArray[it]) }
            data.data = list
            mViewModel.insertConfig(data)


        }
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
        mBinding.btnSave.setOnClickListener { v -> startActivity(Intent(this, UidActivity::class.java)) }
        mBinding.btnDialog.setOnClickListener { v ->
            mViewModel.repository.loadSimpleConfigList(object : ResultObserver<List<SimpleConfig>>() {
                override fun onSuccess(t: List<SimpleConfig>) {
                    ConfigListDialog.newInstance(t as MutableList<SimpleConfig>, selectId, object : ConfigListDialog.AddConfigListener {
                        override fun addConfigSussr(sussr: MutableList<String>) {

                        }

                        override fun addConfigSsr(ssr: Array<String>) {

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
    }


    fun showDialog() {
        if (loadingDialog == null) {
            loadingDialog = LoadingDialog.newInstance("应用信息读取中", true)
        }

        loadingDialog?.show(supportFragmentManager, "loading")
    }


    fun dismissDialog() {
        loadingDialog?.dismiss()
    }

    fun saveConfig() {
        mConfigBean?.configName = dataList[0].data.value
        mViewModel.saveConfig(mConfigBean)
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onStop() {
        saveConfig()
        super.onStop()
    }

}