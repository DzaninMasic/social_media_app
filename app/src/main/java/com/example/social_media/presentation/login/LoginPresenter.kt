package com.example.social_media.presentation.login

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.navigation.Navigation
import com.example.social_media.R
import com.example.social_media.data.repository.DataRepository
import com.facebook.AccessToken
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable

class LoginPresenter {
    private val dataRepository = DataRepository()

    private var view: LoginView? = null

    fun signInWithGoogle(credential: AuthCredential, googleSignInClient: GoogleSignInClient?) {
        val observable = dataRepository.loginWithGoogle(credential, googleSignInClient)
        observable.subscribe(object : Observer<FirebaseUser> {
            override fun onNext(t: FirebaseUser) {
                view?.displaySuccess()
            }

            override fun onError(e: Throwable) {
                view?.displayError()
            }

            override fun onSubscribe(d: Disposable) {}

            override fun onComplete() {}

        })
    }

    fun signInWithFacebook(token: AccessToken){
        val observable = dataRepository.loginWithFacebook(token)
        observable.subscribe(object : Observer<FirebaseUser> {
            override fun onNext(t: FirebaseUser) {
                view?.displaySuccess()
            }

            override fun onError(e: Throwable) {
                view?.displayError()
            }

            override fun onSubscribe(d: Disposable) {}

            override fun onComplete() {}

        })
    }

    fun signInWithFirebase(email: String, password: String){
        val observable = dataRepository.loginWithFirebase(email, password)
        observable.subscribe(object : Observer<Unit>{
            override fun onSubscribe(d: Disposable) {

            }

            override fun onNext(t: Unit) {
                view?.displaySuccess()
            }

            override fun onError(e: Throwable) {
                view?.displayError()
            }

            override fun onComplete() {

            }

        })
    }

    fun attachView(view: LoginView){
        this.view = view
    }

    fun detachView(){
        this.view = null
    }
}