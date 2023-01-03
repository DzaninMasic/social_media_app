package com.example.social_media.data.datasource

import android.net.Uri
import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.storage.FileDownloadTask
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.UploadTask.TaskSnapshot
import io.reactivex.rxjava3.core.Observable
import java.io.File

class StorageDataSource {
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var storage = FirebaseStorage.getInstance()
    private var storageReference = storage.reference
    private var profilePicturesReference = storage.reference.child("ProfilePictures")

    fun uploadProfilePicture(imageUri: Uri) : Observable<String> {
        val uploadedImage = profilePicturesReference.child(auth.currentUser?.uid.toString())
        return Observable.create{ emitter ->
            uploadedImage.putFile(imageUri)
                .addOnSuccessListener {
                    Log.i("DZANINPATH", "uploadProfilePicture: ${it.metadata?.path}")
                    //it.metadata?.path.substring(it.metadata?.path.indexOf('/')+1)
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
}