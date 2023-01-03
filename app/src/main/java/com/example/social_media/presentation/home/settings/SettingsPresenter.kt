package com.example.social_media.presentation.home.settings

import android.net.Uri
import android.util.Log
import android.widget.Toast
import com.example.social_media.data.repository.DataRepository
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable

class SettingsPresenter {
    private val dataRepository = DataRepository()
    private var view: SettingsView? =null

    fun getUser(){
        val currentUser = dataRepository.getCurrentUser()
        if(currentUser != null){
            view?.displayProfile(currentUser)
        }else{
            view?.displayError()
        }
    }

    fun changeProfilePicture(imageUri: Uri){
        dataRepository.uploadImage(imageUri).subscribe(
            object : Observer<Uri> {
                override fun onSubscribe(d: Disposable) {}

                override fun onNext(t: Uri) {
                    view?.displaySuccessfulImageUpload(t)
                }

                override fun onError(e: Throwable) {
                    Log.i("DZANIN", "onError: ${e.stackTraceToString()}")
                }

                override fun onComplete() {
                    Log.i("DZANIN", "onComplete")
                }

            }
        )
    }

    fun signOut(){
        dataRepository.signOut()
    }

    fun attachView(view: SettingsView){
        this.view = view
    }
    fun detachView(){
        this.view = null
    }
}