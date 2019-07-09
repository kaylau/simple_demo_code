package com.kay.demo.qrcode;

import android.content.Intent;

import com.kay.demo.qrcode.scan.ScanBaseActivity;
import com.kay.demo.qrcode.utils.LogUtil;


public class CaptureActivity extends ScanBaseActivity {

    @Override
    protected String initTitleText() {
        // 扫码界面无标题, 返回 null.
        return null;
    }

    @Override
    protected String initTitleRightBtnText() {
        return "相册";
    }

    /**
     * 处理识别的二维码结果
     *
     * @param resultText
     */
    @Override
    protected void handleScanResult(String resultText) {
        LogUtil.e("CaptureActivity", "识别的二维码结果: " + resultText);
        Intent intent = new Intent();
        intent.putExtra("result", resultText);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onBack() {
        finish();
    }

}