package com.kay.demo.qrcode.decoder;

import android.graphics.Bitmap;
import android.text.TextUtils;

public class DecoderSDK {


    private static final String TAG = "DecoderSDK";

    private ZbarDecoder zbarDecoder = null;
    /**
     * 识别本地相册二维码
     * 优先使用zbar解码, 解码失败再使用zxing
     *
     * @param path 图片路径
     * @return
     */
    public String decodeQRCode(String path) {
        String content = new ZbarDecoder().decodeQRCode(path);
        if (TextUtils.isEmpty(content)) {
            content = new ZxingDecoder().decodeQRCode(path);
        }
        return content;
    }

    /**
     * 识别本地相册二维码
     * 优先使用zbar解码, 解码失败再使用zxing
     *
     * @param bitmap 图片bitmap
     * @return
     */
    public String decodeQRCode(Bitmap bitmap) {
        String content = new ZbarDecoder().decodeQRCode(bitmap);
        if (TextUtils.isEmpty(content)) {
            content = new ZxingDecoder().decodeQRCode(bitmap);
        }
        return content;
    }


    /**
     * 解析预览图片中的二维码
     * @param rotatedData 预览图数据
     * @param width 预览图的宽
     * @param height 预览图的高
     * @param cropRectLeft 裁剪框距离最左边的距离(左上角的X坐标)
     * @param cropRectTop 裁剪框距离最上边的距离(左上角的Y坐标)
     * @param cropRect_W 裁剪框的宽度
     * @param cropRect_H 裁剪框的高度
     * @return
     */
    public String decodeQRCode(byte[] rotatedData, int width, int height, int cropRectLeft, int cropRectTop, int cropRect_W, int cropRect_H){
        if (zbarDecoder == null){
            zbarDecoder = new ZbarDecoder();
        }
        String result = zbarDecoder.decodeQRCode(rotatedData, width, height, cropRectLeft, cropRectTop , cropRect_W, cropRect_H);
        return result;
    }
}
