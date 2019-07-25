package com.kay.demo.kotlin.recycleview.vp

import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.MotionEvent

/**
 * Date: 2019/7/24 下午1:53
 * Author: kay lau
 * Description:
 */
class MyViewPager constructor(context: Context, attrs: AttributeSet?) : ViewPager(context, attrs) {


    constructor(context: Context) : this(context, null)

    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        return super.onTouchEvent(ev)
//        return true
//        return false
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        if (isIntercept) {
            return true
        }
        return super.onInterceptTouchEvent(ev)
//        return true
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (isRequestDisallowIntercept) {
            parent.requestDisallowInterceptTouchEvent(true)
        }
        return super.dispatchTouchEvent(ev)
//        return true
//        return false
    }

    var isRequestDisallowIntercept = false
    var isIntercept = false

}