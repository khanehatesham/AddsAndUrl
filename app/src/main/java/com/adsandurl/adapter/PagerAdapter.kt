package com.adsandurl.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.adsandurl.fragment.HotFragment
import com.adsandurl.fragment.NewFragment

class PagerAdapter(fm: FragmentManager, val tabCount: Int) : FragmentStatePagerAdapter(fm) {


    override fun getItem(position: Int): Fragment {
        when (position) {
            0 -> return HotFragment()
            1 -> return NewFragment()
            else -> return HotFragment()
        }
    }

    override fun getCount(): Int {
        return tabCount
    }
}