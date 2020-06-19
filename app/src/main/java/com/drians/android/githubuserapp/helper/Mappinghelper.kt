package com.drians.android.githubuserapp.helper

import android.database.Cursor
import com.drians.android.githubuserapp.database.DatabaseContract
import com.drians.android.githubuserapp.model.FavoriteUserItems
import java.util.ArrayList


object MappingHelper {

    fun mapCursorToArrayList(favoriteUserCursor: Cursor?): ArrayList<FavoriteUserItems> {
        val favoriteUserItemsList = ArrayList<FavoriteUserItems>()

        favoriteUserCursor?.apply {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(DatabaseContract.FavoriteUserColumns._ID))
                val login = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteUserColumns.LOGIN))
                val avatarUrl = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteUserColumns.AVATAR_URL))
                val type = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteUserColumns.TYPE))
                favoriteUserItemsList.add(FavoriteUserItems(id, login, avatarUrl, type))
            }
        }
        return favoriteUserItemsList
    }
}