package com.example.hzg.mysussr.features.config;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.example.hzg.mysussr.AppConfig;
import com.example.hzg.mysussr.ResultObserver;
import com.example.hzg.mysussr.SingleResultObserver;
import com.example.hzg.mysussr.util.FileUtil;
import com.example.hzg.mysussr.util.ShellUtil;
import com.example.hzg.mysussr.util.Utils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Single;
import io.reactivex.SingleOnSubscribe;
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
    private SussrConfigRepository sussrRepository;
    private MutableLiveData<Boolean> isLoading;
    private CompositeDisposable mDisposables;

    public MutableLiveData<String> message;
    public MutableLiveData<Boolean> reInstallApk;

    public MainViewModel() {
        sussrRepository = new SussrConfigRepository();
        isLoading = new MutableLiveData<>();
        mDisposables = new CompositeDisposable();
        mSelectConfig = new MutableLiveData<>();
        mConfigList = new MutableLiveData<>();
        message = new MutableLiveData<>();
        reInstallApk = new MutableLiveData<>();
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
        if (FileUtil.INSTANCE.isFileExist(AppConfig.INSTANCE.getSUSSR_SRC_PATH()) && FileUtil.INSTANCE.isFileExist(AppConfig.INSTANCE.getBUSYBOX_SRC_PATH())) {
            isLoading.setValue(false);
            message.setValue("附件已经复制成功");
        } else {

            copyFile();

        }
    }

    public void copyFile() {
        isLoading.setValue(true);
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

    public void changeFileChmod() {
        Single.create((SingleOnSubscribe<String[]>) emitter -> emitter.onSuccess(ShellUtil.INSTANCE.execShell(new String[]{" chmod 777  " + AppConfig.INSTANCE.getBUSYBOX_SRC_PATH()}, false, true))).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleResultObserver<String[]>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(String[] strings) {
                        message.setValue(strings[0] + "/n" + strings[1]);
                        reInstallApk.setValue(true);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }
                });
    }


    public void checkSu() {
        Single.create((SingleOnSubscribe<String[]>) emitter -> emitter.onSuccess(ShellUtil.INSTANCE.execShell(new String[]{"ls"}, true, true)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleResultObserver<String[]>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(String[] strings) {
                        if (strings[1].contains("Permission denied")) {
                            message.setValue("请授予软件root权限");
                        } else {
                            message.setValue(strings[0] + "/n" + strings[1]);
                        }


                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        message.setValue("请授予软件root权限");
                    }
                });
    }


    public void startSussr(ConfigBean bean) {
        ArrayList<String> config = new ArrayList<>();


        for (KeyBean s : bean.getData()) {
            config.add(s.getValue());
        }

        sussrRepository.startSussr( config.toArray(new String[0]), shellObserver);
    }

    private SingleResultObserver<String[]> shellObserver = new SingleResultObserver<String[]>() {
        @Override
        public void onSuccess(String[] strings) {
            Log.d("result",
                    "输出信息:" + strings[0]
                            + "\n错误信息" + strings[1]);
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
        }
    };

    public void stopSussr() {
        sussrRepository.stopSussr(shellObserver);
    }

    public void checkSussr() {
        sussrRepository.checkSussr(shellObserver);
    }

    public void editSussr() {
        sussrRepository.editSussr(new SingleResultObserver<String[]>() {
            @Override
            public void onSuccess(String[] strings) {
                Log.d("shell", strings[0] + "\n" + strings[1]);
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }
        });
    }

    public void installSussr() {
        sussrRepository.installSussr(new SingleResultObserver<String[]>() {
            @Override
            public void onSuccess(String[] strings) {

                if (strings[1].contains("Permission denied"))
                    message.setValue("请授予软件root权限");
                else if (strings[1].contains("sh: <stdin>[3]: unzip: not found"))
                    message.setValue("请安装支持unzip命令的busybox");
                Log.d("installSussr", strings[0] + "\n" + strings[1]);
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }
        });
    }


}
