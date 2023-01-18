package com.example.social_media.presentation.home.feed

import com.example.social_media.domain.post.DomainPost

interface FeedView {
    fun showData(items: MutableList<DomainPost>)
    fun displayError(error: String)
    fun displayDeleteSuccess()
    fun onLike(postId: String)
    fun onComment(position: Int, comment: String, postId: String?)
    fun onDeletePost(position: String)
    fun onDeleteComment(commentPosition: String?, postPosition: String?)
}