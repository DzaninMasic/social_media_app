package com.example.social_media.domain.post

import android.net.Uri

data class Post constructor(
    val description: String? = null,
    val userName: String? = null,
    val userId: String? = null,
    val profilePicture: String? = null
)
