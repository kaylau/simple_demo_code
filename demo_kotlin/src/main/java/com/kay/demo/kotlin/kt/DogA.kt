package com.kay.demo.kotlin.kt

fun main() {

    var baseDog = BaseDog()
    println("baseDog.x=${baseDog.x}")

//    DogA("zxc")
//    DogA("qqw", 2)
//    DogA("asd", 1, "xxwxx")
    val dogA = DogA()
//    dogA.watch()
    println("dogA.x=${dogA.x}")
    println("dogA.age=${dogA.age}")

}

/**
 * Date: 2019/7/12 上午10:50
 * Author: kay lau
 * Description:
 * 如果派生类没有主构造函数，那么每个次构造函数必须使用 super 关键字初始化其基类型，
 * 或委托给另一个构造函数做到这一点。
 * 注意，在这种情况下，不同的次构造函数可以调用基类型的不同的构造函数：
 */
open class DogA : BaseDog {

    constructor() : super()

    constructor(name: String) : this(name, 3) {
        println("---------------DogA constructor1---------------")
    }

    constructor(name: String, age: Int) : super(name, age) {
        println("---------------DogA constructor2---------------")
    }

    constructor(name: String, age: Int, nName: String) : super(name, nName, age) {
        println("---------------DogA constructor3---------------")
    }

    /**
     * 禁止再次覆盖，使用 final 关键字.
     */
    final override fun watch() {
        super.watch()
        println("fun ----------> DogA watch")
    }

    override var x: Int = 9

    override var age: Int = 8

}