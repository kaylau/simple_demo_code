package com.kay.demo.kotlin.recycleview.activitis

import android.graphics.Rect
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v7.app.AppCompatActivity
import android.view.MotionEvent
import android.view.View
import com.kay.demo.kotlin.R
import com.kay.demo.kotlin.recycleview.fragments.BaseFragment
import com.kay.demo.kotlin.recycleview.fragments.CarsFragment
import com.kay.demo.kotlin.util.logutil.LogUtil
import kotlinx.android.synthetic.main.activity_indicator.tabLayout
import kotlinx.android.synthetic.main.activity_scroll_recycle.*

/**
 * Date: 2019/7/23 上午9:52
 * Author: kay lau
 * Description:
 */
class ScrollRecycleActivity : AppCompatActivity() {

    private var titleList: ArrayList<String> = ArrayList()
    private var fragments: ArrayList<BaseFragment> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scroll_recycle)

        loadData()
        initView()
        initTabLayout()
    }

    private fun initView() {
        llTabLayout.viewTreeObserver.addOnGlobalLayoutListener {
            val layoutParams = mmVewPager.layoutParams
            layoutParams.height = bgView.height - llTabLayout.height
            mmVewPager.layoutParams = layoutParams
        }

        mScrollView.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                val rect = Rect()
                mScrollView.getHitRect(rect)
                if (llBottom.getLocalVisibleRect(rect)) {
                    LogUtil.e("tag", "llBottom 可见")
                    llScrRel.isIntercept = true
                    when (event?.action) {
                        MotionEvent.ACTION_DOWN -> {
                            mScrollView.setInterceptTouchEvent(true)
                            LogUtil.e("mScrollView onTouch  按下")
                        }
                        MotionEvent.ACTION_MOVE -> {
                            mScrollView.setInterceptTouchEvent(false)
                            LogUtil.e("mScrollView onTouch  移动")
                        }
                        MotionEvent.ACTION_UP -> {
                            mScrollView.setInterceptTouchEvent(false)
                            LogUtil.e("mScrollView onTouch  抬起")
                        }
                    }
                } else {
                    LogUtil.e("tag", "llBottom 不可见")
                    llScrRel.isIntercept = false
                }
                return false
            }
        })

        // android M
//        mScrollView.setOnScrollChangeListener(
//            (View.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
//                val rect = Rect()
//                mScrollView.getHitRect(rect)
//                if (llBottom.getLocalVisibleRect(rect)) {
//                    LogUtil.e("tag", "llBottom 可见")
//                    fragments[0].onStopSliding()
//                } else {
//                    LogUtil.e("tag", "llBottom 不可见")
//                    fragments[0].onStartSliding()
//                }
//            })
//        )

    }

    private fun loadData() {
        addTitleList()
        addFragments()
    }

    private fun addFragments() {
        for (i in titleList.indices) {
            fragments.add(CarsFragment(this))
        }
    }

    private fun initTabLayout() {
        for (i in titleList.indices) {
            tabLayout?.addTab(tabLayout.newTab().setText(titleList[i]))
        }

        val viewPageAdapter = ViewPageAdapter(supportFragmentManager, fragments)
        mmVewPager!!.adapter = viewPageAdapter
        tabLayout!!.setupWithViewPager(mmVewPager)

        for (i in titleList.indices) {
            tabLayout?.getTabAt(i)?.text = titleList[i]
        }
    }

    private fun addTitleList() {
        titleList.add("新闻")
        titleList.add("视频")
        titleList.add("体育")
        titleList.add("N B A")
        titleList.add("汽车")
        titleList.add("图片")
    }

    inner class ViewPageAdapter(fm: FragmentManager, list: ArrayList<BaseFragment>) :
        FragmentPagerAdapter(fm) {

        private val mList = list

        override fun getItem(position: Int): Fragment {
            return mList[position]
        }

        override fun getCount(): Int {
            return mList.size
        }
    }

}