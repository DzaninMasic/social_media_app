package com.example.social_media.data.dao

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.social_media.data.network.NetworkComment
import com.example.social_media.domain.post.NetworkLike
import com.example.social_media.data.network.NetworkPost
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import io.reactivex.rxjava3.core.Observable


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

    fun updateLikeCount(post: NetworkPost, currentUserId: String?): Observable<Unit> {
        val likesRef = db.child(post.postId.toString()).child("likes")
        return Observable.create{ emitter ->
            val likes: MutableMap<String, NetworkLike> = post.likes?.toMutableMap() ?: mutableMapOf()
            val removedItem = likes.remove(currentUserId).also { removedLike ->
                if (removedLike != null) likesRef.child(removedLike.userId.toString()).removeValue()
            }
            if (removedItem == null) {
                likes.putIfAbsent(currentUserId.toString(), NetworkLike(currentUserId))
            }
            likesRef.updateChildren(likes as Map<String, NetworkLike>)
                .addOnSuccessListener {
                    emitter.onNext(Unit)
                }
                .addOnFailureListener {
                    emitter.onError(it)
                }
        }
    }

    fun uploadComment(
        comment: String,
        currentUser: FirebaseUser?,
        postId: String?
    ): Observable<List<NetworkPost>> {
        val commentsRef = db.child(postId.toString()).child("comments")

        return Observable.create { emitter ->
            val newComment = commentsRef.push()
            val key = newComment.key
            val comment = NetworkComment(key,currentUser?.uid,currentUser?.displayName,comment,postId)
            newComment.setValue(comment)
                .addOnSuccessListener {
                    emitter.onNext(globalNetworkPost)
                }
                .addOnFailureListener {
                    emitter.onError(it)
                }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun deletePost(post: NetworkPost): Observable<List<NetworkPost>> {
        val postRef = db.child(post.postId.toString())
        return Observable.create { emitter ->
            postRef.removeValue()
                .addOnSuccessListener {
                    globalNetworkPost.removeAt(globalNetworkPost.indexOfFirst { it.postId == post.postId })
                    emitter.onNext(globalNetworkPost)
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

            val query: Query = if (page == 0) {
                db.orderByKey().limitToLast(5)
            } else {
                db.orderByKey().endAt(lastPost).limitToLast(5)
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
                            }

                            listOfNetworkPosts.forEach { newPost ->
                                val existingPost = globalNetworkPost.find { it.postId == newPost.postId }
                                if(existingPost == null){
                                    globalNetworkPost.add(newPost)
                                } else {
                                    val index = globalNetworkPost.indexOfFirst { it.postId == newPost.postId }
                                    if (index >= 0) globalNetworkPost[index] = newPost
                                    existingPost.likes = newPost.likes
                                }
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
}