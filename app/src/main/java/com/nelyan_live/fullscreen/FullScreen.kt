package com.nelyan_live.fullscreen

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.nelyan_live.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_full_screen.*

class FullScreen : AppCompatActivity() {

    var str = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_full_screen)


        if(intent.hasExtra("image"))
        {
            str = intent.getStringExtra("image")!!
        }

        Picasso.get().load("http://3.13.214.27:1052/uploads/users/$str")
            .placeholder(
                ContextCompat.getDrawable(
                    ivMainImage.context,
                R.drawable.placeholder
            )!!).into(ivMainImage)

        ivCross.setOnClickListener { onBackPressed() }

    }
}