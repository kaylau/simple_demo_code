package com.kay.demo.kotlin.recycleview.activitis

import android.app.Activity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.kay.demo.kotlin.R
import com.kay.demo.kotlin.recycleview.adapter.MyRecyclerViewAdapter
import com.kay.demo.kotlin.recycleview.RecycleViewItemData
import com.kay.demo.kotlin.util.logutil.LogUtil
import kotlinx.android.synthetic.main.activity_recycle.*
import org.jetbrains.anko.longToast

/**
 * Date: 2019/7/18 上午9:40
 * Author: kay lau
 * Description:
 */
class RecycleActivity : Activity() {

    private val tag: String = RecycleActivity::class.java.simpleName

    private var itemList = mutableListOf<RecycleViewItemData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycle)
        initData()
        initView()
    }

    private fun initView() {
        recycle_view.layoutManager = LinearLayoutManager(this)
        val adapter = MyRecyclerViewAdapter(this, itemList)
        recycle_view.adapter = adapter
        adapter.addItemClickListener(object :
            MyRecyclerViewAdapter.OnItemClickListener {
            override fun onItemClick(v: View?, position: Int) {
                longToast("点击了第 ${position + 1} 个item")
            }
        })
    }

    private fun initData() {
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

        for (i: Int in 1..2) {
            for (index in 1..imgList.size) {
                if (index == 3) {
                    itemList.add(
                        RecycleViewItemData()
                    )
                } else {
                    itemList.add(
                        RecycleViewItemData(
                            "name --> ${index - 1}",
                            imgList[index - 1],
                            "90123987$index-1"
                        )
                    )
                }
            }
        }


        LogUtil.e(tag, "size: ${itemList.size}")
    }
}

