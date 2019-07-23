package com.kay.demo.kotlin.recycleview.fragments

import android.support.v4.app.Fragment

/**
 * Date: 2019/7/23 下午2:19
 * Author: kay lau
 * Description:
 */
abstract class BaseFragment : Fragment() {

    abstract fun onStopSliding()

    abstract fun onStartSliding()
}