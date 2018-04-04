package com.example.hzg.mysussr;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by hzg on 2018/4/4 10:30
 * mail:1039766856@qq.com
 * Sussr
 */

public class UidViewModel extends ViewModel {
    private LiveData<List<AppUidBean>> uidDataList;
    private UidRepository repository;

    public UidViewModel() {
    }

    public void setRepository(UidRepository repository) {
        this.repository = repository;
    }

    @Inject
    public UidViewModel(UidRepository repository) {
        this.repository = repository;
    }

    public LiveData<List<AppUidBean>> getUidDataList() {
        if (uidDataList == null) {
            uidDataList = repository.getUidDataList();
        }
        return uidDataList;
    }
}
