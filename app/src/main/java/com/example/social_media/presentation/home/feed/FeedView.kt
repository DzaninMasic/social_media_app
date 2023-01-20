package com.example.social_media.presentation.home.feed

import com.example.social_media.data.network.NetworkPost
import com.example.social_media.domain.post.DomainPost

interface FeedView {
    fun showData(items: MutableList<DomainPost>)
    fun displayError(error: String)
    fun displayDeleteSuccess(position: String)
    fun onLike(postId: String)
    fun onComment(comment: String, postId: String?)
    fun onDeletePost(post: NetworkPost)
    fun onDeleteComment(commentPosition: String, postPosition: String?)
}