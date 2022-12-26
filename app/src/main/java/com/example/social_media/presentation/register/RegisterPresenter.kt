package com.example.social_media.presentation.register

import android.util.Log
import com.example.social_media.data.repository.DataRepository
import com.facebook.AccessToken
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.AuthCredential

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
        val taskResult = dataRepository.loginWithFacebook(token)
        taskResult.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                view?.displaySuccess()
            } else {
                view?.displayError()
            }
        }
    }

    fun signInWithGoogle(account: GoogleSignInAccount, credential: AuthCredential){
        val taskResult = dataRepository.loginWithGoogle(account, credential)
        taskResult.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                view?.displaySuccess()
            } else {
                view?.displayError()
            }
        }
    }

    fun attachView(view: RegisterView){
        this.view = view
    }
    fun detachView(){
        this.view = null
    }
}