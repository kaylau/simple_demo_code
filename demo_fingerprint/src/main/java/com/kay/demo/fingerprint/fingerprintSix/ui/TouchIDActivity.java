package com.kay.demo.fingerprint.fingerprintSix.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.CheckBox;

import com.kay.demo.fingerprint.R;
import com.kay.demo.fingerprint.fingerprintSix.FingerprintSDK;
import com.kay.demo.fingerprint.fingerprintSix.callback.FingerprintCallback;
import com.kay.demo.fingerprint.fingerprintSix.util.FingerprintSPUtil;
import com.kay.demo.fingerprint.util.LogUtil;
import com.kay.demo.fingerprint.util.ToastUtil;

import javax.crypto.Cipher;


/**
 * Date: 2019/1/4 上午10:40
 * Author: kay lau
 * Description:
 */
public class TouchIDActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = TouchIDActivity.class.getSimpleName();

    private CheckBox cb_switch_touchID_pay;

    private Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_touch_id);
        initView();
    }

    private void initView() {
        findViewById(R.id.ll_switch_touchid_pay).setOnClickListener(this);
        cb_switch_touchID_pay = findViewById(R.id.cb_switch_touchid_pay);
        String iv = FingerprintSPUtil.getString(mContext, "lName");
        if (TextUtils.isEmpty(iv)) {
            cb_switch_touchID_pay.setChecked(false);
        } else {
            cb_switch_touchID_pay.setChecked(true);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.ll_switch_touchid_pay) {
            String iv = FingerprintSPUtil.getString(mContext, "lName");
            if (TextUtils.isEmpty(iv)) {
                cb_switch_touchID_pay.setChecked(false);
                onOpenTouchID();
            } else {
                cb_switch_touchID_pay.setChecked(true);
                showCloseTouchIDDialog("关闭指纹");
            }
        }
    }

    /**
     * 开启指纹
     */
    private void onOpenTouchID() {
        FingerprintSDK.startAuthenticateUnlock(this,
                "lName",
                FingerprintSDK.FINGERPRINT_LOGIN_START,
                new FingerprintCallback() {
                    @Override
                    public void onSuccess(Cipher cipher) {
                        String iv = Base64.encodeToString(cipher.getIV(), Base64.URL_SAFE).trim();
                        if (!TextUtils.isEmpty(iv)) {
                            FingerprintSPUtil.putString(mContext, "lName", iv);
                            cb_switch_touchID_pay.setChecked(true);
                        } else {
                            cb_switch_touchID_pay.setChecked(false);
                        }
                        ToastUtil.showToastCustom(mContext, "Enable");
                        LogUtil.e(TAG, "------开启指纹成功------");
                    }

                    @Override
                    public void onFailed(int code, String errMsg) {
                        if (code == FingerprintSDK.CODE_0) {
                            // 指纹验证失败3次, 吐司提示
                            ToastUtil.showToastAtCenterShort(mContext, "Fingerprint does not match");

                        } else if (code == FingerprintSDK.CODE_5) {
                            // 指纹验证失败5次系统锁定
                            showErrorDialog("Exceed verify fingerprint error limit, please try again later");
                        }
                        cb_switch_touchID_pay.setChecked(false);
                        LogUtil.e(TAG, "------开启指纹失败------");
                    }
                });
    }


    public void showErrorDialog(String errorMsg) {
        new CommonDialog
                .Builder(this)
                .setMessage(errorMsg)
                .setMessageCenter(true)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    private void showCloseTouchIDDialog(String msg) {
        new CommonDialog
                .Builder(this)
                .setMessage(msg)
                .setMessageCenter(true)
                .setCancelable(false)
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                dialog.dismiss();
                            }
                        })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        cb_switch_touchID_pay.setChecked(false);
                        FingerprintSPUtil.putString(mContext, "lName", "");
                        dialog.dismiss();
                    }
                }).show();
    }

}
