package com.example.social_media.data.dao

import android.util.Log
import com.example.social_media.domain.post.Like
import com.example.social_media.domain.post.Post
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import io.reactivex.rxjava3.core.Observable


class DAOPost {

    private val db = FirebaseDatabase
        .getInstance("https://social-media-app-9785b-default-rtdb.europe-west1.firebasedatabase.app/")
        .reference
        .child("Post")

    fun add(description: Post): Task<DataSnapshot> {
        val add = db.get().addOnSuccessListener {
            db.child((it.childrenCount).toString()).setValue(description)
        }
        return add
    }

    fun updateLikeCount(position: Int, currentUserId: String?) : Observable<Unit> {
        val likesRef = db.child(position.toString()).child("likes")

        return Observable.create{ emitter ->
            likesRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (currentUserId?.let { dataSnapshot.hasChild(it) } == true) {
                        likesRef.child(currentUserId).removeValue()
                            .addOnSuccessListener {
                                Log.i("DZANINLIKEPOST", "onDataChange: LIKE REMOVED")
                                emitter.onNext(Unit)
                            }
                            .addOnFailureListener {
                                Log.i("DZANINLIKEPOST", "onDataChange error: $it")
                                emitter.onError(it)
                            }
                    } else {
                        val key = currentUserId
                        val updates: MutableMap<String?, Any> = HashMap()
                        updates[key] = Like(currentUserId)
                        likesRef.updateChildren(updates)
                            .addOnSuccessListener {
                                Log.i("DZANINLIKEPOST", "onDataChange: LIKE ADDED")
                                emitter.onNext(Unit)
                            }
                            .addOnFailureListener{
                                Log.i("DZANINLIKEPOST", "onDataChange error adding: $it")
                                emitter.onError(it)
                            }
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // The read failed, log a message
                    Log.w("DZANINLIKEPOST", "updateLikeCount:onCancelled", databaseError.toException())
                    emitter.onError(databaseError.toException())
                }
            })
        }
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