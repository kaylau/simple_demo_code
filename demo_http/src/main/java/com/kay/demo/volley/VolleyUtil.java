package com.kay.demo.volley;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.kay.demo.AppApplication;
import com.kay.demo.utils.LogUtil;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Date: 2019/7/1 下午2:00
 * Author: kay lau
 * Description:
 */
public class VolleyUtil {

    private static final String TAG = VolleyUtil.class.getSimpleName();

    private static VolleyUtil instance;

    private static RequestQueue requestQueue;

    private long startPostTime;

    public static VolleyUtil getInstance() {
        if (instance == null) {
            synchronized (VolleyUtil.class) {
                if (instance == null) {
                    instance = new VolleyUtil();
                }
            }
        }
        return instance;
    }

    public void sendPost(final String httpUrl, JSONObject jsonObject) {

        startPostTime = System.currentTimeMillis();
        LogUtil.e(TAG, "请求开始时间: " + httpUrl + "---" + startPostTime);

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(AppApplication.getApplication());
        }


        JsonRequest<JSONObject> jsonRequest = new JsonObjectRequest(Request.Method.POST, httpUrl, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        long endPostTime = System.currentTimeMillis();
                        LogUtil.e(TAG, "请求成功时间: " + httpUrl + "---" + endPostTime);
                        long postTime = endPostTime - startPostTime;
                        LogUtil.e(TAG, "请求成功耗时: " + httpUrl + "---" + postTime);
                        LogUtil.e(TAG, "请求响应线程: "+Thread.currentThread().getName());

                        LogUtil.e(TAG, "response -> " + response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                long endPostTime = System.currentTimeMillis();
                LogUtil.e(TAG, "请求失败时间: " + httpUrl + "---" + endPostTime);
                long postTime = endPostTime - startPostTime;
                LogUtil.e(TAG, "请求失败耗时: " + httpUrl + "---" + postTime);
                LogUtil.e(TAG, error.getMessage() + "=====" + error);
            }
        }) {

            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Accept", "application/json");
                headers.put("Content-Type", "application/json; charset=UTF-8");
//                headers.put("Accept-Encoding", "gzip, deflate");

                return headers;
            }

//            @Override
//            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
//                return super.parseNetworkResponse(response);
//            }
        };
        // 设置超时时间连接次数
        jsonRequest.setShouldCache(false)
                .setRetryPolicy(new DefaultRetryPolicy(10 * 1000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(jsonRequest);
    }

}
