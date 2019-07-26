package com.kay.demo.kotlin.util

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import com.kay.demo.kotlin.R
import com.kay.demo.kotlin.recycleview.RecycleViewItemData

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

    fun loadListData(): ArrayList<RecycleViewItemData> {
        val itemList = arrayListOf<RecycleViewItemData>()
        val imgList = listOf(
            R.mipmap.img0,
            R.mipmap.img1,
            R.mipmap.img2,
            R.mipmap.img3,
            R.mipmap.img4,
            R.mipmap.img5,
            R.mipmap.img6,
            R.mipmap.img7,
            R.mipmap.img8,
            R.mipmap.img9,
            R.mipmap.img10
        )

        for (i: Int in 1..2) {
            for (index in 1..imgList.size) {
                if (index == 3) {
                    itemList.add(RecycleViewItemData())
                } else {
                    itemList.add(
                        RecycleViewItemData(
                            "name --> $index",
                            imgList[index - 1],
                            index.toString()
                        )
                    )
                }
            }
        }
        return itemList
    }

    fun addItem(type: String, i: Int): RecycleViewItemData {
        return RecycleViewItemData(
            "name --> ${type}的数据 $i",
            R.mipmap.ic_launcher,
            i.toString()
        )
    }
}