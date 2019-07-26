package com.kay.demo.kotlin.demo

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.view.View
import com.kay.demo.kotlin.R
import com.kay.demo.kotlin.recycleview.adapter.MyRecyclerViewAdapter
import com.kay.demo.kotlin.util.ToolUtil
import com.kay.demo.kotlin.util.logutil.LogUtil
import com.kay.demo.kotlin.weigt.MyRefreshRelativeLayout
import kotlinx.android.synthetic.main.activity_refresh_view.*


/**
 * Date: 2019/7/26 上午10:35
 * Author: kay lau
 * Description:
 */
class RvRefreshActivity : Activity() {

    private var i = 0
    private var j = 0
    private var isScroll = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_refresh_view)
        initView()
    }

    private fun initView() {
        val listData = ToolUtil.loadListData()
        val adapter = MyRecyclerViewAdapter(this, listData)
        rvRefresh.recyclerView.adapter = adapter

        adapter.addItemClickListener(object : MyRecyclerViewAdapter.OnItemClickListener {
            override fun onItemClick(v: View?, position: Int) {
                if (!isScroll) {
                    LogUtil.e("点击了第${position + 1}条目")
                }
            }
        })

        rvRefresh.setOnRefreshListener(object : MyRefreshRelativeLayout.OnRefreshListener {
            override fun onRefresh() {
                isScroll = true
                val element = ToolUtil.addItem("下拉刷新", ++i)
                listData.add(0, element)
                for (index in listData.indices) {
                    val itemData = listData[index]
                    itemData.phone = (index + 1).toString()
                }

                Handler().postDelayed(Runnable {
                    adapter.setData(listData)
                    rvRefresh.setRefreshing(false)
                    adapter.notifyDataSetChanged()
                    isScroll = false
                }, 3000)
                LogUtil.e("onRefresh...")
            }

        })

        rvRefresh.setOnLoadListener(object : MyRefreshRelativeLayout.OnLoadListener {
            override fun onLoad() {
                isScroll = true
                val element = ToolUtil.addItem("上拉加载", ++j)
                listData.add(listData.size, element)
                for (index in listData.indices) {
                    val itemData = listData[index]
                    itemData.phone = (index + 1).toString()
                }

                Handler().postDelayed(Runnable {
                    adapter.setData(listData)
                    rvRefresh.setLoading(false)
                    adapter.notifyDataSetChanged()
                    rvRefresh.recyclerView.smoothScrollToPosition(adapter.itemCount - 1)
                    isScroll = false
                }, 3000)
                LogUtil.e("onLoad...")
            }
        })
    }
}