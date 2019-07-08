package com.kay.demo.okhttp;

import android.os.Handler;
import android.os.Message;

import com.kay.demo.utils.LogUtil;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.CacheControl;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Dispatcher;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Date: 2019/7/1 上午10:15
 * Author: kay lau
 * Description:
 */
public class OKHttpUtil {

    private static final String TAG = OKHttpUtil.class.getSimpleName();

    private static OKHttpUtil instance;

    private long startPostTime;

    private Handler handler;
    private OkHttpClient okHttpClient;


    public static OKHttpUtil getInstance() {
        if (instance == null) {
            synchronized (OKHttpUtil.class) {
                if (instance == null) {
                    instance = new OKHttpUtil();
                }
            }
        }
        return instance;
    }

    private static class OKHttpHandler extends Handler {


        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                String result = (String) msg.obj;
                LogUtil.e(TAG, "请求响应线程: " + Thread.currentThread().getName());
                LogUtil.e(TAG, "result: " + result);
            }
        }
    }

    public void sendPost(final String httpUrl, String jsonStr) {

        if (handler == null) {
            handler = new OKHttpHandler();
        }
        startPostTime = System.currentTimeMillis();
        LogUtil.e(TAG, "请求开始时间: " + httpUrl + "---" + startPostTime);

        if (okHttpClient == null) {
            Dispatcher dispatcher = new Dispatcher();
            dispatcher.setMaxRequests(128);
            dispatcher.setMaxRequestsPerHost(10);// 默认值5, 弱网环境/短时间请求同一host密集时, 造成timeout
            okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)   // 连接超时 从2.5.0版本开始, 默认设置为10秒
                    .writeTimeout(10, TimeUnit.SECONDS)     // 上传超时 从2.5.0版本开始, 默认设置为10秒
                    .readTimeout(10, TimeUnit.SECONDS)      // 响应超时 从2.5.0版本开始, 默认设置为10秒
                    .dispatcher(dispatcher)
                    .build();
        }

        // MediaType  设置Content-Type 标头中包含的媒体类型值
        RequestBody requestBody = FormBody.create(jsonStr, MediaType.parse("application/json; charset=utf-8"));
//        RequestBody requestBody = RequestBody.create(jsonStr, MediaType.parse("text/plain;charset=utf-8"));


        Request request = new Request.Builder()
                .url(httpUrl)//请求的url
                .post(requestBody)
//                .removeHeader("User-Agent")
//                .addHeader("User-Agent", AppGlobalUtil.getInstance().getUserAgent())
                .cacheControl(CacheControl.FORCE_NETWORK)// 不使用缓存
                .build();

        // 创建Call
        Call call = okHttpClient.newCall(request);
        // 加入队列 异步操作
        call.enqueue(new Callback() {
            //请求错误回调方法
            @Override
            public void onFailure(Call call, IOException e) {
                long endPostTime = System.currentTimeMillis();
                LogUtil.e(TAG, "请求失败时间: " + httpUrl + "---" + endPostTime);
                long postTime = endPostTime - startPostTime;
                LogUtil.e(TAG, "请求失败耗时: " + httpUrl + "---" + postTime);
                LogUtil.e(TAG, e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                long endPostTime = System.currentTimeMillis();
                LogUtil.e(TAG, "请求成功时间: " + httpUrl + "---" + endPostTime);
                long postTime = endPostTime - startPostTime;
                LogUtil.e(TAG, "请求成功耗时: " + httpUrl + "---" + postTime);
                LogUtil.e(TAG, "请求响应线程: " + Thread.currentThread().getName());


                ResponseBody body = response.body();// 此方法只能调用一次, 调用一次后已释放资源, 因为默认开发者只会读取一次数据.
                String result = body.string();
                handler.obtainMessage(0, result).sendToTarget();
                LogUtil.e(TAG, "onResponse: " + result);
            }
        });
    }
}
