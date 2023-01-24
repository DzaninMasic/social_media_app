package com.example.social_media.data.datasource

import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.social_media.data.dao.DAOPost
import com.example.social_media.data.network.NetworkPost
import com.google.firebase.auth.FirebaseUser
import io.reactivex.rxjava3.core.Observable

class DatabaseDataSource {
    private val dao = DAOPost()

    fun addPostToDB(description: String, userName: String?, userId: String?, profilePicture: Uri?, postPicture: Uri) : Observable<Unit> {
        val networkPost = userName?.let { NetworkPost(null, description, it, userId, profilePicture.toString(), postPicture.toString(), null, null) }
        return Observable.create{ emitter ->
            networkPost?.let { dao.add(it) }
                ?.addOnSuccessListener {
                    emitter.onNext(Unit)
                }
                ?.addOnFailureListener {
                    emitter.onError(it)
                }
        }
    }

    fun getPostsFromDB(
        onSuccess: (networkPosts: List<NetworkPost>) -> Unit,
        onFailure: (error: String) -> Unit,
        page: Int
    ){
        dao.get(onSuccess, onFailure, page)
    }

    fun likePost(postId: String, currentUserId: String?) : Observable<Unit>{
        return dao.updateLikeCount(postId, currentUserId)
    }

    fun commentOnPost(comment: String, currentUser: FirebaseUser?, postId: String?) : Observable<List<NetworkPost>>{
        return dao.uploadComment(comment, currentUser, postId)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun deletePost(post: NetworkPost) : Observable<List<NetworkPost>>{
        return dao.deletePost(post)
    }

    fun deleteComment(commentPosition: String?, postPosition: String?) : Observable<Unit> {
        return dao.deleteComment(commentPosition, postPosition)
    }
}