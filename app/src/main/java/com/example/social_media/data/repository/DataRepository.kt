package com.example.social_media.data.repository

import android.net.Uri
import android.util.Log
import com.example.social_media.data.datasource.AuthDataSource
import com.example.social_media.data.datasource.DatabaseDataSource
import com.example.social_media.data.datasource.StorageDataSource
import com.example.social_media.data.network.NetworkPost
import com.facebook.AccessToken
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseUser
import io.reactivex.rxjava3.core.Observable

class DataRepository {
    private val authDataSource = AuthDataSource()
    private val databaseDataSource = DatabaseDataSource()
    private val storageDataSource = StorageDataSource()

    fun registerUserWithFirebase(email: String, password: String, name: String): Observable<Unit> {
        return authDataSource.createFirebaseUser(email,password)
            .flatMap {
                authDataSource.updateUserDisplayName(name)
            }
    }

    fun loginWithGoogle(credential: AuthCredential, googleSignInClient: GoogleSignInClient?) : Observable<FirebaseUser>{
        return authDataSource.getGoogleUser(credential, googleSignInClient)
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

    fun signOut(){
        authDataSource.signOut()
    }

    fun addPost(description: String, uri: Uri?) : Observable<Unit> {
        val currentUser = authDataSource.getLoggedInUser()?.displayName
        val currentUserId = authDataSource.getLoggedInUser()?.uid
        val currentUserProfilePicture = authDataSource.getUserDisplayPhotoUri()
        return storageDataSource.uploadPostPicture(uri)
            .flatMap {
                storageDataSource.getPostPicture(it)
            }
            .flatMap {
                databaseDataSource.addPostToDB(description, currentUser, currentUserId, currentUserProfilePicture, it)
            }

    }

    fun deletePost(position: String) : Observable<Unit> {
        return databaseDataSource.deletePost(position)
    }

    fun likePost(postId: String) : Observable<Unit> {
        return databaseDataSource.likePost(postId, authDataSource.getLoggedInUser()?.uid)
    }

    fun commentOnPost(position: Int, comment: String, postId: String?) : Observable<Unit> {
        return databaseDataSource.commentOnPost(position, comment, authDataSource.getLoggedInUser(), postId)
    }

    fun deleteComment(commentPosition: String?, postPosition: String?) : Observable<Unit> {
        return databaseDataSource.deleteComment(commentPosition, postPosition)
    }

    fun getPostData(
        onSuccess: (networkPosts: List<NetworkPost>) -> Unit,
        onFailure: (error: String) -> Unit,
        page: Int
    ){
        databaseDataSource.getPostsFromDB(onSuccess, onFailure, page)
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