package com.example.hzg.mysussr.features.uid

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import com.example.hzg.mysussr.R
import java.util.*

class UidActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_uid)
        val viewModel = ViewModelProviders.of(this).get(UidViewModel::class.java)
        viewModel.setRepository(UidRepository())


        val viewPager = findViewById<ViewPager>(R.id.view_pager)
        val tabLayout = findViewById<TabLayout>(R.id.tab_layout)
        val fragmentList = ArrayList<Fragment>();
        val titleList = Arrays.asList("UDP放行", "TCP放行", "UDP禁网", "UDP例外")
        fragmentList.add(AppUidFragment())
        fragmentList.add(AppUidFragment())
        fragmentList.add(AppUidFragment())
        fragmentList.add(AppUidFragment())
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
    }
}
