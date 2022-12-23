package com.example.social_media.presentation.home.settings

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

interface SettingsView {
    fun displayProfile(auth: FirebaseUser)
    fun displayError()
}