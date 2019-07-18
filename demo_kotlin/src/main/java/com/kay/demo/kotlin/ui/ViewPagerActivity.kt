package com.kay.demo.kotlin.ui

import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.app.*
import android.support.v4.view.ViewPager
import android.view.View
import android.view.animation.TranslateAnimation
import android.widget.LinearLayout
import com.kay.demo.kotlin.R
import com.kay.demo.kotlin.ui.fragment.*
import com.kay.demo.kotlin.util.ToolUtil
import kotlinx.android.synthetic.main.activity_view_pager.*
import org.jetbrains.anko.displayMetrics
import org.jetbrains.anko.toast

/**
 * Date: 2019/7/18 下午3:38
 * Author: kay lau
 * Description:
 */
class ViewPagerActivity : FragmentActivity(), View.OnClickListener {

    private var pWidth = 0          // 图片宽度
    private var offset = 0          // 图片偏移量
    private var currentIndex = 0    // 当前标签位置

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_view_pager)

        initView()

    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun initView() {

        tv_one.setOnClickListener(this)
        tv_two.setOnClickListener(this)
        tv_three.setOnClickListener(this)
        tv_four.setOnClickListener(this)
        tv_five.setOnClickListener(this)

        val viewList = mutableListOf<Fragment>()

        viewList.add(FragmentOne())
        viewList.add(FragmentTwo())
        viewList.add(FragmentThree())
        viewList.add(FragmentFour())
        viewList.add(FragmentFive())

        val adapter = ViewPageAdapter(supportFragmentManager, viewList)
        viewPager.adapter = adapter
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{

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
                ivCursor.startAnimation(animation)
                toast("您选择了：$currentIndex 标签页")
            }
        })

        val drawable = resources.getDrawable(R.drawable.cursor, null)
        val bitmap = ToolUtil.getBitmapFormDrawable(drawable)
        pWidth = bitmap.width

        val display = windowManager.defaultDisplay
        display.getMetrics(displayMetrics)

        val screenWidth = displayMetrics.widthPixels
        offset = (screenWidth / viewList.size - pWidth) / 2

        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        layoutParams.setMargins(offset, 0, 0, 0)
        ivCursor.layoutParams = layoutParams

    }


    inner class ViewPageAdapter(fm: FragmentManager, list: MutableList<Fragment>) :
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
            R.id.tv_one -> index = 0
            R.id.tv_two -> index = 1
            R.id.tv_three -> index = 2
            R.id.tv_four -> index = 3
            R.id.tv_five -> index = 4
        }
        viewPager.currentItem = index
    }
}

