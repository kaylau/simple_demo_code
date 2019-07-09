package com.kay.demo.qrcode;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.kay.demo.qrcode.create.QRCodeUtil;

/**
 * Date: 2019/7/9 上午10:01
 * Author: kay lau
 * Description:
 */
public class QRCodeActivity extends Activity implements View.OnClickListener {

    private EditText edt_qrCode_text, edt_barcode_text;
    private ImageView iv_qrCode, iv_barcode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);

        edt_qrCode_text = findViewById(R.id.edt_qrCode_text);
        iv_qrCode = findViewById(R.id.iv_qrCode);
        edt_barcode_text = findViewById(R.id.edt_barcode_text);
        iv_barcode = findViewById(R.id.iv_barcode);
        findViewById(R.id.tv_create_qrCode).setOnClickListener(this);
        findViewById(R.id.tv_create_barcode).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.tv_create_qrCode) {
            createQRCode();

        } else if (id == R.id.tv_create_barcode) {
            createBarCode();
        }
    }

    private void createBarCode() {
        String barcodeContent = edt_barcode_text.getText().toString();
        Bitmap qrBitmap = QRCodeUtil.getBarBitmap(barcodeContent, iv_barcode);
        iv_barcode.setImageBitmap(qrBitmap);
    }

    private void createQRCode() {
        String qrCodeContent = edt_qrCode_text.getText().toString();
        Bitmap qrBitmap = QRCodeUtil.getQRBitmap(qrCodeContent, iv_qrCode);
        iv_qrCode.setImageBitmap(qrBitmap);
    }
}
