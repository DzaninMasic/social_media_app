package com.example.social_media.util

import com.example.social_media.domain.post.DomainPost

class PostDiffUtil (private val oldList: List<DomainPost>, private val newList: List<DomainPost>) : androidx.recyclerview.widget.DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].postId == newList[newItemPosition].postId
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

}