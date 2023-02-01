package com.example.social_media.presentation.home.feed.adapters

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.social_media.databinding.ItemLayoutBinding
import com.example.social_media.domain.post.DomainPost
import com.example.social_media.domain.post.toNetworkPost
import com.example.social_media.presentation.home.feed.FeedView


class FeedFragmentAdapter(private val context: Context, private val feedView: FeedView) : RecyclerView.Adapter<FeedFragmentAdapter.FeedViewHolder>() {

    private var list: List<DomainPost> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder {
        val layoutInflater = LayoutInflater.from(context)
        val binding = ItemLayoutBinding.inflate(layoutInflater,parent,false)
        return FeedViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {
        val descriptions = list[holder.adapterPosition].description
        val userName = list[holder.adapterPosition].userName
        val profilePicture = list[holder.adapterPosition].profilePicture
        val postPicture = list[holder.adapterPosition].postPicture
        holder.description.text = descriptions
        holder.userName.text = userName

        val adapter = CommentAdapter(context, feedView)
        holder.commentRecyclerView.adapter = adapter
        holder.commentRecyclerView.layoutManager = LinearLayoutManager(context)
        adapter.setData(list[holder.adapterPosition].comments?.values?.toList().orEmpty())

        holder.likeButton.isSelected = list[holder.adapterPosition].isLiked

        if(list[holder.adapterPosition].likes?.values.isNullOrEmpty()){
            holder.likeCount.text = "0 people liked this post."
        }else{
            holder.likeCount.text = "${list[holder.adapterPosition].likes?.values?.size} people liked this post."
        }
        if(profilePicture.equals("null")){
            holder.profilePicture.isVisible = true
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
            list[holder.adapterPosition].postId?.let { postId -> feedView.onLike(postId) }
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
                holder.commentEditText.requestFocus()
                val imm = getSystemService(context, InputMethodManager::class.java)
                imm?.showSoftInput(holder.commentEditText, InputMethodManager.SHOW_IMPLICIT)
            }
        }
        holder.addCommentButton.setOnClickListener{
            val comment = holder.commentEditText.text.toString()
            if(comment.trim().equals("")) feedView.displayError("Comment cannot be empty!")
            else{
                holder.commentEditText.text.clear();
                holder.commentEditText.clearFocus();
                feedView.onComment(comment, list[holder.adapterPosition].postId)
            }
        }
        holder.deleteButton.isVisible = list[holder.adapterPosition].canDelete
        holder.deleteButton.setOnClickListener {
            feedView.onDeletePost(list[holder.adapterPosition].toNetworkPost())
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class FeedViewHolder(itemView: ItemLayoutBinding) : RecyclerView.ViewHolder(itemView.root){
        var description: TextView = itemView.textDescription
        var userName: TextView = itemView.textName
        val profilePicture: ImageView = itemView.profilePictureImageView
        val postPicture: ImageView = itemView.postImageView
        val likeButton: ImageView = itemView.likeImageView
        val commentButton: ImageView = itemView.commentImageView
        val likeCount: TextView = itemView.likeCount
        val commentRecyclerView: RecyclerView = itemView.commentRecyclerView
        val deleteButton: ImageView = itemView.deleteBtn
        val commentEditText: EditText = itemView.addCommentEt
        val addCommentButton: Button = itemView.addCommentBtn
    }

    fun setData(items: List<DomainPost>){
        val diffResult = DiffUtil.calculateDiff(com.example.social_media.util.PostDiffUtil(list,items))
        this.list = items
        diffResult.dispatchUpdatesTo(this)
    }
}