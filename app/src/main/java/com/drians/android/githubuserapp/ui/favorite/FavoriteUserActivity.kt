package com.drians.android.githubuserapp.ui.favorite

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.drians.android.githubuserapp.R
import com.drians.android.githubuserapp.adapter.FavoriteUserAdapter
import com.drians.android.githubuserapp.database.FavoriteUserHelper
import com.drians.android.githubuserapp.helper.MappingHelper
import com.drians.android.githubuserapp.model.FavoriteUserItems
import com.drians.android.githubuserapp.ui.detail.DetailActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_favorite_user.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class FavoriteUserActivity : AppCompatActivity() {

    private lateinit var adapter: FavoriteUserAdapter
    private lateinit var favoriteUserHelper: FavoriteUserHelper

    companion object {
        const val EXTRA_LOGIN = "extra_login"
        const val EXTRA_AVATAR_URL = "extra_avatar_url"
        const val EXTRA_TYPE = "extra_type"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite_user)

        supportActionBar?.setTitle(R.string.title_activity_favorite)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        showListFavoriteUser()

        favoriteUserHelper = FavoriteUserHelper.getInstance(applicationContext)
        favoriteUserHelper.open()

        /**
         *  Cek jika savedInstaceState null makan akan melakukan proses asynctask nya
         *  jika tidak,akan mengambil arraylist nya dari yang sudah di simpan
         **/
        if (savedInstanceState == null) {
            loadFavoriteUserAsync()
        } else {
            val list = savedInstanceState.getParcelableArrayList<FavoriteUserItems>(EXTRA_LOGIN)
            if (list != null) {
                adapter.listFavoriteUser = list
            }
        }
    }

    private fun showListFavoriteUser() {
        recyclerviewFavorite.layoutManager = LinearLayoutManager(this)
        recyclerviewFavorite.setHasFixedSize(true)

        adapter = FavoriteUserAdapter{
            val intent = Intent(this@FavoriteUserActivity, DetailActivity::class.java)
            intent.apply {
                putExtra(EXTRA_LOGIN, it.login)
                putExtra(EXTRA_AVATAR_URL, it.avatar_url)
                putExtra(EXTRA_TYPE, it.type)
            }
            startActivity(intent)
        }

        recyclerviewFavorite.adapter = adapter
    }

    private fun loadFavoriteUserAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            progressBar.visibility = View.VISIBLE
            val deferredFavorites = async(Dispatchers.IO) {
                val cursor = favoriteUserHelper.queryAll()
                MappingHelper.mapCursorToArrayList(cursor)
            }
            progressBar.visibility = View.INVISIBLE
            val favorites = deferredFavorites.await()
            if (favorites.size > 0) {
                adapter.listFavoriteUser = favorites
            } else {
                adapter.listFavoriteUser = ArrayList()
                Snackbar.make(recyclerviewFavorite,
                    "Tidak ada data saat ini", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.apply {
            putParcelableArrayList(EXTRA_LOGIN, adapter.listFavoriteUser)
            putParcelableArrayList(EXTRA_AVATAR_URL, adapter.listFavoriteUser)
            putParcelableArrayList(EXTRA_TYPE, adapter.listFavoriteUser)
        }
    }

    override fun onResume() {
        super.onResume()
        showListFavoriteUser()
        loadFavoriteUserAsync()
    }

    override fun onDestroy() {
        super.onDestroy()
        favoriteUserHelper.close()
    }

    // fungsi back button support action bar
    override fun onSupportNavigateUp(): Boolean {
        super.onBackPressed()
        return true
    }

}