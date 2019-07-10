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

        btn_two.text = "test"

        tv_one.setOnClickListener(this)
        btn_two.setOnClickListener(this)

        tv_three.setOnClickListener {
            tvClickImpl(tv_three, "tv_three")
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_one -> {
                tvClickImpl(tv_one, "tv_one")
            }
            R.id.btn_two -> {
                test()
            }
        }
    }

    private fun test() {
        val sum = sum(3, 9)
        LogUtil.e(tag, "sum: $sum")
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
}
