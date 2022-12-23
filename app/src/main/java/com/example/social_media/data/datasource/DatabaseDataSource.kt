package com.example.social_media.data.datasource

import com.example.social_media.data.dao.DAOPost
import com.example.social_media.domain.post.Post
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DatabaseDataSource {
    private val dao = DAOPost()

    fun addPostToDB(text: String) : Task<Void> {
        val post = Post(text)
        return dao.add(post)
    }

    fun getPostsFromDB(
        onSuccess: (posts: List<Post>) -> Unit,
        onFailure: (error: String) -> Unit
    ){
        dao.get(onSuccess, onFailure)
    }
}