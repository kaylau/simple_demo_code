package com.kay.demo.kotlin.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.kay.demo.kotlin.R
import com.kay.demo.kotlin.demo.HomeJDActivity
import com.kay.demo.kotlin.demo.JDHomeActivity
import com.kay.demo.kotlin.demo.RvRefreshActivity
import com.kay.demo.kotlin.recycleview.activitis.RecycleActivity
import com.kay.demo.kotlin.recycleview.activitis.ScrollRecycleActivity
import com.kay.demo.kotlin.util.logutil.LogUtil
import com.kay.demo.kotlin.viewpager.CustomIndicatorActivity
import com.kay.demo.kotlin.viewpager.TabLayoutActivity
import com.kay.demo.kotlin.viewpager.ViewPagerActivity
import kotlinx.android.synthetic.main.activity_first.*
import kotlinx.android.synthetic.main.activity_main.tv_one

/**
 * Date: 2019/7/17 下午4:19
 * Author: kay lau
 * Description:
 */
class FirstActivity : AppCompatActivity(), View.OnClickListener {

    private val tag = FirstActivity::class.java.simpleName

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val isSave = savedInstanceState?.getBoolean("isSave")
        println("   isSave ---> $isSave")
        val str = savedInstanceState?.getString("isSaveStr")
        println("isSaveStr ---> $str")

        setContentView(R.layout.activity_first)

        val stringExtra = intent.getStringExtra("key")
        tv_one.text = "hello world $tag from $stringExtra"

        LogUtil.i(tag, "$tag onCreate")
        LogUtil.d(tag, "$tag onCreate")
        LogUtil.e(tag, "$tag onCreate")

        btn_recycle_view.setOnClickListener(this)
        btn_view_pager.setOnClickListener(this)
        btn_tab_layout.setOnClickListener(this)
        btn_indicator.setOnClickListener(this)
        btn_scroll_recycle.setOnClickListener(this)
        btn_home_jd.setOnClickListener(this)
        btn_jd_home.setOnClickListener(this)
        btn_rv_refresh.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        val intent = Intent()
        when (v?.id) {
            R.id.btn_recycle_view -> intent.setClass(this, RecycleActivity::class.java)
            R.id.btn_view_pager -> intent.setClass(this, ViewPagerActivity::class.java)
            R.id.btn_tab_layout -> intent.setClass(this, TabLayoutActivity::class.java)
            R.id.btn_indicator -> intent.setClass(this, CustomIndicatorActivity::class.java)
            R.id.btn_scroll_recycle -> intent.setClass(this, ScrollRecycleActivity::class.java)
            R.id.btn_home_jd -> intent.setClass(this, HomeJDActivity::class.java)
            R.id.btn_jd_home -> intent.setClass(this, JDHomeActivity::class.java)
            R.id.btn_rv_refresh -> intent.setClass(this, RvRefreshActivity::class.java)
        }
        startActivity(intent)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        println("onSaveInstanceState--->1")
        outState?.putString("isSaveStr", "保存状态")
    }

    override fun onBackPressed() {
        val intent = Intent()
        intent.putExtra("name", "kay")
        intent.putExtra("pwd", "lau123")
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}