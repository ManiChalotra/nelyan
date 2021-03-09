package com.nelyan_live.ui

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nelyan_live.R
import com.nelyan_live.adapter.NotificationAdapter

class NotificationActivity : AppCompatActivity() {
    lateinit  var mContext: Context
    var ivBack: ImageView? = null
    var notificationAdapter: NotificationAdapter? = null
    var recyclerview: RecyclerView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)
        mContext = this
        ivBack = findViewById(R.id.ivBack)
        ivBack!!.setOnClickListener(View.OnClickListener { finish() })
        recyclerview = findViewById(R.id.recyclerview)
        notificationAdapter = NotificationAdapter(mContext)
        recyclerview!!.setLayoutManager(LinearLayoutManager(mContext))
        recyclerview!!.setAdapter(notificationAdapter)
    }
}