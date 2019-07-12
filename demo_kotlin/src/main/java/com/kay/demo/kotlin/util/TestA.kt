package com.kay.demo.kotlin.util

import com.kay.demo.kotlin.kt.Person
import com.kay.demo.kotlin.kt.PersonA


fun main() {
    Person("hello world")
    val personA = PersonA("hi", "hello", 3)
//        personA.age = 9
    val age = personA.age
    "age: $age".also(::println)

}


/**
 * Date: 2019/7/12 上午9:53
 * Author: kay lau
 * Description:
 */
class TestA {


}