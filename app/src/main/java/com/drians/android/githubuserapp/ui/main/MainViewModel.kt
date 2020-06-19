package com.drians.android.githubuserapp.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.drians.android.githubuserapp.BuildConfig
import com.drians.android.githubuserapp.model.UserItems
import org.json.JSONObject


class MainViewModel : ViewModel() {

    private val listUsers = MutableLiveData<ArrayList<UserItems>>()

    fun setUser(username: String) {
        val listItems = ArrayList<UserItems>()

        val url = BuildConfig.BASE_URL+"search/users?q=$username"
        val token = BuildConfig.TOKEN

        AndroidNetworking.get(url)
            .addPathParameter("username", username)
            .addHeaders("Authorization", "token $token")
            .setTag(this)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    try {
                        //parsing json
                        val list = response.getJSONArray("items")

                        for (i in 0 until list.length()) {
                            val user = list.getJSONObject(i)
                            val userItems = UserItems()
                            userItems.apply {
                                id = user.getInt("id")
                                login = user.getString("login")
                                avatar_url = user.getString("avatar_url")
                                type = user.getString("type")
                            }
                            listItems.add(userItems)
                        }

                        listUsers.postValue(listItems)
                    } catch (e: Exception) {
                        Log.d("Exception", e.message.toString())
                    }
                }

                override fun onError(error: ANError) {
                    Log.d("onFailure", error.message.toString())
                }
            })
    }

    fun getUsers(): LiveData<ArrayList<UserItems>> {
        return listUsers
    }
}