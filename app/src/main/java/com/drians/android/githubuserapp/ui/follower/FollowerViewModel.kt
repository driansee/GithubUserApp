package com.drians.android.githubuserapp.ui.follower

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONArrayRequestListener
import com.drians.android.githubuserapp.BuildConfig
import com.drians.android.githubuserapp.model.FollowerItems
import org.json.JSONArray


class FollowerViewModel : ViewModel() {

    private val listFollowers = MutableLiveData<ArrayList<FollowerItems>>()

    fun setFollowers(username: String) {
        val listItems = ArrayList<FollowerItems>()

        val url = BuildConfig.BASE_URL+"users/$username/followers"
        val token = BuildConfig.TOKEN

        AndroidNetworking.get(url)
            .addPathParameter("username", username)
            .addHeaders("Authorization", "token $token")
            .setTag(this)
            .build()
            .getAsJSONArray(object : JSONArrayRequestListener {
                override fun onResponse(response: JSONArray) {
                    try {
                        for (i in 0 until response.length()) {
                            val follower = response.getJSONObject(i)
                            val followerItems = FollowerItems()
                            followerItems.apply {
                                id = follower.getInt("id")
                                login = follower.getString("login")
                                avatar_url = follower.getString("avatar_url")
                                type = follower.getString("type")
                            }
                            listItems.add(followerItems)
                        }

                        listFollowers.postValue(listItems)
                    } catch (e: Exception) {
                        Log.d("Exception", e.message.toString())
                    }
                }

                override fun onError(error: ANError) {
                    Log.d("onFailure", error.message.toString())
                }
            })
    }

    fun getFollowers(): LiveData<ArrayList<FollowerItems>> {
        return listFollowers
    }
}