package com.nelyan_live.adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nelyan_live.R
import com.nelyan_live.modals.ActivitiesEventsDaysModel
import com.nelyan_live.modals.homeSectorization.GetPrivate
import com.nelyan_live.modals.traderPostDetails.TraderDaysTiming
import com.nelyan_live.ui.ActivityDetailsActivity
import com.nelyan_live.ui.TraderPublishActivty
import com.nelyan_live.utils.from_admin_image_base_URl
/*import com.nelyan_live.utils.image_url_local*/
import kotlin.collections.ArrayList

class TraderShopsDaysAdapter(internal  var context: Context, internal  var activity: TraderPublishActivty, internal var datalist: ArrayList<TraderDaysTiming>) : RecyclerView.Adapter<TraderShopsDaysAdapter.Vh>() {
    var rl_1: RelativeLayout? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        val v = LayoutInflater.from(context).inflate(R.layout.item_traders_shop_days, parent, false)
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
        lateinit var tvDay: TextView
        lateinit var tvTimings: TextView

        fun bindMethod(traderDaysTiming: TraderDaysTiming) {
            tvDay = itemView.findViewById(R.id.tv_day)
            tvTimings = itemView.findViewById(R.id.tv_timings)
            tvDay.text = traderDaysTiming.day
            tvTimings.text = traderDaysTiming.startTime+"-"+traderDaysTiming.endTime+", "+traderDaysTiming.secondStartTime+"-"+traderDaysTiming.secondEndTime

        }

    }

    init {
      }
}