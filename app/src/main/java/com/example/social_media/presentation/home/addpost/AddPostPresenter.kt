package com.example.social_media.presentation.home.addpost

import com.example.social_media.data.repository.DataRepository
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable

class AddPostPresenter {
    private val dataRepository = DataRepository()
    private var view: AddPostView? = null

    fun addPost(text: String){
        val observable = dataRepository.addPost(text)
        observable.subscribe(object: Observer<Unit> {
            override fun onSubscribe(d: Disposable) {
            }

            override fun onNext(t: Unit) {
                view?.showSuccessfulResponse()
            }

            override fun onError(e: Throwable) {
                view?.showFailedResponse()
            }

            override fun onComplete() {
            }

        })
    }

    fun attachView(view: AddPostView){
        this.view = view
    }
    fun detachView(){
        this.view = null
    }
}