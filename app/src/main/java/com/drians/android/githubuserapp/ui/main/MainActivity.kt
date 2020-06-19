package com.drians.android.githubuserapp.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView.OnEditorActionListener
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.drians.android.githubuserapp.R
import com.drians.android.githubuserapp.adapter.UserAdapter
import com.drians.android.githubuserapp.ui.detail.DetailActivity
import com.drians.android.githubuserapp.ui.favorite.FavoriteUserActivity
import com.drians.android.githubuserapp.ui.settings.SettingsActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_LOGIN = "extra_login"
        const val EXTRA_AVATAR_URL = "extra_avatar_url"
        const val EXTRA_TYPE = "extra_type"
    }

    private lateinit var adapter: UserAdapter
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.setTitle(R.string.title_activity_main)

        showListUser()

        mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(MainViewModel::class.java)

        editTextSearch.setOnEditorActionListener(OnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                val username = textInputSearch.editText?.text.toString()

                if (username.isEmpty()) return@OnEditorActionListener true

                val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(editTextSearch.windowToken, 0)

                showLoading(true)
                mainViewModel.setUser(username)
            }
            false
        })

        mainViewModel.getUsers().observe(this, Observer { userItems ->
            if (userItems != null) {
                adapter.setData(userItems)
                showLoading(false)
            }
        })

    }

    private fun showListUser() {
        recyclerviewUser.layoutManager = LinearLayoutManager(this)

        adapter = UserAdapter {
            val intent = Intent(this@MainActivity, DetailActivity::class.java)
            intent.apply {
                putExtra(EXTRA_LOGIN, it.login)
                putExtra(EXTRA_AVATAR_URL, it.avatar_url)
                putExtra(EXTRA_TYPE, it.type)
            }
            startActivity(intent)
        }

        recyclerviewUser.adapter = adapter
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_favorite -> {
                val intent = Intent(this, FavoriteUserActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.menu_settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    // fungsi untuk menampilkan alert dialog saat ingin menutup aplikasi
    override fun onBackPressed() {
        MaterialAlertDialogBuilder(this@MainActivity)
            .setIcon(R.mipmap.ic_launcher_foreground)
            .setTitle(resources.getString(R.string.dialog_title))
            .setMessage(resources.getString(R.string.dialog_supporting_text))
            .setNegativeButton(resources.getString(R.string.dialog_cancel)) { dialog, _ ->
                dialog.cancel()
            }
            .setPositiveButton(resources.getString(R.string.dialog_yes)) { _, _ ->
                finish()
            }
            .show()
    }

}