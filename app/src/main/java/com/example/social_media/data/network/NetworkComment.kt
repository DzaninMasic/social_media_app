package com.example.social_media.data.network

import com.example.social_media.domain.post.DomainComment

data class NetworkComment constructor(
    var commentId: String? = null,
    val userId: String? = null,
    val userName: String? = null,
    val comment: String? = null,
    val postId: String? = null
)

fun NetworkComment.toDomainComment(userId: String): DomainComment {
    return DomainComment(
        commentId,
        userId,
        userName,
        comment,
        userId == this.userId,
        postId
    )
}