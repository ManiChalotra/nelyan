package com.nelyanlive.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nelyanlive.R
import com.nelyanlive.modals.ActivitiesEventsDaysModel
import com.nelyanlive.ui.ActivityDetailsActivity

class ActivitiesEventsDaysAdapter(internal  var context: Context, internal  var activity: ActivityDetailsActivity, internal var datalist: ArrayList<ActivitiesEventsDaysModel>) : RecyclerView.Adapter<ActivitiesEventsDaysAdapter.Vh>() {
    var rl_1: RelativeLayout? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        val v = LayoutInflater.from(context).inflate(R.layout.row_activtydetail_days, parent, false)
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

        fun bindMethod(activitiesEventsDaysList: ActivitiesEventsDaysModel) {
            tvDay = itemView.findViewById(R.id.tv_day)
            tvDay.text = activitiesEventsDaysList.daysName
           
        }
    }
}