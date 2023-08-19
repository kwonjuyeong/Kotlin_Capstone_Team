package com.example.myapplication.Main.Fragment.BoardFragment

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.myapplication.Main.Fragment.BoardFragment.Mine.MineFragment
import com.example.myapplication.Main.Fragment.BoardFragment.Recent.RecentFragment

private const val NUM_PAGES = 2

class SearchFragViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = NUM_PAGES

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> RecentFragment()
            1 -> MineFragment()
            else -> RecentFragment()
        }
    }
}