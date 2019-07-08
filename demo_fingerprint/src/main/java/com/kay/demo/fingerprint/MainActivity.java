package com.kay.demo.fingerprint;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.kay.demo.fingerprint.fingerprintNine.BiometricActivity;
import com.kay.demo.fingerprint.fingerprintSix.ui.TouchIDActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_six).setOnClickListener(this);

        findViewById(R.id.btn_nine).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_six) {
            startActivity(new Intent(this, TouchIDActivity.class));

        } else if (id == R.id.btn_nine) {
            startActivity(new Intent(this, BiometricActivity.class));
        }
    }
}
