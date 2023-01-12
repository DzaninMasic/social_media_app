package com.example.social_media.presentation.home.feed.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.social_media.R

class CommentAdapter(private val context: Context) : RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val layoutInflater = LayoutInflater.from(context)
        val itemView = layoutInflater.inflate(R.layout.comment_view, parent, false)
        return CommentViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return 2
    }

    class CommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val testTv: TextView = itemView.findViewById(R.id.testTv)
    }
}