package com.kay.demo.retrofit;

import com.kay.demo.utils.LogUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Date: 2019/7/3 上午9:46
 * Author: kay lau
 * Description:
 */
public class RetrofitUtil {

    private static final String TAG = RetrofitUtil.class.getSimpleName();

    //      URL
    //    http://fanyi.youdao.com/translate

    //      URL实例
    //    http://fanyi.youdao.com/translate?doctype=json&jsonversion=&type=&keyfrom=&model=&mid=&imei=&vendor=&screen=&ssid=&network=&abtest=


    // 参数说明
    // doctype：json 或 xml
    // jsonversion：如果 doctype 值是 xml，则去除该值，若 doctype 值是 json，该值为空即可
    // xmlVersion：如果 doctype 值是 json，则去除该值，若 doctype 值是 xml，该值为空即可
    // type：语言自动检测时为 null，为 null 时可为空。英译中为 EN2ZH_CN，中译英为 ZH_CN2EN，日译中为 JA2ZH_CN，中译日为 ZH_CN2JA，韩译中为 KR2ZH_CN，中译韩为 ZH_CN2KR，中译法为 ZH_CN2FR，法译中为 FR2ZH_CN
    // keyform：mdict. + 版本号 + .手机平台。可为空
    // model：手机型号。可为空
    // mid：平台版本。可为空
    // imei：???。可为空
    // vendor：应用下载平台。可为空
    // screen：屏幕宽高。可为空
    // ssid：用户名。可为空
    // abtest：???。可为空

    // 请求方式说明
    // 请求方式：POST
    // 请求体：i
    // 请求格式：x-www-form-urlencoded
    public static void sendPostHttp(String url) {
        //步骤4:创建Retrofit对象
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url) // 设置 网络请求 Url 必须以 / 结尾
                .addConverterFactory(GsonConverterFactory.create()) //设置使用Gson解析(记得加入依赖)
                .build();

        // 步骤5:创建 网络请求接口 的实例
        Request_Interface request = retrofit.create(Request_Interface.class);

        //对 发送请求 进行封装(设置需要翻译的内容)
        Call<Translation1> call = request.getCall("扫码领红包");
        LogUtil.e(TAG, "call request: " + call.request().toString());



        //步骤6:发送网络请求(异步)
        call.enqueue(new Callback<Translation1>() {

            //请求成功时回调
            @Override
            public void onResponse(Call<Translation1> call, Response<Translation1> response) {
                // 步骤7：处理返回的数据结果：输出翻译的内容
                LogUtil.e(TAG, response.body().getTranslateResult().get(0).get(0).getTgt());

            }

            //请求失败时回调
            @Override
            public void onFailure(Call<Translation1> call, Throwable throwable) {
                LogUtil.e(TAG, "请求失败");
                LogUtil.e(TAG, throwable.getMessage());
            }
        });
    }


    //      URL模板
    //    http://fy.iciba.com/ajax.php

    //      URL实例
    //    http://fy.iciba.com/ajax.php?a=fy&f=auto&t=auto&w=hello%20world

    // 参数说明：
    // a：固定值 fy
    // f：原文内容类型，日语取 ja，中文取 zh，英语取 en，韩语取 ko，德语取 de，西班牙语取 es，法语取 fr，自动则取 auto
    // t：译文内容类型，日语取 ja，中文取 zh，英语取 en，韩语取 ko，德语取 de，西班牙语取 es，法语取 fr，自动则取 auto
    // w：查询内容
    private void sendGETHttp() {

        //步骤4:创建Retrofit对象
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://fy.iciba.com/") // 设置 网络请求 Url
                .addConverterFactory(GsonConverterFactory.create()) //设置使用Gson解析(记得加入依赖)
                .build();

        // 步骤5:创建 网络请求接口 的实例
        Request_Interface request = retrofit.create(Request_Interface.class);

        //对 发送请求 进行封装
        Call<Translation> call = request.getCall();

        //步骤6:发送网络请求(异步)
        call.enqueue(new Callback<Translation>() {
            //请求成功时回调
            @Override
            public void onResponse(Call<Translation> call, Response<Translation> response) {
                // 步骤7：处理返回的数据结果
                response.body().show();
            }

            //请求失败时回调
            @Override
            public void onFailure(Call<Translation> call, Throwable throwable) {
                LogUtil.e(TAG, "连接失败");
            }
        });
    }
}
