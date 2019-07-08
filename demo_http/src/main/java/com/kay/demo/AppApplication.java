package com.kay.demo;

import android.app.Application;


/**
 * Date: 2019/7/3 上午9:41
 * Author: kay lau
 * Description:
 */
public class AppApplication extends Application {


    private static AppApplication instance = null;

    @Override
    public void onCreate() {
        super.onCreate();
        if (instance == null) {
            instance = this;
        }
    }

    public static AppApplication getApplication() {
        return instance;
    }
}
