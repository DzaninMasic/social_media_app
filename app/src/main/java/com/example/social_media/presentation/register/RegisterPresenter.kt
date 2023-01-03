package com.example.social_media.presentation.register

import android.util.Log
import com.example.social_media.data.repository.DataRepository
import com.facebook.AccessToken
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseUser
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable

class RegisterPresenter {
    private val dataRepository = DataRepository()

    private var view: RegisterView? = null

    fun signUpUserWithFirebase(name: String, email: String, password: String){
        val observable = dataRepository.registerUserWithFirebase(email, password, name)
        observable.subscribe(object: Observer<Unit>{
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

    fun signUpWithFacebook(token: AccessToken){
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

    fun signInWithGoogle(credential: AuthCredential, googleSignInClient: GoogleSignInClient?){
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

    fun attachView(view: RegisterView){
        this.view = view
    }
    fun detachView(){
        this.view = null
    }
}