package com.nelyan_live.ui

import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nelyan_live.HELPER.image
import com.nelyan_live.R
import com.nelyan_live.adapter.ChatAdapter

class Chat1Activity : image() {
    lateinit  var mContext: Context
    var ivBack: ImageView? = null
    var ivAttachment: ImageView? = null
    var chatAdapter: ChatAdapter? = null
    var recyclerview: RecyclerView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat1)
        mContext = this
        ivBack = findViewById(R.id.ivBack)
        ivBack!!.setOnClickListener(View.OnClickListener { finish() })
        ivAttachment = findViewById(R.id.ivAttachment)
        ivAttachment!!.setOnClickListener(View.OnClickListener { image("all") })
        recyclerview = findViewById(R.id.recyclerview)
        chatAdapter = ChatAdapter(mContext)
        recyclerview!!.setLayoutManager(LinearLayoutManager(mContext))
        recyclerview!!.setAdapter(chatAdapter)
    }

    override fun selectedImage(var1: Bitmap, var2: String) {
        ivAttachment!!.setImageBitmap(var1)
    }
}