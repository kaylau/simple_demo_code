package com.kay.demo.kotlin.kt.test

/**
 * Date: 2019/7/12 下午3:23
 * Author: kay lau
 * Description:
 */

fun main() {
//    C().f()
    Z().xx()
    Z().yy()
}

/**
 * 一个类从它的直接超类继承相同成员的多个实现，
 * 它必须覆盖这个成员并提供其自己的实现（也许用继承来的其中之一）。
 * 为了表示采用从哪个超类型继承的实现，
 * 我们使用由尖括号中超类型名限定的 super，如 super<Base>：
 */
open class A : E() {
    override fun f() {
        super.f()
        println("A.f")
    }

    fun a() {
        println("A.a")
    }
}

interface B {
    fun f() {
        println("B.f")
        b()
    } // 接口成员默认就是“open”的

    fun b() {
        println("B.b")
    }
}

interface D {
    fun dd() {
        println("D.dd")
    }

    fun f() {
        println("D.f")
    }
}

open class E {
    open fun f() {
        println("E.ff")
    }
}

open class C : A(), B, D {
    // 编译器要求覆盖 f()：
    // f() 由 C 继承了多个实现，所以我们必须在 C 中覆盖 f() 并且提供我们自己的实现来消除歧义。
    override fun f() {
        super<A>.f() // 调用 A.f()
//        super<B>.f() // 调用 B.f()
        super<D>.f()

        dd()
    }
}

abstract class X {
    abstract fun xx()
}

open class Y : X() {

    override fun xx() {
        println("Y.xx")
    }

    open fun yy() {
        println("Y.yy")
    }
}

class Z : Y() {

    override fun yy() {
        super.yy()
        println("Z.yy")
    }

    override fun xx() {
        super.xx()
        println("Z.xx")
    }
}

fun pp() {
    println("---> pp")
}