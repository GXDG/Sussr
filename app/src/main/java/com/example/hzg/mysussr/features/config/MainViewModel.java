package com.example.hzg.mysussr.features.config;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.example.hzg.mysussr.ResultObserver;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.logging.Logger;

import io.reactivex.disposables.CompositeDisposable;
import kotlin.Unit;

/**
 * Created by hzg on 2018/4/3 16:51
 * mail:1039766856@qq.com
 * Sussr
 */

public class MainViewModel extends ViewModel {
    private MutableLiveData<List<ConfigBean>> mConfigList;
    private ConfigRepository repository;
    private MutableLiveData<Boolean> isLoading;
    private CompositeDisposable mDisposables;

    public MainViewModel() {
        isLoading = new MutableLiveData<>();
        mDisposables = new CompositeDisposable();
    }

    public void setRepository(ConfigRepository repository) {
        this.repository = repository;
    }

    public ConfigRepository getRepository() {
        return repository;
    }

    public MutableLiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public MutableLiveData<List<ConfigBean>> getConfigList() {
        if (mConfigList == null) {
            mConfigList = new MutableLiveData<>();
            isLoading.setValue(true);
            mDisposables.add(repository.loadConfig(new ResultObserver<List<ConfigBean>>() {
                @Override
                public void onSuccess(List<ConfigBean> configBeans) {
                    mConfigList.setValue(configBeans);
                    isLoading.setValue(false);
                }

                @Override
                public void onFailure(@NotNull Throwable e) {
                    e.printStackTrace();

                    isLoading.setValue(false);
                }
            }));

        }
        return mConfigList;
    }

    public void insertConfig(ConfigBean... configBeans) {
            mDisposables.add(repository.insertConfig(configBeans, new ResultObserver<Unit>() {
                @Override
                public void onSuccess(Unit unit) {

                    Log.d("insertConfig","执行成功");
                }

                @Override
                public void onFailure(@NotNull Throwable e) {
                    e.printStackTrace();
                    Log.d("insertConfig","执行失败");
                }
            }));
             //  repository.configDao.insertAll(configBeans);


    }
}
