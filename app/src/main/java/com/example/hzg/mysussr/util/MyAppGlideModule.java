package com.example.hzg.mysussr.util;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.AppGlideModule;
import com.example.hzg.mysussr.util.applogo.AppLogo;
import com.example.hzg.mysussr.util.applogo.AppLogoLoader;

import java.io.InputStream;

/**
 * Created by DELL on 2017年12月1日 001.
 * Glide注解生成GlideApp类
 */
@GlideModule
public final class MyAppGlideModule extends AppGlideModule {

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        super.applyOptions(context, builder);
    }

    @Override
    public void registerComponents(Context context, Glide glide, Registry registry) {
        registry.replace(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory());
        registry.replace(AppLogo.class, InputStream.class, new AppLogoLoader.Factory(context));
    }

}
