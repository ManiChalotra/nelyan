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
import com.bumptech.glide.Glide
import com.nelyan_live.R
import com.nelyan_live.modals.HomeEventModel
import java.util.*

class MyEventAdapter(activity: FragmentActivity, internal var datalist: ArrayList<HomeEventModel>) : RecyclerView.Adapter<MyEventAdapter.Vh>() {
    var a: Activity
    var context: Context? = null
    var rl_1: RelativeLayout? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        val v = LayoutInflater.from(a).inflate(R.layout.row_myevent, parent, false)
        context = parent.context
        return Vh(v)
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {

        holder.bindMethod(datalist[position])

       /* holder.eimg.setImageResource(datalist[position].img)
        holder.ename.text = datalist[position].eventName
        holder.eloc.text = datalist[position].eventLocation
        holder.edate.text = datalist[position].eventDate
        holder.etime.text = datalist[position].eventTime
        holder.etimetwo.text = datalist[position].eventTimeSecond
        holder.eprice.text = datalist[position].eventPrice.toString()
        holder.edesc.text = datalist[position].eventDesc
        holder.itemView.setOnClickListener {
           context?.OpenActivity(ActivityDetailsActivity::class.java)
            // AppUtils.gotoFragment(a, ActivityDetailsFragment(), R.id.frame_container, true)

        }*/
    }

    override fun getItemCount(): Int {
        return datalist.size
    }

    inner class Vh(itemView: View) : RecyclerView.ViewHolder(itemView) {

        lateinit var ename: TextView
        lateinit var eloc: TextView
        lateinit var edate: TextView
        lateinit var etime: TextView
        lateinit var etimetwo: TextView
        lateinit var eprice: TextView
        lateinit var edesc: TextView
        lateinit var eimg: ImageView
       
        
        
        fun bindMethod(eventList: HomeEventModel) {
            ename = itemView.findViewById(R.id.tv_eventname)
            eloc = itemView.findViewById(R.id.tv_eventloc)
            edate = itemView.findViewById(R.id.tv_eventdate)
            etime = itemView.findViewById(R.id.tv_eventtime)
            etimetwo = itemView.findViewById(R.id.tv_eventtimetwo)
            eprice = itemView.findViewById(R.id.tv_eventprice)
            edesc = itemView.findViewById(R.id.tv_eventdesc)
            eimg = itemView.findViewById(R.id.iv_eventimg)

            Glide.with(context!!).load(eventList.img).error(R.drawable.places_autocomplete_toolbar_shadow).into(eimg)
            ename.text = eventList.eventName
            eloc.text = eventList.eventLocation
            edate.text = eventList.eventDate
            etime.text = eventList.eventTime
            etimetwo.text = eventList.eventTimeSecond
            eprice.text = eventList.eventPrice.toString()
            edesc.text = eventList.eventDesc
       
        }

    }

    init {
        a = activity
        this.datalist = datalist
    }
}