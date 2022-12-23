package com.example.social_media.data.datasource

import android.content.Intent
import android.widget.Toast
import com.facebook.AccessToken
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.*

class AuthDataSource {
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun createFirebaseUser(email: String, password: String) : Task<AuthResult> {
        return auth.createUserWithEmailAndPassword(email, password)
    }

    fun getGoogleUser(account: GoogleSignInAccount, credential: AuthCredential) : Task<AuthResult> {
        return auth.signInWithCredential(credential)
    }

    fun getFacebookUser(token: AccessToken) : Task<AuthResult>{
        val fbCredential = FacebookAuthProvider.getCredential(token.token)
        return auth.signInWithCredential(fbCredential)
    }

    fun getFirebaseUser(email: String, password: String) : Task<AuthResult> {
        return auth.signInWithEmailAndPassword(email, password)
    }

    fun getLoggedInUser(): FirebaseUser? {
        return auth.currentUser
    }

}