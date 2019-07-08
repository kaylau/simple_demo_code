package com.kay.demo.push;

import android.content.Intent;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.kay.demo.push.logutil.LogUtil;

/**
 * Date: 2017/9/15 下午5:43
 * Author: kay lau
 * Description:
 */
public class MyFireBaseInstanceIDService extends FirebaseInstanceIdService {

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtil.e("tag", "MyFireBaseInstanceIDService onCreate()--->");
    }


    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        LogUtil.e("tag", "Refreshed fcm token: " + refreshedToken);
        String fcmId = FirebaseInstanceId.getInstance().getId();
        LogUtil.e("tag", "Refreshed fcm fcmId: " + fcmId);

        Intent intent = new Intent(this, RefreshTokenActivity.class);
        intent.putExtra("fcmId", fcmId);
        intent.putExtra("token", refreshedToken);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        // If you want to send messages to this application instance or
//        // manage this apps subscriptions on the server side, send the
//        // Instance ID token to your app server.
////        sendRegistrationToServer(refreshedToken);
    }

}
