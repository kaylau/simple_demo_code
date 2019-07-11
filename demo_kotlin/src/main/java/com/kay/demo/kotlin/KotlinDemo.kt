package com.kay.demo.kotlin

import com.kay.demo.kotlin.util.LogUtil

/**
 * Date: 2019/7/10 下午5:26
 * Author: kay lau
 * Description:
 */
class KotlinDemo {

    companion object {

        private const val LOGTAG = "KotlinDemo"
        const val TAG = "KotlinDemo"

        val pi = 3.1415926  // 不可重新赋值

        var x = 0           // 可重新赋值

        fun testVal() {
            LogUtil.e(LOGTAG, "**********************************")
            // 定义只读局部变量使用关键字 val 定义。只能为其赋值一次。
            val a = "qed asd"
            LogUtil.e(LOGTAG, " a = $a")

            // 可重新赋值的变量使用 var 关键字：
            var b = 0
            b += 9
            LogUtil.e(LOGTAG, " b = $b")

            LogUtil.e(LOGTAG, " pi = $pi")

            LogUtil.e(LOGTAG, " x = $x")
            x = 1 + 90
            LogUtil.e(LOGTAG, " x = $x")


            LogUtil.d(LOGTAG, "--------------------------字符串模板使用-------------------------")
            var asd = 1
            // 模板中的简单名称：
            val s1 = "asd is $asd"
            LogUtil.d(LOGTAG, " s1 = $s1")

            asd = 2
            // 模板中的任意表达式：
            val s2 = "${s1.replace("is", "was")}, but now is $asd"
            LogUtil.d(LOGTAG, " s2 = $s2")
            LogUtil.d(LOGTAG, "--------------------------字符串模板使用-------------------------")

        }

        /**
         * @param str: String? 可以接收空参数
         * @return Int?  可以返回null
         */
        fun parseInt(str: String?): Int? {

            if (str != null) {
                return str.toInt()
            }
            return null
        }

        /**
         * @param str: String 不可以接收空参数, 编译报错
         * @return Int
         */
        fun parseInt(str: String): Int {

            return str.toInt()
        }

        /**
         * is 运算符检测一个表达式是否某类型的一个实例。
         * 如果一个不可变的局部变量或属性已经判断出为某类型，
         * 那么检测后的分支中可以直接当作该类型使用，无需显式转换
         */
        fun getStringLength(obj: Any): Int? {

            LogUtil.d(LOGTAG, "obj: $obj")

            if (obj is String) {
                // `obj` 在该条件分支内自动转换成 `String`
                return obj.length
            }

            if (obj is Int) {
                return obj.toString().length
            }

            // 在离开类型检测分支后，`obj` 仍然是 `Any` 类型
            return null
        }

        fun asStr(obj: Any?): String? {

            LogUtil.e(LOGTAG, "obj: $obj")

            val str = obj as String?

            LogUtil.e(LOGTAG, "str: $str")

            return str
        }

        fun asStrs(obj: Any): String {

            LogUtil.e(LOGTAG, "obj: $obj")

            val str = obj as String

            LogUtil.e(LOGTAG, "str: $str")

            return str
        }

        fun testFor() {
            LogUtil.d(LOGTAG, "--------------------------使用 for 循环-------------------------")
            val items = listOf("apple", "banana", "kiwifruit")

            for (item in items) {

                LogUtil.d(LOGTAG, "item: $item")
            }

            for (index in items.indices) {
                LogUtil.i(LOGTAG, "item at $index is ${items[index]}")
            }
            LogUtil.d(LOGTAG, "--------------------------使用 for 循环-------------------------")
        }

        fun testWhile() {
            LogUtil.e(LOGTAG, "--------------------------使用 while 循环-------------------------")
            val items = listOf("apple", "banana", "kiwifruit", "orange")
            var index = 0
            while (index < items.size) {
                LogUtil.e(LOGTAG, "item at $index is ${items[index]}")
                index++
            }
            LogUtil.e(LOGTAG, "--------------------------使用 while 循环-------------------------")
        }

        fun testWhen(i: Int) {
            LogUtil.d(LOGTAG, "--------------------------使用 when-------------------------")
            LogUtil.i(LOGTAG, "参数为: $i")
            // 作为一个表达式使用，则必须有 else 分支, 除非编译器检测到全部覆盖的情况
            when (i) {
                // 可以用任意表达式（而不只是常量）作为分支条件
                parseInt("123987289") -> LogUtil.d(LOGTAG, "parseInt: $i")

                // 用来取代 if-else if链
                1, 3 -> LogUtil.d(LOGTAG, "i == 1 或者 3")

                2 -> LogUtil.d(LOGTAG, "i == 2")

                in 5..10 -> LogUtil.d(LOGTAG, "i is in the range")

                else -> LogUtil.d(LOGTAG, "else i: $i")

            }
            LogUtil.d(LOGTAG, "--------------------------使用 when-------------------------")

        }

        fun bb(): Boolean {
            return false
        }

        fun hasPrefix(x: Any) = when (x) {
            is String -> x.startsWith("prefix")
            else -> false
        }

        fun testIn() {
            LogUtil.e(LOGTAG, "--------------------------使用区间（range）-------------------------")
            // 使用 in 运算符来检测某个数字是否在指定区间内
            val x = 10
            val y = 9
            if (x in 1..y + 1) {
                LogUtil.e(LOGTAG, "fits in range")
            }

            // 检测某个数字是否在指定区间外
            val list = listOf("a", "b", "c")

            if (-1 !in 0..list.lastIndex) {
                LogUtil.e(LOGTAG, "-1 is out of range")
            }
            if (list.size !in list.indices) {
                LogUtil.e(LOGTAG, "list size is out of valid list indices range, too")
            }

            LogUtil.i(LOGTAG, "---------------------------------------------------")

            // 区间迭代
            for (x in 1..5) {
                LogUtil.e(LOGTAG, "区间迭代x: $x")
            }

            LogUtil.i(LOGTAG, "---------------------------------------------------")

            // 数列迭代
            for (x in 1..10 step 2) {
                LogUtil.e(LOGTAG, "数列迭代x: $x")
            }

            LogUtil.i(LOGTAG, "---------------------------------------------------")

            for (y in 9 downTo 0 step 3) {
                LogUtil.e(LOGTAG, "数列迭代y: $y")
            }

            LogUtil.e(LOGTAG, "--------------------------使用区间（range）-------------------------")
        }

        fun testList() {
            LogUtil.d(LOGTAG, "--------------------------使用List-------------------------")
            val fruits = listOf("banana", "avocado", "apple", "kiwifruit")
            for (item in fruits) {
                LogUtil.d(LOGTAG, item)
            }
            LogUtil.i(LOGTAG, "---------------------------------------------------")

            when {
                "orange" in fruits -> LogUtil.d(LOGTAG, "juicy")
                "apple" in fruits -> LogUtil.d(LOGTAG, "apple is fine too")
            }
            LogUtil.i(LOGTAG, "---------------------------------------------------")
            fruits.filter { it.startsWith("a") }
                    .sortedBy { it }
                    .map { it.toUpperCase() }
                    .forEach { LogUtil.d(LOGTAG, it) }
            LogUtil.d(LOGTAG, "--------------------------使用List-------------------------")
        }
    }

}