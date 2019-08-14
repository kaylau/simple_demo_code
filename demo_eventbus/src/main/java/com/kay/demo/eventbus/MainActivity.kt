package com.kay.demo.eventbus

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        EventBus.getDefault().register(this)  //事件的注册

        tvOpenActivity.visibility = View.VISIBLE
        tvOpenActivity.setOnClickListener {
            val intent = Intent(this@MainActivity, EventBusActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun handlerEvent(msg: MessageWrap) {
        tvMsg.text = msg.message
    }
}
