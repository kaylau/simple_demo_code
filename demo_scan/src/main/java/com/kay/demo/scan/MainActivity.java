package com.kay.demo.scan;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.kay.demo.scan.zbar.CaptureActivity;

public class MainActivity extends AppCompatActivity {

    private TextView tv_result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "lanting.ttf");

        TextView tv_open_camera = findViewById(R.id.tv_open_camera);
        tv_open_camera.setTypeface(typeface);
        tv_open_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CaptureActivity.class);
                startActivityForResult(intent, 90);
            }
        });

        tv_result = findViewById(R.id.tv_result);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == 90) {
            if (data != null) {
                String result = data.getStringExtra("result");
                tv_result.setText(result);
            }
        }
    }
}
