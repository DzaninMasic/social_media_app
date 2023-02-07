package com.example.social_media.presentation.home.feed.adapters

import android.content.Context
import android.net.Uri
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.social_media.R
import com.example.social_media.common.model.NetworkConnection
import com.example.social_media.databinding.ItemLayoutBinding
import com.example.social_media.databinding.ItemViewNormalBinding
import com.example.social_media.domain.post.DomainPost
import com.example.social_media.domain.post.toNetworkPost
import com.example.social_media.presentation.home.feed.FeedView

class TestAdapter (private val context: Context, private val feedView: FeedView) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private var list: List<DomainPost> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(context)
        return if(viewType == OWNER_POST){
            val binding = ItemLayoutBinding.inflate(layoutInflater,parent,false)
            OwnerViewHolder(binding)
        }else{
            val binding = ItemViewNormalBinding.inflate(layoutInflater,parent,false)
            StandardUserViewHolder(binding)
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            OWNER_POST -> {
                val bindingHolder = holder as OwnerViewHolder
                val descriptions = list[bindingHolder.adapterPosition].description
                val userName = list[bindingHolder.adapterPosition].userName
                val profilePicture = list[bindingHolder.adapterPosition].profilePicture
                val postPicture = list[bindingHolder.adapterPosition].postPicture
                bindingHolder.view.textDescription.text = descriptions
                bindingHolder.view.textName.text = userName
                bindingHolder.view.postImageView.isClickable = true

                val adapter = CommentAdapter(context, feedView)
                bindingHolder.view.commentRecyclerView.adapter = adapter
                bindingHolder.view.commentRecyclerView.layoutManager = LinearLayoutManager(context)
                adapter.setData(list[bindingHolder.adapterPosition].comments?.values?.toList().orEmpty())

                bindingHolder.view.likeImageView.isSelected = list[bindingHolder.adapterPosition].isLiked

                if(list[bindingHolder.adapterPosition].likes?.values.isNullOrEmpty()){
                    bindingHolder.view.likeCount.text = "0 people liked this post."
                }else{
                    bindingHolder.view.likeCount.text = "${list[bindingHolder.adapterPosition].likes?.values?.size} people liked this post."
                }
                if(profilePicture.equals("null")){
                    Glide.with(context).load(R.drawable.rounded_profile_picture2).circleCrop().into(bindingHolder.view.profilePictureImageView)
                }else{
                    Glide.with(context).load(Uri.parse(profilePicture)).circleCrop().into(bindingHolder.view.profilePictureImageView)
                }
                if(postPicture.equals("")){
                    bindingHolder.view.postImageView.isVisible = false
                }else{
                    bindingHolder.view.postImageView.isVisible = true
                    Glide.with(context).load(Uri.parse(postPicture)).centerCrop().into(bindingHolder.view.postImageView)
                }
                bindingHolder.view.likeImageView.setOnClickListener {
                    feedView.onLike(list[bindingHolder.adapterPosition].toNetworkPost())
                }
                bindingHolder.view.commentImageView.setOnClickListener {
                    if(bindingHolder.view.commentRecyclerView.isVisible && bindingHolder.view.addCommentEt.isVisible && bindingHolder.view.addCommentBtn.isVisible){
                        bindingHolder.view.commentRecyclerView.isVisible = false
                        bindingHolder.view.addCommentEt.isVisible = false
                        bindingHolder.view.addCommentBtn.isVisible = false
                    }else{
                        bindingHolder.view.commentRecyclerView.isVisible = true
                        bindingHolder.view.addCommentEt.isVisible = true
                        bindingHolder.view.addCommentBtn.isVisible = true
                        bindingHolder.view.addCommentEt.requestFocus()
                        val imm =
                            ContextCompat.getSystemService(context, InputMethodManager::class.java)
                        imm?.showSoftInput(bindingHolder.view.addCommentEt, InputMethodManager.SHOW_IMPLICIT)
                    }
                }
                bindingHolder.view.addCommentBtn.setOnClickListener{
                    val comment = bindingHolder.view.addCommentEt.text.toString()
                    if(comment.trim().equals("")) feedView.displayError("Comment cannot be empty!")
                    else{
                        bindingHolder.view.addCommentEt.text.clear();
                        bindingHolder.view.addCommentEt.clearFocus();
                        feedView.onComment(comment, list[bindingHolder.adapterPosition].postId)
                    }
                }
                bindingHolder.view.postImageView.setOnClickListener {
                    bindingHolder.view.postImageView.isClickable = false
                    feedView.onImageClick(list[bindingHolder.adapterPosition].postPicture.toString())
                }
                bindingHolder.view.deleteBtn.setOnClickListener {
                    if(!NetworkConnection.isOnline(context)) {
                        feedView.displayError("Unable to delete post. No internet connection!")
                    }else{
                        feedView.onDeletePost(list[bindingHolder.adapterPosition].toNetworkPost())
                    }
                }
            }

            NORMAL_POST -> {
                val bindingHolder = holder as StandardUserViewHolder
                val descriptions = list[bindingHolder.adapterPosition].description
                val userName = list[bindingHolder.adapterPosition].userName
                val profilePicture = list[bindingHolder.adapterPosition].profilePicture
                val postPicture = list[bindingHolder.adapterPosition].postPicture
                bindingHolder.view.textDescription.text = descriptions
                bindingHolder.view.textName.text = userName
                bindingHolder.view.postImageView.isClickable = true

                val adapter = CommentAdapter(context, feedView)
                bindingHolder.view.commentRecyclerView.adapter = adapter
                bindingHolder.view.commentRecyclerView.layoutManager = LinearLayoutManager(context)
                adapter.setData(list[bindingHolder.adapterPosition].comments?.values?.toList().orEmpty())

                bindingHolder.view.likeImageView.isSelected = list[bindingHolder.adapterPosition].isLiked

                if(list[bindingHolder.adapterPosition].likes?.values.isNullOrEmpty()){
                    bindingHolder.view.likeCount.text = "0 people liked this post."
                }else{
                    bindingHolder.view.likeCount.text = "${list[bindingHolder.adapterPosition].likes?.values?.size} people liked this post."
                }
                if(profilePicture.equals("null")){
                    Glide.with(context).load(R.drawable.rounded_profile_picture2).circleCrop().into(bindingHolder.view.profilePictureImageView)
                }else{
                    Glide.with(context).load(Uri.parse(profilePicture)).circleCrop().into(bindingHolder.view.profilePictureImageView)
                }
                if(postPicture.equals("")){
                    bindingHolder.view.postImageView.isVisible = false
                }else{
                    bindingHolder.view.postImageView.isVisible = true
                    Glide.with(context).load(Uri.parse(postPicture)).centerCrop().into(bindingHolder.view.postImageView)
                }
                bindingHolder.view.likeImageView.setOnClickListener {
                    feedView.onLike(list[bindingHolder.adapterPosition].toNetworkPost())
                }
                bindingHolder.view.commentImageView.setOnClickListener {
                    if(bindingHolder.view.commentRecyclerView.isVisible && bindingHolder.view.addCommentEt.isVisible && bindingHolder.view.addCommentBtn.isVisible){
                        bindingHolder.view.commentRecyclerView.isVisible = false
                        bindingHolder.view.addCommentEt.isVisible = false
                        bindingHolder.view.addCommentBtn.isVisible = false
                    }else{
                        bindingHolder.view.commentRecyclerView.isVisible = true
                        bindingHolder.view.addCommentEt.isVisible = true
                        bindingHolder.view.addCommentBtn.isVisible = true
                        bindingHolder.view.addCommentEt.requestFocus()
                        val imm =
                            ContextCompat.getSystemService(context, InputMethodManager::class.java)
                        imm?.showSoftInput(bindingHolder.view.addCommentEt, InputMethodManager.SHOW_IMPLICIT)
                    }
                }
                bindingHolder.view.addCommentBtn.setOnClickListener{
                    val comment = bindingHolder.view.addCommentEt.text.toString()
                    if(comment.trim().equals("")) feedView.displayError("Comment cannot be empty!")
                    else{
                        bindingHolder.view.addCommentEt.text.clear();
                        bindingHolder.view.addCommentEt.clearFocus();
                        feedView.onComment(comment, list[bindingHolder.adapterPosition].postId)
                    }
                }
                bindingHolder.view.postImageView.setOnClickListener {
                    bindingHolder.view.postImageView.isClickable = false
                    feedView.onImageClick(list[bindingHolder.adapterPosition].postPicture.toString())
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun getItemViewType(position: Int): Int {
        return if(list[position].canDelete){
            OWNER_POST
        }else{
            NORMAL_POST
        }
    }

    class OwnerViewHolder(itemView: ItemLayoutBinding) : RecyclerView.ViewHolder(itemView.root){
        val view = itemView
    }

    class StandardUserViewHolder(itemView: ItemViewNormalBinding) : RecyclerView.ViewHolder(itemView.root){
        val view = itemView
    }

    fun setData(items: List<DomainPost>){
        val diffResult = DiffUtil.calculateDiff(com.example.social_media.util.PostDiffUtil(list,items))
        this.list = items
        diffResult.dispatchUpdatesTo(this)
    }
}

private const val OWNER_POST = 1
private const val NORMAL_POST = 0
