package com.drians.android.githubuserapp.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FavoriteUserItems(
    var id: Int = 0,
    var login: String? = null,
    var avatar_url: String? = null,
    var type: String? = null
) : Parcelable