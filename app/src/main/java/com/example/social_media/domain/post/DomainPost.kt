package com.example.social_media.domain.post

import com.example.social_media.data.network.NetworkComment
import com.example.social_media.data.network.NetworkPost

data class DomainPost(
    var postId: String? = null,
    val description: String? = null,
    val userName: String? = null,
    val userId: String? = null,
    val profilePicture: String? = null,
    val postPicture: String? = null,
    val likes: HashMap<String, NetworkLike>? = null,
    val comments: HashMap<String, DomainComment>? = null,
    val canDelete: Boolean = false,
    val isLiked: Boolean = false
)

fun DomainPost.toNetworkPost(): NetworkPost{
    val networkComments = comments?.mapValues { (_, value) ->
        value.toNetworkComment()
    }
    return NetworkPost(
        postId,
        description,
        userName,
        userId,
        profilePicture,
        postPicture,
        likes,
        networkComments as HashMap<String, NetworkComment>?
    )
}