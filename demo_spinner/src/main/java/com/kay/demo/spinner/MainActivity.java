package com.kay.demo.spinner;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.PopupWindow;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText edt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edt = findViewById(R.id.edt);
        findViewById(R.id.tv_open).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopWindow();
            }
        });

    }

    private PopupWindow popupWindow;

    private void showPopWindow() {

        final ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            list.add("phone: " + 812345678 + i);
        }
        popupWindow = new DemoPopupWindow(this, edt.getWidth(), list, new DemoPopupWindow.OnSpinnerListener() {
            @Override
            public void onCallback(String phoneNum) {
                edt.setText(phoneNum);
            }
        });
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        ColorDrawable cd = new ColorDrawable(0x00ffffff);// 背景颜色全透明
        popupWindow.setBackgroundDrawable(cd);
        popupWindow.setAnimationStyle(R.style.style_pop_animation);// 动画效果必须放在showAsDropDown()方法上边，否则无效
//        backgroundAlpha(0.5f);// 设置背景半透明
        popupWindow.showAsDropDown(edt);
    }

    // 设置popupWindow背景半透明
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha;// 0.0-1.0
        getWindow().setAttributes(lp);
    }
}
