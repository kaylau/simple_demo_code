package com.kay.demo.kotlin.weigt

import android.support.v7.widget.RecyclerView

open class MyRecycleViewScrollListener : RecyclerView.OnScrollListener() {

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        if (!recyclerView.canScrollVertically(-1)) {
            onScrolledToTop()

        } else if (!recyclerView.canScrollVertically(1)) {
            onScrolledToBottom()

        } else if (dy < 0) {
            onScrolledUp()

        } else if (dy > 0) {
            onScrolledDown()
        }
    }

    open fun onScrolledUp() {}

    open fun onScrolledDown() {}

    open fun onScrolledToTop() {}

    open fun onScrolledToBottom() {}
}