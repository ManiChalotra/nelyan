package com.nelyanlive.fullscreen

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.nelyanlive.R
import com.nelyanlive.adapter.DetailsImageAdapter
import com.nelyanlive.modals.postDetails.Activityimage
import kotlinx.android.synthetic.main.activity_full_screen.*
import java.util.*

class FullScreen : AppCompatActivity() {

    var str = ""
    var listActivityimage = ArrayList<Activityimage>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_full_screen)

        listActivityimage = intent.getSerializableExtra("imagearry") as ArrayList<Activityimage>

        val ad = DetailsImageAdapter(this, listActivityimage, "Fullscreen")
        rv_activties_images_full.getLayoutManager()!!.scrollToPosition(intent.getStringExtra("image")!!.toInt() - 1)

        rv_activties_images_full!!.adapter = ad
        indicator_full!!.attachToRecyclerView(rv_activties_images_full!!)

/*
        if (intent.hasExtra("image")) {
            str = from_admin_image_base_URl + intent.getStringExtra("image")!!
            Picasso.get().load(str)
                .placeholder(
                    ContextCompat.getDrawable(
                        ivMainImage.context,
                        R.drawable.placeholder)!!
                ).into(ivMainImage)
        }
*/

        if (intent.hasExtra("productImage")) {
            str = intent.getStringExtra("productImage")!!
            Glide.with(this).asBitmap().load(str).error(R.mipmap.no_image_placeholder)
                .into(ivMainImage)
            Log.d(FullScreen::class.java.name, "FullScreen_Str   " + str)
        }

        ivCross.setOnClickListener {
            onBackPressed()
        }
    }
}