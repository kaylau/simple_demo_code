package com.kay.demo.kotlin.recycleview.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.kay.demo.kotlin.R
import com.kay.demo.kotlin.recycleview.RecycleViewItemData

/**
 * Date: 2019/7/18 上午10:42
 * Author: kay lau
 * Description:
 */
class NewsRecycleAdapter(
    context: Context?,
    list: MutableList<RecycleViewItemData>
) : RecyclerView.Adapter<NewsRecycleAdapter.ViewHolder>(), View.OnClickListener {

    override fun onClick(v: View?) {
        var position = 0
        val tag = v?.tag
        if (tag is Int) {
            position = tag.toInt()
        }
        mListener?.onItemClick(v, position)
    }


    fun addItemClickListener(listener: OnItemClickListener?) {
        this.mListener = listener
    }

    private val mContext = context
    private val mList = list
    private var mListener: OnItemClickListener? = null
    lateinit var viewGroup: ViewGroup

    override fun onCreateViewHolder(viewGroup: ViewGroup, p1: Int): ViewHolder {
        this.viewGroup = viewGroup
        val inflate =
            LayoutInflater.from(mContext).inflate(R.layout.item_recycle_layout, null, false)

        inflate?.setOnClickListener(this)

        return ViewHolder(inflate)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        if (viewGroup is ViewPager) {
//            viewGroup.id = mList[position].hashCode()
//        }
        val item = mList[position]
        val resId = item.resId
        holder.ivIcon.setImageResource(resId!!)
        holder.tvName.text = item.name
        holder.tvPhone.text = item.phone

        holder.itemView.tag = position
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivIcon: ImageView = itemView.findViewById(R.id.iv_icon)
        val tvName: TextView = itemView.findViewById(R.id.tv_name)
        val tvPhone: TextView = itemView.findViewById(R.id.tv_phone)
    }

    interface OnItemClickListener {

        fun onItemClick(v: View?, position: Int)
    }

}