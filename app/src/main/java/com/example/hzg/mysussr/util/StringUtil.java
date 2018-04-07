package com.example.hzg.mysussr.util;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Created by DELL on 2018年1月12日 012.
 * E-Mail:n.zjx@163.com
 * PrivyM8
 * StringUtil: 字符串操作相关工具类
 */

public class StringUtil {

    private StringUtil() {

    }

    public static final Gson GSON = new GsonBuilder().create();

    /**
     * 格式化String
     *
     * @param source  需要格式化的字符串
     * @param objects 放入字符串的参数
     * @return
     */
    public static String format(String source, Object... objects) {
        return String.format(Locale.getDefault(), source, objects);
    }

    /**
     * 对象转json
     *
     * @param obj
     * @return obj 的 json
     */
    public static String obj2Json(Object obj) {
        return GSON.toJson(obj);
    }

    /**
     * json 转对象
     *
     * @param json json字符串
     * @param type 对象类型
     * @param <T>
     * @return
     */
    public static <T> T json2Obj(String json, Class<T> type) {
        return GSON.fromJson(json, type);
    }

    /**
     * json转对象
     *
     * @param json Gson jsonObject
     * @param type 对象类型
     * @param <T>
     * @return
     */
    public static <T> T json2Obj(JsonObject json, Class<T> type) {
        return GSON.fromJson(json, type);
    }

    /**
     * json转List
     *
     * @param json
     * @param type
     * @param <T>
     * @return
     */
    public static <T> List<T> json2List(String json, Class<T[]> type) {
        T[] array = GSON.fromJson(json, type);
        return Arrays.asList(array);
    }

    /**
     * 从parentJson获取key对应的json字符串
     */
    public static String getJsonByKey(@NonNull String parentJson, @NonNull String key) {
        return getStringFromJson(GSON.fromJson(parentJson, JsonObject.class), key);

    }

    /**
     * null 转"”
     *
     * @param s source
     * @return "" or s
     */
    public static String trimNull(String s) {
        return s == null ? "" : s;
    }

    /**
     * @param object Gson 的JsonObject
     * @param key    键
     * @return value or null
     */
    public static String getStringFromJson(JsonObject object, String key) {
        return object.has(key) ? object.get(key).getAsString() : null;
    }

    /**
     * @param object object Gson 的JsonObject
     * @param key    键
     * @return value or 0
     */
    public static int getIntFromJson(JsonObject object, String key) {
        return object.has(key) ? object.get(key).getAsInt() : 0;
    }


}
