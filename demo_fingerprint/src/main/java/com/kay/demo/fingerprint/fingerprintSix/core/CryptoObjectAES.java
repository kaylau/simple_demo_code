package com.kay.demo.fingerprint.fingerprintSix.core;

import android.annotation.TargetApi;
import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.util.Base64;


import com.kay.demo.fingerprint.util.LogUtil;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

@TargetApi(Build.VERSION_CODES.M)
public class CryptoObjectAES {
    private static final String TAG = CryptoObjectAES.class.getSimpleName();

    private FingerprintManagerCompat.CryptoObject mCryptoObject;
    private KeyStore mKeyStore;
    private KeyGenerator mKeyGenerator;
    private Cipher mCipher;
    private ICryptoObjectCreateListener iCryptoObjectCreateListener;
    private String iv;
    private String keyStoreAliasName;//  Android Key Store里面key的别名 这个名字应该是保证唯一的 (app包名+lname + CryptoObjectAES.class.getSimpleName() )

    public interface ICryptoObjectCreateListener {
        /**
         * CryptoObjectAES 成功
         */
        void onDataPreparedSuccess();

        /**
         * CryptoObjectAES 失败
         *
         * @param errMsg 错误信息
         */
        void onDataPreparedFailed(String errMsg);
    }

    /**
     * @param iv
     * @param keyStoreAliasName (app包名+lname + CryptoObjectAES.class.getSimpleName() )
     * @param listener
     */
    public CryptoObjectAES(String iv, String keyStoreAliasName, ICryptoObjectCreateListener listener) {
        this.iv = iv;
        this.keyStoreAliasName = keyStoreAliasName + CryptoObjectAES.class.getSimpleName();
        this.iCryptoObjectCreateListener = listener;
        preData();
    }

    private void preData() {
        try {
            mKeyStore = providesKeystore();
            mKeyGenerator = providesKeyGenerator();
            mCipher = providesCipher();
            createKey();
            // Set up the crypto object for later. The object will be authenticated by use
            // of the fingerprint.
            initCipher();
            if (mKeyStore != null && mKeyGenerator != null && mCipher != null) {
                mCryptoObject = new FingerprintManagerCompat.CryptoObject(mCipher);
                LogUtil.i(TAG, "CryptoObject 初始化成功");
                onSuccessCallback();
            } else {
                LogUtil.i(TAG, "CryptoObject 初始化失败");
                onErrorCallback("CryptoObject 初始化失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.d(TAG, "CryptoObjectAES初始化失败2：" + e.getMessage());
            LogUtil.e(TAG, "initCipher iv: " + iv);
            onErrorCallback(e.getMessage());
        }
    }

    private KeyStore providesKeystore() throws KeyStoreException {

        KeyStore mKeyStore = KeyStore.getInstance("AndroidKeyStore");
        LogUtil.e(TAG, "KeyStore is created..");
        return mKeyStore;
    }

    private KeyGenerator providesKeyGenerator() throws NoSuchProviderException, NoSuchAlgorithmException {
        return KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
    }

    private Cipher providesCipher() throws NoSuchPaddingException, NoSuchAlgorithmException {
        return Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/" + KeyProperties.BLOCK_MODE_CBC + "/" + KeyProperties.ENCRYPTION_PADDING_PKCS7);
    }

    /**
     * Creates a symmetric key in the Android Key Store which can only be used after the user has
     * authenticated with fingerprint.
     */
    private void createKey() throws CertificateException, NoSuchAlgorithmException, IOException, InvalidAlgorithmParameterException {
        // The enrolling flow for fingerprint. This is where you ask the user to set up fingerprint
        // for your flow. Use of keys is necessary if you need to know if the set of
        // enrolled fingerprints has changed.

        mKeyStore.load(null);
        //只要是开启 ，直接 覆盖之前的key
        if (FingerprintMain.getInstance().isStartType()) {
            mKeyGenerator.init(new KeyGenParameterSpec.Builder(keyStoreAliasName, // (app包名+lname)
                    KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)//即AES算法使用CBC模式。
                    // Require the user to authenticate with a fingerprint to authorize every use
                    // of the key
                    .setUserAuthenticationRequired(true)//在使用key之前用户的身份需要被认证。
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)//使用PKSC7（Public Key Cryptography Standard #7）的方式去产生用于填充AES数据块的字节(也叫Padding)，这样就是要保证每个数据块的大小相同的。
                    .build());
            mKeyGenerator.generateKey();
        }
    }

    /**
     * Initialize the {@link Cipher} instance with the created key in the {@link #createKey()}
     * method.
     *
     * @return {@code true} if initialization is successful, {@code false} if the lock screen has
     * been disabled or reset after the key was generated, or if a fingerprint got enrolled after
     * the key was generated.
     */
    private void initCipher() throws CertificateException, NoSuchAlgorithmException, IOException, UnrecoverableKeyException, KeyStoreException, InvalidKeyException, InvalidAlgorithmParameterException {
        if (mKeyStore != null) {
            mKeyStore.load(null);

            SecretKey key = (SecretKey) mKeyStore.getKey(keyStoreAliasName, null);
            if (FingerprintMain.getInstance().isStartType()) {
                LogUtil.e(TAG, "initCipher method 开启指纹");
                mCipher.init(KeyProperties.PURPOSE_ENCRYPT, key);
            } else if (FingerprintMain.getInstance().isVerifyType()) {
                LogUtil.e(TAG, "initCipher method 验证指纹");
                mCipher.init(KeyProperties.PURPOSE_DECRYPT, key, new IvParameterSpec(Base64.decode(iv, Base64.URL_SAFE)));
            }
            LogUtil.e(TAG, "initCipher succeed..");
        } else {
            LogUtil.e(TAG, "initCipher mKeyStore null..");
        }
    }

    public FingerprintManagerCompat.CryptoObject getCryptoObject() {
        return mCryptoObject;
    }

    private void onErrorCallback(String msg) {
        if (iCryptoObjectCreateListener != null) {
            iCryptoObjectCreateListener.onDataPreparedFailed(msg);
        }
    }

    private void onSuccessCallback() {
        if (iCryptoObjectCreateListener != null) {
            iCryptoObjectCreateListener.onDataPreparedSuccess();
        }
    }

    public void onDestroy() {
        mCipher = null;
        mCryptoObject = null;
        mKeyStore = null;
    }
}
