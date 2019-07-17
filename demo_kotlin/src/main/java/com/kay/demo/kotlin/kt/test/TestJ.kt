package com.kay.demo.kotlin.kt.test


/**
 * Date: 2019/7/17 上午10:49
 * Author: kay lau
 * Description: 类型别名
 */

typealias Predicate<T> = (T) -> Boolean

fun foo(p: Predicate<Int>) = p(42)

fun main() {
    val f: (Int) -> Boolean = { it > 0 }
    println(foo(f)) // 输出 "true"

    val p: Predicate<Int> = { it > 0 }
    println(listOf(1, -2).filter(p)) // 输出 "[1]"

    ee(AInner())
    TestJB().tb()
}

class TestJA {

    class Inner {
        fun aa() {
            println("---> aa")
        }
    }
}

class TestJB {
    inner class Inner {
        fun bb() {
            println("---> bb")
        }
    }

    fun tb() {
        dd(BInner())
    }
}

typealias AInner = TestJA.Inner
typealias BInner = TestJB.Inner

fun ee(a: AInner) {
    a.aa()
}

fun dd(b: BInner) {
    b.bb()
}

