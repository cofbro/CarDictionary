package com.example.cardictionary.data.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class WrongTopicPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(
        fragmentManager, lifecycle
    ) {

    private lateinit var pagerList: List<Fragment>
    override fun getItemCount(): Int {
        return pagerList.size
    }

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> pagerList[0]
            1 -> pagerList[1]
            else -> pagerList[0]
        }
    }

    fun setPager2(pagerList: List<Fragment>) {
        this.pagerList = pagerList
    }
}