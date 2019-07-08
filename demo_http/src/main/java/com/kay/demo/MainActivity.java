package com.kay.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.kay.demo.retrofit.RetrofitUtil;

public class MainActivity extends AppCompatActivity {

public static final String HTTP_URL = "http://fanyi.youdao.com/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RetrofitUtil.sendPostHttp(HTTP_URL);

    }



}
