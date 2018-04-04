package com.example.hzg.mysussr;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

/**
 * Created by hzg on 2018/4/3 16:51
 * mail:1039766856@qq.com
 * Sussr
 */

public class MainViewModel extends ViewModel {
    private MutableLiveData<String> mUserName;

    public MutableLiveData<String> getUserName() {
        if (mUserName == null) {
            mUserName = new MutableLiveData<>();
        }
        return mUserName;
    }
}
