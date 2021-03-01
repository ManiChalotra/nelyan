package com.nelyan.adapter

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
import com.nelyan.AppUtils
import com.nelyan.R
import com.nelyan.fragments.ActivityDetailsFragment
import com.nelyan.modals.EventModel
import java.util.*

class MyEventAdapter(activity: FragmentActivity, datalist: ArrayList<EventModel>) : RecyclerView.Adapter<MyEventAdapter.Vh>() {
    var a: Activity
    var context: Context? = null
    var rl_1: RelativeLayout? = null
    var datalist: ArrayList<EventModel>
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        val v = LayoutInflater.from(a).inflate(R.layout.row_myevent, parent, false)
        return Vh(v)
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.eimg.setImageResource(datalist[position].img)
        holder.ename.text = datalist[position].eventName
        holder.eloc.text = datalist[position].eventLocation
        holder.edate.text = datalist[position].eventDate
        holder.etime.text = datalist[position].eventTime
        holder.etimetwo.text = datalist[position].eventTimeSecond
        holder.eprice.text = datalist[position].eventPrice.toString()
        holder.edesc.text = datalist[position].eventDesc
        holder.itemView.setOnClickListener { AppUtils.gotoFragment(a, ActivityDetailsFragment(), R.id.frame_container, false) }
    }

    override fun getItemCount(): Int {
        return datalist.size
    }

    inner class Vh(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var ename: TextView
        var eloc: TextView
        var edate: TextView
        var etime: TextView
        var etimetwo: TextView
        var eprice: TextView
        var edesc: TextView
        var eimg: ImageView

        init {
            ename = itemView.findViewById(R.id.tv_eventname)
            eloc = itemView.findViewById(R.id.tv_eventloc)
            edate = itemView.findViewById(R.id.tv_eventdate)
            etime = itemView.findViewById(R.id.tv_eventtime)
            etimetwo = itemView.findViewById(R.id.tv_eventtimetwo)
            eprice = itemView.findViewById(R.id.tv_eventprice)
            edesc = itemView.findViewById(R.id.tv_eventdesc)
            eimg = itemView.findViewById(R.id.iv_eventimg)
        }
    }

    init {
        a = activity
        this.datalist = datalist
    }
}