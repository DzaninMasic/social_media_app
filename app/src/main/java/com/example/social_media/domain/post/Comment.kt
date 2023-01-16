package com.example.social_media.domain.post

data class Comment constructor(
    var commentId: String? = null,
    val userId: String? = null,
    val userName: String? = null,
    val comment: String? = null,
)
