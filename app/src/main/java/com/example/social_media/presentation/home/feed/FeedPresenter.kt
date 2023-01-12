package com.example.social_media.presentation.home.feed

import android.util.Log
import com.example.social_media.data.repository.DataRepository
import com.example.social_media.domain.post.Post
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable

class FeedPresenter {
    private val dataRepository = DataRepository()
    private var view: FeedView? = null

    fun getData(){
        dataRepository.getPostData(
            onSuccess = { posts ->
                Log.i("DZANINPOSTS", "getData: ${posts}")
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
            override fun onSubscribe(d: Disposable) {

            }

            override fun onNext(t: Unit) {

            }

            override fun onError(e: Throwable) {
                view?.displayError(e.toString())
            }

            override fun onComplete() {

            }

        })
    }

    fun attachView(view: FeedView){
        this.view = view
    }
    fun detachView(){
        this.view = null
    }

    private fun loadData(posts: List<Post>) {
        Log.i("DZANINPOSTS", "loadData: ${posts}")
        view?.showData(posts)
    }

    private fun showError(error: String) {
        view?.displayError(error)
    }
}