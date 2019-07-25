package com.kay.demo.kotlin.weigt

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.LinearLayout

/**
 * Date: 2019/7/24 下午1:53
 * Author: kay lau
 * Description:
 */
class MyLinearLayout constructor(context: Context, attrs: AttributeSet?) :
    LinearLayout(context, attrs) {


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
        requestDisallowIntercept()
        return super.dispatchTouchEvent(ev)
//        return true
//        return false
    }

    var isRequestDisallowIntercept = false
    var isIntercept = false

    private fun requestDisallowIntercept() {
        if (isRequestDisallowIntercept) {
            parent.requestDisallowInterceptTouchEvent(true)
        }
    }

}