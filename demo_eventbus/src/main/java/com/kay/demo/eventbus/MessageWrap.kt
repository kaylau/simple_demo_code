package com.kay.demo.eventbus

/**
 * Date: 2019/8/14 下午2:20
 * Author: kay lau
 * Description:
 */
class MessageWrap private constructor(val message: String) {

    companion object {

        fun getInstance(message: String): MessageWrap {
            return MessageWrap(message)
        }
    }
}
