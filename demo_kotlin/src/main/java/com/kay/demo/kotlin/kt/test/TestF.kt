package com.kay.demo.kotlin.kt.test

import kotlin.Triple as Triple1

/**
 * Date: 2019/7/16 上午11:57
 * Author: kay lau
 * Description: 数据类
 */

data class DataA(val name: String, val age: Int) {

    var nName: String = ""

    fun qaz(triple: Triple1<Any, Any, Any>) {
        println("triple.first=${triple.first}")
        println("triple.second=${triple.second}")
        println("triple.third=${triple.third}")
    }
}

fun main() {
    val dataA1 = DataA("x h", 5)
    val dataA2 = DataA("x m", 3)
    val dataA3 = dataA2.copy(age = 6)
    val dataA4 = dataA1.copy(name = "y y")

    dataA1.nName = "hh"
    dataA2.nName = "mm"
    dataA4.nName = "mm-yy"

    println(dataA1.toString() + "--nName: ${dataA1.nName}")
    println(dataA2.toString() + "--nName: ${dataA2.nName}")
    println(dataA3.toString() + "--nName: ${dataA3.nName}")
    println(dataA4.toString() + "--nName: ${dataA4.nName}")

    // 解构声明
    val (name, age) = dataA1

    println("name: $name----age: $age")

    // Triple Pair
    val pair = Pair<Any, Any>(DataA("qa", 2), DataA("zx", 1))
    val (any, any1) = pair
    println(any.toString())
    println(any1.toString())
    println(pair.first)
    println(pair.second)

    // Triple Triple
    val triple = Triple1<Any, Any, Any>("qaz", 123, 9)

    dataA1.qaz(triple)

}