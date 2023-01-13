package com.example.social_media.domain.post

import com.example.social_media.common.model.BaseModel

data class Comment constructor(
    var commentId: String? = null,
    val userId: String? = null,
    val userName: String? = null,
    val comment: String? = null,
)
