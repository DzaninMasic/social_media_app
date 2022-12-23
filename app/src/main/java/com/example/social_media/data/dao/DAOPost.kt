package com.example.social_media.data.dao

import android.util.Log
import com.example.social_media.domain.post.Post
import com.google.android.gms.tasks.Task
import com.google.firebase.database.*

class DAOPost {

    private val db = FirebaseDatabase
        .getInstance("https://social-media-app-9785b-default-rtdb.europe-west1.firebasedatabase.app/")
        .getReference("Post")

    fun add(description: Post): Task<Void> {
        Log.i("ADD", "add: ${description.description}")
        return db.push().setValue(description)
    }

    fun get(
        onSuccess: (posts: List<Post>) -> Unit,
        onFailure: (error: String) -> Unit
    ) {
        db.addValueEventListener(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val listOfPosts: MutableList<Post> = mutableListOf()
                    if(snapshot.exists()){
                        for(child in snapshot.children){
                            val post = child.getValue(Post::class.java) as Post
                            listOfPosts.add(post)
                        }
                    }
                    // CHANGE THIS LOGIC
                    listOfPosts.reverse()
                    onSuccess(listOfPosts)
                }

                override fun onCancelled(error: DatabaseError) {
                    onFailure(error.message)
                }
            }
        )
    }
}