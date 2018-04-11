package com.example.hzg.mysussr.features.config;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.example.hzg.mysussr.ResultObserver;
import com.example.hzg.mysussr.SingleResultObserver;
import com.example.hzg.mysussr.util.FileUtil;
import com.example.hzg.mysussr.util.Utils;
import com.hzg.mysussr.AppConfig;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
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

    public MutableLiveData<String> message;

    public MainViewModel() {
        isLoading = new MutableLiveData<>();
        mDisposables = new CompositeDisposable();
        mSelectConfig = new MutableLiveData<>();
        mConfigList = new MutableLiveData<>();
        message = new MutableLiveData<>();
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

    public MutableLiveData<ConfigBean> getSelectConfig() {
        return mSelectConfig;
    }

    public void loadSelectConfig(int uid) {
        if (mSelectConfig != null) {
            repository.loadSelectConfig(uid, new SingleResultObserver<ConfigBean>() {
                @Override
                public void onSubscribe(Disposable d) {

                }

                @Override
                public void onSuccess(ConfigBean configBean) {
                    mSelectConfig.setValue(configBean);
                }

                @Override
                public void onError(Throwable e) {
                    e.printStackTrace();
                    loadConfigList();
                }
            });
        }

    }

    public MutableLiveData<List<ConfigBean>> getConfigList() {
        return mConfigList;
    }

    public void deleteConfig(int uid) {
        mDisposables.add(repository.deleteConfig(uid, new ResultObserver<Unit>() {
            @Override
            public void onSuccess(Unit unit) {

            }

            @Override
            public void onFailure(@NotNull Throwable e) {
                e.printStackTrace();
            }
        }));

    }

    public void loadConfigList() {
        if (mConfigList != null) {
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
    }

    public void insertConfig(ConfigBean configBean) {
        mDisposables.add(repository.insertConfig(configBean, new ResultObserver<ConfigBean>() {
            @Override
            public void onSuccess(ConfigBean data) {
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

    public void checkFile() {
        isLoading.setValue(true);
        if (FileUtil.INSTANCE.isFileExist(AppConfig.INSTANCE.getSUSSR_SRC_PATH()) && FileUtil.INSTANCE.isFileExist(AppConfig.INSTANCE.getBUSYBOX_SRC_PATH())) {
            isLoading.setValue(false);
            message.setValue("附件已经复制成功");
        } else {
            message.setValue("附件正在复制");
            Observable.create(new ObservableOnSubscribe<Boolean>() {
                @Override
                public void subscribe(ObservableEmitter<Boolean> emitter) throws Exception {
                    FileUtil.INSTANCE.copyFileFromAssets(Utils.INSTANCE.getApp(), AppConfig.INSTANCE.getFILE_PATH());
                    emitter.onNext(true);
                }
            })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new ResultObserver<Boolean>() {
                        @Override
                        public void onSuccess(Boolean aBoolean) {
                            isLoading.setValue(false);
                            message.setValue("附件复制完成");
                        }

                        @Override
                        public void onFailure(@NotNull Throwable e) {
                            e.printStackTrace();
                            isLoading.setValue(false);
                            message.setValue("附件复制失败");
                        }
                    });

        }
    }
}
