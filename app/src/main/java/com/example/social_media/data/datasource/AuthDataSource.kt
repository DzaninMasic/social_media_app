package com.example.social_media.data.datasource

import android.net.Uri
import android.util.Log
import com.facebook.AccessToken
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.*
import com.google.firebase.storage.FileDownloadTask


class AuthDataSource {
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun createFirebaseUser(email: String, password: String) : Task<AuthResult> {
        return auth.createUserWithEmailAndPassword(email, password)
    }

    fun updateUserDisplayName(name: String) : Task<Void> {
        val currentUser = getLoggedInUser()
        val profileUpdates = UserProfileChangeRequest.Builder()
            .setDisplayName(name)
            .build()

        return currentUser!!.updateProfile(profileUpdates)
    }

    fun updateUserDisplayPhoto(uri: Uri) : Task<Void>{
        val currentUser = getLoggedInUser()
        val profileUpdates = UserProfileChangeRequest.Builder()
            .setPhotoUri(uri)
            .build()

        return currentUser!!.updateProfile(profileUpdates)
    }

    fun getUserDisplayPhotoUri() : Uri?{
        val currentUser = getLoggedInUser()
        return currentUser?.photoUrl
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