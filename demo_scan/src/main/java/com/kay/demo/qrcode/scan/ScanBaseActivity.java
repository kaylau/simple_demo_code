package com.kay.demo.qrcode.scan;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Rect;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kay.demo.qrcode.R;
import com.kay.demo.qrcode.decoder.DecoderSDK;
import com.kay.demo.qrcode.scan.camera.CameraManager;
import com.kay.demo.qrcode.scan.decode.MainHandler;
import com.kay.demo.qrcode.utils.LogUtil;

import java.io.IOException;
import java.lang.reflect.Field;

/**
 * Date: 2018/11/19 上午11:55
 * Author: kay lau
 */
public abstract class ScanBaseActivity extends Activity implements SurfaceHolder.Callback {

    private static final int REQUEST_READ_STORAGE_PERMISSION = 100;

    private static final String TAG = "ScanBaseActivity";

    private static final int RES_ID_BACK_WHITE = R.drawable.selector_btn_title_back_white;

    /**
     * 相册图片以file:///开头
     */
    private static final String START_WITH_FILE = "file:///";

    protected static final int REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY = 666;
    private static final long ANIMATION_DURATION_TIME = 1800;
    protected MainHandler mainHandler;
    protected SurfaceHolder mHolder;

    protected CameraManager mCameraManager;

    protected SurfaceView scanPreview;
    private RelativeLayout scanContainer;
    protected RelativeLayout scanCropView;
    private ImageView scanLine;

    protected boolean isHasSurface = false;

    private String picturePath;
    private DecodeAsyncTask myAsyncTask;
    private int titleBarHeight = 0;

    public Handler getHandler() {
        return mainHandler;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        initTitleView();
        initView();
    }

    //初始化titleView
    private void initTitleView() {
        View inflate = findViewById(R.id.title);
        inflate.setBackgroundResource(R.color.CC_00000000);

        // 标题栏左侧按钮
        View backView = inflate.findViewById(R.id.btn_back);
        backView.setBackgroundResource(RES_ID_BACK_WHITE);
        backView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBack();
            }
        });

        // 标题栏右侧按钮
        Button backNext = inflate.findViewById(R.id.btn_next);
        //设置距右R.dimen.dp_10
        RelativeLayout.LayoutParams layoutParams;
        layoutParams = (RelativeLayout.LayoutParams) backNext.getLayoutParams();
        layoutParams.setMargins(0, 0, getResources().getDimensionPixelSize(R.dimen.dp_10), 0);//4个参数按顺序分别是左上右下
        backNext.setLayoutParams(layoutParams);
        backNext.setBackgroundResource(R.color.CC_00000000);
        backNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkStoragePer();
            }
        });
        String titleRightText = initTitleRightBtnText();
        if (!TextUtils.isEmpty(titleRightText)) {
            backNext.setText(titleRightText);
            backNext.setVisibility(View.VISIBLE);
        } else {
            backNext.setVisibility(View.INVISIBLE);
        }

        // 标题中间文案
        TextView tv_title = inflate.findViewById(R.id.tv_title);
        String titleText = initTitleText();
        if (!TextUtils.isEmpty(titleText)) {
            tv_title.setText(titleText);
            tv_title.setVisibility(View.VISIBLE);
        } else {
            tv_title.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 初始化标题栏中间标题文案
     *
     * @return
     */
    protected abstract String initTitleText();

    /**
     * 左上角返回键
     */
    protected abstract void onBack();

    /**
     * 初始化title右侧按钮文案
     *
     * @return
     */
    protected abstract String initTitleRightBtnText();


    private void initView() {
        scanPreview = findViewById(R.id.capture_preview);
        scanContainer = findViewById(R.id.capture_container);
        scanCropView = findViewById(R.id.capture_crop_view);
        scanLine = findViewById(R.id.capture_scan_line);
        isHasSurface = false;
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
        clearScanLine();
    }

    /**
     * region 初始化和回收相关资源
     *
     * @param surfaceHolder
     */
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
            LogUtil.e(TAG, e.toString());
            LogUtil.e(TAG, "Unexpected error initializing camera");
        }
    }

    /**
     * 扫码结果处理
     *
     * @param scanResult
     */
    protected abstract void handleScanResult(String scanResult);

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

    protected void reInitCamera() {
        initScanLineAnimation();
        mHolder = scanPreview.getHolder();

        if (isHasSurface) {
            initCamera(mHolder);
        } else {
            mHolder.addCallback(this);
            mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
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

    /**
     * 检查sd卡的读权限，做相应的处理
     */
    private void checkStoragePer() {
        if (Build.VERSION.SDK_INT < 23 || checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            openAlbum();

        } else if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
//            openAppSettingDetails("");

        } else {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_READ_STORAGE_PERMISSION);
        }
    }

    private void openAlbum() {
        //其他手机url路径：content://media/external/images/media/299
        //华为手机获取的路径：content://com.android.providers.media.documents/document/image%3A100595
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        onBack();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //remove SurfaceCallback
        if (!isHasSurface && scanCropView != null) {
            scanPreview.getHolder().removeCallback(this);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY) {
            if (null != data) {
                if (data.getDataString().startsWith(START_WITH_FILE)) {
                    picturePath = data.getDataString().replace(START_WITH_FILE, "");
                    if (TextUtils.isEmpty(picturePath)) {
                        LogUtil.d(TAG, "读取相册图片失败");
                        Toast.makeText(ScanBaseActivity.this, "读取相册图片失败", Toast.LENGTH_LONG).show();
                        finish();
                        return;
                    }
                } else {
                    Uri uri = data.getData();
                    String[] proj = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(uri, proj, null, null, null);
                    if (cursor == null) {
                        Toast.makeText(ScanBaseActivity.this, "读取相册图片失败", Toast.LENGTH_LONG).show();
                        finish();
                        return;
                    }
                    if (cursor.moveToFirst()) {
                        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                        picturePath = cursor.getString(columnIndex);
                        if (TextUtils.isEmpty(picturePath)) {
                            LogUtil.d(TAG, "读取相册图片失败");
                        }
                        LogUtil.d(TAG, "读取相册图片path：" + picturePath);
                    }
                    cursor.close();
                }
                // 执行解码任务
                onExecuteDecode();
            } else {
                Toast.makeText(ScanBaseActivity.this, "读取相册图片失败", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    private void onExecuteDecode() {
        if (myAsyncTask != null) {
            if (myAsyncTask.getStatus() == AsyncTask.Status.RUNNING) {
                myAsyncTask.cancel(true);
            }
            myAsyncTask = null;
        }
        myAsyncTask = new DecodeAsyncTask();
        myAsyncTask.execute();
    }

    private class DecodeAsyncTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            return new DecoderSDK().decodeQRCode(picturePath);
        }

        @Override
        protected void onPostExecute(String result) {

            if (TextUtils.isEmpty(result)) {
                Toast.makeText(ScanBaseActivity.this, "读取相册图片失败", Toast.LENGTH_LONG).show();
            } else {
                LogUtil.d(TAG, "识别结果： " + result);
                // 识别二维码图片
                gcCamera();
                clearScanLine();
                handleScanResult(result);
            }
        }
    }

    //region 扫描结果
    public void checkResult(final String result) {
        LogUtil.e(TAG, "扫码result: " + result);
        if (TextUtils.isEmpty(result)) {
            Toast.makeText(this, "扫码结果为null", Toast.LENGTH_LONG).show();
            reInitCamera();

        } else {
            if (!isFinishing()) {
                // 扫码结果处理
                gcCamera();
                clearScanLine();
                handleScanResult(result);
                if (myAsyncTask != null) {
                    myAsyncTask.cancel(true);
                }
            }
        }
    }

    private void clearScanLine() {
        if (scanLine != null) {
            scanLine.clearAnimation();
            scanLine.setVisibility(View.GONE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_READ_STORAGE_PERMISSION && grantResults != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openAlbum();

        } else if (requestCode == REQUEST_READ_STORAGE_PERMISSION) {
//            openAppSettingDetails("");
        }
    }

}
