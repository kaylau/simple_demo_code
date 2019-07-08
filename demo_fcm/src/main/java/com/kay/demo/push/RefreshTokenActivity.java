package com.kay.demo.push;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Date: 2018/8/28 下午3:37
 * Author: kay lau
 * Description:
 */
public class RefreshTokenActivity extends Activity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.refresh_token);
        EditText et_token = findViewById(R.id.et_token);
        EditText et_fcmId = findViewById(R.id.et_instanceId);
        Intent intent = getIntent();
        if (intent != null) {
            String token = intent.getStringExtra("token");
            String fcmId = intent.getStringExtra("fcmId");
            et_token.setText(token);
            et_fcmId.setText(fcmId);
        }
    }
}
