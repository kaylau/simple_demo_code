package com.kay.demo.kotlin.data

import com.kay.demo.kotlin.R

/**
 * Date: 2019/7/18 上午10:01
 * Author: kay lau
 * Description:
 */
data class ItemData(
    var name: String = "name",
    var resId: Int? = R.mipmap.ic_launcher_round,
    var phone: String = "xxx1234560"
)