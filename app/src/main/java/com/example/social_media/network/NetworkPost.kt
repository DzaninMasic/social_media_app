package com.example.social_media.network

import com.example.social_media.domain.post.Comment
import com.example.social_media.domain.post.Like

data class NetworkPost constructor(
    var postId: String? = null,
    val description: String? = null,
    val userName: String? = null,
    val userId: String? = null,
    val profilePicture: String? = null,
    val postPicture: String? = null,
    val likes: HashMap<String, Like>? = null,
    val comments: List<Comment>? = null
)