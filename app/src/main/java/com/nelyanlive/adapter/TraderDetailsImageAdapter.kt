package com.nelyanlive.adapter

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nelyanlive.R
import com.nelyanlive.fullscreen.FullScreen
import com.nelyanlive.modals.postDetails.Activityimage
import com.nelyanlive.modals.traderPostDetails.Tradersimage
import com.nelyanlive.ui.TraderPublishActivty
import com.nelyanlive.utils.image_base_URl

class TraderDetailsImageAdapter(
    traderDetailsActivity: TraderPublishActivty,
    var dataList: ArrayList<Tradersimage>
) : RecyclerView.Adapter<TraderDetailsImageAdapter.Vh>() {
    var a: Activity = traderDetailsActivity

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        val v = LayoutInflater.from(a).inflate(R.layout.row_detailsimg, parent, false)
        return Vh(v)
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        Log.d("serverImages", "------------------------------" + dataList[position].images)
        Glide.with(a).asBitmap().load(image_base_URl + dataList[position].images).into(holder.img)

        holder.img.setOnClickListener {
            val listActivityimage = ArrayList<Activityimage>()
            listActivityimage.clear()
            var totalimages = 0
            var findpoz = 0

            for (i in 0 until dataList.size) {

                totalimages++
                Log.e(
                    "checkimage",
                    "===" + dataList[i].images + "   DatalistSize   " + dataList.size
                )
                listActivityimage.add(
                    Activityimage(
                        0, dataList[i].id.toInt(), image_base_URl + dataList[i].images.toString(), 1
                    )
                )

                if (dataList[i].images.equals(dataList[position].images)) // Image
                {
                    findpoz = totalimages
                }
            }

            (holder.img.context as Activity).startActivity(
                Intent(holder.img.context, FullScreen::class.java).putExtra(
                    "image", findpoz.toString()
                ).putExtra("imagearry", listActivityimage)
            )

        }


    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class Vh(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var img: ImageView = itemView.findViewById(R.id.detailsimg)

    }

}