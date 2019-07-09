/*
 * Copyright (C) 2010 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.kay.demo.qrcode.scan.decode;

import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.kay.demo.qrcode.decoder.DecoderSDK;
import com.kay.demo.qrcode.scan.ScanBaseActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

final class DecodeHandler extends Handler {

    private final ScanBaseActivity activity;
    private DecoderSDK decoderSDK = null;
    private boolean running = true;

    DecodeHandler(ScanBaseActivity activity) {
        this.activity = activity;
    }

    @Override
    public void handleMessage(Message message) {
        if (!running) {
            return;
        }
        if (message.what == MainHandler.MSG_DECODE) {
            decode((byte[]) message.obj, message.arg1, message.arg2);

        } else if (message.what == MainHandler.MSG_QUIT) {
            running = false;
            Looper.myLooper().quit();
        }
    }


    /**
     * 扫描解码
     */
    private void decode(byte[] data, int width, int height) {
        // long start = System.currentTimeMillis();
        // 这里需要将获取的data翻转一下，因为相机默认拿的的横屏的数据
        byte[] rotatedData = new byte[data.length];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++){
                rotatedData[x * height + height - y - 1] = data[x + y * width];
            }
        }

        // 宽高也要调整
        int tmp = width;
        width = height;
        height = tmp;
        Rect mCropRect = activity.initCrop();
        String result = null;
        try {
            if (decoderSDK == null){
                decoderSDK = new DecoderSDK();
            }
            result = decoderSDK.decodeQRCode(rotatedData, width, height, mCropRect.left, mCropRect.top, mCropRect.width(), mCropRect.height());
        } catch (Exception ex) {
            ex.printStackTrace();
            decoderSDK = null ;
        }

//            byte[] bytes = decodePreData(data, height, width);
//            //保存整张图片
//            ImageUtil.saveBitmap(BitmapFactory.decodeByteArray(bytes,0,bytes.length), ImageUtil.getImagePath(activity, "test.png"));
//            //裁剪后的bitmap
//            Bitmap bitmap = ImageUtil.cropBitmap(activity, bytes, activity.getCameraSize(), activity.initCrop());
//            //保存裁剪后的图片
//            ImageUtil.saveBitmap(bitmap, ImageUtil.getImagePath(activity, "crop.png"));

        Handler handler = activity.getHandler();
        if (result != null) {
            // long end = System.currentTimeMillis();
            if (handler != null) {
                Message message = Message.obtain(handler,
                        MainHandler.MSG_DECODE_SUCCEEDED, result);
                message.sendToTarget();
            }
        } else {
            if (handler != null) {
                Message message = Message.obtain(handler, MainHandler.MSG_DECODE_FAILED);
                message.sendToTarget();
            }
        }
    }
    private byte[] decodePreData(byte[] preData , int w , int h){
        byte[] tmp = null;
        ByteArrayOutputStream os = null;
        try {
            YuvImage image = new YuvImage(preData, ImageFormat.NV21, w, h,null);
            os = new ByteArrayOutputStream(preData.length);
            if (!image.compressToJpeg(new Rect(0,0,w, h), 100, os)){
                return null;
            }
            tmp = os.toByteArray();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                if (os != null){
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return tmp;
    }

}
