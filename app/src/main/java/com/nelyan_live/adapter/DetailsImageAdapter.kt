package com.nelyan_live.adapter

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nelyan_live.R
import com.nelyan_live.adapter.DetailsImageAdapter.Vh
import com.nelyan_live.modals.DetailsImageModal
import com.nelyan_live.modals.postDetails.Activityimage
import com.nelyan_live.utils.image_base_URl
import java.util.*
import kotlin.collections.ArrayList

class DetailsImageAdapter(activityDetailsActivity: FragmentActivity, datalist: ArrayList<Activityimage>) : RecyclerView.Adapter<Vh>() {
    var a: Activity
    var datalist: ArrayList<Activityimage>
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        val v = LayoutInflater.from(a).inflate(R.layout.row_detailsimg, parent, false)
        return Vh(v)
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        Log.d("serverImages", "------------------------------"+ datalist.get(position).images)
        Glide.with(a).asBitmap().load(image_base_URl+datalist.get(position).images).into(holder.img)
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
        a = activityDetailsActivity
        this.datalist = datalist
    }
}