package com.nelyanlive.fullscreen

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.nelyanlive.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_full_screen.*

class FullScreen : AppCompatActivity() {

    var str = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_full_screen)


        if(intent.hasExtra("image"))
        {
            str = "http://3.13.214.27:1052/uploads/users/"+intent.getStringExtra("image")!!

            Picasso.get().load(str)
                .placeholder(
                    ContextCompat.getDrawable(
                        ivMainImage.context,
                        R.drawable.placeholder
                    )!!).into(ivMainImage)

        }

        if(intent.hasExtra("productImage"))
        {
            str = intent.getStringExtra("productImage")!!
            Glide.with(this).asBitmap().load(str).error(R.mipmap.no_image_placeholder).into(ivMainImage)

        }




        ivCross.setOnClickListener { onBackPressed() }

    }
}