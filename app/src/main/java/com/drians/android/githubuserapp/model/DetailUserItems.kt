package com.drians.android.githubuserapp.model

data class DetailUserItems(
    var id: Int = 0,
    var login: String? = null,
    var avatar_url: String? = null,
    var name: String? = null,
    var company: String? = null,
    var location: String? = null,
    var public_repos: String? = null,
    var followers: String? = null,
    var following: String? = null
)