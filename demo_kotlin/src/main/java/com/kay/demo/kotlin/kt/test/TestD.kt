package com.kay.demo.kotlin.kt.test

/**
 * Date: 2019/7/16 上午10:04
 * Author: kay lau
 * Description: 可见性修饰符
 *
 * 对于类内部声明的成员：
 * private 意味着只在这个类内部（包含其所有成员）可见；
 * protected—— 和 private一样 + 在子类中可见。
 * internal —— 能见到类声明的 本模块内 的任何客户端都可见其 internal 成员；
 * public —— 能见到类声明的任何客户端都可见其 public 成员。
 * 注意 对于Java用户：Kotlin 中外部类不能访问内部类的 private 成员。
 *
 */

fun main() {
    Subclass().sa
    Unrelated(Subclass()).a
}

open class Outer {

    private val a = 1
    protected open val b = 2
    internal val c = 3
    val d = 4  // 默认 public

//    val g = Nested().f // 编译失败, Kotlin 中外部类不能访问内部类的 private 成员。

    protected class Nested {
        val e: Int = 5
        private val f: Int = 6
    }

    class Inner {
        val a = "qwe"
    }

    private fun test() {
        val a = 0

    }
}


class Subclass : Outer() {
    // a 不可见
    // b、c、d 可见
    // Nested 和 e 可见

    override val b = 5   // “b”为 protected
    val sa = Outer.Nested().e
}

class Unrelated internal constructor(o: Outer) {
    // o.a、o.b 不可见
    // o.c 和 o.d 可见（相同模块）
    // Outer.Nested 不可见，Nested::e 也不可见
    val a = Outer.Inner().a
    val b = o.c
}

//open class Unrelated protected constructor(o: Outer) {
//    // o.a、o.b 不可见
//    // o.c 和 o.d 可见（相同模块）
//    // Outer.Nested 不可见，Nested::e 也不可见
//}