package com.kay.demo.fingerprint.fingerprintSix;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;

import com.kay.demo.fingerprint.fingerprintSix.callback.FingerprintPaymentCallback;
import com.kay.demo.fingerprint.fingerprintSix.callback.FingerprintUnlockCallback;
import com.kay.demo.fingerprint.fingerprintSix.core.FingerprintMain;
import com.kay.demo.fingerprint.fingerprintSix.util.FingerprintConstants;
import com.kay.demo.fingerprint.fingerprintSix.util.FingerprintSPUtil;
import com.kay.demo.fingerprint.fingerprintSix.util.FingerprintUtil;
import com.kay.demo.fingerprint.util.LogUtil;


public class FingerprintSDK {

    private static final String TAG = FingerprintSDK.class.getSimpleName();

    /**
     * 验证成功
     */
    public static int CODE_SUCCESS = 1000;

    /**
     * 错误指纹--sdk 指纹验证失败3次
     */
    public static int CODE_0 = 0;
    /**
     * 取消指纹
     */
    public static int CODE_1 = 1;
    /**
     * 不支持指纹功能
     */
    public static int CODE_2 = 2;
    /**
     * 支持但是没有指纹
     */
    public static int CODE_3 = 3;
    /**
     * 参数错误
     */
    public static int CODE_4 = 4;
    /**
     * 指纹锁定--系统锁定
     */
    public static int CODE_5 = 5;
    /**
     * 其他错误（异常）
     */
    public static int CODE_6 = 6;
    /**
     * 主动点击PIN验证按钮
     */
    public static int CODE_7 = 7;
    /**
     * 指纹认证存在安全漏洞
     */
    public static int CODE_8 = 8;

    /**
     * 指纹支付开启, 认证成功, 但是RSA秘钥生成失败
     */
    public static int CODE_9 = 9;
    /**
     * 指纹支付验证, 首次和第二次验证失败
     */
    public static int CODE_10 = 10;

    /**
     * 开启指纹登录
     */
    public static int FINGERPRINT_LOGIN_START = 100;
    /**
     * 验证指纹登录
     */
    public static int FINGERPRINT_LOGIN_VERIFY = 101;


    /**
     * 开启指纹支付
     */
    public static int FINGERPRINT_PAY_START = 200;

    /**
     * 验证指纹支付
     */
    public static int FINGERPRINT_PAY_VERIFY = 201;

    /**
     * 重新开启指纹支付 ---- 指纹支付时, 新增指纹, code8 再次开启指纹支付, rsa秘钥不重新生成
     */
    public static int FINGERPRINT_PAY_RE_START = 202;

    /**
     * 收银台或者pay码重新开启指纹时, 验证指纹通过, 先把iv重置, 等支付下单成功再更新指纹sdk的iv.
     * 原因: 收银台新增指纹时, 验证指纹通过, 不去支付下单关闭收银台, 再批价进入收银台, 指纹支付会成功.
     */
    public static String INIT_IV_ERROR = "init_iv_error";


    /**
     * 是否支持指纹功能
     *
     * @param mContext
     * @return
     */
    public static boolean isSupport(Context mContext) {
        if (mContext == null) {
            return false;
        }
        return FingerprintMain.getInstance().isSupport(mContext);
    }

    /**
     * 是否至少包含一个指纹
     *
     * @param mContext
     * @return
     */
    public static boolean isHasFingerprints(Context mContext) {
        if (mContext == null) {
            return false;
        }
        return FingerprintMain.getInstance().isHasEnrolledFingerprints(mContext);
    }

    /**
     * 开始验证指纹--解锁
     *
     * @param activity   传入当前activity
     * @param LName
     * @param verifyType 传入当前验证类型
     * @param callback   指纹验证后的回调
     */
    public static void startAuthenticateUnlock(Activity activity, String LName, int verifyType, FingerprintUnlockCallback callback) {
        LogUtil.d(TAG, "业务层开始调 startAuthenticate ");

        if (callback == null) {
            LogUtil.e(TAG, "mFingerprintResultCallback is null");
            return;
        }
        if (activity == null || TextUtils.isEmpty(LName)) {
            LogUtil.e(TAG, "param activity is null");
            callback.onFailed(FingerprintSDK.CODE_4, " activity or LName is  null");
            return;
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            LogUtil.e(TAG, "android 系统版本号低于 6.0");
            callback.onFailed(FingerprintSDK.CODE_2, "android 系统版本号低于 6.0");
            return;
        }
        FingerprintMain.getInstance().setUnlockCallback(callback);
        FingerprintMain.getInstance().startAuthenticate(activity, LName, verifyType);
    }

    /**
     * 开始验证指纹--支付
     *
     * @param activity   传入当前activity
     * @param LName
     * @param verifyType 传入当前验证类型
     * @param callback   指纹验证后的回调
     */
    public static void startAuthenticatePayment(Activity activity, String LName, int verifyType, FingerprintPaymentCallback callback) {
        LogUtil.d(TAG, "业务层开始调 startAuthenticate ");

        if (callback == null) {
            LogUtil.e(TAG, "mFingerprintResultCallback is null");
            return;
        }
        if (activity == null || TextUtils.isEmpty(LName)) {
            LogUtil.e(TAG, "param activity is null");
            callback.onFailed(FingerprintSDK.CODE_4, " activity or LName is  null");
            return;
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            LogUtil.e(TAG, "android 系统版本号低于 6.0");
            callback.onFailed(FingerprintSDK.CODE_2, "android 系统版本号低于 6.0");
            return;
        }
        FingerprintMain.getInstance().setPaymentCallback(callback);
        FingerprintMain.getInstance().startAuthenticate(activity, LName, verifyType);
    }

    /**
     * 打开指纹设置界面
     *
     * @param mContext
     */
    public static void openFingerprintSettings(Context mContext) {
        if (mContext == null) {
            LogUtil.e(TAG, "param error: context is null");
            return;
        }
        FingerprintUtil.openFingerPrintSettingPage(mContext);
    }

    /**
     * 取消指纹验证, 关闭验证弹窗
     */
    public static void cancelFingerprintRecognition() {
        FingerprintMain.getInstance().cancelFingerprintRecognition();
    }

    public static void putIv(Context mContext, String iv) {
        FingerprintSPUtil.putString(mContext, FingerprintConstants.KEY_ENCRYPT_IV, iv.trim());
    }

    public static String getIV(Context context) {
        return FingerprintSPUtil.getString(context, FingerprintConstants.KEY_ENCRYPT_IV).trim();
    }
}

