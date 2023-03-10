package com.example.social_media.data.network

import com.example.social_media.domain.post.NetworkLike

data class NetworkPost constructor(
    var postId: String? = null,
    val description: String? = null,
    val userName: String? = null,
    val userId: String? = null,
    val profilePicture: String? = null,
    val postPicture: String? = null,
    var likes: HashMap<String, NetworkLike>? = null,
    val comments: HashMap<String, NetworkComment>? = null
)