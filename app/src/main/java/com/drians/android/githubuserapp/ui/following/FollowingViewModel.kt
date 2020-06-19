package com.drians.android.githubuserapp.ui.following

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONArrayRequestListener
import com.drians.android.githubuserapp.BuildConfig
import com.drians.android.githubuserapp.model.FollowingItems
import org.json.JSONArray


class FollowingViewModel : ViewModel() {

    private val listFollowing = MutableLiveData<ArrayList<FollowingItems>>()

    fun setFollowing(username: String) {
        val listItems = ArrayList<FollowingItems>()

        val url = BuildConfig.BASE_URL+"users/$username/following"
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
                            val following = response.getJSONObject(i)
                            val followingItems = FollowingItems()
                            followingItems.apply {
                                id = following.getInt("id")
                                login = following.getString("login")
                                avatar_url = following.getString("avatar_url")
                                type = following.getString("type")
                            }
                            listItems.add(followingItems)
                        }

                        listFollowing.postValue(listItems)
                    } catch (e: Exception) {
                        Log.d("Exception", e.message.toString())
                    }
                }

                override fun onError(error: ANError) {
                    Log.d("onFailure", error.message.toString())
                }
            })
    }

    fun getFollowing(): LiveData<ArrayList<FollowingItems>> {
        return listFollowing
    }
}