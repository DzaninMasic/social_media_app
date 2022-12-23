package com.example.social_media.presentation.login

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.navigation.Navigation
import com.example.social_media.R
import com.example.social_media.data.repository.DataRepository
import com.facebook.AccessToken
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.GoogleAuthProvider

class LoginPresenter {
    private val dataRepository = DataRepository()

    private var view: LoginView? = null

    fun signInWithGoogle(account: GoogleSignInAccount, credential: AuthCredential) {
        val taskResult = dataRepository.loginWithGoogle(account, credential)
        taskResult.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                view?.displaySuccess()
            } else {
                view?.displayError()
            }
        }
    }

    fun signInWithFacebook(token: AccessToken){
        val taskResult = dataRepository.loginWithFacebook(token)
        taskResult.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                view?.displaySuccess()
            } else {
                view?.displayError()
            }
        }
    }

    fun signInWithFirebase(email: String, password: String){
        val taskResult = dataRepository.loginWithFirebase(email, password)
        taskResult.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                view?.displaySuccess()
            } else {
                view?.displayError()
            }
        }
    }

    fun attachView(view: LoginView){
        this.view = view
    }

    fun detachView(){
        this.view = null
    }
}