package com.example.hzg.mysussr.util.applogo;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.util.Log;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.data.DataFetcher;
import com.example.hzg.mysussr.util.FormatTools;

import java.io.InputStream;

/**
 * Created by hzg on 2018/4/4 15:37
 * mail:1039766856@qq.com
 * Sussr
 */

public class AppLogoFetcher implements DataFetcher<InputStream> {
    private String packageName;
    private Context context;

    public AppLogoFetcher(String packageName, Context context) {
        this.packageName = packageName;
        this.context = context;
    }

    @Override
    public void loadData(Priority priority, DataCallback<? super InputStream> callback) {
        try {
            Drawable drawable = context.getPackageManager().getApplicationIcon(packageName);
            Log.d("loadData", packageName);
            callback.onDataReady(FormatTools.Drawable2InputStream(drawable));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            callback.onLoadFailed(e);
        }
    }

    @Override
    public void cleanup() {

    }

    @Override
    public void cancel() {

    }

    @NonNull
    @Override
    public Class<InputStream> getDataClass() {
        return InputStream.class;
    }

    @NonNull
    @Override
    public DataSource getDataSource() {
        return DataSource.LOCAL;
    }
}
