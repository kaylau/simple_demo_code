package com.kay.demo.qrcode.create;

import android.graphics.Bitmap;
import android.text.TextUtils;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.util.Hashtable;

/**
 * Date: 2018/10/9 下午2:25
 * Author: kay lau
 * Description: 生成条形码或者二维码
 */
public class QRCodeUtil {

    /**
     * 根据format生成一维码或者二维码
     *
     * @param content
     * @param format
     * @param codeWidth
     * @param codeHeight
     * @return
     */
    private static Bitmap onCreateCoder(String content, BarcodeFormat format, int codeWidth, int codeHeight) {

        if (TextUtils.isEmpty(content)) {
            return null;
        }
        BitMatrix matrix;
        Hashtable<EncodeHintType, Object> param;
        // 用于设置QR二维码参数
        param = new Hashtable<>();
        // 设置QR二维码的纠错级别——这里选择最高H级别
        param.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        param.put(EncodeHintType.MARGIN, 0);
        // 设置编码方式
        param.put(EncodeHintType.CHARACTER_SET, "utf-8");

        //生成二维矩阵,编码时指定大小,不要生成了图片以后再进行缩放,这样会模糊导致识别失败
        try {
            matrix = new MultiFormatWriter().encode(content, format, codeWidth, codeHeight, param);
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        //二维矩阵转为一维像素数组,也就是一直横着排了
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (matrix.get(x, y)) {
                    pixels[y * width + x] = 0xff000000;
                } else {//这个else要加上去，否者保存的二维码全黑
                    pixels[y * width + x] = 0xffffffff;
                }
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        //通过像素数组生成bitmap,具体参考api
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    /**
     * 生成指定大小的二维码
     *
     * @param content
     * @param imageView
     * @return
     */
    public static Bitmap getQRBitmap(String content, ImageView imageView) {
        int width = 320;
        int height = 320;
        if (imageView != null) {
            int w = imageView.getWidth();
            int h = imageView.getHeight();
            width = w <= 0 ? width : w;
            height = h <= 0 ? height : h;
        }
        return onCreateCoder(content, BarcodeFormat.QR_CODE, width, height);
    }

    /**
     * 生成指定大小的条形码
     *
     * @param content
     * @param imageView
     * @return
     */
    public static Bitmap getBarBitmap(String content, ImageView imageView) {
        int width = 590;
        int height = 165;
        if (imageView != null) {
            int w = imageView.getWidth();
            int h = imageView.getHeight();
            width = w <= 0 ? width : w;
            height = h <= 0 ? height : h;
        }
        return onCreateCoder(content, BarcodeFormat.CODE_128, width, height);
    }
}
