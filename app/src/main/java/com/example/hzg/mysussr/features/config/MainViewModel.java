package com.example.hzg.mysussr.features.config;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.persistence.room.Room;
import android.util.Log;

import com.example.hzg.mysussr.AppConfig;
import com.example.hzg.mysussr.ResultObserver;
import com.example.hzg.mysussr.SingleResultObserver;
import com.example.hzg.mysussr.net.HttpService;
import com.example.hzg.mysussr.net.ServiceFactory;
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
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import kotlin.Unit;
import okhttp3.ResponseBody;

/**
 * Created by hzg on 2018/4/3 16:51
 * mail:1039766856@qq.com
 * Sussr
 */

public class MainViewModel extends ViewModel {

    private ConfigRepository repository;
    private SussrConfigRepository sussrRepository;
    private HttpService httpService;
    private MutableLiveData<List<ConfigBean>> mConfigList;
    private MutableLiveData<ConfigBean> mSelectConfig;
    private MutableLiveData<Boolean> isLoading;
    private CompositeDisposable mDisposables;

    public MutableLiveData<String> errorMessage;
    public MutableLiveData<String> editMessage;
    public MutableLiveData<String> shellResultMessage;
    public MutableLiveData<Boolean> reInstallApk;

    public MainViewModel() {
        AppDataBase db = Room.databaseBuilder(Utils.INSTANCE.getApp(),
                AppDataBase.class, "sussr.db")
                .fallbackToDestructiveMigration()
                .build();
        sussrRepository = new SussrConfigRepository();
        httpService = ServiceFactory.Companion.getInstance().createService();
        repository = new ConfigRepository(db.configDao());
        isLoading = new MutableLiveData<>();
        mDisposables = new CompositeDisposable();
        mSelectConfig = new MutableLiveData<>();
        mConfigList = new MutableLiveData<>();
        errorMessage = new MutableLiveData<>();
        editMessage = new MutableLiveData<>();
        shellResultMessage = new MutableLiveData<>();
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
            errorMessage.setValue("附件已经复制成功");
        } else {

            copyFile();

        }
    }

    public void copyFile() {

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
                    protected void onStart() {
                        super.onStart();
                        isLoading.setValue(true);
                    }

                    @Override
                    public void onSuccess(Boolean aBoolean) {
                        isLoading.setValue(false);
                        errorMessage.setValue("附件复制完成");
                    }

                    @Override
                    public void onFailure(@NotNull Throwable e) {
                        e.printStackTrace();
                        isLoading.setValue(false);
                        errorMessage.setValue("附件复制失败");
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
                        errorMessage.setValue(strings[0] + "/n" + strings[1]);
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
                            errorMessage.setValue("请授予软件root权限");
                        } else {
                            errorMessage.setValue(strings[0] + "/n" + strings[1]);
                        }


                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        errorMessage.setValue("请授予软件root权限");
                    }
                });
    }


    public void startSussr(ConfigBean bean) {
        ArrayList<String> config = new ArrayList<>();


        for (KeyBean s : bean.getData()) {
            config.add(s.getValue());
        }

        sussrRepository.startSussr(config.toArray(new String[0]), shellObserver);
    }

    private SingleResultObserver<String[]> shellObserver = new SingleResultObserver<String[]>() {
        @Override
        public void onSubscribe(@NotNull Disposable d) {
            super.onSubscribe(d);
            isLoading.setValue(true);
        }

        @Override
        public void onSuccess(String[] strings) {
            Log.d("result",
                    "输出信息:" + strings[0]
                            + "\n错误信息" + strings[1]);
            if (strings[1].contains("Permission denied"))
                errorMessage.setValue("请授予软件root权限");
            else if (strings[1].contains("No such file or directory")||strings[1].contains("not found"))
                errorMessage.setValue("请先安装Sussr");
            else {
                shellResultMessage.setValue("输出信息:\n" + strings[0]
                        + "\n错误信息\n" + strings[1]);
            }

            isLoading.setValue(false);
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
            errorMessage.setValue("请授予软件root权限");
            isLoading.setValue(false);
        }
    };

    public void stopSussr() {

        sussrRepository.stopSussr(shellObserver);
    }

    public void removeSussr() {
        sussrRepository.removeSussr(shellObserver);
    }

    public void checkSussr() {
        sussrRepository.checkSussr(shellObserver);
    }

    public void saveSussrSetting(String s) {
        sussrRepository.saveEditSussr(s, new SingleResultObserver<String[]>() {
            @Override
            public void onSuccess(String[] strings) {
                errorMessage.setValue("保存成功");
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }
        });
    }

    public void editSussr() {
        sussrRepository.editSussr(new SingleResultObserver<String[]>() {
            @Override
            public void onSuccess(String[] strings) {
                Log.d("result",
                        "输出信息:" + strings[0]
                                + "\n错误信息" + strings[1]);
                editMessage.setValue(strings[0]);
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                errorMessage.setValue("请授予软件root权限");
            }
        });
    }

    public void installSussr() {
        sussrRepository.installSussr(new SingleResultObserver<String[]>() {
            @Override
            public void onSubscribe(@NotNull Disposable d) {
                super.onSubscribe(d);
                isLoading.setValue(true);
            }

            @Override
            public void onSuccess(String[] strings) {
                isLoading.setValue(false);
                if (strings[1].contains("Permission denied"))
                    errorMessage.setValue("请授予软件root权限");
                else if (strings[1].contains("sh: <stdin>[3]: unzip: not found"))
                    errorMessage.setValue("请安装支持unzip命令的busybox");
                else if (strings[1].contains("bunzip2: Can't open input file"))
                    errorMessage.setValue("请先点击重置附件,完成附件的初始化");

                else {
                    errorMessage.setValue("安装Sussr成功");
                }
                Log.d("installSussr", strings[0] + "\n" + strings[1]);
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                errorMessage.setValue("请授予软件root权限");
                isLoading.setValue(false);
            }
        });
    }


    public void checkIp() {
        httpService.checkIp()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<ResponseBody, String>() {
                    @Override
                    public String apply(ResponseBody responseBody) throws Exception {
                        return responseBody.string();
                    }
                })
                .subscribe(new ResultObserver<String>() {
                    @Override
                    protected void onStart() {
                        super.onStart();
                        isLoading.setValue(true);
                    }

                    @Override
                    public void onSuccess(String s) {
                        shellResultMessage.setValue(s);
                        isLoading.setValue(false);
                    }

                    @Override
                    public void onFailure(@NotNull Throwable e) {
                        e.printStackTrace();
                        isLoading.setValue(false);
                    }
                });
    }

}
