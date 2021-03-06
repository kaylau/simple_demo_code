package com.kay.demo.kotlin.kt

/**
 * Date: 2019/7/12 上午10:48
 * Author: kay lau
 * Description:
 */
open class BaseDog {

    constructor()

    constructor(name: String, age: Int) : this(name, "--x", age) {
        println("BaseDog name=$name age=$age")
    }

    constructor(name: String, nickName: String, age: Int) {
        println("BaseDog name=$name nickName=$nickName, age=$age")
    }


    open fun watch() {
        println("fun ----------> BaseDog watch")
    }

    open var age = 2

    open var x: Int = 900
}
