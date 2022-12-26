package com.example.social_media.data.repository

import android.content.Intent
import com.example.social_media.data.datasource.AuthDataSource
import com.example.social_media.data.datasource.DatabaseDataSource
import com.example.social_media.domain.post.Post
import com.facebook.AccessToken
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class DataRepository {
    private val authDataSource = AuthDataSource()
    private val databaseDataSource = DatabaseDataSource()
    fun registerUserWithFirebase(email: String, password: String): Task<AuthResult> {
        return authDataSource.createFirebaseUser(email,password)
    }

    fun updateName(name: String) : Task<Void> {
        return authDataSource.updateUserDisplayName(name)
    }

    fun loginWithGoogle(account: GoogleSignInAccount, credential: AuthCredential) : Task<AuthResult>{
        return authDataSource.getGoogleUser(account, credential)
    }

    fun loginWithFacebook(token: AccessToken): Task<AuthResult>{
        return authDataSource.getFacebookUser(token)
    }

    fun loginWithFirebase(email: String, password: String): Task<AuthResult>{
        return authDataSource.getFirebaseUser(email, password)
    }

    fun getCurrentUser(): FirebaseUser? {
        return authDataSource.getLoggedInUser()
    }

    fun addPost(text: String) : Task<Void>{
        return databaseDataSource.addPostToDB(text)
    }

    fun getPostData(
        onSuccess: (posts: List<Post>) -> Unit,
        onFailure: (error: String) -> Unit
    ){
        databaseDataSource.getPostsFromDB(onSuccess, onFailure)
    }
}