package com.example.hzg.mysussr.net

import io.reactivex.observers.DisposableObserver

/**
 * Created by hzg on 2017/9/30.
 *
 */
abstract class HttpResultObserver<T> : DisposableObserver<T>() {
    override fun onNext(t: T) {
        if (t != null)
            onSuccess(t)
        else onFailure(Throwable("empty"))
    }

    override fun onComplete() {

    }

    override fun onError(e: Throwable) {
        onFailure(e)
    }

    abstract fun onSuccess(t: T)
    abstract fun onFailure(e: Throwable)
}