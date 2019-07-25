package com.kay.demo.kotlin.weigt;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

import com.kay.demo.kotlin.ScrollViewListener;

public class ObservableScrollView extends ScrollView {

    /**
     * ScrollView正在向上滑动
     */
    public static final int SCROLL_UP = 0x01;
    /**
     * ScrollView正在向下滑动
     */
    public static final int SCROLL_DOWN = 0x10;
    /**
     * 最小的滑动距离
     */
    private static final int SCROLLLIMIT = 10;
  
    private ScrollViewListener scrollViewListener = null;
  
    public ObservableScrollView(Context context) {
        super(context);  
    }  
  
    public ObservableScrollView(Context context, AttributeSet attrs,
            int defStyle) {  
        super(context, attrs, defStyle);  
    }  
  
    public ObservableScrollView(Context context, AttributeSet attrs) {  
        super(context, attrs);  
    }  
  
    public void setScrollViewListener(ScrollViewListener scrollViewListener) {  
        this.scrollViewListener = scrollViewListener;  
    }  
  
    @Override  
    protected void onScrollChanged(int x, int y, int oldx, int oldy) {  
        super.onScrollChanged(x, y, oldx, oldy);  
        if (scrollViewListener != null) {  
            scrollViewListener.onScrollChanged(this, x, y, oldx, oldy);  
        }

        if (oldy > y && oldy - y > SCROLLLIMIT) {// 向下
            if (scrollViewListener != null)
                scrollViewListener.scrollOrientation(SCROLL_DOWN);
        } else if (oldy < y && y - oldy > SCROLLLIMIT) {// 向上
            if (scrollViewListener != null)
                scrollViewListener.scrollOrientation(SCROLL_UP);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return super.onTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (isInterceptTouchEvent) {
            return true;
        }
        return super.onInterceptTouchEvent(ev);
    }

    private boolean isInterceptTouchEvent;

    public void setInterceptTouchEvent(boolean interceptTouchEvent) {
        isInterceptTouchEvent = interceptTouchEvent;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

}