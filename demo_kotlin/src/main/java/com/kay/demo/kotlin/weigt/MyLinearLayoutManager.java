package com.kay.demo.kotlin.weigt;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;

public class MyLinearLayoutManager extends LinearLayoutManager {

    private boolean mCanVerticalScroll = true;

    public MyLinearLayoutManager(Context context) {
        super(context);
    }

    @Override
    public boolean canScrollVertically() {
        if (!mCanVerticalScroll){
            return false;
        }else {
            return super.canScrollVertically();
        }
    }

    public void setmCanVerticalScroll(boolean b){
        mCanVerticalScroll = b;
    }
}