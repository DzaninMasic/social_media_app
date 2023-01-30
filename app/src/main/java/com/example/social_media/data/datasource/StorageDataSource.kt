package com.example.social_media.data.datasource

import android.net.Uri
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.storage.FirebaseStorage
import io.reactivex.rxjava3.core.Observable
import kotlin.random.Random

class StorageDataSource {
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var storage = FirebaseStorage.getInstance()
    private var profilePicturesReference = storage.reference.child("ProfilePictures")
    private var postPicturesReference = storage.reference.child("PostPictures")

    fun uploadProfilePicture(imageUri: Uri) : Observable<String> {
        val uploadedImage = profilePicturesReference.child(auth.currentUser?.uid.toString())
        return Observable.create{ emitter ->
            uploadedImage.putFile(imageUri)
                .addOnSuccessListener {
                    emitter.onNext(it.metadata?.path ?: "")
                }
                .addOnFailureListener{
                    emitter.onError(it)
                }
                .addOnCompleteListener {
                    emitter.onComplete()
                }
        }
    }

    fun uploadPostPicture(imageUri: Uri?) : Observable<String>{
        return Observable.create{ emitter ->
            if (imageUri != null) {
                val uploadedImage = postPicturesReference.child(getRandomString())
                uploadedImage.putFile(imageUri)
                    .addOnSuccessListener {
                        emitter.onNext(
                            it.metadata?.path?.substring(it.metadata?.path.toString().indexOf('/')) ?: ""
                        )
                    }
                    .addOnFailureListener {
                        emitter.onError(it)
                    }
            } else {
                emitter.onNext("")
            }
        }
    }

    fun getPostPicture(path: String) : Observable<Uri> {
        return Observable.create{ emitter ->
            if(!path.equals("")){
                postPicturesReference.child(path).downloadUrl
                    .addOnSuccessListener {
                        emitter.onNext(it)
                    }
                    .addOnFailureListener {
                        emitter.onError(it)
                    }
            }else{
                emitter.onNext(Uri.parse(""))
            }
        }
    }

    fun getProfilePicture(user: FirebaseUser?) : Observable<Uri> {
        return Observable.create { emitter ->
            profilePicturesReference.child(user?.uid.toString()).downloadUrl
                .addOnSuccessListener {
                    emitter.onNext(it)
                }
                .addOnFailureListener {
                    emitter.onError(it)
                }
                .addOnCompleteListener {
                    emitter.onComplete()
                }
        }
    }

    fun getRandomString() : String {
        return Random(System.currentTimeMillis()).nextInt(1, 300).toString()
    }
}