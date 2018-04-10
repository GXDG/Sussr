package com.example.hzg.mysussr.features.config;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.example.hzg.mysussr.ResultObserver;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import kotlin.Unit;

/**
 * Created by hzg on 2018/4/3 16:51
 * mail:1039766856@qq.com
 * Sussr
 */

public class MainViewModel extends ViewModel {
    private MutableLiveData<List<ConfigBean>> mConfigList;
    private MutableLiveData<ConfigBean> mSelectConfig;
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

    public MutableLiveData<ConfigBean> getSelectConfig(int uid) {
        if (mSelectConfig == null) {
            mSelectConfig = new MutableLiveData<>();
            mDisposables.add(repository.loadSelectConfig(uid, new ResultObserver<ConfigBean>() {
                @Override
                public void onSuccess(ConfigBean configBean) {
                    mSelectConfig.setValue(configBean);
                }

                @Override
                public void onFailure(@NotNull Throwable e) {
                    e.printStackTrace();
                    getConfigList();
                }
            }));
        }
        return mSelectConfig;
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

    public void insertConfig(ConfigBean configBean) {
        mDisposables.add(repository.insertConfig(configBean, new ResultObserver<ConfigBean>() {
            @Override
            public void onSuccess(ConfigBean data) {
                if (mSelectConfig == null) {
                    mSelectConfig = new MutableLiveData<>();
                }
                mSelectConfig.setValue(data);
                Log.d("insertConfig", "执行成功");
            }

            @Override
            public void onFailure(@NotNull Throwable e) {
                e.printStackTrace();
                Log.d("insertConfig", "执行失败");
            }
        }));
    }
    public void saveConfig(ConfigBean configBean) {
        mDisposables.add(repository.updateConfig(configBean, new ResultObserver<Unit>() {
            @Override
            public void onSuccess(Unit unit) {

            }

            @Override
            public void onFailure(@NotNull Throwable e) {
                e.printStackTrace();
            }
        }));
    }

}
