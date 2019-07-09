package com.kay.demo.qrcode.scan.decode;

import android.os.Handler;
import android.os.Message;

import com.kay.demo.qrcode.scan.ScanBaseActivity;
import com.kay.demo.qrcode.scan.camera.CameraManager;


/**
 * desc:主线程的Handler
 * Date: 2016-11-03 15:55
 */
public class MainHandler extends Handler {

    public static final int MSG_DECODE_SUCCEEDED = 0;
    public static final int MSG_DECODE_FAILED = 1;
    public static final int MSG_QUIT = 2;
    public static final int MSG_DECODE = 3;
    public static final int MSG_RESTART_PREVIEW = 4;

    private final ScanBaseActivity activity;

    /**
     * 真正负责扫描任务的核心线程
     */
    private final DecodeThread decodeThread;

    private State state;

    private final CameraManager cameraManager;

    public MainHandler(ScanBaseActivity activity, CameraManager cameraManager) {
        this.activity = activity;
        // 启动扫描线程
        decodeThread = new DecodeThread(activity);
        decodeThread.start();
        state = State.SUCCESS;
        this.cameraManager = cameraManager;

        // 开启相机预览界面.并且自动聚焦
        cameraManager.startPreview();

        restartPreviewAndDecode();
    }

    /**
     * 当前扫描的状态
     */
    private enum State {
        /**
         * 预览
         */
        PREVIEW,
        /**
         * 扫描成功
         */
        SUCCESS,
        /**
         * 结束扫描
         */
        DONE
    }


    @Override
    public void handleMessage(Message msg) {
        if (msg.what == MSG_DECODE_SUCCEEDED) {
            String result = (String) msg.obj;
            activity.checkResult(result);

        } else if (msg.what == MSG_RESTART_PREVIEW) {
            restartPreviewAndDecode();

        } else if (msg.what == MSG_DECODE_FAILED) {
            // We're decoding as fast as possible, so when one MSG_DECODE fails,
            // start another.
            state = State.PREVIEW;
            cameraManager.requestPreviewFrame(decodeThread.getHandler(), MSG_DECODE);
        }
    }

    /**
     * 完成一次扫描后，只需要再调用此方法即可
     */
    public void restartPreviewAndDecode() {
        if (state == State.SUCCESS) {
            state = State.PREVIEW;
            // 向decodeThread绑定的handler（DecodeHandler)发送解码消息
            cameraManager.requestPreviewFrame(decodeThread.getHandler(), MSG_DECODE);
        }
    }

    public void quitSynchronously() {
        state = State.DONE;
        cameraManager.stopPreview();
        Message.obtain(decodeThread.getHandler(), MSG_QUIT).sendToTarget();

        try {
            // Wait at most half a second; should be enough time, and onPause()
            // will timeout quickly
            decodeThread.join(500L);
        } catch (InterruptedException e) {
            // continue
        }

        // Be absolutely sure we don't send any queued up messages
        removeMessages(MSG_DECODE_SUCCEEDED);
        removeMessages(MSG_DECODE_FAILED);
    }

}
