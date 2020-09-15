package com.kay.demo.push;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
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

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
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
            sendNotification(title, body, "subText");
//            sendNotification(title, body);

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

    private static final int NOTIFICATION_ID = 1001;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void sendNotification(String title, String messageBody, String subText) {

        Intent intent = new Intent(this, PushMsgActivity.class);
        intent.putExtra("msg", messageBody);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        //1、NotificationManager
        NotificationManager manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        /** 2、Builder->Notification
         *  必要属性有三项
         *  小图标，通过 setSmallIcon() 方法设置
         *  标题，通过 setContentTitle() 方法设置
         *  内容，通过 setContentText() 方法设置*/
        Notification.Builder builder = new Notification.Builder(this);
        builder.setContentInfo("Content info")
                .setContentText(messageBody)//设置通知内容
                .setContentTitle(title)//设置通知标题
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher))
                .setSmallIcon(R.mipmap.ic_launcher_round)//不能缺少的一个属性
                .setSubText(subText)
                .setTicker("滚动消息......")
                .setContentIntent(pendingIntent)
                .setWhen(System.currentTimeMillis());//设置通知时间，默认为系统发出通知的时间，通常不用设置

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("001","my_channel",NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableLights(true); //是否在桌面icon右上角展示小红点
            channel.setLightColor(Color.GREEN); //小红点颜色
            channel.setShowBadge(true); //是否在久按桌面图标时显示此渠道的通知
            manager.createNotificationChannel(channel);
            builder.setChannelId("001");
        }

        Notification n = builder.build();
        //3、manager.notify()
        manager.notify(NOTIFICATION_ID,n);
    }


}
