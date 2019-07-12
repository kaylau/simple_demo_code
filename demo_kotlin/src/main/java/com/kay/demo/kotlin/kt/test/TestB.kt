package com.kay.demo.kotlin.kt.test

fun main() {
    val testB = TestB()
    println("testB.isEmpty: ${testB.isEmpty}")
    testB.size = 0
    println("testB.isEmpty: ${testB.isEmpty}")

    println("testB.value: ${testB.value}")
    testB.stringRepresentation = "ll"
    println("testB.value: ${testB.value}")

    testB.setterVisibility

    testB.counter = -2

}

/**
 * Date: 2019/7/12 下午3:52
 * Author: kay lau
 * Description: 属性与字段
 * 属性既可以用关键字 var 声明为可变的，也可以用关键字 val 声明为只读的。
 * 要使用一个属性，只要用名称引用它即可，就像 Java 中的字段：
 */

class TestB {

    var value: String = "mm"

    var size = 1

//    val isEmpty: Boolean
//        get() = this.size == 0

    // Kotlin 1.1 起，如果可以从 getter 推断出属性类型，则可以省略类型Boolean
    val isEmpty get() = this.size == 0

    var stringRepresentation: String
        get() = this.toString()
        set(value) {
            this.value = value
        }

    var setterVisibility: String = "abc"
        private set // 此 setter 是私有的并且有默认实现

    var counter = 0 // 注意：这个初始器直接为幕后字段赋值
        set(value) {
            if (value >= 0) field = value
            println("field: $field")
            println("counter.value: $value")
//            field: 0
//            counter.value: -2
        }

}