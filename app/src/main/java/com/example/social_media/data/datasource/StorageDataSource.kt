package com.example.social_media.data.datasource

import android.net.Uri
import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.storage.FileDownloadTask
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import java.io.File

class StorageDataSource {
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var storage = FirebaseStorage.getInstance()
    private var storageReference = storage.reference
//    private var profilePicturesReference = storage.reference.child("ProfilePictures")

    fun uploadProfilePicture(imageUri: Uri) : UploadTask {
        Log.i("PROFILEPICTURE", "uploadProfilePicture REFERENCE: ${storage.reference}")
        val uploadedImage = storageReference.child("ProfilePictures/${auth.currentUser?.uid.toString()}")
//        val blabla = profilePicturesReference.child(auth.currentUser?.uid.toString())
        uploadedImage.downloadUrl.addOnSuccessListener {
            Log.i("PROFILEPICTURE", "uploadProfilePicture URI: $it")
        }
        return uploadedImage.putFile(imageUri)
    }

    fun getProfilePicture(user: FirebaseUser?) : Task<Uri> {
        val profileImage = storageReference.child("ProfilePictures/${user?.uid.toString()}")
        return profileImage.downloadUrl
    }
}