package com.kay.demo.kotlin

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.kay.demo.kotlin.util.LogUtil

import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.longToast
import org.jetbrains.anko.toast

class MainActivity : AppCompatActivity(), View.OnClickListener {

    var tag = "MainActivity"
    var i = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()

    }

    private fun initView() {
        btn_two.text = "基本语法"

        tv_one.setOnClickListener(this)
        btn_two.setOnClickListener(this)

        tv_three.setOnClickListener {
            tvClickImpl(tv_three, "tv_three")
        }
    }

    override fun onClick(v: View?) {
        // 当做语句使用
        when (v?.id) {
            R.id.tv_one -> {
                tvClickImpl(tv_one, "tv_one")
            }
            R.id.btn_two -> {
                grammar()
            }
        }
    }

    private fun tvClickImpl(tv: TextView, text: String) {
        i++
        if (i % 2 == 0) {
            tv.text = "---ha ha 你好---" + text + ", 点了" + i + "次"
            toast(tv_one.text)
        } else {
            tv.text = "---hello word--" + text + ", 点了" + i + "次"
            longToast(tv_one.text)
        }
    }

    private fun sum(a: Int, b: Int): Int {
        val sum = a + b
        longToast("$a+$b=$sum")
        return sum
    }

    /**
     * 基本语法
     */
    private fun grammar() {
        val sum = sum(3, 9)
        LogUtil.e(tag, "sum: $sum")

        KotlinDemo.testVal()

        val parseInt = KotlinDemo.parseInt(null)
        LogUtil.e(tag, " parseInt = $parseInt")
        val parseInt1 = KotlinDemo.parseInt("123987289")
        LogUtil.e(tag, " parseInt1 = $parseInt1")

        val strLen = KotlinDemo.getStringLength("qwe asd 123")
        LogUtil.d(tag, "strLen: $strLen")
        val intLen = KotlinDemo.getStringLength(123456)
        LogUtil.d(tag, "intLen: $intLen")

        KotlinDemo.asStr(null)
        KotlinDemo.asStrs("1234567qwe")
//        KotlinDemo.asStrs('q')

        KotlinDemo.testFor()

        KotlinDemo.testWhile()

        KotlinDemo.testWhen(parseInt1)
        KotlinDemo.testWhen(0)
        KotlinDemo.testWhen(1)
        KotlinDemo.testWhen(2)
        KotlinDemo.testWhen(3)
        KotlinDemo.testWhen(6)

        val prefix = KotlinDemo.hasPrefix("prefix")
        LogUtil.i(tag, "hasPrefix: $prefix")

        val qwe = KotlinDemo.hasPrefix("qwe")
        LogUtil.i(tag, "hasPrefix: $qwe")

        KotlinDemo.testIn()

        KotlinDemo.testList()


    }
}
