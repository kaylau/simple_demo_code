package com.kay.demo.kotlin.recycleview.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kay.demo.kotlin.R
import com.kay.demo.kotlin.recycleview.MyRecycleAdapter
import com.kay.demo.kotlin.recycleview.RecycleViewItemData
import com.kay.demo.kotlin.util.logutil.LogUtil

/**
 * Date: 2019/7/23 上午9:59
 * Author: kay lau
 * Description:
 */

@SuppressLint("ValidFragment")
class CarsFragment(activity: Activity) : BaseFragment() {

    override fun onStopSliding() {
        recycleView?.isNestedScrollingEnabled = false
    }

    override fun onStartSliding() {
        recycleView?.isNestedScrollingEnabled = true
    }

    internal val tag = CarsFragment::class.java.simpleName
    var recycleView: RecyclerView? = null
    var itemList = mutableListOf<RecycleViewItemData>()

    var mActivity: Activity? = activity

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val inflate = inflater.inflate(R.layout.fragment_list, container, false)
        initView(inflate)
        return inflate
    }

    private fun initView(view: View?) {
        recycleView = view?.findViewById(R.id.recycleView)
        initData()
    }

    private fun initData() {
        initListData()
        recycleView?.layoutManager = LinearLayoutManager(mActivity)
        val adapter = MyRecycleAdapter(mActivity, itemList)
        recycleView?.adapter = adapter
        adapter.notifyDataSetChanged()
        adapter.addItemClickListener(object :
            MyRecycleAdapter.OnItemClickListener {
            override fun onItemClick(v: View?, position: Int) {
                LogUtil.e(tag, "点击了第 ${position + 1} 个item")
            }
        })

        recycleView?.addOnScrollListener(object : MyRecycleViewScrollListener() {

            override fun onScrolledToTop() {
//                onStopSliding()
            }
        })
    }


    private fun initListData() {
        val imgList = listOf(
            R.mipmap.img0,
            R.mipmap.img1,
            R.mipmap.img2,
            R.mipmap.img3,
            R.mipmap.img4,
            R.mipmap.img5,
            R.mipmap.img6,
            R.mipmap.img7,
            R.mipmap.img8,
            R.mipmap.img9,
            R.mipmap.img10
        )

        for (i: Int in 1..3) {
            for (index in 1..imgList.size) {
                if (index == 3) {
                    itemList.add(
                        RecycleViewItemData()
                    )
                } else {
                    itemList.add(
                        RecycleViewItemData(
                            "name --> $i ${index - 1}",
                            imgList[index - 1],
                            "90123987 $i $index-1"
                        )
                    )
                }
            }
        }

        LogUtil.e("tag", "size: ${itemList.size}")
    }

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
}