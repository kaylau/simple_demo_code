package com.kay.demo.push;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.kay.demo.push.logutil.LogUtil;

import java.util.Map;
import java.util.Set;

import static com.kay.demo.push.MainActivity.BROADCAST_ACTION;
import static com.kay.demo.push.logutil.Constants.IS_FIRST_INSTALL_APP_KEY;


/**
 * Date: 2017/9/15 下午5:43
 * Author: kay lau
 * Description:
 */
public class MyFireBaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = MyFireBaseMessagingService.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtil.e(TAG, "MyFireBaseMessagingService onCreate--->");
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        LogUtil.e(TAG, "From: " + remoteMessage.getFrom());
        String title = "";
        String body = "";
        Map<String, String> data = remoteMessage.getData();
        if (data.size() > 0) {
            LogUtil.e(TAG, "Message data payload: " + data);
            Set<String> keySet = data.keySet();
            for (String key : keySet) {
                String value = data.get(key);
                LogUtil.e("tag", "  key: " + key);
                LogUtil.e("tag", "value: " + value);
                if (TextUtils.equals("body", key)) {
                    body = data.get(key);
                }

                if (TextUtils.equals("title", key)) {
                    title = data.get(key);
                }

            }
            sendNotification(title, body);

        }

        RemoteMessage.Notification notification = remoteMessage.getNotification();
        if (notification != null) {
            body = notification.getBody();
            title = notification.getTitle();
            LogUtil.e(TAG, " Message Notification Body: " + body);
            LogUtil.e(TAG, "Message Notification title: " + title);
        }

        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(body)) {
            return;
        }

        sendNotification(title, body);

    }

    private void sendNotification(String title, String messageBody) {
        Intent intent = new Intent(this, PushMsgActivity.class);
        intent.putExtra("msg", messageBody);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }


    @Override
    public void onMessageSent(String s) {
        super.onMessageSent(s);
        LogUtil.e("Service", "onMessageSent: " + s);
    }


    @Override
    public void onNewToken(String refreshedToken) {
        super.onNewToken(refreshedToken);

        String fcmId = FirebaseInstanceId.getInstance().getId();
        boolean isFirstInstall = AppUtil.getBoolean(this, IS_FIRST_INSTALL_APP_KEY);
        if (isFirstInstall) {
            AppUtil.putBoolean(this, IS_FIRST_INSTALL_APP_KEY, false);
            sendRefreshMainActivity(refreshedToken, fcmId);
        } else {
            deleteToken2RefreshTokenActivity(refreshedToken, fcmId);
        }

        LogUtil.e("tag", "onNewToken: " + refreshedToken);
        LogUtil.e("tag", "     fcmId: " + fcmId);

    }

    private void deleteToken2RefreshTokenActivity(String refreshedToken, String fcmId) {
        Intent intent = new Intent(this, RefreshTokenActivity.class);
        intent.putExtra("fcmId", fcmId);
        intent.putExtra("token", refreshedToken);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void sendRefreshMainActivity(String refreshedToken, String fcmId) {
        LogUtil.e("tag", "send Refresh MainActivity textView token");
        Intent refreshText = new Intent();
        refreshText.setAction(BROADCAST_ACTION);
        refreshText.putExtra("fcmId", fcmId);
        refreshText.putExtra("onNewToken", refreshedToken);
        sendBroadcast(refreshText);
    }

}
