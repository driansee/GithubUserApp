package com.drians.android.githubuserapp.ui.detail

import android.content.ContentValues
import android.content.Intent
import android.database.sqlite.SQLiteConstraintException
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.drians.android.githubuserapp.R
import com.drians.android.githubuserapp.adapter.TabsPagerAdapter
import com.drians.android.githubuserapp.database.DatabaseContract
import com.drians.android.githubuserapp.database.FavoriteUserHelper
import com.drians.android.githubuserapp.ui.settings.SettingsActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail_tabs.*


class DetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_LOGIN = "extra_login"
        const val EXTRA_AVATAR_URL = "extra_avatar_url"
        const val EXTRA_TYPE = "extra_type"
    }

    private lateinit var detailViewModel: DetailViewModel
    private lateinit var favoriteUserHelper: FavoriteUserHelper
    private lateinit var username: String
    private lateinit var avatarUrl: String
    private lateinit var type: String
    private var menuItem: Menu? = null
    private var isFavorite: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)
        supportActionBar?.title = null
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        favoriteUserHelper = FavoriteUserHelper.getInstance(applicationContext)
        favoriteUserHelper.open()

        showTabs()

        detailViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(DetailViewModel::class.java)

        username = intent?.getStringExtra(EXTRA_LOGIN).toString()

        detailViewModel.setDetailUser(username)

        detailViewModel.getDetailUser().observe(this, Observer { detailUserItems ->
            if (detailUserItems != null) {
                Glide.with(this)
                    .load(detailUserItems[0].avatar_url)
                    .apply(RequestOptions()
                        .placeholder(R.drawable.ic_account_circle)
                        .error(R.drawable.ic_account_circle)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .priority(Priority.HIGH))
                    .into(imageAvatarHeader)
                toolbar_layout.title = detailUserItems[0].name
            }
        })

        favoriteState()
    }

    // fungsi untuk menampilkan tabs
    private fun showTabs() {
        val sectionsPagerAdapter = TabsPagerAdapter(this, supportFragmentManager)
        viewPager.adapter = sectionsPagerAdapter
        tabs.setupWithViewPager(viewPager)
    }

    // fungsi untuk mengecek status apakah favorite user sudah masuk ke dalam database atau belum
    private fun favoriteState(){
        username = intent?.getStringExtra(EXTRA_LOGIN).toString()
        val result = favoriteUserHelper.queryByLogin(username)
        val favorite = (1 .. result.count).map {
            result.apply {
                moveToNext()
                getInt(result.getColumnIndexOrThrow(DatabaseContract.FavoriteUserColumns.LOGIN))
            }
        }
        if (favorite.isNotEmpty()) isFavorite = true
    }

    // fungsi untuk menambahkan favorite user ke database sqlite
    private fun addFavoriteUser() {
        try {
            username = intent?.getStringExtra(EXTRA_LOGIN).toString()
            avatarUrl = intent?.getStringExtra(EXTRA_AVATAR_URL).toString()
            type = intent?.getStringExtra(EXTRA_TYPE).toString()

            val values = ContentValues().apply {
                put(DatabaseContract.FavoriteUserColumns.LOGIN, username)
                put(DatabaseContract.FavoriteUserColumns.AVATAR_URL, avatarUrl)
                put(DatabaseContract.FavoriteUserColumns.TYPE, type)
            }
            favoriteUserHelper.insert(values)

            showSnackbarMessage("Added to favorite")
            Log.d("INSERT VALUES ::::: ", values.toString())
        } catch (e: SQLiteConstraintException) {
            showSnackbarMessage(""+e.localizedMessage)
        }
    }

    // fungsi untuk menghapus data favorite user dari database sqlite
    private fun removeFavoriteUser(){
        try {
            username = intent?.getStringExtra(EXTRA_LOGIN).toString()
            val result = favoriteUserHelper.deleteByLogin(username)

            showSnackbarMessage("Removed to favorite")
            Log.d("REMOVE VALUES ::::: ", result.toString())
        } catch (e: SQLiteConstraintException){
            showSnackbarMessage(""+e.localizedMessage)
        }
    }

    // fungsi untuk mengatur ikon pada tombol Favorite
    private fun setFavorite() {
        if (isFavorite)
            menuItem?.getItem(0)?.icon = ContextCompat.getDrawable(this, R.drawable.ic_added_favorite)
        else
            menuItem?.getItem(0)?.icon = ContextCompat.getDrawable(this, R.drawable.ic_add_favorite)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_detail, menu)
        menuItem = menu
        setFavorite()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.menu_favorite -> {
                if (isFavorite) removeFavoriteUser() else addFavoriteUser()

                isFavorite = !isFavorite
                setFavorite()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    // fungsi back button support action bar
    override fun onSupportNavigateUp(): Boolean {
        super.onBackPressed()
        return true
    }

    // Tampilkan snackbar
    private fun showSnackbarMessage(message: String) {
        Snackbar.make(viewPager, message, Snackbar.LENGTH_SHORT).show()
    }

}