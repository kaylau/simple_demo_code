package com.kay.demo.kotlin.recycleview.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import com.kay.demo.kotlin.R
import com.kay.demo.kotlin.weigt.MyLinearLayoutManager
import com.kay.demo.kotlin.weigt.MyRecyclerView
import com.kay.demo.kotlin.recycleview.RecycleViewItemData
import com.kay.demo.kotlin.recycleview.adapter.MyRecyclerViewAdapter
import com.kay.demo.kotlin.util.logutil.LogUtil

/**
 * Date: 2019/7/23 上午9:59
 * Author: kay lau
 * Description:
 */

@SuppressLint("ValidFragment")
class CarsFragment(activity: Activity) : BaseFragment(), View.OnClickListener {

    override fun onClick(v: View?) {
    }

    override fun onStopSliding() {
        relFragmentBg?.visibility = View.VISIBLE
        LogUtil.e("onStopSliding")
    }

    override fun onStartSliding() {
        relFragmentBg?.visibility = View.GONE
        LogUtil.e("onStartSliding")
    }

    internal val tag = CarsFragment::class.java.simpleName
    var recycleView: MyRecyclerView? = null
    var relFragmentBg: RelativeLayout? = null
    var itemList = mutableListOf<RecycleViewItemData>()

    var mActivity: Activity? = activity

    var layoutManager: MyLinearLayoutManager? = null

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
        relFragmentBg = view?.findViewById(R.id.relFragmentBg)
        initData()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initData() {
        initListData()
        layoutManager = MyLinearLayoutManager(mActivity)
        recycleView?.layoutManager = layoutManager
        val adapter =
            MyRecyclerViewAdapter(mActivity, itemList)
        recycleView?.adapter = adapter
        adapter.notifyDataSetChanged()

        adapter.addItemClickListener(object :
            MyRecyclerViewAdapter.OnItemClickListener {
            override fun onItemClick(v: View?, position: Int) {
                LogUtil.e(tag, "点击了第 ${position + 1} 个item")
            }
        })

        relFragmentBg?.setOnClickListener(this)

        recycleView?.viewTreeObserver?.addOnGlobalLayoutListener {
            val lp = relFragmentBg?.layoutParams
            lp?.height = recycleView!!.height
            relFragmentBg?.layoutParams = lp
        }
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
}