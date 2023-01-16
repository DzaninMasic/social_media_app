package com.example.social_media.presentation.home.feed

import com.example.social_media.domain.post.DomainPost

interface FeedView {
    fun showData(items: MutableList<DomainPost>)
    fun displayError(error: String)
    fun displayDeleteSuccess()
    fun onLike(position: Int)
    fun onComment(position: Int, comment: String)
    fun onDelete(position: String)
    fun currentUser(): String?
}