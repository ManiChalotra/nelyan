package com.nelyan.ui

import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import com.nelyan.HELPER.image
import com.nelyan.R
import de.hdodenhof.circleimageview.CircleImageView

class EditProfileActivity : image() {
    var mContext: Context? = null
    var ivBack: ImageView? = null
    var iv_uploader: ImageView? = null
    var ivImg: ImageView? = null
    var btnSave: Button? = null
    var iv_profile: CircleImageView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        mContext = this
        ivBack = findViewById(R.id.ivBack)
        ivBack!!.setOnClickListener(View.OnClickListener { finish() })
        btnSave = findViewById(R.id.btnSave)
        btnSave!!.setOnClickListener(View.OnClickListener { finish() })
        ivImg = findViewById(R.id.iv_profile)
        iv_profile = findViewById(R.id.iv_profile)
        iv_profile!!.setOnClickListener(View.OnClickListener { image("all") })
    }

    override fun selectedImage(var1: Bitmap, var2: String) {
        ivImg!!.setImageBitmap(var1)
    }
}