package com.kay.demo.push;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Date: 2018/8/30 上午10:06
 * Author: kay lau
 * Description:
 */
public class AppUtil {

    public static boolean getBoolean(Context context, String key) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getBoolean(key , true);
    }

    public static void putBoolean(Context context, String key, boolean bool) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferences.edit().putBoolean(key, bool).commit();
    }
}
