package com.example.hzg.mysussr.util;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.widget.ImageView;

/**
 * Created by hzg on 2018/4/4 16:28
 * mail:1039766856@qq.com
 * Sussr
 */

public class ImageLoader {
    private static ImageLoader sInstance;

    private ImageLoader() {

    }

    public synchronized static ImageLoader getInstance() {
        if (sInstance == null) {
            sInstance = new ImageLoader();
        }
        return sInstance;
    }

    public void loadAppIcon(Context context, AppLogo packageName, @DrawableRes int placeholder, ImageView imageView) {
        GlideApp.with(context)
                .load(packageName)
                .centerInside()
                .into(imageView);
    }
}
