package com.example.social_media.data.datasource

import android.net.Uri
import android.util.Log
import com.facebook.AccessToken
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.*
import com.google.firebase.storage.FileDownloadTask
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject


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

    fun updateUserDisplayPhoto(uri: Uri) : Observable<Unit> {
        return Observable.create<Unit> { emitter ->

            val profileUpdates = UserProfileChangeRequest.Builder()
                .setPhotoUri(uri)
                .build()

            auth.currentUser?.updateProfile(profileUpdates)
                ?.addOnSuccessListener { _ ->
                    emitter.onNext(Unit)
                }
                ?.addOnFailureListener {
                    emitter.onError(it)
                }
        }
    }

    fun getUserDisplayPhotoUri() : Uri?{
        return getLoggedInUser()?.photoUrl
    }

    // OBSERVABLE EXAMPLE
    fun getGoogleUser(credential: AuthCredential) : Observable<FirebaseUser> {
        return Observable.create { emitter ->
            auth.signInWithCredential(credential)
                .addOnSuccessListener {
                    emitter.onNext(it.user)
                }
                .addOnFailureListener {
                    emitter.onError(it)
                }
                .addOnCompleteListener {
                    emitter.onComplete()
                }
        }
    }

    fun getFacebookUser(token: AccessToken) : Observable<FirebaseUser> {
        val fbCredential = FacebookAuthProvider.getCredential(token.token)
        return Observable.create{ emitter ->
            auth.signInWithCredential(fbCredential)
                .addOnSuccessListener {
                    emitter.onNext(it.user)
                }
                .addOnFailureListener {
                    emitter.onError(it)
                }
        }
    }

    fun getFirebaseUser(email: String, password: String) : Task<AuthResult> {
        return auth.signInWithEmailAndPassword(email, password)
    }

    fun getLoggedInUser(): FirebaseUser? {
        return auth.currentUser
    }

}