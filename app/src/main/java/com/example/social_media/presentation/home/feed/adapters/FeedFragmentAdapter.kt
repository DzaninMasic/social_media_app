package com.example.social_media.presentation.home.feed.adapters

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.social_media.R
import com.example.social_media.domain.post.DomainPost
import com.example.social_media.presentation.home.feed.FeedView

class FeedFragmentAdapter(private val context: Context, private val feedView: FeedView) : RecyclerView.Adapter<FeedFragmentAdapter.FeedViewHolder>() {

    private var list: List<DomainPost> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder {
        val layoutInflater = LayoutInflater.from(context)
        val itemView = layoutInflater.inflate(R.layout.item_layout, parent, false)
        return FeedViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {
        val descriptions = list[position].description
        val userName = list[position].userName
        val profilePicture = list[position].profilePicture
        val postPicture = list[position].postPicture
        holder.description.text = descriptions
        holder.userName.text = userName

        val adapter = CommentAdapter(context, feedView)
        holder.commentRecyclerView.adapter = adapter
        holder.commentRecyclerView.layoutManager = LinearLayoutManager(context)
        adapter.setData(list[position].comments ?: listOf())

        if(list[position].likes?.values.isNullOrEmpty()){
            holder.likeCount.text = "0 people liked this post."
        }else{
            holder.likeCount.text = "${list[position].likes?.values?.size} people liked this post."
        }
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
        holder.commentButton.setOnClickListener {
            if(holder.commentRecyclerView.isVisible && holder.commentEditText.isVisible && holder.addCommentButton.isVisible){
                holder.commentRecyclerView.isVisible = false
                holder.commentEditText.isVisible = false
                holder.addCommentButton.isVisible = false
            }else{
                holder.commentRecyclerView.isVisible = true
                holder.commentEditText.isVisible = true
                holder.addCommentButton.isVisible = true

            }
        }
        holder.addCommentButton.setOnClickListener{
            val comment = holder.commentEditText.text.toString()
            holder.commentEditText.text.clear()
            holder.commentEditText.clearFocus()
            feedView.onComment(list.size-position-1,comment, list[position].postId)
        }
        if(list[position].canDelete == true){
            holder.deleteButton.isVisible = true
            holder.deleteButton.setOnClickListener {
                holder.postViewLayout.isVisible = false
                list[position].postId?.let { it1 -> feedView.onDeletePost(it1) }
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class FeedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var description: TextView = itemView.findViewById(R.id.textDescription)
        var userName: TextView = itemView.findViewById(R.id.textName)
        val profilePicture: ImageView = itemView.findViewById(R.id.profilePictureImageView)
        val postPicture: ImageView = itemView.findViewById(R.id.postImageView)
        val likeButton: ImageView = itemView.findViewById(R.id.likeImageView)
        val commentButton: ImageView = itemView.findViewById(R.id.commentImageView)
        val likeCount: TextView = itemView.findViewById(R.id.likeCount)
        val commentRecyclerView: RecyclerView = itemView.findViewById(R.id.commentRecyclerView)
        val deleteButton: ImageView = itemView.findViewById(R.id.deleteBtn)
        val commentEditText: EditText = itemView.findViewById(R.id.addCommentEt)
        val addCommentButton: Button = itemView.findViewById(R.id.addCommentBtn)

        val postViewLayout: RelativeLayout = itemView.findViewById(R.id.postViewLayout)
    }

    fun setData(items: List<DomainPost>){
        val diffResult = DiffUtil.calculateDiff(com.example.social_media.util.PostDiffUtil(list,items))
        this.list = items
        diffResult.dispatchUpdatesTo(this)
    }
}