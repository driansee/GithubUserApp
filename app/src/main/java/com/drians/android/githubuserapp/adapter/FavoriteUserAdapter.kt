package com.drians.android.githubuserapp.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.drians.android.githubuserapp.R
import com.drians.android.githubuserapp.model.FavoriteUserItems
import kotlinx.android.synthetic.main.items_favorite.view.*
import java.util.ArrayList


class FavoriteUserAdapter(private val listener: (FavoriteUserItems) -> Unit) :
    RecyclerView.Adapter<FavoriteUserAdapter.FavoriteViewHolder>() {

    var listFavoriteUser = ArrayList<FavoriteUserItems>()
        set(listFavoriteUser) {
            if (listFavoriteUser.size > 0) {
                this.listFavoriteUser.clear()
            }
            this.listFavoriteUser.addAll(listFavoriteUser)
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.items_favorite, parent, false)
        return FavoriteViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(listFavoriteUser[position], listener)
    }

    override fun getItemCount(): Int = this.listFavoriteUser.size

    inner class FavoriteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(favoriteUserItems: FavoriteUserItems, listener: (FavoriteUserItems) -> Unit) {
            with(itemView){
                textLogin.text = favoriteUserItems.login
                textType.text = favoriteUserItems.type
                Glide.with(context)
                    .load(favoriteUserItems.avatar_url)
                    .apply(RequestOptions()
                        .override(56,56)
                        .placeholder(R.drawable.ic_account_circle)
                        .error(R.drawable.ic_account_circle)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .priority(Priority.HIGH))
                    .into(imageAvatar)
                setOnClickListener { listener(favoriteUserItems) }
            }
        }
    }
}