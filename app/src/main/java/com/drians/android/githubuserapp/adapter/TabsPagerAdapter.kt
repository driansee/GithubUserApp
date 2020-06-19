package com.drians.android.githubuserapp.adapter

import android.content.Context
import androidx.annotation.Nullable
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.drians.android.githubuserapp.R
import com.drians.android.githubuserapp.ui.detail.DetailFragment
import com.drians.android.githubuserapp.ui.follower.FollowerFragment
import com.drians.android.githubuserapp.ui.following.FollowingFragment


class TabsPagerAdapter(private val mContext: Context, fm: FragmentManager) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    @StringRes
    private val tabTitles = intArrayOf(R.string.tab_text_1, R.string.tab_text_2, R.string.tab_text_3)

    override fun getItem(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = DetailFragment()
            1 -> fragment = FollowerFragment()
            2 -> fragment = FollowingFragment()
        }
        return fragment as Fragment
    }

    @Nullable
    override fun getPageTitle(position: Int): CharSequence? {
        return mContext.resources.getString(tabTitles[position])
    }

    override fun getCount(): Int {
        return 3
    }
}