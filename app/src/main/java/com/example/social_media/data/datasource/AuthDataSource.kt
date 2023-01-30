package com.example.social_media.data.datasource

import android.net.Uri
import android.util.Log
import com.facebook.AccessToken
import com.facebook.login.LoginManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.*
import com.google.firebase.storage.FileDownloadTask
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject


class AuthDataSource {
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val loginManager = LoginManager.getInstance()
    private var googleSignInClient: GoogleSignInClient? = null

    fun createFirebaseUser(email: String, password: String) : Observable<Unit> {
        return Observable.create{ emitter ->
            auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    emitter.onNext(Unit)
                }
                .addOnFailureListener {
                    emitter.onError(it)
                }
        }
    }

    fun updateUserDisplayName(name: String) : Observable<Unit> {
        val currentUser = getLoggedInUser()
        val profileUpdates = UserProfileChangeRequest.Builder()
            .setDisplayName(name)
            .build()

        return Observable.create{ emitter ->
            currentUser?.updateProfile(profileUpdates)
                ?.addOnSuccessListener {
                    emitter.onNext(Unit)
                }
                ?.addOnFailureListener {
                    emitter.onError(it)
                }
        }
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

    fun getGoogleUser(credential: AuthCredential, googleSignInClient: GoogleSignInClient?) : Observable<FirebaseUser> {
        this.googleSignInClient = googleSignInClient
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

    fun getFirebaseUser(email: String, password: String) : Observable<Unit> {
        return Observable.create{ emitter ->
            auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    emitter.onNext(Unit)
                }
                .addOnFailureListener {
                    emitter.onError(it)
                }
        }
    }

    fun getLoggedInUser(): FirebaseUser? {
        return auth.currentUser
    }

    fun signOut(){
        auth.signOut()
        loginManager.logOut()
        googleSignInClient?.signOut()
    }
}