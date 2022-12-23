package com.example.social_media.presentation.home.feed.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.social_media.R
import com.example.social_media.domain.post.Post

class FeedFragmentAdapter(private val context: Context) : RecyclerView.Adapter<FeedFragmentAdapter.MyViewHolder>() {

    private var list: List<Post> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(context)
        val itemView = layoutInflater.inflate(R.layout.item_layout, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val descriptions = list[position].description
        holder.description.text = descriptions
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var description: TextView = itemView.findViewById(R.id.textDescription)
    }

    fun setData(items: List<Post>){
        Log.i("DZANINPOSTS", "setData: ${list}")
        this.list = items
        Log.i("DZANINPOSTS", "setData AFTER: ${list}")
        this.notifyDataSetChanged()
    }
}