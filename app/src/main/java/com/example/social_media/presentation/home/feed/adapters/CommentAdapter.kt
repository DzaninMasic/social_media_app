package com.example.social_media.presentation.home.feed.adapters

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.social_media.R
import com.example.social_media.common.model.NetworkConnection
import com.example.social_media.databinding.CommentViewBinding
import com.example.social_media.domain.post.DomainComment
import com.example.social_media.presentation.home.feed.FeedView
import com.example.social_media.util.CommentDiffUtil

class CommentAdapter(private val context: Context, private val feedView: FeedView) : RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {

    private var list: List<DomainComment> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val layoutInflater = LayoutInflater.from(context)
        val binding = CommentViewBinding.inflate(layoutInflater,parent,false)
        return CommentViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val name = list[position].userName
        val comment = list[position].comment
        holder.nameTv.text = name
        holder.commentTv.text = comment

        if(list[position].canDelete == true){
            holder.deleteBtn.isVisible = true
            holder.deleteBtn.setOnClickListener {
                if(!NetworkConnection.isOnline(context)){
                    feedView.displayError("Cannot delete comment. No internet connection!")
                }else{
                    list[position].commentId?.let { it1 -> feedView.onDeleteComment(it1, list[position].postId) }
                }
            }
        }
        else holder.deleteBtn.isVisible = false
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class CommentViewHolder(itemView: CommentViewBinding) : RecyclerView.ViewHolder(itemView.root){
        val nameTv: TextView = itemView.nameTv
        val commentTv: TextView = itemView.commentTv
        val deleteBtn: ImageView = itemView.commentDeleteBtn
    }

    fun setData(list: List<DomainComment>){
        val diffResult = DiffUtil.calculateDiff(CommentDiffUtil(this.list, list))
        this.list = list
        diffResult.dispatchUpdatesTo(this)
    }
}