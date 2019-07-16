package com.kay.demo.kotlin.kt.test

/**
 * Date: 2019/7/16 上午10:32
 * Author: kay lau
 * Description: 扩展函数
 */

// 扩展属性
// 由于扩展没有实际的将成员插入类中，因此对扩展属性来说幕后字段是无效的。
// 这就是为什么扩展属性不能有初始化器。他们的行为只能由显式提供的 getters/setters 定义。
val <T> List<T>.penultIndex: Int get() = size - 2

//val <T> List<T>.asd = 1 // 错误：扩展属性不能有初始化器

fun main() {
    val list = mutableListOf(1, 2, 3, 4, 5, 6)
    list.swap(0, 2)     // “swap()”内部的“this”会保存“list”的值
    println(list)               // 打印: [3, 2, 1]
    println(list.penultIndex)

    // 调用的扩展函数只取决于参数的声明类型
    printFoo(TestEBase())       // 打印: TestEBase
    printFoo(TestESub())        // 打印: TestESub

    // 如果一个类定义有一个成员函数与一个扩展函数，而这两个函数又有相同的接收者类型、相同的名字，都适用给定的参数，这种情况总是取成员函数
    TestE().qwe()               // 打印: member qwe

    // 扩展函数重载同样名字但不同签名成员函数也完全可以
    TestE().qwe(123)          // 打印: extension qwe(123)

    // 可空接收者
    val asd: TestE? = null
    println(asd.toString())     // 打印: null

    // 伴生对象扩展
    // 伴生对象的其他普通成员，只需用类名作为限定符去调用他们
    TestEMyClass.foo()          // TestEMyClass.Companion.foo
}

fun <T> MutableList<T>.swap(index1: Int, index2: Int) {
    val tmp = this[index1] // “this”对应该列表
    this[index1] = this[index2]
    this[index2] = tmp
}

open class TestEBase

class TestESub : TestEBase()

fun TestEBase.foo(): String {
    return "TestEBase"
}

fun TestESub.foo() = "TestESub"

fun printFoo(c: TestEBase) {
    println(c.foo())
}

fun printFoo(d: TestESub) {
    println(d.foo())
}

class TestE {

    fun qwe() {
        println("member qwe")
    }
}

fun TestE.qwe() {
    println("extension qwe")
}

/**
 * 扩展函数重载同样名字但不同签名成员函数也完全可以
 */
fun TestE.qwe(i: Int) {
    println("extension qwe($i)")
}

fun Any?.toString(): String {
    if (this == null) return "null"
    // 空检测之后，“this”会自动转换为非空类型，所以下面的 toString()
    // 解析为 Any 类的成员函数
    return toString()
}

class TestEMyClass {
    companion object {}  // 将被称为 "Companion"
}

fun TestEMyClass.Companion.foo() {
    println("TestEMyClass.Companion.foo")
}