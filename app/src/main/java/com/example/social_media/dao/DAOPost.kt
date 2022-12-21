package com.example.social_media.dao

import android.util.Log
import com.example.social_media.data_class.Post
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class DAOPost {
    private lateinit var databaseReference: DatabaseReference
    init {
        val db = FirebaseDatabase.getInstance("https://social-media-app-9785b-default-rtdb.europe-west1.firebasedatabase.app/")
        databaseReference = Post::class.java.simpleName?.let { db.getReference(it) }!!
    }
    fun add(description: Post): Task<Void> {
        Log.i("ADD", "add: ${description.description}")
        return databaseReference.push().setValue(description)
    }

}