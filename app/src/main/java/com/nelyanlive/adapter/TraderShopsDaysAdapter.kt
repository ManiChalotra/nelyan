package com.nelyanlive.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nelyanlive.R
import com.nelyanlive.modals.traderPostDetails.TraderDaysTiming
import com.nelyanlive.ui.TraderPublishActivty
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class TraderShopsDaysAdapter(internal  var context: Context, internal  var activity: TraderPublishActivty, internal var datalist: ArrayList<TraderDaysTiming>) : RecyclerView.Adapter<TraderShopsDaysAdapter.Vh>() {
    var rl_1: RelativeLayout? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        val v = LayoutInflater.from(context).inflate(R.layout.item_traders_shop_days, parent, false)
        context = parent.context
        return Vh(v)
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        try {
            Log.e("asdfasdf","=======${datalist[position].day}")
            holder.bindMethod(datalist[position])

        }
        catch (e:Exception)
        {
            e.printStackTrace()
        }

     }

    override fun getItemCount(): Int {
        return datalist.size
    }

    inner class Vh(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private lateinit var tvDay: TextView
        lateinit var tvTimings: TextView

        fun bindMethod(traderDaysTiming: TraderDaysTiming) {
            tvDay = itemView.findViewById(R.id.tv_day)
            tvTimings = itemView.findViewById(R.id.tv_timings)
            tvDay.text = traderDaysTiming.day
            tvTimings.text = "${getTime(traderDaysTiming.startTime)} - ${getTime(traderDaysTiming.endTime)},${getTime(traderDaysTiming.secondStartTime)} - ${getTime(traderDaysTiming.secondEndTime)}"

        }

        private fun getTime(time: String): String {
            var time2 = time
            val orignalFormat: DateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
            val date: Date
            try {
                date = orignalFormat.parse(time2)!!
                time2 = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(date)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return time2
        }

    }
}