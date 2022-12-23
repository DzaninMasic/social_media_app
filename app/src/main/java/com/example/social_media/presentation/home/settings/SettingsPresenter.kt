package com.example.social_media.presentation.home.settings

import com.example.social_media.data.repository.DataRepository

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

    fun attachView(view: SettingsView){
        this.view = view
    }
    fun detachView(){
        this.view = null
    }
}