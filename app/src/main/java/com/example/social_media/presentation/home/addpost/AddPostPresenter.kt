package com.example.social_media.presentation.home.addpost

import com.example.social_media.data.repository.DataRepository

class AddPostPresenter {
    private val dataRepository = DataRepository()
    private var view: AddPostView? = null

    fun addPost(text: String){
        val taskResult = dataRepository.addPost(text)
        if (taskResult != null) {
            taskResult.addOnSuccessListener {
                view?.showSuccessfulResponse()
            }.addOnFailureListener {
                view?.showFailedResponse()
            }
        }
    }

    fun attachView(view: AddPostView){
        this.view = view
    }
    fun detachView(){
        this.view = null
    }
}