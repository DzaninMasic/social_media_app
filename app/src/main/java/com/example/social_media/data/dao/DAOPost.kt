package com.example.social_media.data.dao

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.social_media.domain.post.Comment
import com.example.social_media.domain.post.Like
import com.example.social_media.data.network.NetworkPost
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import io.reactivex.rxjava3.core.Observable
import java.util.function.UnaryOperator


class DAOPost {
    private var globalNetworkPost: MutableList<NetworkPost> = mutableListOf()
    private var tempPost: MutableList<NetworkPost> = mutableListOf()
    var lastPost: String? = null

    private val db = FirebaseDatabase
        .getInstance("https://social-media-app-9785b-default-rtdb.europe-west1.firebasedatabase.app/")
        .reference
        .child("Post")

    fun add(networkPost: NetworkPost): Task<DataSnapshot> {
        val add = db.get().addOnSuccessListener {
            val newChildRef = db.push()
            val newChildKey = newChildRef.key
            networkPost.postId = newChildKey
            newChildRef.setValue(networkPost)
        }
        return add
    }

    fun updateLikeCount(postId: String, currentUserId: String?): Observable<Unit> {
        val likesRef = db.child(postId).child("likes")
        Log.i("DZANINLIKEPOST", "updateLikeCount: position $postId, currentUserId $currentUserId")

        return Observable.create { emitter ->
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
                            .addOnFailureListener {
                                Log.i("DZANINLIKEPOST", "onDataChange error adding: $it")
                                emitter.onError(it)
                            }
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // The read failed, log a message
                    Log.w(
                        "DZANINLIKEPOST",
                        "updateLikeCount:onCancelled",
                        databaseError.toException()
                    )
                    emitter.onError(databaseError.toException())
                }
            })
        }
    }

    fun uploadComment(
        position: Int,
        comment: String,
        currentUser: FirebaseUser?,
        postId: String?
    ): Observable<Unit> {
        val commentsRef = db.child(position.toString()).child("comments")

        return Observable.create { emitter ->
            commentsRef.get().addOnSuccessListener {
                commentsRef.child(it.childrenCount.toString()).setValue(
                    Comment(
                        it.childrenCount.toString(),
                        currentUser?.uid,
                        currentUser?.displayName,
                        comment,
                        null,
                        postId
                    )
                )
                    .addOnSuccessListener {
                        emitter.onNext(Unit)
                    }
                    .addOnFailureListener {
                        emitter.onError(it)
                    }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun deletePost(position: String): Observable<Unit> {
        val postRef = db.child(position)
        return Observable.create { emitter ->
            postRef.removeValue()
                .addOnSuccessListener {
                    tempPost = globalNetworkPost
                    tempPost.removeIf{
                        it.postId == position
                    }
                    globalNetworkPost = tempPost
                    emitter.onNext(Unit)
                }
                .addOnFailureListener {
                    emitter.onError(it)
                }
        }
    }

    fun deleteComment(commentPosition: String?, postPosition: String?): Observable<Unit> {
        val commentsRef = postPosition?.let { db.child(it).child("comments") }
        return Observable.create { emitter ->
            if (commentPosition != null) {
                commentsRef?.child(commentPosition)?.removeValue()
                    ?.addOnSuccessListener {
                        emitter.onNext(Unit)
                    }
                    ?.addOnFailureListener {
                        emitter.onError(it)
                    }
            }
        }
    }

    fun get(
        onSuccess: (networkPosts: List<NetworkPost>) -> Unit,
        onFailure: (error: String) -> Unit,
        page: Int
    ) {
        db.get().addOnSuccessListener {
            val query: Query?

            if (page == 0) {
                query = db.orderByKey().limitToLast(5)
            } else {
                Log.i("STARTATPOSITION", "get: $lastPost")
                query = db.orderByKey().endAt(lastPost).limitToLast(5)
            }

            query
                .addValueEventListener(
                    object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            val listOfNetworkPosts: MutableList<NetworkPost> = mutableListOf()
                            if (snapshot.exists()) {
                                for (child in snapshot.children) {
                                    val networkPost =
                                        child.getValue(NetworkPost::class.java) as NetworkPost
                                    listOfNetworkPosts.add(networkPost)
                                }
                            }
                            listOfNetworkPosts.reverse()
                            if(listOfNetworkPosts.isNotEmpty()){
                                lastPost = listOfNetworkPosts.last().postId
                                Log.i("STARTATPOSITION", "onDataChange: $lastPost")
                            }
                            for(newPost in listOfNetworkPosts){
                                var existingPost = globalNetworkPost.find { it.postId == newPost.postId }
                                if(existingPost == null){
                                    globalNetworkPost.add(newPost)
                                }else{
                                    existingPost.likes = newPost.likes
                                }
                            }

                            Log.i("GLOBALNETWORKPOST", "PAGE: $page")
                            globalNetworkPost.forEach{
                                Log.i("GLOBALNETWORKPOST", "onDataChange: ${it.postId}")
                            }

                            onSuccess(globalNetworkPost)
                        }
                        override fun onCancelled(error: DatabaseError) {
                            onFailure(error.message)
                        }
                    }
                )
        }
    }

    companion object {
        private const val LOAD_AMOUNT = 5
    }
}