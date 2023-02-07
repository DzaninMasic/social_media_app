package com.example.social_media.presentation.home.settings

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

interface SettingsView {
    fun displayProfile(auth: FirebaseUser)
    fun displayError()
    fun displaySuccessfulImageUpload(uri : Uri)
    fun displayFailedImageUpload()
    fun showLoaded()
}