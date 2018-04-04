package com.example.hzg.mysussr.util.applogo;

import android.content.Context;
import android.support.annotation.Nullable;

import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.MultiModelLoaderFactory;
import com.bumptech.glide.signature.ObjectKey;

import java.io.InputStream;

/**
 * Created by hzg on 2018/4/4 16:05
 * mail:1039766856@qq.com
 * Sussr
 */

public class AppLogoLoader implements ModelLoader<AppLogo, InputStream> {
    public final Context context;

    public AppLogoLoader(Context context) {
        this.context = context.getApplicationContext();
    }

    @Nullable
    @Override
    public LoadData<InputStream> buildLoadData(AppLogo s, int width, int height, Options options) {
        return new LoadData<>(new ObjectKey(s.packageName), new AppLogoFetcher(s.packageName, context));
    }

    @Override
    public boolean handles(AppLogo s) {
        return s.packageName != null;
    }

    public static class Factory implements ModelLoaderFactory<AppLogo, InputStream> {

        private Context context;

        public Factory(Context context) {
            this.context = context;
        }

        @Override
        public ModelLoader<AppLogo, InputStream> build(MultiModelLoaderFactory multiFactory) {
            return new AppLogoLoader(context);
        }

        @Override
        public void teardown() {

        }
    }
}
