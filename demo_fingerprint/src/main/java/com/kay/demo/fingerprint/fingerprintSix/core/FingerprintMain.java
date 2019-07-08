package com.kay.demo.fingerprint.fingerprintSix.core;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;

import com.kay.demo.fingerprint.fingerprintSix.FingerprintSDK;
import com.kay.demo.fingerprint.fingerprintSix.callback.FingerprintCallback;
import com.kay.demo.fingerprint.fingerprintSix.ui.CommonDialog;
import com.kay.demo.fingerprint.fingerprintSix.ui.FingerprintDialog;
import com.kay.demo.fingerprint.util.LogUtil;

public class FingerprintMain {

    private final static String TAG = FingerprintMain.class.getSimpleName();
    private static FingerprintMain instance;

    public FingerprintCore mFingerprintCore;

    public int fingerprint_type; //用于区分是  开启、验证
    private FingerprintDialog fragment;

    private FingerprintCallback unlockCallback;

    public static FingerprintMain getInstance() {
        if (instance == null) {
            synchronized (FingerprintMain.class) {
                instance = new FingerprintMain();
            }
        }
        return instance;
    }


    private void initFingerprintSdk(Context mContext) {
        LogUtil.e(TAG, "FingerprintMain is init ...");
        mFingerprintCore = new FingerprintCore(mContext);
    }

    /**
     * 是否支持指纹识别（手机系统、硬件是否支持）
     *
     * @return true 支持；false 不支持
     */
    public boolean isSupport(Context mContext) {
        initFingerprintSdk(mContext);
        if (!mFingerprintCore.isSupport()) {
            LogUtil.e(TAG, "手机不支持指纹支付");
            onDestroy();
            return false;
        }
        onDestroy();
        return true;
    }

    /**
     * 是否录入指纹，有些设备上即使录入了指纹，但是没有开启锁屏密码的话此方法还是返回false
     *
     * @return true 已录入；false 未录入
     */
    public boolean isHasEnrolledFingerprints(Context mContext) {
        initFingerprintSdk(mContext);
        if (!mFingerprintCore.isHasEnrolledFingerprints()) {
            LogUtil.e(TAG, "还没有录入指纹");
            onDestroy();
            return false;
        }
        onDestroy();
        return true;
    }

    /**
     * 开启指纹验证
     *
     * @param activity
     * @param LName
     * @param verifyType
     */
    public void startAuthenticate(Activity activity, String LName, int verifyType) {
        // Android Key Store里面key的别名 这个名字应该是保证唯一的，建议使用APPID,这是androidstyore    (app包名+lname)
        String keyStoreAliasName = activity.getPackageName() + LName;
        fingerprint_type = verifyType;
        String iv = FingerprintSDK.getIV(activity);

        initFingerprintSdk(activity.getBaseContext());

        if (!mFingerprintCore.isSupport()) {
            LogUtil.e(TAG, "手机不支持指纹支付");
            onErrorCallback(FingerprintSDK.CODE_2, "手机不支持指纹功能");
            onDestroy();
            return;
        }

        if (!mFingerprintCore.isHasEnrolledFingerprints()) {
            LogUtil.e(TAG, "还没有录入指纹");
            new CommonDialog
                    .Builder(activity)
                    .setMessage("Unset fingerprints in your device, please set Touch ID in the system setting - > fingerprints")
                    .setMessageCenter(true)
                    .setCancelable(false)
                    .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();
            onErrorCallback(FingerprintSDK.CODE_3, "手机没有录入指纹");
            onDestroy();
            return;
        }

        //初始化AES密钥
        mFingerprintCore.initCryptoObject(iv, keyStoreAliasName, new CryptoObjectAES.ICryptoObjectCreateListener() {
            @Override
            public void onDataPreparedSuccess() {
                LogUtil.i(TAG, "initCryptoObject success,开始调起弹窗");
                openFingerDialog(activity, verifyType);
            }

            @Override
            public void onDataPreparedFailed(String msg) {
                LogUtil.i(TAG, "create cryptoObject failed,开始把失败返给业务层:" + msg);
                onErrorCallback(FingerprintSDK.CODE_8, msg);
            }
        });
    }

    /**
     * 打开指纹弹窗
     *
     * @param activity
     * @param verifyType
     */
    private void openFingerDialog(Activity activity, int verifyType) {
        try {
            //为了解决异常:Can not perform this action after onSaveInstanceState，要用commitAllowingStageLoss()，dismissAllowingStateLoss
            //https://stackoverflow.com/questions/15729138/on-showing-dialog-i-get-can-not-perform-this-action-after-onsaveinstancestate
            FragmentTransaction ft = activity.getFragmentManager().beginTransaction();
            if (isFingerprintUnlock(verifyType)) {
                fragment = FingerprintDialog.newInstance(unlockCallback, mFingerprintCore, verifyType);
            }
            ft.add(fragment, "fingerprintDialog");
            ft.commitAllowingStateLoss();
        } catch (Exception e) {
            e.printStackTrace();
            onErrorCallback(FingerprintSDK.CODE_6, e.getMessage());
            onDestroy();
        }
    }

    private void onErrorCallback(int errCode, String errMsg) {
        if (unlockCallback != null && (isFingerprintUnlock(fingerprint_type))) {
            unlockCallback.onFailed(errCode, errMsg);

        }
    }

    /**
     * 判断是否为开启指纹
     *
     * @return
     */
    public boolean isStartType() {
        return fingerprint_type == FingerprintSDK.FINGERPRINT_LOGIN_START
                || fingerprint_type == FingerprintSDK.FINGERPRINT_PAY_START
                || fingerprint_type == FingerprintSDK.FINGERPRINT_PAY_RE_START;
    }

    public boolean isReStartType() {
        return fingerprint_type == FingerprintSDK.FINGERPRINT_PAY_RE_START;
    }

    /**
     * 判断是否为验证指纹
     *
     * @return
     */
    public boolean isVerifyType() {
        return fingerprint_type == FingerprintSDK.FINGERPRINT_LOGIN_VERIFY || fingerprint_type == FingerprintSDK.FINGERPRINT_PAY_VERIFY;
    }

    /**
     * 判断是否为指纹解锁
     *
     * @param verifyType
     * @return
     */
    public boolean isFingerprintUnlock(int verifyType) {
        return verifyType == FingerprintSDK.FINGERPRINT_LOGIN_START
                || verifyType == FingerprintSDK.FINGERPRINT_LOGIN_VERIFY;
    }

    public void onDestroy() {
        LogUtil.e(TAG, "FingerprintMain onDestroy...");
        instance = null;
        if (mFingerprintCore != null) {
            mFingerprintCore.onDestroy();
            mFingerprintCore = null;
        }
    }

    public void setUnlockCallback(FingerprintCallback unlockCallback) {
        this.unlockCallback = unlockCallback;
    }

}
