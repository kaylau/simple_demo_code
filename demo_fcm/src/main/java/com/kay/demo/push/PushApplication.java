package com.kay.demo.push;

import android.app.Application;
import android.content.Context;

import com.google.firebase.iid.FirebaseInstanceId;

import static com.kay.demo.push.logutil.Constants.IS_FIRST_INSTALL_APP_KEY;

/**
 * Date: 2018/8/29 上午10:36
 * Author: kay lau
 * Description:
 */
public class PushApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AppUtil.putBoolean(this, IS_FIRST_INSTALL_APP_KEY, true);
//        FirebaseInstanceId.getInstance();
    }

    @Override
    public Context getApplicationContext() {
        return super.getApplicationContext();
    }
}
