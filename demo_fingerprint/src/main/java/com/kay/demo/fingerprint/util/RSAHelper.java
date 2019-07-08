package com.kay.demo.fingerprint.util;


import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;
import java.util.Map;


public class RSAHelper {

    public static final String KEY_ALGORITHM = "RSA";
    public static final String PUBLIC_KEY = "publicKey";
    public static final String PRIVATE_KEY = "privateKey";


    /**
     * 生成公钥和私钥
     *
     * @throws NoSuchAlgorithmException
     */
    public static Map<String, Object> getKeys() throws NoSuchAlgorithmException {
        HashMap<String, Object> map = new HashMap<>();
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        keyPairGen.initialize(1024);
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        PrivateKeyInfo pki = PrivateKeyInfo.getInstance(privateKey.getEncoded());
        map.put(PUBLIC_KEY, publicKey);
        map.put(PRIVATE_KEY, pki);
        return map;
    }


}