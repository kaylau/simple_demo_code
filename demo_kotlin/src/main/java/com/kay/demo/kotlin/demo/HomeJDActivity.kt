package com.kay.demo.kotlin.demo

import android.content.Context
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import com.kay.demo.kotlin.R
import com.kay.demo.kotlin.recycleview.vp.DecoratorViewPager
import com.kay.demo.kotlin.weigt.MyRecycleViewScrollListener
import com.kay.demo.kotlin.recycleview.fragments.BaseFragment
import com.kay.demo.kotlin.recycleview.fragments.NewsFragment
import com.kay.demo.kotlin.util.logutil.LogUtil
import kotlinx.android.synthetic.main.activity_home_jd.*
import org.jetbrains.anko.displayMetrics

/**
 * Date: 2019/7/24 上午10:09
 * Author: kay lau
 * Description:
 */
class HomeJDActivity : AppCompatActivity() {

    private var titleList: ArrayList<String> = ArrayList()
    private var fragments: ArrayList<BaseFragment> = ArrayList()

    val itemTypeBody = 1

    val load = 2001             // 正在加载
    val loadEnd = 2002          // 加载到底
    val loadComplete = 2003     // 加载完成

    private fun addTitleList() {
        titleList.add("视频")
        titleList.add("娱乐")
        titleList.add("体育")
        titleList.add("涨知识")
        titleList.add("搞笑")
        titleList.add("NBA")
    }

    private fun addFragmentList() {
        for (i in titleList.indices) {
            fragments.add(NewsFragment(this))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addTitleList()
        addFragmentList()
        setContentView(R.layout.activity_home_jd)


        initRecycleView()

    }

    private fun initRecycleView() {
        val list = ArrayList<ItemData>()
        list.add(ItemData(itemTypeBody))

        val resources = resources
        val statusBarId = resources.getIdentifier("status_bar_height", "dimen", "android")
        val statusBarHeight = resources.getDimensionPixelSize(statusBarId)

        val navigationBarId = resources.getIdentifier("navigation_bar_height", "dimen", "android")
        val navigationBarHeight = resources.getDimensionPixelSize(navigationBarId)

        val height = displayMetrics.heightPixels - statusBarHeight - navigationBarHeight - dp2Px(8)

        LogUtil.e("displayMetrics.heightPixels: ${displayMetrics.heightPixels}")
        LogUtil.e("statusBarHeight: $statusBarHeight")
        LogUtil.e("height: $height")
        LogUtil.e("navigationBarHeight: $navigationBarHeight")

        reclHomeJD.layoutManager = LinearLayoutManager(this)
        val adapter = MyAdapter(this, list, height)
        reclHomeJD.adapter = adapter


        reclHomeJD.addOnScrollListener(object : MyRecycleViewScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                LogUtil.e("onScrolled--->")

            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager
                val firstVisibleItemPosition =
                    linearLayoutManager.findFirstVisibleItemPosition()
                LogUtil.e("第一个条目: $firstVisibleItemPosition")

                if (linearLayoutManager.findLastVisibleItemPosition() == list.size - 1) {
                    adapter.notifyLoadState(loadEnd)
                }
            }
        })
    }

    private fun initTabLayout(tabLayout: TabLayout) {
        for (i in titleList.indices) {
            tabLayout.addTab(tabLayout.newTab().setText(titleList[i]))
        }
    }

    fun dp2Px(dp: Int): Int {
        val density = resources.displayMetrics.density.toInt()
        return (dp * density + 0.5f).toInt()
    }

    inner class MyAdapter(context: Context, list: ArrayList<ItemData>, height: Int) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        val footType = 1001
        val bodyType = 1002

        val mList = list
        val mContext = context
        val mHeight = height

        var loadState: Int = 0

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            if (viewType == footType) {
                val view = LayoutInflater.from(mContext).inflate(R.layout.item_foot_layout, null)
                return FootViewHolder(view)
            }
            val view = LayoutInflater.from(mContext).inflate(R.layout.item_body_layout, null)
            return BodyViewHolder(view)
        }

        override fun getItemCount(): Int {
            return mList.size + 1
        }

        override fun getItemViewType(position: Int): Int {
            if (position >= mList.size) {
                return footType
            }
            return bodyType
        }

        override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
            if (viewHolder is BodyViewHolder) {
                viewHolder.tv.text = "item one"

            } else if (viewHolder is FootViewHolder) {
                val layoutParams = viewHolder.footBg.layoutParams
                layoutParams.height = mHeight
                viewHolder.footBg.layoutParams = layoutParams
                initTabLayout(viewHolder.tabLayout)

                val viewPageAdapter = ViewPageAdapter(supportFragmentManager, fragments)
                viewHolder.viewPager.adapter = viewPageAdapter

                viewHolder.tabLayout.setupWithViewPager(viewHolder.viewPager)
                for (i in titleList.indices) {
                    viewHolder.tabLayout.getTabAt(i)?.text = titleList[i]
                }
                viewHolder.viewPager.setNestedpParent(viewHolder.viewPager.parent as ViewGroup)

                when (loadState) {
                    load -> viewHolder.footBg.visibility = View.GONE
                    loadEnd -> viewHolder.footBg.visibility = View.VISIBLE
                    loadComplete -> viewHolder.footBg.visibility = View.GONE
                }
            }
        }

        fun notifyLoadState(state: Int) {
            loadState = state
            notifyDataSetChanged()
        }

        inner class BodyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val tv = itemView.findViewById<TextView>(R.id.itemOneTv1)
        }

        inner class FootViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val footBg = itemView.findViewById<RelativeLayout>(R.id.itemTwoBg)
            val tabLayout = itemView.findViewById<TabLayout>(R.id.itemTwoTabLayout)
            val viewPager = itemView.findViewById<DecoratorViewPager>(R.id.itemTwoViewPager)
        }
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

data class ItemData(var type: Int)