package com.example.social_media.domain.post

import com.example.social_media.data.network.NetworkComment

data class DomainComment constructor(
    var commentId: String? = null,
    val userId: String? = null,
    val userName: String? = null,
    val comment: String? = null,
    var canDelete: Boolean? = null,
    val postId: String? = null
)

fun DomainComment.toNetworkComment() : NetworkComment{
    return NetworkComment(
        commentId,
        userId,
        userName,
        comment,
        postId
    )
}
