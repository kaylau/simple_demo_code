package com.kay.demo.retrofit;

import android.util.Log;

public class Translation {

    private int status;

    private content content;

    private static class content {
        private String from;
        private String to;
        private String vendor;
        private String out;
        private int errNo;
    }

    //定义 输出返回数据 的方法
    public void show() {

        Log.e("TAG", "-----------status: " + status);
        Log.e("TAG", "-----content.from: " + content.from);
        Log.e("TAG", "-------content.to: " + content.to);
        Log.e("TAG", "---content.vendor: " + content.vendor);
        Log.e("TAG", "------content.out: " + content.out);
        Log.e("TAG", "----content.errNo: " + content.errNo);

    }
}
