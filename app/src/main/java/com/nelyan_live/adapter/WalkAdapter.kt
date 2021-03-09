package com.nelyan_live.adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.nelyan_live.AppUtils
import com.nelyan_live.R
import com.nelyan_live.fragments.ActivityDetailsFragment
import com.nelyan_live.modals.WalkthroughModal
import java.util.*

class WalkAdapter(activity: FragmentActivity, datalist: ArrayList<WalkthroughModal>) : RecyclerView.Adapter<WalkAdapter.Vh>() {
    var a: Activity
    var context: Context? = null
    var rl_1: RelativeLayout? = null
    var datalist: ArrayList<WalkthroughModal>
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        val v = LayoutInflater.from(a).inflate(R.layout.row_myevent, parent, false)
        return Vh(v)
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.img.setImageResource(datalist[position].imagslideid)
        holder.nic.text = datalist[position].tv_walk
        holder.big.text = datalist[position].tv_walkdesc
        holder.itemView.setOnClickListener { AppUtils.gotoFragment(a, ActivityDetailsFragment(), R.id.frame_container, false) }
    }

    override fun getItemCount(): Int {
        return datalist.size
    }

    inner class Vh(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var nic: TextView
        var big: TextView
        var img: ImageView

        init {
            nic = itemView.findViewById(R.id.tv_walk)
            big = itemView.findViewById(R.id.tv_walkdesc)
            img = itemView.findViewById(R.id.iv_eventimg)
        }
    }

    init {
        a = activity
        this.datalist = datalist
    }
}