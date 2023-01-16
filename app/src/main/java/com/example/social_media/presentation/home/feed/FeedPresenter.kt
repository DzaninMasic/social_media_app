package com.example.social_media.presentation.home.feed

import android.util.Log
import com.example.social_media.data.repository.DataRepository
import com.example.social_media.domain.post.DomainPost
import com.example.social_media.extensions.canUserDelete
import com.example.social_media.network.NetworkPost
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable

class FeedPresenter {
    private val dataRepository = DataRepository()
    private var view: FeedView? = null

    fun getData(){
        dataRepository.getPostData(
            onSuccess = { posts ->
                loadData(posts)
            },
            onFailure = { error ->
                showError(error)
            }
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

    fun commentOnPost(position: Int, comment: String){
        val observable = dataRepository.commentOnPost(position, comment)
        observable.subscribe(object : Observer<Unit> {
            override fun onSubscribe(d: Disposable) {}
            override fun onNext(t: Unit) {
                Log.i("ADDCOMMENT", "onNext: success")
            }
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

    fun getCurrentUserId() : String?{
        return dataRepository.getCurrentUser()?.uid
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
        networkPosts.forEach { domainPosts.add(DomainPost(it.postId,it.description,it.userName,it.userId,it.profilePicture,it.postPicture,it.likes,it.comments,it.canUserDelete(userId.orEmpty()))) }
        view?.showData(domainPosts)
    }

    private fun showError(error: String) {
        view?.displayError(error)
    }
}