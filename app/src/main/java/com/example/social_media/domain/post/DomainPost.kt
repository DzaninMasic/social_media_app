package com.example.social_media.domain.post

data class DomainPost(
    var postId: String? = null,
    val description: String? = null,
    val userName: String? = null,
    val userId: String? = null,
    val profilePicture: String? = null,
    val postPicture: String? = null,
    val likes: HashMap<String, Like>? = null,
    val comments: List<Comment>? = null,
    val canDelete: Boolean? = false
)
