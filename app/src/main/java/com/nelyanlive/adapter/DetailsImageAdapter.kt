package com.nelyanlive.adapter

import android.app.Activity
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
import com.nelyanlive.modals.postDetails.Activityimage
import com.nelyanlive.utils.image_base_URl

class DetailsImageAdapter(activityDetailsActivity: FragmentActivity, datalist: ArrayList<Activityimage>) : RecyclerView.Adapter<Vh>() {
    var a: Activity
    var datalist: ArrayList<Activityimage>
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        val v = LayoutInflater.from(a).inflate(R.layout.row_detailsimg, parent, false)
        return Vh(v)
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        Log.d("serverImages", "------------------------------"+ datalist[position].images)
        Glide.with(a).asBitmap().load(image_base_URl+datalist.get(position).images).into(holder.img)

    }

    override fun getItemCount(): Int {
        return datalist.size
    }

    inner class Vh(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var img: ImageView
        var videoPic: ImageView? = null

        init {
            img = itemView.findViewById(R.id.detailsimg)
            // videoPic= itemView.findViewById(R.id.iv_videoicon);
        }
    }

    init {
        a = activityDetailsActivity
        this.datalist = datalist
    }
}