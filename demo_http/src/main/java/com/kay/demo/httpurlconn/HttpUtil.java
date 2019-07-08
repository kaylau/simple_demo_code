package com.kay.demo.httpurlconn;


import com.kay.demo.utils.LogUtil;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.zip.GZIPInputStream;

public class HttpUtil {

    private static final String TAG = HttpUtil.class.getSimpleName();
    private static final int bufSize = 1024 * 8;
    private static final String PREFIX = "--", LINE_END = "\r\n";
    private static long startPostTime;

    /**
     * POST请求
     *
     * @param urlString
     * @param paramBytes
     * @return
     */
    public static String sendPost(String urlString, byte[] paramBytes) throws Exception {

        byte[] result = null;
        HttpURLConnection httpURLConnection;
        LogUtil.i(TAG, "http request link: " + urlString);

        startPostTime = System.currentTimeMillis();
        LogUtil.e(TAG, "请求开始时间: " + urlString + "---" + startPostTime);

        URL url = new URL(urlString);
        httpURLConnection = (HttpURLConnection) url.openConnection();

        httpURLConnection.setRequestMethod("POSt");
        httpURLConnection.setUseCaches(false);//不使用缓存
        httpURLConnection.setConnectTimeout(10 * 1000);//连接服务器超时（单位：毫秒）
        httpURLConnection.setReadTimeout(10 * 1000);//从服务器读取数据超时（单位：毫秒）
        httpURLConnection.setDoInput(true);// 设置运行输入
        httpURLConnection.setDoOutput(true);// 设置运行输出
        //设定传送的内容类型是可序列化的java对象
        // (如果不设此项,在传送序列化对象时,当WEB服务默认的不是这种类型时可能抛java.io.EOFException)
        httpURLConnection.addRequestProperty("Content-type", "application/json");
        httpURLConnection.addRequestProperty("charset", "UTF-8");
        httpURLConnection.addRequestProperty("Accept-Encoding", "gzip, deflate");//HttpURLConnection 默认使用gzip压缩和自动解压缩
//            httpURLConnection.setRequestProperty("Accept-Encoding", "identity");//屏蔽gzip压缩

//        httpURLConnection.setRequestProperty("User-Agent", "ua");

        // 将请求的数据写入输出流中
        OutputStream os = httpURLConnection.getOutputStream();
        BufferedOutputStream bos = new BufferedOutputStream(os);
        bos.write(paramBytes);
        bos.flush();// 刷新对象输出流，将任何字节都写入潜在的流中
        bos.close();
        os.close();

        int responseCode = httpURLConnection.getResponseCode();
        LogUtil.i(TAG, "httpURLConnection.getResponseCode() POST:" + responseCode);
        if (HttpURLConnection.HTTP_OK == responseCode) {
            long endPostTime = System.currentTimeMillis();
            LogUtil.e(TAG, "请求成功时间: " + urlString + "---" + endPostTime);
            long postTime = endPostTime - startPostTime;
            LogUtil.e(TAG, "请求成功耗时: " + urlString + "---" + postTime);
            InputStream inputStream = httpURLConnection.getInputStream();
            if ("gzip".equals(httpURLConnection.getHeaderField("Content-Encoding"))) {
                inputStream = new GZIPInputStream(new BufferedInputStream(inputStream));
            }
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            int len = -1;
            byte[] buffer = new byte[bufSize];
            while ((len = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
            }
            result = outputStream.toByteArray();
            outputStream.close();
            inputStream.close();
        }
        httpURLConnection.disconnect();

        return new String(result);
    }

}
