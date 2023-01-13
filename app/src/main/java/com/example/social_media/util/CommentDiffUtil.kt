package com.example.social_media.util

import androidx.recyclerview.widget.DiffUtil
import com.example.social_media.domain.post.Comment

class CommentDiffUtil(private val oldList: List<Comment>, private val newList: List<Comment>) : DiffUtil.Callback(){
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].commentId == newList[newItemPosition].commentId
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}