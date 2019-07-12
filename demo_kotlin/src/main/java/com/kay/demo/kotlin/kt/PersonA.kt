package com.kay.demo.kotlin.kt


fun main() {
    var personA = PersonA("hi hi", "hao ya", 6)

    println("personA.fName: ${personA.firstName.capitalize()}")
    println("personA.lName: ${personA.lastName.toUpperCase()}")
    println("personA.age: ${personA.age}")
    personA.age = 8
    println("personA.len: ${personA.len}")
    println("personA.age: ${personA.age}")

    println("=========================================================")

    personA.Baz().g()

}

/**
 * Date: 2019/7/12 上午9:50
 * Author: kay lau
 * Description:
 * 主构造函数: 参数类型var可被重新赋值
 *
 */
class PersonA(val firstName: String, val lastName: String, var age: Int) : Person("pp") {

    var len = firstName.length + lastName.length

    override fun sleep() {
        println("---> PersonA.sleep")
    }

    inner class Baz {
        fun g() {
            super@PersonA.sleep()
            println(super@PersonA.firstProperty)
        }
    }


}

open class Foo {
    open fun f() {
        println("Foo.f()")
    }

    open val x: Int get() = 1
}

class Bar : Foo() {
    override fun f() { /* …… */
    }

    override val x: Int get() = 0

    inner class Baz {
        fun g() {
            super@Bar.f() // 调用 Foo 实现的 f()
            println(super@Bar.x) // 使用 Foo 实现的 x 的 getter
        }
    }
}