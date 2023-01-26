package com.example.social_media.presentation.home.addpost

import android.net.Uri
import android.util.Log
import com.example.social_media.data.repository.DataRepository
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import javax.inject.Inject

class AddPostPresenter @Inject constructor(private val dataRepository: DataRepository){

    private var view: AddPostView? = null
    private var localImageUri: Uri? = null

    fun addPost(text: String){
        if(text.equals("") && this.localImageUri == null){
            view?.showFailedResponse()
        }else{
            val observable = dataRepository.addPost(text, this.localImageUri)
            observable.subscribe(object: Observer<Unit> {
                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(t: Unit) {
                    view?.showSuccessfulResponse()
                }

                override fun onError(e: Throwable) {
                    Log.i("DZANINADDPOST", "onError: ${e.message}")
                    view?.showFailedResponse()
                }

                override fun onComplete() {
                }

            })
        }
    }

    fun uploadPostPicture(imageUri: Uri){
        this.localImageUri = imageUri
        view?.showChosenImage(imageUri)
    }

    fun attachView(view: AddPostView){
        this.view = view
    }
    fun detachView(){
        this.view = null
    }
}