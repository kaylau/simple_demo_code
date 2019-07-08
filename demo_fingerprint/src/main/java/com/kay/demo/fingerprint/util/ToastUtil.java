package com.kay.demo.fingerprint.util;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.kay.demo.fingerprint.R;

/**
 * Created by kaylau on 2016/12/14.
 */

public class ToastUtil {

    private static Toast toast;
    private static Toast toastEnable;

    public static void showToastShort(Context context, String text) {
        if (toast == null) {
            toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        } else {
            //如果toast不为空，则直接更改当前toast的文本
            toast.setText(text);
        }
        toast.show();
    }

    public static void showToastLong(Context context, String text) {
        if (toast == null) {
            toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
        } else {
            //如果toast不为空，则直接更改当前toast的文本
            toast.setText(text);
        }
        toast.show();
    }

    public static void showToastAtCenterShort(Context context, String text) {
        if (toast == null) {
            toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        } else {
            //如果toast不为空，则直接更改当前toast的文本
            toast.setText(text);
        }
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public static void showToastAtCenterLong(Context context, String text) {
        if (toast == null) {
            toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
        } else {
            //如果toast不为空，则直接更改当前toast的文本
            toast.setText(text);
        }
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public static void showToastCustom(Context context, String text) {
        if (context == null) {
            return;
        }
        if (toastEnable == null) {
            toastEnable = new Toast(context);
        }
        View view = View.inflate(context, R.layout.layout_toast_view, null);
        TextView tv_toast = view.findViewById(R.id.tv_toast);
        tv_toast.setText(text);
        toastEnable.setView(view);
        toastEnable.setDuration(Toast.LENGTH_SHORT);
        toastEnable.setGravity(Gravity.CENTER, 0, 0);
        toastEnable.show();
    }

    public static void onDestroy() {
        toast = null;
    }
}
