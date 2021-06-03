package com.nelyanlive.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.nelyanlive.R
import com.nelyanlive.modals.DetailsTimeModal
import java.util.*

class DetailsTimeAdapter(activityDetailsActivity: FragmentActivity, datalisttime: ArrayList<DetailsTimeModal>) : RecyclerView.Adapter<DetailsTimeAdapter.Vh>() {
    var a: Activity
    var datalisttime: ArrayList<DetailsTimeModal>
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        val v = LayoutInflater.from(a).inflate(R.layout.row_activtydetail_time, parent, false)
        return Vh(v)
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.t1.text = datalisttime[position].fromTime
        holder.t2.text = datalisttime[position].fromYears + a.getString(R.string.years)
        holder.t3.text = datalisttime[position].toTime
        holder.t4.text = datalisttime[position].toYears + a.getString(R.string.years)
    }

    override fun getItemCount(): Int {
        return datalisttime.size
    }

    inner class Vh(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var t1: TextView
        var t2: TextView
        var t3: TextView
        var t4: TextView

        init {
            t1 = itemView.findViewById(R.id.text1)
            t2 = itemView.findViewById(R.id.text2)
            t3 = itemView.findViewById(R.id.text3)
            t4 = itemView.findViewById(R.id.text4)
        }
    }

    init {
        a = activityDetailsActivity
        this.datalisttime = datalisttime
    }
}