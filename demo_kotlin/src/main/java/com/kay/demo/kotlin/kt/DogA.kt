package com.kay.demo.kotlin.kt

fun main() {

    DogA("zxc")
    DogA("qqw", 2)
    DogA("asd", 1, "xxwxx")
}

/**
 * Date: 2019/7/12 上午10:50
 * Author: kay lau
 * Description:
 * 如果派生类没有主构造函数，那么每个次构造函数必须使用 super 关键字初始化其基类型，
 * 或委托给另一个构造函数做到这一点。
 * 注意，在这种情况下，不同的次构造函数可以调用基类型的不同的构造函数：
 */
class DogA : BaseDog {

    constructor(name: String) : this(name, 3) {
        println("---------------DogA constructor1---------------")
    }

    constructor(name: String, age: Int) : super(name, age) {
        println("---------------DogA constructor2---------------")
    }

    constructor(name: String, age: Int, nName: String) : super(name, nName, age) {
        println("---------------DogA constructor3---------------")
    }


}