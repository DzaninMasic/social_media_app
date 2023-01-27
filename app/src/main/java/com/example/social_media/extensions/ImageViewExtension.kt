package com.example.social_media.extensions

import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide

fun ImageView.loadImage(uri: Uri){
    Glide.with(this).load(uri).circleCrop().into(this)
}

fun ImageView.loadImage(url: String){
    Glide.with(this).load(url).circleCrop().into(this)
}