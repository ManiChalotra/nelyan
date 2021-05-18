package com.nelyan_live.ui

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import com.nelyan_live.HELPER.image
import com.nelyan_live.R

class ChatActivity : image() {
    var mContext: Context? = null
    var ivBack: ImageView? = null
    var ivMan: ImageView? = null
    var ivOn: ImageView? = null
    var ivOf: ImageView? = null
    var ivAttachment: ImageView? = null
    var btnRegulation: Button? = null
    var ll_1: LinearLayout? = null
    var ll_2: LinearLayout? = null
    var dialog: Dialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        mContext = this
        ivOn = findViewById(R.id.ivOn)
        ivOf = findViewById(R.id.ivOf)
        ivOn!!.setOnClickListener(View.OnClickListener {
            ivOf!!.setVisibility(View.VISIBLE)
            ivOn!!.setVisibility(View.GONE)
        })
        ivOf!!.setOnClickListener(View.OnClickListener {
            ivOf!!.setVisibility(View.GONE)
            ivOn!!.setVisibility(View.VISIBLE)
        })
        ivBack = findViewById(R.id.ivBack)
        ivBack!!.setOnClickListener(View.OnClickListener { finish() })
        ivAttachment = findViewById(R.id.ivAttachment)
        ivAttachment!!.setOnClickListener(View.OnClickListener { image("all") })
        btnRegulation = findViewById(R.id.btnRegulation)
        btnRegulation!!.setOnClickListener(View.OnClickListener {

        })

        ll_1 = findViewById(R.id.ll_public)
        ll_2 = findViewById(R.id.ll_privates)
        ll_1!!.setOnClickListener(View.OnClickListener { showDailog() })
        ll_2!!.setOnClickListener(View.OnClickListener { dailogDelete() })
    }

    fun showDailog() {
        dialog = Dialog(mContext!!)
        dialog!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog!!.setContentView(R.layout.alert_chat_flag)
        dialog!!.setCancelable(true)
        val btnSubmit: RelativeLayout
        btnSubmit = dialog!!.findViewById(R.id.btnSubmit)
        btnSubmit.setOnClickListener { //  mContext.startActivity(new Intent(mContext, HomeActivity.class));
            dialog!!.dismiss()
        }
        dialog!!.show()
    }

    fun dailogDelete() {
        dialog = Dialog(mContext!!)
        dialog!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog!!.setContentView(R.layout.alert_chat_delete)
        dialog!!.setCancelable(true)
        val rl_1: RelativeLayout
        rl_1 = dialog!!.findViewById(R.id.rl_1)
        rl_1.setOnClickListener { //  mContext.startActivity(new Intent(mContext, HomeActivity.class));
            dialog!!.dismiss()
        }
        dialog!!.show()
    }

    override fun selectedImage(var1: Bitmap, var2: String) {
        ivAttachment!!.setImageBitmap(var1)
    }
}