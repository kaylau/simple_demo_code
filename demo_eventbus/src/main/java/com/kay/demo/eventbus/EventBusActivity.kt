package com.kay.demo.eventbus

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.activity_event_bus.*
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.toast

/**
 * Date: 2019/8/14 下午2:31
 * Author: kay lau
 * Description:
 */
class EventBusActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_bus)
        tvFinishActivity.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                publishContent()
                val intent = Intent(this@EventBusActivity, EventBusStickyActivity::class.java)
                startActivity(intent)
            }
        })
    }

    private fun publishContent() {
        val msg = tvEdt.text.toString()
//        EventBus.getDefault().post(MessageWrap.getInstance(msg))
        EventBus.getDefault().postSticky(MessageWrap.getInstance(msg))
        toast("发布信息...")
    }
}