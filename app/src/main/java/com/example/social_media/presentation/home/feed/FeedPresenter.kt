package com.example.social_media.presentation.home.feed

import android.util.Log
import com.example.social_media.data.repository.DataRepository
import com.example.social_media.domain.post.Post

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
        dataRepository.likePost(position)
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