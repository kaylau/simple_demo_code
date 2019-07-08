package com.kay.demo.fingerprint.fingerprintSix.core;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import com.kay.demo.fingerprint.BuildConfig;
import com.kay.demo.fingerprint.fingerprintSix.util.FingerprintConstants;
import com.kay.demo.fingerprint.fingerprintSix.util.FingerprintSPUtil;
import com.kay.demo.fingerprint.util.LogUtil;

import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.ECGenParameterSpec;

@TargetApi(Build.VERSION_CODES.M)
public class CryptoObjectRSA {
    private static final String TAG = CryptoObjectRSA.class.getSimpleName();
    //Android Key Store里面key的别名
    public static final String KEY_NAME = BuildConfig.APPLICATION_ID;//这个名字应该是保证唯一的，建议使用APPID,这是androidstyore

    private FingerprintManagerCompat.CryptoObject mCryptoObject;
    private KeyStore mKeyStore;
    public KeyPairGenerator mKeyPairGenerator;//产生非对称密钥
    private ICryptoObjectCreateListener iCryptoObjectCreateListener;
    private Signature mSignature;
    private Context mContext;
    private String encryptKey;

    public interface ICryptoObjectCreateListener {
        void onDataPreparedFailed();
    }

    public CryptoObjectRSA(Context mContext, ICryptoObjectCreateListener createListener) {
        this.mContext = mContext;
        this.iCryptoObjectCreateListener = createListener;
        mKeyStore = providesKeystore();
        mKeyPairGenerator = providesKeyPairGenerator();
        mSignature = providesSignature();

        prepareData();

        if (mKeyStore != null && mKeyPairGenerator != null && mSignature != null) {
            mCryptoObject = new FingerprintManagerCompat.CryptoObject(mSignature);
            LogUtil.e(TAG, "CryptoObject is created..");
        } else {
            LogUtil.e(TAG, "CryptoObject is not created..");
            onErrorCallback();
        }
    }


    private void prepareData() {
        try {
            createKey();
            // Set up the crypto object for later. The object will be authenticated by use
            // of the fingerprint.
            if (!initSignature()) {
                LogUtil.e(TAG, "Failed to init Cipher.");
                onErrorCallback();
            }
        } catch (Exception e) {
            LogUtil.e(TAG, " Failed to init Cipher, e:" + Log.getStackTraceString(e));
            onErrorCallback();
        }
    }

    private boolean initSignature() {
        try {
            mKeyStore.load(null);
            PrivateKey key = (PrivateKey) mKeyStore.getKey(KEY_NAME, null);
            //将这个公钥传给后台，后台以便能解密我们传入的加密数据
            PublicKey publicKey = mKeyStore.getCertificate(KEY_NAME).getPublicKey();
            FingerprintSPUtil.putString(mContext, FingerprintConstants.KEY_ASYMMETRIC_PUBLICKKEY, new String(Base64.encode(publicKey.getEncoded(), Base64.URL_SAFE), "utf-8"));
//            PrivateKey key = mKeyPairGenerator.generateKeyPair().getPrivate();
            //用私钥初始化
            mSignature.initSign(key);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            onErrorCallback();
            return false;
        }
    }

    /**
     * Creates a symmetric key in the Android Key Store which can only be used after the user has
     * authenticated with fingerprint.
     */
    private void createKey() {
        // The enrolling flow for fingerprint. This is where you ask the user to set up fingerprint
        // for your flow. Use of keys is necessary if you need to know if the set of
        // enrolled fingerprints has changed.
        try {
            mKeyStore.load(null);
//            encryptKey = FingerprintSPUtil.getString(mContext, FingerprintConstants.KEY_ENCRYPT_DATA);
            if (TextUtils.isEmpty(encryptKey)) {
                //产生密钥对
                mKeyPairGenerator.initialize(
                        new KeyGenParameterSpec.Builder(KEY_NAME,
                                KeyProperties.PURPOSE_SIGN)
                                .setDigests(KeyProperties.DIGEST_SHA256)
                                .setAlgorithmParameterSpec(new ECGenParameterSpec("secp256r1"))
                                // Require the user to authenticate with a fingerprint to authorize
                                // every use of the private key
                                .setUserAuthenticationRequired(true)
                                .build());
                mKeyPairGenerator.generateKeyPair();
            }

        } catch (Exception e) {
            LogUtil.e(TAG, " Failed to createKey, e:" + Log.getStackTraceString(e));
            onErrorCallback();
            throw new RuntimeException(e);
        }
    }


    private KeyStore providesKeystore() {
        try {
            KeyStore mKeyStore = KeyStore.getInstance("AndroidKeyStore");
            LogUtil.e(TAG, "KeyStore is created..");
            return mKeyStore;
        } catch (Throwable e) {
            onErrorCallback();
            e.printStackTrace();
            return null;
        }
    }

    private void onErrorCallback() {
        if (iCryptoObjectCreateListener != null) {
            iCryptoObjectCreateListener.onDataPreparedFailed();
        }
    }

    private KeyPairGenerator providesKeyPairGenerator() {
        try {
            return KeyPairGenerator.getInstance(KeyProperties.KEY_ALGORITHM_EC, "AndroidKeyStore");
        } catch (Throwable e) {
            e.printStackTrace();
            onErrorCallback();
        }
        return null;
    }

    private Signature providesSignature() {
        try {
            return Signature.getInstance("SHA256withECDSA");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            onErrorCallback();
        }
        return null;
    }


    public FingerprintManagerCompat.CryptoObject getCryptoObject() {
        return mCryptoObject;
    }

    public void onDestroy() {
        mCryptoObject = null;
        mSignature = null;
        mKeyStore = null;
    }
}
