package com.drians.android.githubuserapp.ui.following

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.drians.android.githubuserapp.R
import com.drians.android.githubuserapp.adapter.FollowingAdapter
import kotlinx.android.synthetic.main.following_fragment.*


class FollowingFragment : Fragment() {

    companion object {
        const val EXTRA_LOGIN = "extra_login"
    }

    private lateinit var adapter: FollowingAdapter
    private lateinit var followingViewModel: FollowingViewModel
    private lateinit var username: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.following_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        showListFollowing()

        followingViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(FollowingViewModel::class.java)

        username = activity?.intent?.getStringExtra(EXTRA_LOGIN).toString()

        showLoading(true)
        followingViewModel.setFollowing(username)

        followingViewModel.getFollowing().observe(viewLifecycleOwner, Observer { followingItems ->
            if (followingItems != null) {
                adapter.setData(followingItems)
                showLoading(false)
            }
        })
    }

    private fun showListFollowing() {
        adapter = FollowingAdapter()
        adapter.notifyDataSetChanged()

        recyclerviewFollowing.layoutManager = LinearLayoutManager(context)
        recyclerviewFollowing.adapter = adapter
        recyclerviewFollowing.setHasFixedSize(true)
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
        }
    }
}