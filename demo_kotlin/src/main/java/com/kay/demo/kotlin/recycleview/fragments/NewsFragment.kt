package com.kay.demo.kotlin.recycleview.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kay.demo.kotlin.R
import com.kay.demo.kotlin.weigt.MyLinearLayoutManager
import com.kay.demo.kotlin.recycleview.RecycleViewItemData
import com.kay.demo.kotlin.recycleview.adapter.NewsRecycleAdapter
import com.kay.demo.kotlin.util.logutil.LogUtil

/**
 * Date: 2019/7/23 上午9:59
 * Author: kay lau
 * Description:
 */
@SuppressLint("ValidFragment")
class NewsFragment(activity: Activity) : BaseFragment() {


    override fun onStopSliding() {
        LogUtil.e("onStopSliding")
    }

    override fun onStartSliding() {
        LogUtil.e("onStartSliding")
    }

    internal val tag = NewsFragment::class.java.simpleName
    var recycleView: RecyclerView? = null
    var itemList = mutableListOf<RecycleViewItemData>()

    var mActivity: Activity? = activity

    var layoutManager: MyLinearLayoutManager? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val inflate = inflater.inflate(R.layout.fragment_news, container, false)
        initView(inflate)
        return inflate
    }

    private fun initView(view: View?) {
        recycleView = view?.findViewById(R.id.newsRecyclerView)
        initData()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initData() {
        initListData()
        layoutManager = MyLinearLayoutManager(mActivity)
        recycleView?.layoutManager = layoutManager
        val adapter = NewsRecycleAdapter(mActivity, itemList)
        recycleView?.adapter = adapter
        adapter.notifyDataSetChanged()

        adapter.addItemClickListener(object :
            NewsRecycleAdapter.OnItemClickListener {
            override fun onItemClick(v: View?, position: Int) {
                LogUtil.e(tag, "点击了第 ${position + 1} 个item")
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

        LogUtil.e("size: ${itemList.size}")
    }
}