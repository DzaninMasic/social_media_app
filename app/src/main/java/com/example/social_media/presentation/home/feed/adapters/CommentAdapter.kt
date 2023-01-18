package com.example.social_media.presentation.home.feed.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.social_media.R
import com.example.social_media.domain.post.Comment
import com.example.social_media.presentation.home.feed.FeedView
import com.example.social_media.util.CommentDiffUtil

class CommentAdapter(private val context: Context, private val feedView: FeedView) : RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {

    private var list: List<Comment> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val layoutInflater = LayoutInflater.from(context)
        val itemView = layoutInflater.inflate(R.layout.comment_view, parent, false)
        return CommentViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val name = list[position].userName
        val comment = list[position].comment
        holder.nameTv.text = name
        holder.commentTv.text = comment

        if(list[position].canDelete == true){
            holder.deleteBtn.isVisible = true
            holder.deleteBtn.setOnClickListener {
                feedView.onDeleteComment(list[position].commentId, list[position].postId)
            }
        }
        else holder.deleteBtn.isVisible = false
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class CommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val nameTv: TextView = itemView.findViewById(R.id.nameTv)
        val commentTv: TextView = itemView.findViewById(R.id.commentTv)
        val deleteBtn: ImageView = itemView.findViewById(R.id.commentDeleteBtn)
    }

    fun setData(list: List<Comment>){
        val diffResult = DiffUtil.calculateDiff(CommentDiffUtil(this.list, list))
        this.list = list
        diffResult.dispatchUpdatesTo(this)
    }
}