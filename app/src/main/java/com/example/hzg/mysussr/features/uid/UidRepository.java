package com.example.hzg.mysussr.features.uid;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.example.hzg.mysussr.features.uid.bean.AppUidBean;
import com.example.hzg.mysussr.util.Utils;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by hzg on 2018/4/4 10:45
 * mail:1039766856@qq.com
 * Sussr
 */

public class UidRepository {
    @Inject
    public UidRepository() {

    }

    public LiveData<List<AppUidBean>> getUidDataList() {
        Log.d("getUidDataList", "获取Uid数据");
        final MutableLiveData<List<AppUidBean>> data = new MutableLiveData<>();
        data.setValue(Utils.INSTANCE.getAppUidList());
        return data;
    }
}
