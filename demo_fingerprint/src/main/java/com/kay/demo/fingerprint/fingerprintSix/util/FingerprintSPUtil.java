package com.kay.demo.fingerprint.fingerprintSix.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Date: 2018/7/19 下午3:24
 * Author: kay lau
 * Description:
 */
public class FingerprintSPUtil {


    private static final String key_rsa_private = "_rsa_private";

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


    /**
     * 指纹SDK 开启成功后生成的RSA 私钥
     *
     * @param context
     * @param lname
     * @param data
     */
    public static void putRSA_private(Context context, String lname, String data) {
        putStringRSA(context, lname + key_rsa_private, lname , data);

    }

    public static String getRSA_private(Context context, String lname) {
        return getStringRSA(context, lname + key_rsa_private, lname );
    }


    private static String getStringRSA(Context context, String spkey, String dataKey) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(spkey, Activity.MODE_PRIVATE);
        return sharedPreferences.getString(dataKey, "");
    }

    private static void putStringRSA(Context context, String spkey, String dataKey, String str) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(spkey, Activity.MODE_PRIVATE);
        sharedPreferences.edit().putString(dataKey, str).commit();
    }
}
