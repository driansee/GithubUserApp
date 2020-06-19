package com.drians.android.githubuserapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.drians.android.githubuserapp.R
import com.drians.android.githubuserapp.model.FollowingItems
import kotlinx.android.synthetic.main.items_following.view.*

class FollowingAdapter : RecyclerView.Adapter<FollowingAdapter.FollowingViewHolder>() {

    private val mData = ArrayList<FollowingItems>()

    fun setData(items: ArrayList<FollowingItems>) {
        mData.clear()
        mData.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, position: Int): FollowingViewHolder {
        val mView = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.items_following, viewGroup, false)
        return FollowingViewHolder(mView)
    }

    override fun onBindViewHolder(followingViewHolder: FollowingViewHolder, position: Int) {
        followingViewHolder.bind(mData[position])
    }

    override fun getItemCount(): Int = mData.size

    inner class FollowingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(followingItems: FollowingItems) {
            with(itemView) {
                textLogin.text = followingItems.login
                textType.text = followingItems.type
                Glide.with(context)
                    .load(followingItems.avatar_url)
                    .apply(RequestOptions()
                        .override(56,56)
                        .placeholder(R.drawable.ic_account_circle)
                        .error(R.drawable.ic_account_circle)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .priority(Priority.HIGH))
                    .into(imageAvatar)
            }
        }
    }

}
