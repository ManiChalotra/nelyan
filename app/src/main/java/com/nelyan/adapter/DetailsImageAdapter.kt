package com.nelyan.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.nelyan.R
import com.nelyan.adapter.DetailsImageAdapter.Vh
import com.nelyan.modals.DetailsImageModal
import java.util.*

class DetailsImageAdapter(activityDetailsActivity: FragmentActivity, datalist: ArrayList<DetailsImageModal>) : RecyclerView.Adapter<Vh>() {
    var a: Activity
    var datalist: ArrayList<DetailsImageModal>
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        val v = LayoutInflater.from(a).inflate(R.layout.row_detailsimg, parent, false)
        return Vh(v)
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.img.setImageResource(datalist[position].img)
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