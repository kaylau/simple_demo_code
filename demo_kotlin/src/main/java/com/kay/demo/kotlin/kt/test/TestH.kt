package com.kay.demo.kotlin.kt.test

/**
 * Date: 2019/7/16 下午4:29
 * Author: kay lau
 * Description:  枚举
 */

enum class Direction {
    NORTH, SOUTH, WEST, EAST
}

enum class Color(val rgb: Int) {
    RED(0xFF0000),
    GREEN(0x00FF00),
    BLUE(0x0000FF)
}

// 枚举常量也可以声明自己的匿名类
enum class ProtocolState {
    WAITING {
        override fun signal() = TALKING
    },

    TALKING {
        override fun signal() = HAHA
    },

    HAHA {
        override fun signal() = WAITING
    };

    abstract fun signal(): ProtocolState
}

enum class RGB { RED, GREEN, BLUE }

inline fun <reified T : Enum<T>> printAllValues() {
    println(enumValues<T>().joinToString { it.name })
}

fun main() {
    printAllValues<RGB>() // 输出 RED, GREEN, BLUE

    val message = haha({ name -> "$name -- heihei" })
    println(message)

    // 每个枚举常量都具有在枚举类声明中获取其名称与位置的属性：name: String,  ordinal: Int
    println(ProtocolState.TALKING.ordinal)
    println(ProtocolState.WAITING)
    println(ProtocolState.HAHA.name)
}

inline fun haha(area: (name: String) -> String): String {
    return area("haha")
}