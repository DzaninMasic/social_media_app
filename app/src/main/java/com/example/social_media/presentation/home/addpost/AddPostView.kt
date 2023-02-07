package com.example.social_media.presentation.home.addpost

import android.net.Uri

interface AddPostView {
    fun showSuccessfulResponse()
    fun showFailedResponse(string: String)
    fun showChosenImage(uri: Uri)
}