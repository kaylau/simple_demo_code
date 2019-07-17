package com.kay.demo.kotlin.kt.test

/**
 * Date: 2019/7/17 上午9:33
 * Author: kay lau
 * Description: 对象表达式与对象声明
 */

fun main() {

    var clickCount = 0
    var enterCount = 0

    val testI = TestI()
    testI.addAdapter(object : TestIAdapter {
        override fun mouseClicked(s: String) {
            println(s)
            // 对象表达式中的代码可以访问来自包含它的作用域的变量
            // 与 Java 不同的是，不仅限于 final 或实际相当于 final 的变量
            clickCount++
        }

        override fun mouseEntered(i: Int) {
            println("i = $i")
            enterCount++
        }
    })
    testI.clicked("mouseClicked--->")
    testI.entered(6)

    testI.test()
}

interface TestIAdapter {

    fun mouseClicked(s: String)

    fun mouseEntered(i: Int)
}

class TestI {

    private lateinit var adapter: TestIAdapter

    fun addAdapter(adapter: TestIAdapter) {
        this.adapter = adapter
    }

    fun clicked(s: String) {
        adapter.mouseClicked(s)
    }

    fun entered(i: Int) {
        adapter.mouseEntered(i)
    }

    fun test() {
        // 如果我们只需要“一个对象而已”，并不需要特殊超类型，那么我们可以简单地写.
        val qwe = object {
            var x: Int = 8
            var y: String = "aad"
        }
        println("x = ${qwe.x}, y = ${qwe.y}")
    }

    // 私有函数，所以其返回类型是匿名对象类型
    private fun foo() = object {
        val x: String = "x"
    }

    // 公有函数，所以其返回类型是 Any
    fun publicFoo() = object {
        val x: String = "x"
    }

    fun bar() {
        val x1 = foo().x        // 私有函数没问题
//        val x2 = publicFoo().x       // 错误：公有函数,未能解析的引用“x”
    }
}


//==================================================================

open class TestIBaseClass(i: Int) {

    open val y: Int = i
}

interface TestIInterface {
    fun test()
}

// 如果超类型有一个构造函数，则必须传递适当的构造函数参数给它。
// 多个超类型可以由跟在冒号后面的逗号分隔的列表指定：
class TestISubClass : TestIBaseClass(3), TestIInterface {

    override fun test() {
        SingleClass.register("name qa", 22)

        CompanionClass.create()

        // 其自身所用的类的名称（不是另一个名称的限定符）可用作对该类的伴生对象 （无论是否命名）的引用：
//        val companionClass = CompanionClass
//        companionClass.create()

//        var companion = CompanionClass.Companion

    }
}

//==================================================================

/**
 * 单例模式
 * 对象声明: 对象声明不是一个表达式，不能用在赋值语句的右边。
 *
 * 调用 SingleClass.register("name qa", 22)
 *
 * 可以有超类型
 */
object SingleClass : TestIInterface {

    override fun test() {

    }

    lateinit var str: String

    fun register(name: String, pwd: Int) {

    }
}

/**
 * 伴生对象
 */
class CompanionClass {

//    companion object { }

    // 即使伴生对象的成员看起来像其他语言的静态成员，
    // 在运行时他们仍然是真实对象的实例成员，而且还可以实现接口
    val companionClass: FactoryInterface<CompanionClass> = CompanionClass

    companion object Factory : FactoryInterface<CompanionClass> {

        override fun cre(): CompanionClass {
            return CompanionClass()
        }

        fun create(): CompanionClass = CompanionClass()
    }
}

interface FactoryInterface<T> {

    fun cre(): T
}