package com.dea.myappgithub.ui

import android.content.Context
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.dea.myappgithub.R

class SectionPagerAdapter(
    private val context: Context,
    fm: FragmentManager,
    private val data: Bundle
) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private var fragmentData: Bundle? = null

    init {
        fragmentData = data
    }

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(R.string.tab_1, R.string.tab_2)
    }

    override fun getCount(): Int = TAB_TITLES.size

    override fun getPageTitle(position: Int): CharSequence? {
        return context.resources.getString(TAB_TITLES[position])
    }

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                val followersFragment = FollowersFragment()
                followersFragment.arguments = data
                followersFragment
            }

            1 -> {
                val followingFragment = FollowingFragment()
                followingFragment.arguments = data
                followingFragment
            }

            else -> throw IllegalArgumentException("Invalid position: $position")
        }
    }

}