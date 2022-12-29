package com.example.social_media.data.datasource

import android.net.Uri
import com.example.social_media.data.dao.DAOPost
import com.example.social_media.domain.post.Post
import com.google.android.gms.tasks.Task

class DatabaseDataSource {
    private val dao = DAOPost()

    fun addPostToDB(description: String, userName: String?, userId: String, profilePicture: Uri) : Task<Void>? {
        val post = userName?.let { Post(description, it, userId, profilePicture) }
        return post?.let { dao.add(it) }
    }

    fun getPostsFromDB(
        onSuccess: (posts: List<Post>) -> Unit,
        onFailure: (error: String) -> Unit
    ){
        dao.get(onSuccess, onFailure)
    }
}