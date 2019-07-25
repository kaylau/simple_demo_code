package com.kay.demo.kotlin;

import com.kay.demo.kotlin.weigt.ObservableScrollView;

public interface ScrollViewListener {
  
    void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy);

    void scrollOrientation(int orientation);
  
}