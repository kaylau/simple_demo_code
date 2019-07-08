package com.kay.demo.fingerprint.fingerprintSix.core;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v4.os.CancellationSignal;
import android.util.Base64;
import android.util.Log;


import com.kay.demo.fingerprint.fingerprintSix.FingerprintSDK;
import com.kay.demo.fingerprint.util.LogUtil;

import java.io.Serializable;
import java.lang.ref.WeakReference;

import javax.crypto.Cipher;

@TargetApi(Build.VERSION_CODES.M)
public class FingerprintCore {
    private static final String TAG = FingerprintCore.class.getSimpleName();
    private static final int NONE = 0;
    private static final int CANCEL = 1;
    private static final int AUTHENTICATING = 2;
    private int mState = NONE;

    private FingerprintManagerCompat mFingerprintManager;
    private WeakReference<IFingerprintResultListener> mFpResultListener;
    private CancellationSignal mCancellationSignal;
    private CryptoObjectAES mCryptoObjectCreator;
    //    private CryptoObjectRSA mCryptoObjectCreator;
    private FingerprintManagerCompat.AuthenticationCallback mAuthCallback;
    private FingerprintManagerCompat.CryptoObject mCryptoObject;

    private boolean isSupport = false;
    private Context mContext;

    /**
     * 指纹识别回调接口
     */
    public interface IFingerprintResultListener extends Serializable {
        /**
         * 指纹识别成功
         */
        void onAuthenticateSuccess(Cipher result);
//        void onAuthenticateSuccess(Signature result);

        /**
         * 指纹识别失败
         */
        void onAuthenticateFailed(int helpId);

        /**
         * 指纹识别发生错误-不可短暂恢复
         */
        void onAuthenticateError(int idMsg, String errMsg);

        /**
         * 开始指纹识别监听成功
         */
        void onStartAuthenticateResult(boolean isSuccess);
    }

    public FingerprintCore(Context context) {
        this.mContext = context;
        mFingerprintManager = getFingerprintManager(context);
        isSupport = (mFingerprintManager != null && isSystemDetected() && isHardwareDetected());
        LogUtil.e(TAG, "fingerprint isSupport: " + isSupport);
    }

    /**
     * @param iv
     * @param keyStoreAliasName (app包名+lname)
     * @param listener
     */
    public void initCryptoObject(String iv, String keyStoreAliasName, CryptoObjectAES.ICryptoObjectCreateListener listener) {
        try {
            if (mCryptoObjectCreator == null) {
                mCryptoObjectCreator = new CryptoObjectAES(iv, keyStoreAliasName, listener);
                mCryptoObject = mCryptoObjectCreator.getCryptoObject();
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            LogUtil.e(TAG, "create cryptoObject failed!");
            notifyStartAuthenticateResult(false, throwable.getMessage());
        }
    }

    public void setFingerprintManager(IFingerprintResultListener fingerprintResultListener) {
        mFpResultListener = new WeakReference<>(fingerprintResultListener);
    }

    public void startAuthenticate() {
        startAuthenticate(mCryptoObject);
    }

    public boolean isAuthenticating() {
        return mState == AUTHENTICATING;
    }

    private void startAuthenticate(FingerprintManagerCompat.CryptoObject cryptoObject) {
        String errMsg;
        if (cryptoObject == null) {
            errMsg = "cryptoObject is null ...";
            LogUtil.e(TAG, errMsg);
            notifyStartAuthenticateResult(false, errMsg);
            return;
        }
        LogUtil.e(TAG, "startAuthenticate is start ....");
        prepareData();
        mState = AUTHENTICATING;
        try {
            mFingerprintManager.authenticate(cryptoObject, 0, mCancellationSignal, mAuthCallback, null);
            notifyStartAuthenticateResult(true, "");
        } catch (SecurityException e) {
            e.printStackTrace();
            try {
                mFingerprintManager.authenticate(null, 0, mCancellationSignal, mAuthCallback, null);
                notifyStartAuthenticateResult(true, "");
            } catch (SecurityException e2) {
                e2.printStackTrace();
                notifyStartAuthenticateResult(false, Log.getStackTraceString(e2));
            } catch (Throwable throwable) {
                throwable.printStackTrace();
                notifyStartAuthenticateResult(false, throwable.getMessage());
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            notifyStartAuthenticateResult(false, throwable.getMessage());
        }
    }

    private void notifyStartAuthenticateResult(boolean isSuccess, String exceptionMsg) {
        if (isSuccess) {
            LogUtil.e(TAG, "start authenticate...");
        } else {
            LogUtil.e(TAG, "startListening, Exception: " + exceptionMsg);
        }
        if (mFpResultListener != null && mFpResultListener.get() != null) {
            mFpResultListener.get().onStartAuthenticateResult(isSuccess);
        }
    }

    private void notifyAuthenticationSucceeded(Cipher result) {
        LogUtil.e(TAG, "onAuthenticationSucceeded");
        if (null != mFpResultListener && null != mFpResultListener.get()) {
            mFpResultListener.get().onAuthenticateSuccess(result);
        }
    }

    private void notifyAuthenticationError(int errMsgId, CharSequence errString) {
        LogUtil.e(TAG, "onAuthenticationError, errId:" + errMsgId + ", err:" + errString + ", retry after 30 seconds");
        if (null != mFpResultListener && null != mFpResultListener.get()) {
            mFpResultListener.get().onAuthenticateError(errMsgId, errString.toString());
        }
    }

    private void notifyAuthenticationFailed(int msgId, String errString) {
        LogUtil.e(TAG, "onAuthenticationFailed, msdId: " + msgId + " errString: " + errString);
        if (null != mFpResultListener && null != mFpResultListener.get()) {
            mFpResultListener.get().onAuthenticateFailed(msgId);
        }
    }

    private void prepareData() {
        if (mCancellationSignal == null) {
            mCancellationSignal = new CancellationSignal();
        }
        if (mAuthCallback == null)
            mAuthCallback = new FingerprintManagerCompat.AuthenticationCallback() {
                @Override
                public void onAuthenticationError(int errMsgId, CharSequence errString) {
                    // 多次指纹密码验证错误后，进入此方法；并且，不能短时间内调用指纹验证,一般间隔从几秒到几十秒不等,一般是30秒
                    // 这种情况不建议重试，建议提示用户用其他的方式解锁或者认证
                    mState = NONE;
                    notifyAuthenticationError(errMsgId, errString);
                }

                @Override
                public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
                    mState = NONE;
                    // 建议根据参数helpString返回值，并且仅针对特定的机型做处理，并不能保证所有厂商返回的状态一致
                    notifyAuthenticationFailed(helpMsgId, helpString.toString());
                }

                @Override
                public void onAuthenticationFailed() {
                    mState = NONE;
                    notifyAuthenticationFailed(0, "onAuthenticationFailed");
                }

                @Override
                public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result) {
                    LogUtil.e(TAG, "指纹识别成功..");
                    Cipher cipher = result.getCryptoObject().getCipher();

                    //通过此值，之判断第一次，如果没有值就运行cipher的加密算法，后面都是运行cipher的解密算法
                    if (FingerprintMain.getInstance().isStartType()) {
                        byte[] iv = cipher.getIV();
                        String ivStr = Base64.encodeToString(iv, Base64.URL_SAFE);
                        LogUtil.e(TAG, "加密iv为:" + ivStr);
                        if (FingerprintMain.getInstance().isReStartType()) {
                            FingerprintSDK.putIv(mContext, FingerprintSDK.INIT_IV_ERROR);
                        } else {
                            FingerprintSDK.putIv(mContext, ivStr.trim());
                        }
                    }
                    mState = NONE;
                    notifyAuthenticationSucceeded(cipher);
                }
            };
    }

    public void cancelAuthenticate() {
        if (mCancellationSignal != null && mState != CANCEL) {
            LogUtil.e(TAG, "cancelAuthenticate...");
            mState = CANCEL;
            mCancellationSignal.cancel();
            mCancellationSignal = null;
        }
    }

//    private void onFailedRetry(int msgId, String helpString) {
//        LogUtil.e(TAG,"onFailedRetry: msgId " + msgId + " helpString: " + helpString);
//        cancelAuthenticate();
//        mHandler.removeCallbacks(mFailedRetryRunnable);
//        mHandler.postDelayed(mFailedRetryRunnable, 1000 * 30); // 每次重试间隔一会儿再启动
//    }

//    private Runnable mFailedRetryRunnable = new Runnable() {
//        @Override
//        public void run() {
//            startAuthenticate(mCryptoObject);
//        }
//    };

    public boolean isSupport() {
        return isSupport;
    }

    /**
     * 时候有指纹识别硬件支持
     *
     * @return
     */
    public boolean isHardwareDetected() {
        try {
            boolean isHardwareDetected = mFingerprintManager.isHardwareDetected();
            LogUtil.e(TAG, "硬件支持标识：" + isHardwareDetected);
            if (isHardwareDetected) {
                LogUtil.e(TAG, "硬件支持指纹");
                return true;
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        LogUtil.e(TAG, "硬件不支持指纹");
        return false;
    }

    /**
     * 6.0以上的系统才支持指纹，6.0以下的系统硬件支持指纹的不考虑
     *
     * @return
     */
    public boolean isSystemDetected() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            LogUtil.e(TAG, "手机系统低于6.0不考虑指纹");
            return false;
        }
        return true;
    }

    /**
     * 是否录入指纹，有些设备上即使录入了指纹，但是没有开启锁屏密码的话此方法还是返回false
     *
     * @return
     */
    public boolean isHasEnrolledFingerprints() {
        try {
            boolean isHasEnrolledFingerprints = mFingerprintManager.hasEnrolledFingerprints();
            if (isHasEnrolledFingerprints) {
                LogUtil.e(TAG, "已经录入指纹");
                return true;
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        LogUtil.e(TAG, "没有录入指纹");
        return false;
    }

    @SuppressLint("ServiceCast")
    public static FingerprintManagerCompat getFingerprintManager(Context context) {
        FingerprintManagerCompat fingerprintManager = null;
        try {
            fingerprintManager = FingerprintManagerCompat.from(context);
        } catch (Throwable e) {
            e.printStackTrace();
            LogUtil.e(TAG, "have not class FingerprintManager");
        }
        return fingerprintManager;
    }

    public void onDestroy() {
        cancelAuthenticate();
        mAuthCallback = null;
        mFpResultListener = null;
        mCancellationSignal = null;
        mFingerprintManager = null;
        if (mCryptoObjectCreator != null) {
            mCryptoObjectCreator.onDestroy();
            mCryptoObjectCreator = null;
        }
    }
}