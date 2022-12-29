package com.example.social_media.data

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.subjects.PublishSubject

class Dzanin(val observer: Observer<String>) {

    val subject: PublishSubject<String> = PublishSubject.create()
    init {
        subject.subscribe(observer)
    }

    fun doSomething() {
        val observable = Observable.just("Dzanin")
            .map { it.length }
            .map { "Masic" }

        val observable2 = Observable.just(
            Observable.just(
                Observable.just("Dzanin")
            ).switchMap { it }
        )
            .switchMap { it }
    }

    fun success(successValue: String, observer: Observer<String>) {
        subject.onNext(successValue)
    }
}