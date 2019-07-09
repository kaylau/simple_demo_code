package com.kay.demo.qrcode;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.kay.demo.scan.R;

public class MainActivity extends Activity implements View.OnClickListener {

    private TextView tv_result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "lanting.ttf");

        TextView tv_open_camera = findViewById(R.id.tv_open_scan);
        tv_open_camera.setTypeface(typeface);
        tv_open_camera.setOnClickListener(this);

        TextView tv_qrCode = findViewById(R.id.tv_qrCode);
        tv_qrCode.setTypeface(typeface);
        tv_qrCode.setOnClickListener(this);

        TextView tv_tips = findViewById(R.id.tv_tips);
        tv_tips.setTypeface(typeface);

        tv_result = findViewById(R.id.tv_result);
    }

    private void openScan() {
        Intent intent = new Intent(this, CaptureActivity.class);
        startActivityForResult(intent, 90);
    }

    private void openQRCode() {
        Intent intent = new Intent(this, QRCodeActivity.class);
        startActivity(intent);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == 90) {
            if (data != null) {
                String result = data.getStringExtra("result");
                if (!TextUtils.isEmpty(result)) {
                    tv_result.setText(result);
                } else {
                    tv_result.setText("result is null");
                }
            }
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.tv_open_scan) {
            openScan();

        } else if (id == R.id.tv_qrCode) {
            openQRCode();
        }
    }
}
