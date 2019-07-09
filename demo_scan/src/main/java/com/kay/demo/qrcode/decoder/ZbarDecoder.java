package com.kay.demo.qrcode.decoder;

import android.graphics.Bitmap;

import com.kay.demo.qrcode.utils.LogUtil;


/**
 * 创建时间:16/4/8 下午11:22
 * 描述:Zbar解析二维码图片
 */
public class ZbarDecoder {

    /**
     * 识别相册图片
     * @param path
     * @return
     */
    public String decodeQRCode(String path) {
        Bitmap scanBitmap = DecoderUtil.getLocalBitmap(path);
        return decodeQRCode(scanBitmap) ;
    }

    /**
     * @param mBitmap
     * @return
     */
    public String decodeQRCode(Bitmap mBitmap) {
        LogUtil.d(" ", "------------开始执行【 ZBar 解码 】------------");
        byte[] data = DecoderUtil.getYUV420sp(mBitmap.getWidth(), mBitmap.getHeight(), mBitmap);

        //调用Zbar 解码
        com.dtr.zbar.build.ZBarDecoder mZBarDecoder = new com.dtr.zbar.build.ZBarDecoder();
        String decodeRawStr =  mZBarDecoder.decodeRaw(data, mBitmap.getWidth(), mBitmap.getHeight());
        DecoderUtil.gcBitmap(mBitmap);
        return decodeRawStr ;
    }


    /**
     * @return
     */
    public String decodeQRCode(byte[] rotatedData, int width, int height, int cropRectLeft, int cropRectTop, int cropRect_W, int cropRect_H){
        LogUtil.d(" ", "------------开始执行【 解码 】------------");
        //调用Zbar 解码
        com.dtr.zbar.build.ZBarDecoder mZBarDecoder = new com.dtr.zbar.build.ZBarDecoder();
        String result = mZBarDecoder.decodeCrop(rotatedData, width, height, cropRectLeft, cropRectTop , cropRect_W, cropRect_H);
        return result;
    }

}