package com.kay.demo.fingerprint.fingerprintSix.ui;

import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.kay.demo.fingerprint.R;
import com.kay.demo.fingerprint.fingerprintSix.FingerprintSDK;
import com.kay.demo.fingerprint.fingerprintSix.callback.FingerprintPaymentCallback;
import com.kay.demo.fingerprint.fingerprintSix.callback.FingerprintUnlockCallback;
import com.kay.demo.fingerprint.fingerprintSix.core.FingerprintCore;
import com.kay.demo.fingerprint.fingerprintSix.core.FingerprintMain;
import com.kay.demo.fingerprint.fingerprintSix.util.FingerprintConstants;
import com.kay.demo.fingerprint.fingerprintSix.util.FingerprintSPUtil;
import com.kay.demo.fingerprint.util.LogUtil;
import com.kay.demo.fingerprint.util.RSAHelper;

import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.pkcs.RSAPrivateKey;

import java.security.PublicKey;
import java.util.Map;

import javax.crypto.Cipher;


public class FingerprintDialog extends DialogFragment implements View.OnClickListener {

    private static final String TAG = FingerprintDialog.class.getSimpleName();
    private static FingerprintUnlockCallback unlockCallback;
    private static FingerprintPaymentCallback paymentCallback;
    private static FingerprintCore fingerprintCore;
    private static int verifyType;

    private TextView tv_fingerprint_title, tv_fingerprint_msg, tv_fingerprint_cancel, tv_dialog_cancel, tv_dialog_pin;
    private View ll_dialog_double_btn;
    private FingerprintCore.IFingerprintResultListener fingerprintResultListener;
    public int errorNum = 0;


    public static FingerprintDialog newInstance(FingerprintUnlockCallback callback, FingerprintCore core, int vType) {
        FingerprintDialog f = init(core, vType);
        unlockCallback = callback;
        return f;
    }

    public static FingerprintDialog newInstance(FingerprintPaymentCallback callback, FingerprintCore core, int vType) {
        FingerprintDialog f = init(core, vType);
        paymentCallback = callback;
        return f;
    }

    @NonNull
    private static FingerprintDialog init(FingerprintCore core, int vType) {
        FingerprintDialog f = new FingerprintDialog();
        f.setCancelable(false);
        fingerprintCore = core;
        verifyType = vType;
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        Window window = getDialog().getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        View v = inflater.inflate(R.layout.dialog_layout_fingerprint, container, false);
        initView(v);
        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Material_Light_Dialog_NoActionBar);
        }
    }

    private void initView(View v) {
        // 弹窗提示文案
        tv_fingerprint_title = v.findViewById(R.id.tv_fingerprint_title);
        tv_fingerprint_msg = v.findViewById(R.id.tv_fingerprint_msg);

        // 单选按钮
        tv_fingerprint_cancel = v.findViewById(R.id.tv_fingerprint_cancel);
        tv_fingerprint_cancel.setVisibility(View.VISIBLE);

        // 双选按钮
        ll_dialog_double_btn = v.findViewById(R.id.ll_dialog_double_btn);
        ll_dialog_double_btn.setVisibility(View.GONE);
        tv_dialog_cancel = v.findViewById(R.id.tv_dialog_cancel);
        tv_dialog_pin = v.findViewById(R.id.tv_dialog_pin);
        initListener();
        initData();
    }

    private void initData() {
        // 根据不同类型 设置对应的展示文案
        if (verifyType == FingerprintSDK.FINGERPRINT_LOGIN_START
                || verifyType == FingerprintSDK.FINGERPRINT_LOGIN_VERIFY) {
            tv_fingerprint_title.setText("Touch ID for ***");
            tv_fingerprint_msg.setText("Verify existing fingerprint to ***");

        } else if (verifyType == FingerprintSDK.FINGERPRINT_PAY_START
                || verifyType == FingerprintSDK.FINGERPRINT_PAY_VERIFY
                || verifyType == FingerprintSDK.FINGERPRINT_PAY_RE_START) {
            tv_fingerprint_title.setText("Touch ID for ***");
            tv_fingerprint_msg.setText("Verify your fingerprint to ***");
        }

        tv_dialog_pin.post(new Runnable() {
            @Override
            public void run() {
                //页面加载完成之后再调验证逻辑
                if (unlockCallback == null && paymentCallback == null) {
                    LogUtil.e(TAG, "传过来的回调为null");
                    return;
                }
                if (fingerprintCore != null) {
                    fingerprintCore.setFingerprintManager(fingerprintResultListener);
                    fingerprintCore.startAuthenticate();
                }
            }
        });
    }

    private void initListener() {
        tv_fingerprint_cancel.setOnClickListener(this);
        tv_dialog_cancel.setOnClickListener(this);
        tv_dialog_pin.setOnClickListener(this);

        fingerprintResultListener = new FingerprintCore.IFingerprintResultListener() {

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onAuthenticateSuccess(final Cipher results) {
                onSuccessCallback(results);
            }

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onAuthenticateFailed(int helpId) {
                showError();
            }

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onAuthenticateError(int msgId, final String errMsg) {
                if (msgId >= 7) {
                    onFailCallback(FingerprintSDK.CODE_5, errMsg);
                } else {
                    showError();
                }
            }

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onStartAuthenticateResult(boolean isSuccess) {
                if (!isSuccess) {
                    String errMsg = "指纹认证存在安全漏洞";
                    onFailCallback(FingerprintSDK.CODE_8, errMsg);
                }
            }
        };
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void showError() {
        // 验证错误两次之内, 显示try again, 否则直接callback
        errorNum++;
        if (errorNum >= FingerprintConstants.FINGERPRINT_LIMIT_NUM) {
            onFailCallback(FingerprintSDK.CODE_0, "错误指纹");
            return;
        }
        // 验证错误两次之内, 显示try again, 展示双选按钮
        if (verifyType == FingerprintSDK.FINGERPRINT_PAY_START
                || verifyType == FingerprintSDK.FINGERPRINT_PAY_RE_START) {
            ll_dialog_double_btn.setVisibility(View.GONE);
            tv_fingerprint_cancel.setVisibility(View.VISIBLE);

        } else if (verifyType == FingerprintSDK.FINGERPRINT_PAY_VERIFY) {
            ll_dialog_double_btn.setVisibility(View.VISIBLE);
            tv_fingerprint_cancel.setVisibility(View.GONE);
        }
        tv_fingerprint_title.setText("Try again");
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tv_fingerprint_cancel) {
            onFailCallback(FingerprintSDK.CODE_1, "取消验证指纹");

        } else if (id == R.id.tv_dialog_cancel) {
            // 首次和第二次验证失败, 点击取消按钮
            onFailCallback(FingerprintSDK.CODE_10, "取消验证指纹支付");

        } else if (id == R.id.tv_dialog_pin) {
            // 首次和第二次验证失败, 点击 PIN 按钮
            onFailCallback(FingerprintSDK.CODE_7, "主动点击PIN验证按钮");
        }
    }

    private void onFailCallback(int code, String msg) {
        if (unlockCallback != null
                && FingerprintMain.getInstance().isFingerprintUnlock(verifyType)) {
            unlockCallback.onFailed(code, msg);

        } else if (paymentCallback != null
                && FingerprintMain.getInstance().isFingerprintPayment(verifyType)) {
            paymentCallback.onFailed(code, msg);
        }
        dismissAllowingStateLoss();
    }

    private void onSuccessCallback(Cipher results) {
        if (unlockCallback != null
                && FingerprintMain.getInstance().isFingerprintUnlock(verifyType)) {
            unlockCallback.onSuccess(results);

        } else if (paymentCallback != null
                && FingerprintMain.getInstance().isFingerprintPayment(verifyType)) {
            onFingerprintPaymentCallback(Base64.encodeToString(results.getIV(), Base64.URL_SAFE));
        }
        dismissAllowingStateLoss();
    }

    private void onFingerprintPaymentCallback(String iv) {
        if (verifyType == FingerprintSDK.FINGERPRINT_PAY_START) {
            // 生成RSA秘钥
            String publicKeyStr = "";
            String privateKeyStr = "";
            Map<String, Object> keys;
            try {
                keys = RSAHelper.getKeys();
                for (Map.Entry<String, Object> entry : keys.entrySet()) {
                    String key = entry.getKey();
                    Object obj = entry.getValue();
                    if (TextUtils.equals(key, RSAHelper.PUBLIC_KEY)) {
                        PublicKey publicKey = (PublicKey) obj;
                        publicKeyStr = Base64.encodeToString(publicKey.getEncoded(), Base64.DEFAULT);
                    } else if (TextUtils.equals(key, RSAHelper.PRIVATE_KEY)) {
                        PrivateKeyInfo pki = (PrivateKeyInfo) obj;
                        RSAPrivateKey pkcs1Key = RSAPrivateKey.getInstance(pki.parsePrivateKey());
                        privateKeyStr = Base64.encodeToString(pkcs1Key.getEncoded(), Base64.DEFAULT);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                LogUtil.e(TAG, "onCreateRsaKey Exception: " + e.toString());
            }
            if (TextUtils.isEmpty(publicKeyStr) || TextUtils.isEmpty(privateKeyStr)) {
                // 秘钥为空 认为开启指纹支付失败
                onFailCallback(FingerprintSDK.CODE_9, "");
                LogUtil.e(TAG, "指纹支付开启, 验证成功, 但是RSA秘钥生成失败...");

            } else {
                FingerprintSPUtil.putRSA_private(getActivity(), FingerprintMain.getInstance().getLname(), privateKeyStr);
                paymentCallback.onSuccess(iv, publicKeyStr, "");
            }
        } else if (verifyType == FingerprintSDK.FINGERPRINT_PAY_RE_START) {
            paymentCallback.onSuccess(iv, "", "");

        } else {
            // 指纹支付验证时, 不返回公钥
            String rsa_private = FingerprintSPUtil.getRSA_private(getActivity(), FingerprintMain.getInstance().getLname());
            paymentCallback.onSuccess(iv, "", rsa_private);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (fingerprintCore != null) {
            fingerprintCore.cancelAuthenticate();
            fingerprintCore = null;
        }
        if (unlockCallback != null) {
            unlockCallback = null;
        }
        if (paymentCallback != null) {
            paymentCallback = null;
        }
        FingerprintMain.getInstance().onDestroy();
    }
}
