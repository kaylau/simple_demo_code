package com.kay.demo.kotlin.viewpager

import android.content.Context
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.kay.demo.kotlin.R
import com.kay.demo.kotlin.viewpager.fragment.FragmentOne
import kotlinx.android.synthetic.main.activity_indicator.*

/**
 * Date: 2019/7/22 上午10:07
 * Author: kay lau
 * Description:
 */
class CustomIndicatorActivity : AppCompatActivity() {

    private var titleList: ArrayList<String> = ArrayList()
    private var fragments: ArrayList<Fragment> = ArrayList()
    private lateinit var mContext: Context


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_indicator)
        addTitleList()
        addFragmentList()
        mContext = this
        initTabLayout()
        initTabLayout1()
        initTabLayout2()
        initTabLayout3()
        initTabLayout4()

    }

    private fun addFragmentList() {
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
    }

    private fun initTabLayout4() {
        for (i in titleList.indices) {
            tabLayout4?.addTab(tabLayout4.newTab().setText(titleList[i]))
        }
    }

    private fun initTabLayout3() {
        for (i in titleList.indices) {
            val tab = tabLayout3.newTab()
            val inflate =
                LayoutInflater.from(mContext).inflate(R.layout.tab_item_layout1, null, false)
            inflate?.findViewById<TextView>(R.id.tabTitle)?.text = titleList[i]
            tab.customView = inflate
            tabLayout3?.addTab(tab)
        }

        tabLayout3?.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                tab?.customView?.findViewById<ImageView>(R.id.tabIcon)
                    ?.setImageResource(R.mipmap.ic_home_normal)
                tab?.customView?.findViewById<TextView>(R.id.tabTitle)
                    ?.setTextColor(ContextCompat.getColor(mContext, R.color.color_2e2e2e))
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.customView?.findViewById<ImageView>(R.id.tabIcon)
                    ?.setImageResource(R.mipmap.ic_home_selected)
                tab?.customView?.findViewById<TextView>(R.id.tabTitle)
                    ?.setTextColor(ContextCompat.getColor(mContext, R.color.color_ff0000))
            }

        })
    }

    private fun initTabLayout2() {
        for (i in titleList.indices) {
            tabLayout2?.addTab(tabLayout2.newTab().setText(titleList[i]))
        }
        val viewPageAdapter = ViewPageAdapter(supportFragmentManager, fragments)
        mViewPager!!.adapter = viewPageAdapter
        tabLayout2!!.setupWithViewPager(mViewPager)

        for (i in titleList.indices) {
            val view = getTabTextView(i)
            tabLayout2.getTabAt(i)?.customView = view
        }

        tabLayout2!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                val tab_item_text = tab?.customView?.findViewById<TextView>(R.id.tab_item_text)
                tab_item_text?.setTextColor(ContextCompat.getColor(mContext, R.color.color_0000ff))
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                val tab_item_text = tab?.customView?.findViewById<TextView>(R.id.tab_item_text)
                tab_item_text?.setTextColor(ContextCompat.getColor(mContext, R.color.color_ff8800))
            }
        })
    }

    private fun getTabTextView(i: Int): View? {
        val view: View? = LayoutInflater.from(this).inflate(R.layout.tab_item_layout, null, false)
        val tabItemText = view?.findViewById<TextView>(R.id.tab_item_text)
        tabItemText?.text = titleList[i]
        if (i == 0) {
            tabItemText?.setTextColor(ContextCompat.getColor(mContext, R.color.color_ff8800))
        } else {
            tabItemText?.setTextColor(ContextCompat.getColor(this, R.color.color_0000ff))
        }
        return view
    }

    private fun initTabLayout1() {
        for (i in titleList.indices) {
            tabLayout1?.addTab(tabLayout1.newTab().setText(titleList[i]))
        }
    }

    private fun initTabLayout() {
        for (i in titleList.indices) {
            tabLayout?.addTab(tabLayout.newTab().setText(titleList[i]))
        }
    }

    private fun addTitleList() {
        titleList.add("推荐")
        titleList.add("视频")
        titleList.add("热点")
        titleList.add("娱乐")
        titleList.add("体育")
        titleList.add("财经")
        titleList.add("科技")
        titleList.add("涨知识")
        titleList.add("汽车")
        titleList.add("搞笑")
        titleList.add("历史")
        titleList.add("NBA")
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
}