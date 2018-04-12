package com.example.hzg.mysussr

import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver

/**
 * Created by hzg on 2018/2/7.
 */
abstract class SingleResultObserver<T> : SingleObserver<T> {

    override fun onSubscribe(d: Disposable) {

    }
//    override fun onNext(t: T) {
//        onSuccess(t)
//    }
//
//    override fun onComplete() {
//
//    }

//    override fun onError(e: Throwable) {
//        onFailure(e)
//    }


//    abstract fun onSuccess(t: T)
//    abstract fun onFailure(e: Throwable)
}