package com.drians.android.githubuserapp.ui.follower

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.drians.android.githubuserapp.R
import com.drians.android.githubuserapp.adapter.FollowerAdapter
import kotlinx.android.synthetic.main.follower_fragment.*


class FollowerFragment : Fragment() {

    companion object {
        const val EXTRA_LOGIN = "extra_login"
    }

    private lateinit var adapter: FollowerAdapter
    private lateinit var followerViewModel: FollowerViewModel
    private lateinit var username: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.follower_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        showListFollowers()

        followerViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(FollowerViewModel::class.java)

        username = activity?.intent?.getStringExtra(EXTRA_LOGIN).toString()

        showLoading(true)
        followerViewModel.setFollowers(username)

        followerViewModel.getFollowers().observe(viewLifecycleOwner, Observer { followerItems ->
            if (followerItems != null) {
                adapter.setData(followerItems)
                showLoading(false)
            }
        })
    }

    private fun showListFollowers() {
        adapter = FollowerAdapter()
        adapter.notifyDataSetChanged()

        recyclerviewFollower.layoutManager = LinearLayoutManager(context)
        recyclerviewFollower.adapter = adapter
        recyclerviewFollower.setHasFixedSize(true)
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
        }
    }

}