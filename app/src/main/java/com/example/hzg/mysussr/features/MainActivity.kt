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
import com.example.hzg.mysussr.widget.LoadingDialog

class MainActivity : BaseActivity() {
    private lateinit var mBinding: ActivityMainBinding
    var loadingDialog: LoadingDialog? = null
    override fun getLayoutResId(): Int {
        return R.layout.activity_main;
    }

    override fun initView() {
        mBinding = getDataBinding();
    }

    override fun initData() {
        val db = Room.databaseBuilder(getApplicationContext(),
                AppDataBase::class.java!!, "sussr").build()
        val viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        val dataList = ArrayList<ConfigAdapter.Data>()
        val adapter = ConfigAdapter(this, dataList)
        mBinding.rvConfig.layoutManager = LinearLayoutManager(this)
        mBinding.rvConfig.adapter = adapter
        mBinding.rvConfig.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        viewModel.setRepository(ConfigRepository(db.configDao()))
        viewModel.configList.observe(this, Observer {
            Log.d("xxxx", "xxxx")
            if (it != null) {
                it[0].data?.forEach {
                    dataList.add(ConfigAdapter.Data(it))
                    adapter.notifyDataSetChanged()
                }

            }
        })
        viewModel.isLoading.observe(this, Observer {
            if (it!!) {
                showDialog()
            } else {
                dismissDialog()
            }
        })

        mBinding.btnLoad.setOnClickListener {
            viewModel.repository.loadConfig(object : ResultObserver<List<ConfigBean>>() {
                override fun onSuccess(t: List<ConfigBean>) {
                    t.forEach {
                        Log.d("xxx", it.configName)
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
            data.uid = 1
            val list = ArrayList<KeyBean>()
            list.add(KeyBean("服务器", "127.0.0.1"))
            list.add(KeyBean("端口", "80"))
            list.add(KeyBean("密码", ""))
            list.add(KeyBean("加密方法", "chacha20"))
            data.data = list
            viewModel.insertConfig(data)

        }
    }

    override fun initListener() {
        mBinding.btnSave.setOnClickListener { v -> startActivity(Intent(this, UidActivity::class.java)) }
        mBinding.btnDialog.setOnClickListener { v -> showDialog() }
    }


    fun showDialog() {
        if (loadingDialog == null) {
            loadingDialog = LoadingDialog.newInstance("应用信息读取中", false)
        }

        loadingDialog?.show(supportFragmentManager, "loading")
    }


    fun dismissDialog() {
        loadingDialog?.dismiss()
    }

}