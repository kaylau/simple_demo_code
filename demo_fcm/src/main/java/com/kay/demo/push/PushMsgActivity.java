package com.kay.demo.push;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

/**
 * Date: 2018/8/28 下午3:37
 * Author: kay lau
 * Description:
 */
public class PushMsgActivity extends Activity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.push_msg);
        TextView tv_msg = findViewById(R.id.tv_msg);
        Intent intent = getIntent();
        if (intent != null) {
            String msg = intent.getStringExtra("msg");
            tv_msg.setText(msg);
        }
    }
}
