package com.kay.demo.kotlin.demo

import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.view.View
import android.view.animation.TranslateAnimation
import android.widget.LinearLayout
import com.kay.demo.kotlin.weigt.ObservableScrollView
import com.kay.demo.kotlin.R
import com.kay.demo.kotlin.ScrollViewListener
import com.kay.demo.kotlin.recycleview.fragments.BaseFragment
import com.kay.demo.kotlin.recycleview.fragments.CarsFragment
import com.kay.demo.kotlin.util.ToolUtil
import com.kay.demo.kotlin.util.logutil.LogUtil
import kotlinx.android.synthetic.main.activity_jd_home.*
import org.jetbrains.anko.displayMetrics
import org.jetbrains.anko.toast

/**
 * Date: 2019/7/18 下午3:38
 * Author: kay lau
 * Description:
 */
class JDHomeActivity : FragmentActivity(), View.OnClickListener {

    private var pWidth = 0          // 图片宽度
    private var offset = 0          // 图片偏移量
    private var currentIndex = 0    // 当前标签位置

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_jd_home)

        initView()

    }

    private fun initView() {

        jdHomeTv1.setOnClickListener(this)
        jdHomeTv2.setOnClickListener(this)
        jdHomeTv3.setOnClickListener(this)
        jdHomeTv4.setOnClickListener(this)
        jdHomeTv5.setOnClickListener(this)

        val fragmentList = ArrayList<BaseFragment>()

        fragmentList.add(CarsFragment(this))
        fragmentList.add(CarsFragment(this))
        fragmentList.add(CarsFragment(this))
        fragmentList.add(CarsFragment(this))
        fragmentList.add(CarsFragment(this))

        val adapter = ViewPageAdapter(supportFragmentManager, fragmentList)
        jdHomeVp.adapter = adapter
        jdHomeVp.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageScrollStateChanged(p0: Int) {
            }

            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {
            }

            override fun onPageSelected(position: Int) {
                val section = offset * 2 + pWidth
                val animation =
                    TranslateAnimation(
                        section * currentIndex.toFloat(),
                        section * position.toFloat(), 0.0f, 0.0f
                    )
                currentIndex = position
                animation.duration = 300L
                animation.fillAfter = true  // 动画结束后停留在当前所处位置
                jdHomeCursor.startAnimation(animation)
                toast("您选择了：$currentIndex 标签页")
            }
        })

        val drawable = resources.getDrawable(R.drawable.cursor)
        val bitmap = ToolUtil.getBitmapFormDrawable(drawable)
        pWidth = bitmap.width

        val display = windowManager.defaultDisplay
        display.getMetrics(displayMetrics)

        val screenWidth = displayMetrics.widthPixels
        offset = (screenWidth / fragmentList.size - pWidth) / 2

        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        layoutParams.setMargins(offset, 0, 0, 0)
        jdHomeCursor.layoutParams = layoutParams

        jdHomeTab.viewTreeObserver.addOnGlobalLayoutListener {
            val height = bgJDHome.height - jdHomeTab.height - jdHomeCursor.height
            val vpLayoutParams = jdHomeVp.layoutParams
            vpLayoutParams?.height = height
            jdHomeVp.layoutParams = vpLayoutParams
        }
        jdHomeMyLinear.setOnClickListener(this)
        jdHomeMyLinear.isIntercept = true

        jdHomeScroll.setScrollViewListener(object : ScrollViewListener {
            override fun onScrollChanged(
                scrollView: ObservableScrollView?,
                x: Int,
                y: Int,
                oldx: Int,
                oldy: Int
            ) {
                val rect = Rect()
                jdHomeScroll.getHitRect(rect)
                if (llBottomJDHome.getLocalVisibleRect(rect)) {
                    for (fragment in fragmentList) {
                        fragment.onStopSliding()
                    }
                    LogUtil.e("tag", "llBottom 可见---------->")
                } else {
                    for (fragment in fragmentList) {
                        fragment.onStartSliding()
                    }
                    LogUtil.e("tag", "llBottom 不可见-------->")
                }
            }

            override fun scrollOrientation(orientation: Int) {
            }
        })
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

    override fun onClick(v: View?) {
        var index = 0
        when (v?.id) {
            R.id.jdHomeTv1 -> index = 0
            R.id.jdHomeTv2 -> index = 1
            R.id.jdHomeTv3 -> index = 2
            R.id.jdHomeTv4 -> index = 3
            R.id.jdHomeTv5 -> index = 4
        }
        jdHomeVp.currentItem = index
    }
}

