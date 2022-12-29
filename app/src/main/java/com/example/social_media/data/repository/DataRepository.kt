package com.example.social_media.data.repository

import android.content.Intent
import android.net.Uri
import android.util.Log
import com.example.social_media.data.datasource.AuthDataSource
import com.example.social_media.data.datasource.DatabaseDataSource
import com.example.social_media.data.datasource.StorageDataSource
import com.example.social_media.domain.post.Post
import com.facebook.AccessToken
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.storage.UploadTask
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.subjects.PublishSubject

class DataRepository {
    private val authDataSource = AuthDataSource()
    private val databaseDataSource = DatabaseDataSource()
    private val storageDataSource = StorageDataSource()

    fun registerUserWithFirebase(email: String, password: String, name: String): Observable<Unit> {
        return authDataSource.createFirebaseUser(email,password)
            .flatMap {
                authDataSource.updateUserDisplayName(name)
            }

        //return authDataSource.createFirebaseUser(email,password)
    }

    fun loginWithGoogle(credential: AuthCredential) : Observable<FirebaseUser>{
        return authDataSource.getGoogleUser(credential)
    }

    fun loginWithFacebook(token: AccessToken): Observable<FirebaseUser>{
        return authDataSource.getFacebookUser(token)
    }

    fun loginWithFirebase(email: String, password: String): Observable<Unit> {
        return authDataSource.getFirebaseUser(email, password)
    }

    fun getCurrentUser(): FirebaseUser? {
        return authDataSource.getLoggedInUser()
    }

    fun addPost(description: String) : Task<Void>?{
        val currentUser = authDataSource.getLoggedInUser()?.displayName
        val currentUserId = authDataSource.getLoggedInUser()!!.uid
        val currentUserProfilePicture = authDataSource.getUserDisplayPhotoUri()
        return databaseDataSource.addPostToDB(description, currentUser, currentUserId, currentUserProfilePicture!!)
    }

    fun getPostData(
        onSuccess: (posts: List<Post>) -> Unit,
        onFailure: (error: String) -> Unit
    ){
        databaseDataSource.getPostsFromDB(onSuccess, onFailure)
    }


    fun uploadImage(uri: Uri): Observable<Uri> {
        return storageDataSource.uploadProfilePicture(uri)
            .flatMap {
                Log.i("DZANIN", "flatMap1: $it")
                storageDataSource.getProfilePicture(authDataSource.getLoggedInUser())
            }
            .flatMap {
                Log.i("DZANIN", "flatMap2: $it")
                authDataSource.updateUserDisplayPhoto(it)
            }.map {
                authDataSource.getUserDisplayPhotoUri() ?: Uri.parse("")
            }
    }
}