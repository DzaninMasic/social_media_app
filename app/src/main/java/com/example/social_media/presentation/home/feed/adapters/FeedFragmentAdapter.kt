package com.example.social_media.presentation.home.feed.adapters

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.social_media.R
import com.example.social_media.domain.post.Post
import com.example.social_media.presentation.home.feed.FeedView

class FeedFragmentAdapter(private val context: Context, private val feedView: FeedView) : RecyclerView.Adapter<FeedFragmentAdapter.MyViewHolder>() {

    private var list: List<Post> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(context)
        val itemView = layoutInflater.inflate(R.layout.item_layout, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val descriptions = list[position].description
        val userName = list[position].userName
        val profilePicture = list[position].profilePicture
        val postPicture = list[position].postPicture
        holder.description.text = descriptions
        holder.userName.text = userName
        if(profilePicture == null){
            holder.profilePicture.isVisible = false
        }else{
            Glide.with(context).load(Uri.parse(profilePicture)).circleCrop().into(holder.profilePicture)
        }
        if(postPicture.equals("")){
            holder.postPicture.isVisible = false
        }else{
            holder.postPicture.isVisible = true
            Glide.with(context).load(Uri.parse(postPicture)).into(holder.postPicture)
        }
        holder.likeButton.setOnClickListener {
            feedView.onLike(list.size-position-1)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var description: TextView = itemView.findViewById(R.id.textDescription)
        var userName: TextView = itemView.findViewById(R.id.textName)
        val profilePicture: ImageView = itemView.findViewById(R.id.profilePictureImageView)
        val postPicture: ImageView = itemView.findViewById(R.id.postImageView)
        val likeButton: ImageView = itemView.findViewById(R.id.likeImageView)
    }

    fun setData(items: List<Post>){
        this.list = items
        this.notifyDataSetChanged()
    }
}