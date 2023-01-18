package com.example.social_media.data.datasource

import android.net.Uri
import android.util.Log
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
                    Log.i("DZANINADDPOST", "addPostToDB: success")
                    emitter.onNext(Unit)
                }
                ?.addOnFailureListener {
                    Log.i("DZANINADDPOST", "addPostToDB: $it")
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

    fun likePost(position: Int, currentUserId: String?) : Observable<Unit>{
        return dao.updateLikeCount(position, currentUserId)
    }

    fun commentOnPost(position: Int, comment: String, currentUser: FirebaseUser?, postId: String?) : Observable<Unit>{
        return dao.uploadComment(position, comment, currentUser, postId)
    }

    fun deletePost(position: String) : Observable<Unit>{
        return dao.deletePost(position)
    }

    fun deleteComment(commentPosition: String?, postPosition: String?) : Observable<Unit> {
        return dao.deleteComment(commentPosition, postPosition)
    }
}