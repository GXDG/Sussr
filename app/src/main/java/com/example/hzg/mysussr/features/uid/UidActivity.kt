package com.example.hzg.mysussr.features.uid

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import com.example.hzg.mysussr.AppConfig
import com.example.hzg.mysussr.R
import com.example.hzg.mysussr.util.DelegateExt
import com.example.hzg.mysussr.widget.LoadingDialog
import kotlinx.android.synthetic.main.activity_uid.*
import java.util.*

class UidActivity : AppCompatActivity() {
    var loadingDialog: LoadingDialog? = null
    var TFX by DelegateExt.sPreference(AppConfig.KEY_TFX, "")
    var UFX by DelegateExt.sPreference(AppConfig.KEY_UFX, "")
    var UJW by DelegateExt.sPreference(AppConfig.KEY_UJW, "")
    var ULW by DelegateExt.sPreference(AppConfig.KEY_ULW, "")
    lateinit var fragmentList: ArrayList<AppUidFragment>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_uid)
        val viewModel = ViewModelProviders.of(this).get(UidViewModel::class.java)
        viewModel.setRepository(UidRepository())
        viewModel.showLoading.observe(this, Observer { showLoading ->
            if (showLoading!!) {
                showDialog()
            } else {
                dismissDialog()
            }
        })

        val viewPager = findViewById<ViewPager>(R.id.view_pager)
        val tabLayout = findViewById<TabLayout>(R.id.tab_layout)
        fragmentList = ArrayList();
        val titleList = Arrays.asList("TCP放行", "UDP放行", "UDP禁网", "UDP例外")
        fragmentList.add(AppUidFragment.newInstance("TFX", TFX))
        fragmentList.add(AppUidFragment.newInstance("UFX", UFX))
        fragmentList.add(AppUidFragment.newInstance("UJW", UJW))
        fragmentList.add(AppUidFragment.newInstance("ULW", ULW))

        viewPager.adapter = object : FragmentPagerAdapter(supportFragmentManager) {
            override fun getItem(position: Int): Fragment {
                return fragmentList[position]
            }

            override fun getCount(): Int {
                return fragmentList.size
            }

            override fun getPageTitle(position: Int): CharSequence? {
                return titleList[position]
            }
        }
        tabLayout.setupWithViewPager(viewPager)
        tv_save.setOnClickListener {

            TFX = fragmentList[0].uidString
            UFX = fragmentList[1].uidString
            UJW = fragmentList[2].uidString
            ULW = fragmentList[3].uidString


            Snackbar.make(tv_save, "保存成功", Snackbar.LENGTH_SHORT).show()
        }
        iv_back.setOnClickListener {
            finish()
        }
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

    override fun onStop() {
        super.onStop()
    }
}
