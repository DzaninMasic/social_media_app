package com.example.social_media.presentation.home.feed.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.social_media.R
import com.example.social_media.domain.post.Comment
import com.example.social_media.util.CommentDiffUtil

class CommentAdapter(private val context: Context) : RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {

    private var list: List<Comment> = emptyList()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val layoutInflater = LayoutInflater.from(context)
        val itemView = layoutInflater.inflate(R.layout.comment_view, parent, false)
        return CommentViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val comment = list[position].comment
        Log.i("COMMENTADAPTER", "onBindViewHolder: $comment")
        holder.testTv.text = comment
        Log.i("COMMENTADAPTER", "onBindViewHolder HOLDER: ${holder.testTv.text}")

    }

    override fun getItemCount(): Int {
        return list.size
    }

    class CommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val testTv: TextView = itemView.findViewById(R.id.testTv)
    }

    fun setData(list: List<Comment>){
        val diffResult = DiffUtil.calculateDiff(CommentDiffUtil(this.list, list))
        this.list = list
        diffResult.dispatchUpdatesTo(this)
    }
}