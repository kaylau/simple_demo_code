package com.kay.demo.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Date: 2018/7/19 下午3:24
 * Author: kay lau
 * Description:
 */
public class SharedPreferencesUtil {

    public static String getString(Context context, String key, String defaultValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("sample", Activity.MODE_PRIVATE);
        return sharedPreferences.getString(key, defaultValue);
    }

    public static String getString(Context context, String key) {
        return getString(context, key, "");
    }

    public static void putString(Context context, String key, String str) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("sample", Activity.MODE_PRIVATE);
        sharedPreferences.edit().putString(key, str).commit();
    }

    public static void removeKey(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("sample", Activity.MODE_PRIVATE);
        sharedPreferences.edit().remove(key).commit();
    }
}
