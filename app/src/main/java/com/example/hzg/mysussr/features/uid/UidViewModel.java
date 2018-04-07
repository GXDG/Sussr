package com.example.hzg.mysussr.features.uid;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.example.hzg.mysussr.ResultObserver;
import com.example.hzg.mysussr.features.uid.bean.AppUidBean;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by hzg on 2018/4/4 10:30
 * mail:1039766856@qq.com
 * Sussr
 */

public class UidViewModel extends ViewModel {
    private MutableLiveData<List<AppUidBean>> uidDataList;
    private UidRepository repository;
    private MutableLiveData<Boolean> showLoading;
    private CompositeDisposable mDisposables;

    public UidViewModel() {
        showLoading = new MutableLiveData<>();
        mDisposables = new CompositeDisposable();
        showLoading.setValue(false);

    }

    public void setRepository(UidRepository repository) {
        this.repository = repository;
    }

    public MutableLiveData<Boolean> getShowLoading() {
        return showLoading;
    }

    public LiveData<List<AppUidBean>> getUidDataList() {
        if (uidDataList == null) {
            uidDataList = new MutableLiveData<>();
            showLoading.setValue(true);
            mDisposables.add(repository.loadUid(new ResultObserver<List<AppUidBean>>() {
                        @Override
                        public void onSuccess(List<AppUidBean> appUidBeans) {
                            uidDataList.setValue(appUidBeans);
                            showLoading.setValue(false);
                        }

                        @Override
                        public void onFailure(@NotNull Throwable e) {
                            showLoading.setValue(false);
                            Log.d("读取uid失败", e.getMessage());
                        }
                    })
            );
        }
        return uidDataList;
    }
}
