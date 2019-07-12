package com.kay.demo.kotlin.kt

fun main() {
//    BaseCat("")
    Derived("he", "xxx")
}

/**
 * Date: 2019/7/12 上午11:59
 * Author: kay lau
 * Description:
 * 设计一个基类时，应该避免在构造函数、属性初始化器以及 init 块中使用 open 成员。
 */
open class BaseCat(name: String) {

    init {
        println("Initializing Base")
    }

    open val size: Int =
        name.length.also { println("Initializing size in Base: $it") }
}

class Derived(
    name: String,
    val lastName: String
) : BaseCat(name.capitalize().also { println("Argument for Base: $it") }) {

    init {
        println("Initializing Derived")
    }

    override val size: Int =
        (super.size + lastName.length).also { println("Initializing size in Derived: $it") }
}