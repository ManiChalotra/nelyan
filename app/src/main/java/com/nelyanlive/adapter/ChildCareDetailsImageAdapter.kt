package com.nelyanlive.adapter

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nelyanlive.R
import com.nelyanlive.modals.childcarePostdetails.ChildCareImage
import com.nelyanlive.ui.HomeChildCareDetailsActivity
import com.nelyanlive.utils.image_base_URl

class ChildCareDetailsImageAdapter(homeChildCareDetailsActivity: HomeChildCareDetailsActivity, datalist: ArrayList<ChildCareImage>) : RecyclerView.Adapter<ChildCareDetailsImageAdapter.Vh>() {
    var a: Activity
    var datalist: ArrayList<ChildCareImage>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        val v = LayoutInflater.from(a).inflate(R.layout.row_detailsimg, parent, false)
        return Vh(v)
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        Log.d("serverImages", "------------------------------"+ datalist.get(position).image)
        Glide.with(a).asBitmap().load(image_base_URl+datalist.get(position).image).into(holder.img)
        //holder.img.setImageResource(datalist[position].images.toString())
        /* if(position%2==1)
        {
            holder.videoPic.setVisibility(View.VISIBLE);
        }
        else{
            holder.videoPic.setVisibility(View.GONE);
        }*/
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
        a = homeChildCareDetailsActivity
        this.datalist = datalist
    }

}