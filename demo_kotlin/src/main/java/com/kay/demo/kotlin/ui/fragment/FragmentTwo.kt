package com.kay.demo.kotlin.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.kay.demo.kotlin.R

/**
 * Date: 2019/7/18 下午6:30
 * Author: kay lau
 * Description:
 */
class FragmentTwo : Fragment() {

    private val fragmentTag = FragmentTwo::class.java.simpleName

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.layout_fragment, container, false)
        initView(view)
        return view
    }

    @SuppressLint("SetTextI18n")
    private fun initView(view: View?) {
        val tvSubView = view?.findViewById<TextView>(R.id.tvSubView)
        tvSubView?.text = "这是标签页 $fragmentTag"
    }
}