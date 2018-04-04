package com.example.hzg.mysussr.util.applogo;

/**
 * Created by hzg on 2018/4/4 16:35
 * mail:1039766856@qq.com
 * Sussr
 */

public class AppLogo {
    public String packageName;

    public AppLogo(String packageName) {
        this.packageName = packageName;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        AppLogo imageFid = (AppLogo) obj;

        return packageName.equals(imageFid.packageName);

    }

    @Override
    public int hashCode() {
        return packageName.hashCode();
    }
}
