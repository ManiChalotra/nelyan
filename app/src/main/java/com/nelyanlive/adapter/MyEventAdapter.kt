package com.nelyanlive.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nelyanlive.R
import com.nelyanlive.modals.HomeEventModel
import com.nelyanlive.ui.ActivityDetailsActivity
import com.nelyanlive.utils.OpenActivity
import com.nelyanlive.utils.image_base_URl
import java.util.*

class MyEventAdapter(activity: FragmentActivity, internal var datalist: ArrayList<HomeEventModel>,
                     internal var OnCLICK: OnEventItemClickListner) : RecyclerView.Adapter<MyEventAdapter.Vh>() {
    var a: Activity = activity
    var context: Context? = null
    var rl_1: RelativeLayout? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        val v = LayoutInflater.from(a).inflate(R.layout.row_myevent, parent, false)
        context = parent.context
        return Vh(v)
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {

        holder.bindMethod(datalist[position])
    }

    override fun getItemCount(): Int {
        return datalist.size
    }

    inner class Vh(itemView: View) : RecyclerView.ViewHolder(itemView) {

        lateinit var ename: TextView
        lateinit var eloc: TextView
        lateinit var eStartdate: TextView
        lateinit var eEnddate: TextView
        lateinit var etime: TextView
        lateinit var etimetwo: TextView
        lateinit var eprice: TextView
        lateinit var edesc: TextView
        lateinit var tv_eventage: TextView
        lateinit var eimg: ImageView
        lateinit var ivEventFav: ImageView
        lateinit var llEvent: LinearLayout

        fun bindMethod(eventList: HomeEventModel) {

            ename = itemView.findViewById(R.id.tv_eventname)
            llEvent = itemView.findViewById(R.id.llEvent)
            eloc = itemView.findViewById(R.id.tv_eventloc)
            eStartdate = itemView.findViewById(R.id.tv_eventStartdate)
            eEnddate = itemView.findViewById(R.id.tv_eventEnddate)
            etime = itemView.findViewById(R.id.tv_eventtime)
          //  etimetwo = itemView.findViewById(R.id.tv_eventtimetwo)
            eprice = itemView.findViewById(R.id.tv_eventprice)
            edesc = itemView.findViewById(R.id.tv_eventdesc)
            ivEventFav = itemView.findViewById(R.id.iv_event_fav)
            eimg = itemView.findViewById(R.id.iv_eventimg)
            tv_eventage = itemView.findViewById(R.id.tv_eventage)

            Glide.with(context!!).load(image_base_URl +eventList.img).error(R.mipmap.no_image_placeholder).into(eimg)

            ename.text = eventList.eventName
            eloc.text = eventList.eventLocation
            eStartdate.text = context!!.getString(R.string.startdate1)+" "+eventList.eventStartDate
            eEnddate.text = context!!.getString(R.string.end_date1)+" "+eventList.eventEndDate
            etime.text = context!!.getString(R.string.start_time1)+" "+eventList.eventStartTime+"  "+context!!.getString(R.string.end_time1)+" "+eventList.eventEndTime
            eprice.text = eventList.eventPrice
            edesc.text = eventList.eventDesc
            tv_eventage.text = "Min ${eventList.minAge} ${context!!.getString(R.string.years_masculinesmall)}  -  Max ${eventList.maxAge} ${context!!.getString(R.string.years_masculinesmall)}"

            llEvent.setOnClickListener {

                context?.OpenActivity(ActivityDetailsActivity::class.java) {
                    putString("activityId",eventList.activityId )
                    putString("categoryId","")
                    putString("lati", eventList.latitude)
                    putString("longi",eventList.longitude)
                }
            }
            ivEventFav.setOnClickListener {
                OnCLICK.onAddFavoriteClick(eventList.id, ivEventFav)
            }
            ivEventFav.setImageDrawable(ContextCompat.getDrawable(ivEventFav.context,if(eventList.isFav=="1"){R.drawable.heart}else{R.drawable.heart_purple}))
        }
    }

    interface OnEventItemClickListner {
        fun onAddFavoriteClick(eventID: String, ivFavourite: ImageView)

    }

}