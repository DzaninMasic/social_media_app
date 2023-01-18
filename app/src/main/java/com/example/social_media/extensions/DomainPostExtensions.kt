package com.example.social_media.extensions

import com.example.social_media.data.network.NetworkPost

fun NetworkPost.canUserDelete(userId: String) : Boolean {
    return userId == this.userId
}