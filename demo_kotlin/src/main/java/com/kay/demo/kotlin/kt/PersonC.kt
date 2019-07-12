package com.kay.demo.kotlin.kt

import com.kay.demo.kotlin.kt.test.pp

fun main() {
    pp()
}
/**
 * Date: 2019/7/12 上午10:46
 * Author: kay lau
 * Description:
 * 如果派生类有一个主构造函数，其基类型可以（并且必须） 用基类的主构造函数参数就地初始化。
 */
class PersonC(name: String) : BasePerson(name) {

}