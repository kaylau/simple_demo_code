package com.kay.demo.kotlin.util

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.PixelFormat
import android.graphics.drawable.Drawable

/**
 * Date: 2019/7/18 下午5:52
 * Author: kay lau
 * Description:
 */

object ToolUtil {

    fun getBitmapFormDrawable(drawable: Drawable): Bitmap {
        val bitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth, drawable.intrinsicHeight,
            Bitmap.Config.RGB_565
        )
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
        drawable.draw(canvas)

        return bitmap
    }
}