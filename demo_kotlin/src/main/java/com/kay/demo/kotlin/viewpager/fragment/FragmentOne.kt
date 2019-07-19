package com.kay.demo.kotlin.viewpager.fragment

import android.annotation.SuppressLint
import android.graphics.Color
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
class FragmentOne : Fragment() {

    private val fragmentTag = FragmentOne::class.java.simpleName
    private var content: String = fragmentTag
    private var position: Int? = -1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val bundle = arguments
        val text: String? = bundle?.getString("content")
        position = bundle?.getInt("position")
        if (text != null) {
            content = text + "position: $position"
        }
        val view = inflater.inflate(R.layout.layout_fragment, container, false)
        initView(view)
        return view
    }

    @SuppressLint("SetTextI18n")
    private fun initView(view: View?) {
        val tvSubView = view?.findViewById<TextView>(R.id.tvSubView)
        tvSubView?.text = "这是标签页 $content"

        val bgFragment = view?.findViewById<View>(R.id.bgFragment)
        when {
            position!! % 2 == 0 -> bgFragment?.setBackgroundColor(Color.RED)
            position!! % 3 == 0 -> bgFragment?.setBackgroundColor(Color.YELLOW)
            else -> bgFragment?.setBackgroundColor(Color.CYAN)
        }
    }
}