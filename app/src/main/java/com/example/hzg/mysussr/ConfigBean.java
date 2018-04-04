package com.example.hzg.mysussr;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.List;

/**
 * Created by hzg on 2018/4/3 17:34
 * mail:1039766856@qq.com
 * Sussr
 */
@Entity
public class ConfigBean {
    @PrimaryKey
    public String configName;

    public List<KeyBean> data;
}
