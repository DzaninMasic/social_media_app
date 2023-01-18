package com.example.social_media.presentation.home.feed

import android.util.Log
import com.example.social_media.data.repository.DataRepository
import com.example.social_media.domain.post.DomainPost
import com.example.social_media.extensions.canUserDelete
import com.example.social_media.data.network.NetworkPost
import com.example.social_media.domain.post.Comment
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable

class FeedPresenter {
    private val dataRepository = DataRepository()
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

    fun likePost(position: Int){
        val observable = dataRepository.likePost(position)
        observable.subscribe(object : Observer<Unit> {
            override fun onSubscribe(d: Disposable) {}
            override fun onNext(t: Unit) {}
            override fun onError(e: Throwable) {
                view?.displayError(e.toString())
            }
            override fun onComplete() {}
        })
    }

    fun commentOnPost(position: Int, comment: String, postId: String?){
        val observable = dataRepository.commentOnPost(position, comment, postId)
        observable.subscribe(object : Observer<Unit> {
            override fun onSubscribe(d: Disposable) {}
            override fun onNext(t: Unit) {}
            override fun onError(e: Throwable) {
                view?.displayError(e.toString())
            }
            override fun onComplete() {}
        })
    }

    fun deletePost(position: String){
        val observable = dataRepository.deletePost(position)
        observable.subscribe(object : Observer<Unit> {
            override fun onSubscribe(d: Disposable) {}
            override fun onNext(t: Unit) {
                view?.displayDeleteSuccess()
            }
            override fun onError(e: Throwable) {
                view?.displayError(e.toString())
            }
            override fun onComplete() {}
        })
    }

    fun deleteComment(commentPosition: String?, postPosition: String?){
        val observable = dataRepository.deleteComment(commentPosition, postPosition)
        observable.subscribe(object : Observer<Unit> {
            override fun onSubscribe(d: Disposable) {}
            override fun onNext(t: Unit) {
                view?.displayDeleteSuccess()
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

        networkPosts.forEach {
            val canDeleteComment = it.canUserDelete(userId.orEmpty())
            it.comments?.forEach {
                if(canDeleteComment){
                    it.canDelete = true
                }
            }

            domainPosts.add(
            DomainPost(
                it.postId,
                it.description,
                it.userName,
                it.userId,
                it.profilePicture,
                it.postPicture,
                it.likes,
                it.comments,
                it.canUserDelete(userId.orEmpty()))) }
        view?.showData(domainPosts)
    }

    private fun showError(error: String) {
        view?.displayError(error)
    }
}