package com.example.social_media.extensions

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar

fun Fragment.hideKeyboard() {
    view?.let { activity?.hideKeyboard(it) }
}

fun Fragment.showSuccesfulImageUpload(){
    Snackbar.make(requireView(), "Image uploaded!", Snackbar.LENGTH_SHORT).show()
}
