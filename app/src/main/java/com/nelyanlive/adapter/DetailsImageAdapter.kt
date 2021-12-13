package com.nelyanlive.adapter

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nelyanlive.R
import com.nelyanlive.adapter.DetailsImageAdapter.Vh
import com.nelyanlive.fullscreen.FullScreen
import com.nelyanlive.modals.postDetails.Activityimage
import com.nelyanlive.utils.image_base_URl

class DetailsImageAdapter(
    activityDetailsActivity: FragmentActivity,
    var dataList: ArrayList<Activityimage>
) : RecyclerView.Adapter<Vh>() {
    var a: Activity = activityDetailsActivity
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        val v = LayoutInflater.from(a).inflate(R.layout.row_detailsimg, parent, false)
        return Vh(v)
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        Log.d("serverImages", "------------------------------" + dataList[position].images)
        Glide.with(a).asBitmap().load(image_base_URl + dataList[position].images).into(holder.img)
//        holder.img.setScaleType(ImageView.ScaleType.FIT_XY);
//        holder.img.setAdjustViewBounds(true)
        holder.img.setOnClickListener {
            (holder.img.context as Activity).startActivity(
                Intent(
                    holder.img.context,
                    FullScreen::class.java
                )
//                    .putExtra("productImage", image_base_URl + dataList[position].images)
                    .putExtra("productImage", image_base_URl + dataList)
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