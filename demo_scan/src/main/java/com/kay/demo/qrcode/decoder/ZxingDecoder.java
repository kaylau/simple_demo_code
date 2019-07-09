package com.kay.demo.qrcode.decoder;

import android.graphics.Bitmap;
import android.text.TextUtils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.GlobalHistogramBinarizer;
import com.google.zxing.common.HybridBinarizer;
import com.kay.demo.qrcode.utils.LogUtil;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class ZxingDecoder {

    public static final Map<DecodeHintType, Object> HINTS = new EnumMap<>(DecodeHintType.class);

    static {
        List<BarcodeFormat> allFormats = new ArrayList<>();
        allFormats.add(BarcodeFormat.AZTEC);
        allFormats.add(BarcodeFormat.CODABAR);
        allFormats.add(BarcodeFormat.CODE_39);
        allFormats.add(BarcodeFormat.CODE_93);
        allFormats.add(BarcodeFormat.CODE_128);
        allFormats.add(BarcodeFormat.DATA_MATRIX);
        allFormats.add(BarcodeFormat.EAN_8);
        allFormats.add(BarcodeFormat.EAN_13);
        allFormats.add(BarcodeFormat.ITF);
        allFormats.add(BarcodeFormat.MAXICODE);
        allFormats.add(BarcodeFormat.PDF_417);
        allFormats.add(BarcodeFormat.QR_CODE);
        allFormats.add(BarcodeFormat.RSS_14);
        allFormats.add(BarcodeFormat.RSS_EXPANDED);
        allFormats.add(BarcodeFormat.UPC_A);
        allFormats.add(BarcodeFormat.UPC_E);
        allFormats.add(BarcodeFormat.UPC_EAN_EXTENSION);

        HINTS.put(DecodeHintType.TRY_HARDER, BarcodeFormat.QR_CODE);
        HINTS.put(DecodeHintType.POSSIBLE_FORMATS, allFormats);
        HINTS.put(DecodeHintType.CHARACTER_SET, "utf-8");
    }

    /**
     * 同步解析本地图片二维码。该方法是耗时操作，请在子线程中调用。
     *
     * @param picturePath 要解析的二维码图片本地路径
     * @return 返回二维码图片里的内容 或 null
     */
    public  String decodeQRCode(String picturePath) {
        Bitmap scanBitmap = DecoderUtil.getLocalBitmap(picturePath);
        return decodeQRCode(scanBitmap);
    }

    /**
     * 同步解析bitmap二维码。该方法是耗时操作，请在子线程中调用。
     * @param bitmap 要解析的二维码图片
     * @return 返回二维码图片里的内容 或 null
     */
    public String decodeQRCode(Bitmap bitmap) {
        LogUtil.d(" ", "------------尝试【 ZXin 解码 】------------");
        Result result = null;
        RGBLuminanceSource source = null;
        try {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            int[] pixels = new int[width * height];
            bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
            source = new RGBLuminanceSource(width, height, pixels);
            result = new MultiFormatReader().decode(new BinaryBitmap(new HybridBinarizer(source)), HINTS);
            DecoderUtil.gcBitmap(bitmap);
            if(!TextUtils.isEmpty(result.getText())){
                LogUtil.d(" ", "------------【 ZXin 解码 成功 】------------");
            }
            return result.getText();
        } catch (Exception e) {
            e.printStackTrace();
            if (source != null) {
                try {
                    result = new MultiFormatReader().decode(new BinaryBitmap(new GlobalHistogramBinarizer(source)), HINTS);
                    if(!TextUtils.isEmpty(result.getText())){
                        LogUtil.d(" ", "------------【 ZXin 解码 成功 】------------");
                    }
                    return result.getText();
                } catch (Throwable e2) {
                    e2.printStackTrace();
                }
            }
            DecoderUtil.gcBitmap(bitmap);
            return null;
        }
    }

}
