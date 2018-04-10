package com.example.hzg.mysussr.features.config

import com.example.hzg.mysussr.ResultObserver
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers


/**
 * Created by hzg on 2018/4/6.
 */
class ConfigRepository {

    lateinit var configDao: ConfigDao

    constructor(configDao: ConfigDao) {
        this.configDao = configDao
    }

    fun insertConfig(configBean: Array<ConfigBean>, observer: ResultObserver<Unit>): Disposable {
        return Observable.create<Unit>({
            it.onNext(configDao.insertAll(*configBean))
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(observer)

    }

    fun insertConfig(configBean: ConfigBean, observer: ResultObserver<ConfigBean>): Disposable {
        return Observable.create<ConfigBean>({
            configDao.insert(configBean)
            it.onNext(configBean)
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(observer)
    }

    fun updateConfig(configBean: ConfigBean, observer: ResultObserver<Unit>): Disposable {
        return Observable.create<Unit>({
            it.onNext(configDao.update(configBean))
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(observer)
    }

    fun loadConfig(observer: ResultObserver<List<ConfigBean>>): Disposable {
        return Observable.create<List<ConfigBean>>({
            it.onNext(getConfig())
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(observer)

    }

    fun loadSelectConfig(selectId: Int, observer: ResultObserver<ConfigBean>): Disposable {
        return Observable.create<ConfigBean>({
            //   it.onNext(configDao.getConfigById(selectId))
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(observer)

    }

    fun loadSimpleConfigList(observer: ResultObserver<List<SimpleConfig>>): Disposable {
        return Observable.create<List<SimpleConfig>>({
            it.onNext(configDao.getConfigNameList())
        })
//                .map {
//                    val list = ArrayList<SimpleConfig>()
//                    it.mapTo(list) {
//                        Log.d("xxxxx", it.data.toString())
//                        SimpleConfig(it.uid, it.configName)
//                    }
//                    return@map list
//                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(observer)

    }


    fun getConfig(): List<ConfigBean> {
        return configDao.getAllConfig();
    }


}