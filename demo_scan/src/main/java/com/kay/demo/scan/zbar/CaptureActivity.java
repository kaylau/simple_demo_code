package com.kay.demo.scan.zbar;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kay.demo.scan.LogUtil;
import com.kay.demo.scan.R;
import com.kay.demo.scan.zbar.scan.camera.CameraManager;
import com.kay.demo.scan.zbar.scan.decode.MainHandler;

import java.io.IOException;
import java.lang.reflect.Field;


/**
 * Desc: 1:启动一个SurfaceView作为取景预览
 * 2:开启camera,在后台独立线程中完成扫描任务
 * 3:对解码返回的结果进行处理.
 * 4:释放资源
 * Update by znq on 2016/11/9.
 */
public class CaptureActivity extends Activity implements SurfaceHolder.Callback, View.OnClickListener {

    private static final String TAG = "CaptureActivity";

    private static final long ANIMATION_DURATION_TIME = 1800;
    private MainHandler mainHandler;
    private SurfaceHolder mHolder;

    private CameraManager mCameraManager;

    private SurfaceView scanPreview;
    private RelativeLayout scanContainer;
    private RelativeLayout scanCropView;
    private ImageView scanLine;

    private boolean isHasSurface = false;

    private int titleBarHeight = 0;
    private TextView tv_amplify, tv_reduce;


    public Handler getHandler() {
        return mainHandler;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture);
        initView();
    }

    private void initView() {
        scanPreview = findViewById(R.id.capture_preview);
        scanContainer = findViewById(R.id.capture_container);
        scanCropView = findViewById(R.id.capture_crop_view);
        scanLine = findViewById(R.id.capture_scan_line);
        isHasSurface = false;

        findViewById(R.id.btn_amplify).setOnClickListener(this);
        findViewById(R.id.btn_reduce).setOnClickListener(this);

        tv_amplify = findViewById(R.id.tv_amplify);
        tv_reduce = findViewById(R.id.tv_reduce);
    }

    private void initScanLineAnimation() {
        TranslateAnimation animation = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, -0.03f,
                Animation.RELATIVE_TO_PARENT, 1.0f);
        animation.setDuration(ANIMATION_DURATION_TIME);
        animation.setRepeatCount(-1);
        animation.setRepeatMode(Animation.RESTART);
        scanLine.startAnimation(animation);
    }

    @Override
    protected void onResume() {
        super.onResume();
        reInitCamera();
    }

    @Override
    public void onPause() {
        gcCamera();
        super.onPause();
        if (scanLine != null) {
            scanLine.clearAnimation();
            scanLine.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //remove SurfaceCallback
        if (!isHasSurface && scanCropView != null) {
            scanPreview.getHolder().removeCallback(this);
        }
    }

    //region 初始化和回收相关资源
    private void initCamera(SurfaceHolder surfaceHolder) {
        mainHandler = null;
        try {
            if (mCameraManager == null) {
                mCameraManager = new CameraManager(getApplication());
            }
            mCameraManager.openDriver(surfaceHolder);
            if (mainHandler == null) {
                mainHandler = new MainHandler(this, mCameraManager);
            }
        } catch (IOException ioe) {
            LogUtil.e(TAG, "相机被占用: " + ioe.toString());
        } catch (RuntimeException e) {
            e.printStackTrace();
            LogUtil.e(TAG, "Unexpected error initializing camera");
        }

    }

    //释放
    private void gcCamera() {
        if (null != mainHandler) {
            //关闭聚焦,停止预览,清空预览回调,quit子线程looper
            mainHandler.quitSynchronously();
            mainHandler = null;
        }
        //关闭相机
        if (mCameraManager != null) {
            mCameraManager.closeDriver();
            mCameraManager = null;
        }
    }

    //重新加载
    private void reInitCamera() {
        initScanLineAnimation();
        mHolder = scanPreview.getHolder();

        if (isHasSurface) {
            initCamera(mHolder);
        } else {
            mHolder.addCallback(this);
            mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
    }

    //region 扫描结果
    public void checkResult(final String result) {
        LogUtil.e(TAG, "扫码result: " + result);
        Intent intent = new Intent();
        intent.putExtra("result", result);
        setResult(RESULT_OK, intent);
        finish();
    }

    //region  初始化截取的矩形区域
    public Rect initCrop() {
        int cameraWidth = 0;
        int cameraHeight = 0;
        if (null != mCameraManager) {
            cameraWidth = mCameraManager.getCameraResolution().y;
            cameraHeight = mCameraManager.getCameraResolution().x;
        }

        /** 获取布局中扫描框的位置信息 */
        int[] location = new int[2];
        scanCropView.getLocationInWindow(location);

        int cropLeft = location[0];
        /**
         * 计算扫描框左上角距离父布局的距离 = 扫描框左上角的Y坐标 - 状态栏的高度 - 布局中状态栏的高度
         */
        int cropTop = location[1] - getStatusBarHeight() - titleBarHeight;

        int cropWidth = scanCropView.getWidth();
        int cropHeight = scanCropView.getHeight();

        /** 获取布局容器的宽高 */
        int containerWidth = scanContainer.getWidth();
        int containerHeight = scanContainer.getHeight();

        /** 计算最终截取的矩形的左上角顶点x坐标 */
        int x = cropLeft * cameraWidth / containerWidth;
        /** 计算最终截取的矩形的左上角顶点y坐标 */
        int y = cropTop * cameraHeight / containerHeight;

        /** 计算最终截取的矩形的宽度 */
        int width = cropWidth * cameraWidth / containerWidth;
        /** 计算最终截取的矩形的高度 */
        int height = cropHeight * cameraHeight / containerHeight;

        /** 生成最终的截取的矩形 */
        return new Rect(x - 10, y - 10, width + x + 10, height + y + 10);
    }

    private int getStatusBarHeight() {
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object obj = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = Integer.parseInt(field.get(obj).toString());
            return getResources().getDimensionPixelSize(x);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    //region SurfaceHolder Callback 回调方法
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (holder == null) {
            LogUtil.e(TAG, "*** 没有添加SurfaceHolder的Callback");
        }
        if (!isHasSurface) {
            isHasSurface = true;
            initCamera(holder);
        }

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        LogUtil.e(TAG, "surfaceChanged---->");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        isHasSurface = false;
    }

    private void onCameraZoom(int zoom) {
        if (mCameraManager == null) {
            return;
        }
        Camera camera = mCameraManager.getCamera();
        if (camera == null) {
            return;
        }
        Camera.Parameters p = camera.getParameters();
        if (!p.isZoomSupported()) {
            return;
        }
        try {
            maxZoom = p.getMaxZoom();
            p.setZoom(zoom);
            camera.setParameters(p);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.e("tag", e.toString());
        }
    }

    private int zoomSize = 10;
    private int zoom;
    private int maxZoom = 100;

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_amplify) {
            if (zoom >= maxZoom) {
                Toast.makeText(this, "已放大到" + maxZoom, Toast.LENGTH_LONG).show();
                return;
            }
            zoom = zoom + zoomSize;
            if (zoom > maxZoom) {
                zoom = zoom - zoomSize;
                return;
            }
            tv_amplify.setText(String.valueOf(zoom));
            onCameraZoom(zoom);
        } else if (id == R.id.btn_reduce) {
            if (zoom <= 0) {
                Toast.makeText(this, "已缩小到0", Toast.LENGTH_LONG).show();
                return;
            }
            zoom = zoom - zoomSize;
            tv_reduce.setText(String.valueOf(zoom));
            onCameraZoom(zoom);
        }
        LogUtil.e("TAG", "zoom size: " + zoom);
    }
}