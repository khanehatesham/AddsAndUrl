package com.adsandurl.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.viewpager.widget.ViewPager
import com.adsandurl.R
import com.adsandurl.adapter.PagerAdapter
import com.adsandurl.util.Helper
import com.google.android.material.tabs.TabLayout

class MainActivity : AppCompatActivity() {

    private var toolbar: Toolbar? = null
    private var viewPager: ViewPager? = null
    private var tabLayout: TabLayout? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        toolbar = findViewById(R.id.toolbar)
        viewPager = findViewById(R.id.viewpager)
        tabLayout = findViewById(R.id.tab_layout)

        toolbar!!.setTitle("Hot")
        setSupportActionBar(toolbar)

        tabLayout!!.addTab(tabLayout!!.newTab().setText("Hot"))
        tabLayout!!.addTab(tabLayout!!.newTab().setText("New"))
        val searchPagerAdapter = PagerAdapter(this.supportFragmentManager, tabLayout!!.tabCount)
        viewPager!!.adapter = searchPagerAdapter
        viewPager!!.currentItem = 0
        viewPager!!.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        tabLayout!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager!!.currentItem = tab.position
                if (tab.position == 0) {
                    toolbar!!.title = "Hot"
                    Helper.hideSoftKeyboard(this@MainActivity)
                } else if (tab.position == 1) {
                    toolbar!!.title = "New"
                    Helper.hideSoftKeyboard(this@MainActivity)

                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}

            override fun onTabReselected(tab: TabLayout.Tab) {
                viewPager!!.currentItem = tab.position


            }
        })

    }


}
