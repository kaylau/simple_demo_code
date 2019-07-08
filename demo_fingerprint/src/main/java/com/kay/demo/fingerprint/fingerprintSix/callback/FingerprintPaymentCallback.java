package com.kay.demo.fingerprint.fingerprintSix.callback;

public interface FingerprintPaymentCallback {

    /**
     * @param iv
     * @param publicKey  用于指纹支付开启接口的传参
     * @param privateKey 用于保存指纹开启的状态
     */
    void onSuccess(String iv, String publicKey, String privateKey);

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
