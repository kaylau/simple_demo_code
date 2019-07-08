/**
 * Copyright 2015 Google Inc. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.kay.demo.push;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.kay.demo.push.logutil.LogUtil;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    public static final String BROADCAST_ACTION = "com.example.corn";

    private BroadcastReceiver mBroadcastReceiver;

    private EditText et_token, et_instanceId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        FirebaseInstanceId.getInstance();

        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = googleApiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (googleApiAvailability.isUserResolvableError(resultCode)) {
                googleApiAvailability.getErrorDialog(this, resultCode, 1213).show();
            }
        }

        initIntentData();

        setContentView(R.layout.activity_main);

        initView();
//        getFcmToken();

        deleteToken();

        mBroadcastReceiver = new RefreshTextReceive();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BROADCAST_ACTION);
        registerReceiver(mBroadcastReceiver, intentFilter);
    }

    private void deleteToken() {
        findViewById(R.id.btn_del).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new Thread() {
                    @Override
                    public void run() {
                        try {
                            FirebaseInstanceId.getInstance().deleteInstanceId();
                        } catch (IOException e) {
                            e.printStackTrace();
                            LogUtil.e("tag", e.toString());
                        }
                    }
                }.start();
            }
        });
    }

    private void initView() {
        et_token = findViewById(R.id.et_token);
        et_instanceId = findViewById(R.id.et_instanceId);
    }

    private void initIntentData() {
        if (getIntent() != null) {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                int size = extras.size();
                LogUtil.e("tag", "Bundle size: " + size);
                for (String key : extras.keySet()) {
                    LogUtil.e("tag", "key: " + key);
                    LogUtil.e("tag", "value: " + extras.getString(key));
                }
            }
        }
    }

    private void setEdtToken(String token, String fcmId) {
        et_token.setText(token);
        et_instanceId.setText(fcmId);
    }

    private void getFcmToken() {

        new Thread() {
            @Override
            public void run() {
                try {
                    FirebaseInstanceId instanceId = FirebaseInstanceId.getInstance();
                    final String fcmId = instanceId.getId();
                    final String token = instanceId.getToken(getString(R.string.gcm_defaultSenderId), FirebaseMessaging.INSTANCE_ID_SCOPE);
                    LogUtil.e("tag", "fcm  fcmId: " + fcmId);
                    LogUtil.e("tag", "fcm  token: " + token);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setEdtToken(token, fcmId);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                    LogUtil.e("tag", e.toString());
                }
            }
        }.start();

//        this.token = instanceId.getToken();
//        LogUtil.e("tag", "fcm  token: " + this.token);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBroadcastReceiver);
    }

    class RefreshTextReceive extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                String fcmId = intent.getStringExtra("fcmId");
                String onNewToken = intent.getStringExtra("onNewToken");
                setEdtToken(onNewToken, fcmId);
            }
        }
    }

}
