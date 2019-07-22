package com.kay.demo.kotlin.viewpager

import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.kay.demo.kotlin.R
import com.kay.demo.kotlin.viewpager.fragment.FragmentOne
import kotlinx.android.synthetic.main.activity_tab_layout.*

/**
 * Date: 2019/7/19 上午10:14
 * Author: kay lau
 * Description:
 */
class TabLayoutActivity : AppCompatActivity() {

    private var titleList: ArrayList<String> = ArrayList()
    private var customViewList: ArrayList<View?> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tab_layout)

        addTitleList()

        initView()
    }

    private fun initView() {
        // 添加tab
        for (i in titleList.indices) {
            tabLayout!!.addTab(tabLayout!!.newTab())
        }

        val fragments = ArrayList<Fragment>()

        for (i in titleList.indices) {
            val listFragment = FragmentOne()
            val bundle = Bundle()
            val sb = StringBuffer()
            for (j in 1..2) {
                sb.append(titleList[i]).append(" --> ")
            }
            bundle.putString("content", sb.toString())
            bundle.putInt("position", i)
            listFragment.arguments = bundle
            fragments.add(listFragment)
        }

        val viewPageAdapter = ViewPageAdapter(supportFragmentManager, fragments)
        viewPager!!.adapter = viewPageAdapter
        tabLayout!!.setupWithViewPager(viewPager)

        // 添加tab
        for (i in titleList.indices) {
            tabLayout.getTabAt(i)?.customView = customViewList[i]

            // 默认选中第一个
            if (i == 0) {
                selectTab(i, 0)
            }
        }

        viewPager!!.setPageTransformer(true, MTransformer())

        tabLayout!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(p0: TabLayout.Tab?) {

            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {

            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                val position = tab!!.position
                for (i in customViewList.indices) {
                    selectTab(i, position)
                }
                viewPager.setCurrentItem(position, false)
            }
        })
    }

    private fun selectTab(i: Int, position: Int) {
        val view = customViewList[i]
        val itemText = view!!.findViewById<TextView>(R.id.tab_item_text)
        val indicator = view.findViewById<View>(R.id.tab_item_indicator)
        if (i == position) {
            itemText!!.setTextColor(Color.RED)
            indicator!!.visibility = View.VISIBLE
            indicator.setBackgroundColor(Color.BLUE)
        } else {
            itemText!!.setTextColor(Color.BLACK)
            indicator!!.visibility = View.INVISIBLE
            indicator.setBackgroundColor(Color.WHITE)
        }
    }

    private fun addTab(tab: String) {
        titleList.add(tab)
        val customView = getTabView(tab, 100, 10, 16)
        customViewList.add(customView)
        tabLayout.addTab(tabLayout.newTab().setCustomView(customView))
    }

    private fun getTabView(text: String, inWidth: Int, inHeight: Int, textSize: Int): View? {
        val view: View? = LayoutInflater.from(this).inflate(R.layout.tab_item_layout, null, false)
        val tabItemText = view?.findViewById<TextView>(R.id.tab_item_text)
        if (inWidth > 0) {
            val indicator = view?.findViewById<View>(R.id.tab_item_indicator)
            val layoutParams = indicator?.layoutParams
            layoutParams?.width = inWidth
            layoutParams?.height = inHeight
            indicator?.layoutParams = layoutParams
        }
        tabItemText?.text = text
        tabItemText?.textSize = textSize.toFloat()
        return view
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

    inner class MTransformer : ViewPager.PageTransformer {

        /**
         * 回调方法,重写viewpager的切换动画
         */
        override fun transformPage(view: View, position: Float) {
            val pageWidth = view.width
            val wallpaper = view.findViewById<View>(R.id.bgFragment)

            when {
                position < -1 -> {
                    wallpaper.translationX = 0.toFloat()
                    view.translationX = 0.toFloat()
                }
                position <= 1 -> {
                    wallpaper.translationX = pageWidth * getFactor(position)
                    view.translationX = 8 * position
                }
                else -> {
                    wallpaper.translationX = 0.toFloat()
                    view.translationX = 0.toFloat()
                }
            }
        }

        private fun getFactor(position: Float): Float {
            return -position / 2
        }
    }

    private fun addTitleList() {
        addTab("推荐")
        addTab("视频")
        addTab("热点")
        addTab("娱乐")
        addTab("体育")
        addTab("北京")
        addTab("财经")
        addTab("科技")
        addTab("汽车")
        addTab("社会")
        addTab("搞笑")
        addTab("军事")
        addTab("历史")
        addTab("涨知识")
        addTab("NBA")
    }
}