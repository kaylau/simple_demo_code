package com.kay.demo.fingerprint.fingerprintSix.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Date: 2018/7/19 下午3:24
 * Author: kay lau
 * Description:
 */
public class FingerprintSPUtil {


    public static void putString(Context context, String key, String str) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferences.edit().putString(key, str).commit();
    }

    public static String getString(Context context, String key, String defaultValue) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString(key, defaultValue);
    }

    public static String getString(Context context, String key) {
        return getString(context, key, "");
    }

}
