package com.example.social_media.presentation.home.feed

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.social_media.data.repository.DataRepository
import com.example.social_media.domain.post.DomainPost
import com.example.social_media.extensions.canUserDelete
import com.example.social_media.data.network.NetworkPost
import com.example.social_media.domain.post.DomainComment
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import javax.inject.Inject

class FeedPresenter @Inject constructor(private val dataRepository: DataRepository){

    private var view: FeedView? = null

    fun getData(page: Int = 0){
        dataRepository.getPostData(
            onSuccess = { posts ->
                loadData(posts)
            },
            onFailure = { error ->
                showError(error)
            },
            page = page
        )
    }

    fun likePost(postId: String){
        val observable = dataRepository.likePost(postId)
        observable.subscribe(object : Observer<Unit> {
            override fun onSubscribe(d: Disposable) {}
            override fun onNext(t: Unit) {}
            override fun onError(e: Throwable) {
                view?.displayError(e.toString())
            }
            override fun onComplete() {}
        })
    }

    fun commentOnPost(comment: String, postId: String?){
        val observable = dataRepository.commentOnPost(comment, postId)
        observable.subscribe(object : Observer<List<NetworkPost>> {
            override fun onSubscribe(d: Disposable) {}
            override fun onNext(networkPosts: List<NetworkPost>) {
                val userId = dataRepository.getCurrentUser()?.uid
                val domainPosts: MutableList<DomainPost> = mutableListOf()
                networkPosts.forEach { networkPost ->
                    val canDeleteComment = networkPost.canUserDelete(userId.orEmpty())
                    val comments: HashMap<String, DomainComment> = hashMapOf()

                    networkPost.comments?.forEach { (key, value) ->
                        comments[key] = DomainComment(
                            value.commentId,
                            value.userId,
                            value.userName,
                            value.comment,
                            canDeleteComment,
                            value.postId
                        )
                    }

                    domainPosts.add(
                        DomainPost(
                            networkPost.postId,
                            networkPost.description,
                            networkPost.userName,
                            networkPost.userId,
                            networkPost.profilePicture,
                            networkPost.postPicture,
                            networkPost.likes,
                            comments,
                            networkPost.canUserDelete(userId.orEmpty()))) }
                view?.showData(domainPosts)
            }
            override fun onError(e: Throwable) {
                view?.displayError(e.toString())
            }
            override fun onComplete() {}
        })
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun deletePost(post: NetworkPost){
        val observable = dataRepository.deletePost(post)
        observable.subscribe(object : Observer<List<NetworkPost>> {
            override fun onSubscribe(d: Disposable) {}
            override fun onNext(networkPosts : List<NetworkPost>) {
                val userId = dataRepository.getCurrentUser()?.uid
                val domainPosts: MutableList<DomainPost> = mutableListOf()
                networkPosts.forEach { networkPost ->
                    val canDeleteComment = networkPost.canUserDelete(userId.orEmpty())
                    val comments: HashMap<String, DomainComment> = hashMapOf()

                    networkPost.comments?.forEach { (key, value) ->
                        comments[key] = DomainComment(
                            value.commentId,
                            value.userId,
                            value.userName,
                            value.comment,
                            canDeleteComment,
                            value.postId
                        )
                    }

                    domainPosts.add(
                        DomainPost(
                            networkPost.postId,
                            networkPost.description,
                            networkPost.userName,
                            networkPost.userId,
                            networkPost.profilePicture,
                            networkPost.postPicture,
                            networkPost.likes,
                            comments,
                            networkPost.canUserDelete(userId.orEmpty()))) }
                view?.showData(domainPosts)
                view?.displayDeleteSuccess(post.postId.toString())
            }
            override fun onError(e: Throwable) {
                view?.displayError(e.toString())
            }
            override fun onComplete() {}
        })
    }

    fun deleteComment(commentPosition: String, postPosition: String?){
        val observable = dataRepository.deleteComment(commentPosition, postPosition)
        observable.subscribe(object : Observer<Unit> {
            override fun onSubscribe(d: Disposable) {}
            override fun onNext(t: Unit) {
                view?.displayDeleteSuccess(position = commentPosition)
            }
            override fun onError(e: Throwable) {
                view?.displayError(e.toString())
            }
            override fun onComplete() {}
        })
    }

    fun attachView(view: FeedView){
        this.view = view
    }
    fun detachView(){
        this.view = null
    }

    private fun loadData(networkPosts: List<NetworkPost>) {
        val userId = dataRepository.getCurrentUser()?.uid
        val domainPosts: MutableList<DomainPost> = mutableListOf()

        networkPosts.forEach { networkPost ->
            val canDeleteComment = networkPost.canUserDelete(userId.orEmpty())
            val comments: HashMap<String, DomainComment> = hashMapOf()

            networkPost.comments?.forEach { (key, value) ->
                comments[key] = DomainComment(
                    value.commentId,
                    value.userId,
                    value.userName,
                    value.comment,
                    canDeleteComment,
                    value.postId
                )
            }

            domainPosts.add(
                DomainPost(
                    networkPost.postId,
                    networkPost.description,
                    networkPost.userName,
                    networkPost.userId,
                    networkPost.profilePicture,
                    networkPost.postPicture,
                    networkPost.likes,
                    comments,
                    networkPost.canUserDelete(userId.orEmpty()),
                    networkPost.likes?.values?.any { it.userId == userId } ?: false
                )
            )
        }

        view?.showData(domainPosts)
    }

    private fun showError(error: String) {
        view?.displayError(error)
    }
}