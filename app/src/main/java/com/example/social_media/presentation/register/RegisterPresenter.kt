package com.example.social_media.presentation.register

import android.util.Log
import com.example.social_media.data.repository.DataRepository
import com.facebook.AccessToken
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseUser
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable

class RegisterPresenter {
    private val dataRepository = DataRepository()

    private var view: RegisterView? = null

    fun signUpUserWithFirebase(name: String, email: String, password: String){
        val taskResult = dataRepository.registerUserWithFirebase(email, password)
        //TASK RESULT
        taskResult.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.w("TAG", "createUserWithEmail: success")
                // USER HAS BEEN CREATED, CALL FUNCTION TO UPDATE THE DISPLAY NAME OF THE USER
                val updateName = dataRepository.updateName(name)
                updateName.addOnCompleteListener { task2 ->
                        if (task2.isSuccessful) {
                            Log.d("TAG", "User profile updated.")
                            view?.displaySuccess()
                        }
                        else {
                            Log.w("TAG", "createUserWithEmail: failure", task2.exception)
                            view?.displayError()
                        }
                    }
            } else {
                // If sign in fails, display a message to the user.
                Log.w("TAG", "createUserWithEmail: failure", task.exception)
                // USER HAS NOT BEEN CREATED, CALL VIEW FUNCTION TO DISPLAY THAT USER HAS NOT BEEN CREATED
                view?.displayError()
            }
        }
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

    fun signInWithGoogle(credential: AuthCredential){
        val observable = dataRepository.loginWithGoogle(credential)
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