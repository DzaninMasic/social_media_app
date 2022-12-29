package com.example.social_media.domain.post

import android.net.Uri

data class Post(
    val description: String = "DEFAULT",
    val userName: String = "USERNAME",
    val userId: String = "DEFAULT UID",
    val profilePicture: Uri = Uri.parse("https://upload.wikimedia.org/wikipedia/commons/thumb/2/2c/Default_pfp.svg/1200px-Default_pfp.svg.png")
)
