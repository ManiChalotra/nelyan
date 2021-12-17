package com.nelyanlive.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nelyanlive.R
import com.nelyanlive.adapter.DetailsImageAdapter.Vh
import com.nelyanlive.fullscreen.FullScreen
import com.nelyanlive.modals.postDetails.Activityimage
import com.nelyanlive.utils.image_base_URl

class DetailsImageAdapter(
    var context: Context,
    var dataList: ArrayList<Activityimage>,
    var Screentype: String
) : RecyclerView.Adapter<Vh>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        val v = LayoutInflater.from(context).inflate(R.layout.row_detailsimg, parent, false)
        return Vh(v)
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        Glide.with(context).asBitmap().load(dataList[position].images).into(holder.img)
        if(!Screentype.equals("Fullscreen"))
        {
            holder.img.setScaleType(ImageView.ScaleType.CENTER_CROP)
        }
//        holder.img.setScaleType(ImageView.ScaleType.FIT_XY);
//        holder.img.setAdjustViewBounds(true)
        holder.img.setOnClickListener {
            if(!Screentype.equals("Fullscreen"))
            {
                val listActivityimage = ArrayList<Activityimage>()
                listActivityimage.clear()
                var totalimages = 0
                var findpoz = 0

                for (i in 0 until dataList.size)
                {

                    totalimages++
                    Log.e("checkimage", "===" + dataList[i].images)
                    listActivityimage.add(
                        Activityimage(
                            0, dataList[i].id.toInt(),  dataList[i].images.toString(), 1
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
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class Vh(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var img: ImageView = itemView.findViewById(R.id.detailsimg)

    }

}