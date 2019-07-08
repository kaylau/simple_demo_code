package com.kay.demo.fingerprint.fingerprintSix.callback;

import javax.crypto.Cipher;

public interface FingerprintUnlockCallback {
    /**
     * 验证成功,返回cipher加解密对象
     */
    void onSuccess(Cipher cipher);

    /**
     * 校验失败
     *
     * @param code   </br> 0---错误指纹
     *               </br> 1---取消指纹
     *               </br> 2---不支持指纹功能
     *               </br> 3---支持但是没有指纹
     *               </br> 4---参数错误
     *               </br> 5---其他错误（异常）
     * @param errMsg
     */
    void onFailed(int code, String errMsg);
}
