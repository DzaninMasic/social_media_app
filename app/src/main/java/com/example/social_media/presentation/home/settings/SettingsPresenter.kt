package com.example.social_media.presentation.home.settings

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import com.example.social_media.common.model.NetworkConnection
import com.example.social_media.data.repository.DataRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import javax.inject.Inject

class SettingsPresenter @Inject constructor(private val dataRepository: DataRepository){

    private val networkConnection = NetworkConnection()
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

                override fun onError(e: Throwable) {}

                override fun onComplete() {}

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