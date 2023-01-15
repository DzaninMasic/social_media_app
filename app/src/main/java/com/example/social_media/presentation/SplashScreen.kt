package com.example.social_media.presentation

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.social_media.R
import com.example.social_media.data.repository.DataRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit


@SuppressLint("CustomSplashScreen")
class SplashScreen : Fragment() {

    private val dataRepository = DataRepository()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Observable.timer(3, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object: Observer<Long>{
                override fun onSubscribe(d: Disposable) {}
                override fun onNext(t: Long) {}
                override fun onError(e: Throwable) {}
                override fun onComplete() {
                    if(dataRepository.getCurrentUser() != null){
                        Navigation.findNavController(requireView()).navigate(R.id.navigateToHome)
                    }else{
                        Navigation.findNavController(requireView()).navigate(R.id.navigateToRegister)
                    }
                }
            })
    }
}