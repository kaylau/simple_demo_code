package com.kay.demo.fingerprint.fingerprintNine;

import android.content.DialogInterface;
import android.hardware.biometrics.BiometricPrompt;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.kay.demo.fingerprint.util.LogUtil;
import com.kay.demo.fingerprint.util.ToastUtil;

/**
 * Date: 2019/7/8 下午4:23
 * Author: kay lau
 * Description:
 */
public class BiometricActivity extends AppCompatActivity {

    private static final String TAG = "kay";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startFingerprint();
    }

    private void startFingerprint() {
        BiometricPrompt mBiometricPrompt = new BiometricPrompt.Builder(this)
                .setTitle("指纹验证")
                .setDescription("描述")
                .setNegativeButton("取消", getMainExecutor(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        LogUtil.e(TAG, "Cancel button clicked");
                        finish();
                    }
                })
                .build();

        CancellationSignal mCancellationSignal = new CancellationSignal();
        mCancellationSignal.setOnCancelListener(new CancellationSignal.OnCancelListener() {
            @Override
            public void onCancel() {
                //handle cancel result
                LogUtil.e(TAG, "Canceled");
            }
        });

        BiometricPrompt.AuthenticationCallback mAuthenticationCallback = new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                LogUtil.e(TAG, "onAuthenticationError: errorCode = " + errorCode);
                LogUtil.e(TAG, "onAuthenticationError: errString = " + errString.toString());
                ToastUtil.showToastAtCenterLong(BiometricActivity.this, "errorCode: " + errorCode);
                finish();

            }

            @Override
            public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                LogUtil.e(TAG, "onAuthenticationSucceeded----->");
                ToastUtil.showToastAtCenterLong(BiometricActivity.this, "onAuthenticationSucceeded");
                finish();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                LogUtil.e(TAG, "onAuthenticationFailed----->");
                ToastUtil.showToastAtCenterLong(BiometricActivity.this, "onAuthenticationFailed");
            }
        };

        mBiometricPrompt.authenticate(mCancellationSignal, getMainExecutor(), mAuthenticationCallback);
    }
}
