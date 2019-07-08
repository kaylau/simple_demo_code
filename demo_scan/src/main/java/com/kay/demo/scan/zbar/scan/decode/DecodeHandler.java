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

package com.kay.demo.scan.zbar.scan.decode;

import android.graphics.Rect;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.dtr.zbar.build.ZBarDecoder;
import com.kay.demo.scan.zbar.CaptureActivity;


final class DecodeHandler extends Handler {

    private final CaptureActivity activity;
    private ZBarDecoder zBarDecoder;

    private boolean running = true;

    DecodeHandler(CaptureActivity activity) {
        zBarDecoder = new ZBarDecoder();
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
     * 解码
     */
    private void decode(byte[] data, int width, int height) {
        // long start = System.currentTimeMillis();
        // 这里需要将获取的data翻转一下，因为相机默认拿的的横屏的数据
        byte[] rotatedData = new byte[data.length];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++)
                rotatedData[x * height + height - y - 1] = data[x + y * width];
        }

        // 宽高也要调整
        int tmp = width;
        width = height;
        height = tmp;
        Rect mCropRect = activity.initCrop();
        String result = null;
        if (zBarDecoder != null) {
            try {
                result = zBarDecoder.decodeCrop(rotatedData, width, height, mCropRect.left, mCropRect.top, mCropRect.width(), mCropRect.height());
            } catch (Exception ex) {
                ex.printStackTrace();
                zBarDecoder = null;
            }
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
    }


}
