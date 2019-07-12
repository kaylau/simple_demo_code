package com.kay.demo.kotlin.kt.test

fun main() {

    val myImpl = MyImpl()
    myImpl.aa()
    myImpl.bb()
    myImpl.foo()

    println(myImpl.propertyWithImplementation)

    println("***********")
    // 解决覆盖冲突
    val dd = DD()
    dd.bar()
    dd.foo()
    println("***********")
    CC().bar()
    CC().foo()

}

/**
 * Date: 2019/7/12 下午4:45
 * Author: kay lau
 * Description: 接口
 */

interface MyInterface {

    val prop: Int // 抽象的

    val propertyWithImplementation: String
        get() = "foo"

    fun foo() {
        println(prop)
    }

    fun aa()

    fun bb() {
        // 可选的方法体
        println("MyInterface.bb")
    }
}

class MyImpl : MyInterface {

    override val prop: Int
        get() = 9

    override fun aa() {
        println("MyImpl.aa")
    }

    override fun bb() {
//        super.bb()
        println("MyImpl.bb")
    }
}

// ******************************

interface AA {

    fun foo() {
        println("AA.foo")
    }

    fun bar()
}

interface BB {

    fun foo() {
        println("BB.foo")
    }

    fun bar() {
        println("BB.bar")
    }
}

class CC : AA {

    override fun bar() {
        println("CC.bar")
    }
}

class DD : AA, BB {

    // 解决覆盖冲突
    override fun foo() {
        super<AA>.foo()
        super<BB>.foo()
    }

    override fun bar() {
        super.bar()
    }
}

// ******************************