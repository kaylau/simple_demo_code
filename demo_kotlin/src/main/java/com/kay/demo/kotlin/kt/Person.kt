package com.kay.demo.kotlin.kt

fun main() {
    var p = Person("a", 8)

}

/**
 * Date: 2019/7/12 上午9:37
 * Author: kay lau
 * Description:
 * 在 Kotlin 中的一个类可以有一个主构造函数以及一个或多个次构造函数。
 * 主构造函数是类头的一部分：它跟在类名（与可选的类型参数）后。
 * 如果主构造函数没有任何注解或者可见性修饰符，可以省略这个 constructor 关键字。
 */
open class Person constructor(name: String) {

    // 在实例初始化期间，初始化块按照它们出现在类体中的顺序执行，与属性初始化器交织在一起.
    val firstProperty = "First property: $name".also(::println)

    init {
        println("First initializer block that prints ${name}")
    }

    val secondProperty = "Second property: ${name.length}".also(::println)

    init {
        println("Second initializer block that prints ${name.length}")
    }

    // 主构造的参数可以在初始化块中使用。它们也可以在类体内声明的属性初始化器中使用.
    var nameU = name.toUpperCase().also(::println)

    constructor(firstName: String, lastName: String, age: Int) : this(lastName)

    constructor(name: String, age: Int) : this(name, "nickName", age)

    fun eat() {
        println("---> Person.eat")
    }

    open fun sleep() {
        println("---> Person.sleep")
    }
}