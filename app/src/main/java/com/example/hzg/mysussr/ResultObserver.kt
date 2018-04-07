package com.example.hzg.mysussr

import io.reactivex.observers.DisposableObserver


/**
 * Created by hzg on 2018/2/7.
 */
abstract class ResultObserver<T> : DisposableObserver<T>() {
    override fun onNext(t: T) {
        onSuccess(t)
    }

    override fun onComplete() {

    }

    override fun onError(e: Throwable) {
        onFailure(e)
    }


    abstract fun onSuccess(t: T)
    abstract fun onFailure(e: Throwable)
}