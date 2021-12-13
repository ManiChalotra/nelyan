package com.nelyanlive.fullscreen

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.nelyanlive.R
import com.nelyanlive.utils.from_admin_image_base_URl
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_full_screen.*

class FullScreen : AppCompatActivity() {

    var str = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_full_screen)

        if (intent.hasExtra("image")) {
            str = from_admin_image_base_URl + intent.getStringExtra("image")!!

            Picasso.get().load(str)
                .placeholder(
                    ContextCompat.getDrawable(
                        ivMainImage.context,
                        R.drawable.placeholder
                    )!!
                ).into(ivMainImage)
        }

        if (intent.hasExtra("productImage")) {
            str = intent.getStringExtra("productImage")!!
            Glide.with(this).asBitmap().load(str).error(R.mipmap.no_image_placeholder).into(ivMainImage)
            Log.d(FullScreen::class.java.name, "FullScreen_Str   " + str)
        }

        ivCross.setOnClickListener { onBackPressed() }
    }
}