package com.drians.android.githubuserapp.ui.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.drians.android.githubuserapp.R
import kotlinx.android.synthetic.main.detail_fragment.*


class DetailFragment : Fragment() {

    companion object {
        const val EXTRA_LOGIN = "extra_login"
    }

    private lateinit var detailViewModel: DetailViewModel
    private lateinit var username: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.detail_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        detailViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(DetailViewModel::class.java)

        username = activity?.intent?.getStringExtra(EXTRA_LOGIN).toString()

        showLoading(true)
        detailViewModel.setDetailUser(username)

        detailViewModel.getDetailUser().observe(viewLifecycleOwner, Observer { detailUserItems ->
            if (detailUserItems != null) {
                textLogin.text = detailUserItems[0].login
                textCompany.text = detailUserItems[0].company
                textLocation.text = detailUserItems[0].location
                textRepository.text = detailUserItems[0].public_repos
                textFollower.text = detailUserItems[0].followers
                textFollowing.text = detailUserItems[0].following
                showLoading(false)
            }
        })

    }

    private fun showLoading(state: Boolean) {
        if (state) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
        }
    }

}