package com.kay.demo.kotlin.kt

import org.json.JSONObject

/**
 * Date: 2019/7/11 下午3:24
 * Author: kay lau
 * Description:
 */
interface HttpRequestCallback {

    fun onCallback(json: JSONObject?, retCode: Int?, errMsg: String?)
}