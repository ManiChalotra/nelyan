package com.nelyan_live.ui

import android.graphics.Bitmap
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.nelyan_live.HELPER.image
import com.nelyan_live.R
import com.nelyan_live.chat.ChatVM
import com.nelyan_live.databinding.ActivityChat1Binding

class Chat1Activity : image() {
    var ivBack: ImageView? = null
    var ivAttachment: ImageView? = null


    lateinit var activityChat1Binding: ActivityChat1Binding

    val chatVM: ChatVM by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityChat1Binding = DataBindingUtil.setContentView(this,R.layout.activity_chat1)
        activityChat1Binding.chatVM = chatVM


        activityChat1Binding.ivBack.setOnClickListener { onBackPressed() }

        ivAttachment = findViewById(R.id.ivAttachment)
        ivAttachment!!.setOnClickListener { image("all") }
        chatVM.rvChat =activityChat1Binding.rvChat

        if (intent.hasExtra("senderID")) {
            chatVM.senderID = intent.getStringExtra("senderID")!!
            chatVM.senderName.set(intent.getStringExtra("senderName")!!)
            chatVM.senderImage.set(intent.getStringExtra("senderImage")!!)
            chatVM.userId = intent.getStringExtra("userId")!!
        }
        chatVM.connectSocket(this)

    }

    override fun onBackPressed() {
        chatVM.disconnectSocket()
        super.onBackPressed()
    }
    override fun selectedImage(var1: Bitmap, var2: String) {
      //  ivAttachment!!.setImageBitmap(var1)
    }
}