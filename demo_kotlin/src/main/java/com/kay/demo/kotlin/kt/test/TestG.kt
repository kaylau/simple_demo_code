package com.kay.demo.kotlin.kt.test

/**
 * Date: 2019/7/16 下午3:45
 * Author: kay lau
 * Description: 嵌套类与内部类
 */

interface OnClick {

    fun onClick(int: Int)

    fun onCancel(string: String)
}

interface OnCallBack {

    fun onCallBack()
}

class TestGOuter {

    private val bar: Int = 1

    // 嵌套类
    class Nested {
        fun foo() = 2

        fun test() = TestGOuter().bar
    }

    // 内部类
    inner class Inner {
        fun foo() = 3
        fun test() = bar
    }

    private var listener: OnClick? = null

    fun addListener(listener: OnClick?) {

        this.listener = listener
    }

    fun click() {

        listener?.onClick(90)
    }

    fun cancel(string: String) {

        listener?.onCancel(string)
    }

    private lateinit var callBack: OnCallBack

    fun addCallback(callback: OnCallBack) {

        this.callBack = callback
    }

    fun onCallback() {

        callBack.onCallBack()
    }

}


fun main() {
    val demo = TestGOuter.Nested().foo() // == 2
    println("demo = $demo")

    val test = TestGOuter.Nested().test()
    println("test = $test")

    // =================================================================

    val testGOuter = TestGOuter()
    val demo1 = testGOuter.Inner().foo() // == 3
    println("demo1 = $demo1")

    val test1 = testGOuter.Inner().test()
    println("test1 = $test1")

    testGOuter.addListener(object : OnClick {

        override fun onClick(int: Int) {
            println("onClick $int")
        }

        override fun onCancel(string: String) {
            println("onCancel $string")
        }
    })

    testGOuter.click()

    testGOuter.cancel("取消--->")

    testGOuter.addListener(null)

    testGOuter.click()

    testGOuter.cancel("取消--->")

    testGOuter.addCallback(object : OnCallBack {
        override fun onCallBack() {
            println("callBack onCallBack--->")
        }
    })

    testGOuter.onCallback()

}
