package com.example.social_media.presentation.home.feed

import com.example.social_media.domain.post.Post

interface FeedView {
    fun showData(items: List<Post>)
    fun displayError(error: String)
    fun onLike(position: Int)
}