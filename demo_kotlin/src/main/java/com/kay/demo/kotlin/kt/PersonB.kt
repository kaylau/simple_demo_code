package com.kay.demo.kotlin.kt

fun main() {

    PersonB(2)
    PersonB("hi", 0, 9)
    PersonB()
}

/**
 * Date: 2019/7/12 上午10:20
 * Author: kay lau
 * Description:
 */
class PersonB {

    // 请注意，初始化块中的代码实际上会成为主构造函数的一部分。
    // 委托给主构造函数会作为次构造函数的第一条语句，
    // 因此所有初始化块中的代码都会在次构造函数体之前执行。
    // 即使该类没有主构造函数，这种委托仍会隐式发生，并且仍会执行初始化块.

    init {
        println("--------------------Init block--------------------")
    }

    constructor(name: String = "hello", i: Int = 8, age: Int = 6) : this(age, name) {
        println("Constructor --> i=$i --> name=$name --> age=$age")
    }

    constructor(age: Int) : this("xxx", 1, age) {
        println("constructor age=$age")
    }

    constructor(age: Int, name: String) {
        println("constructor age=$age, name=$name")
    }

}