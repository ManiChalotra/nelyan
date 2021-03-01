package com.nelyan.adapter

import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nelyan.R
import com.nelyan.modals.TimeModel
import java.util.*

class TimeRepeatAdapter(var context: Context, var arrayList: ArrayList<TimeModel?>) : RecyclerView.Adapter<TimeRepeatAdapter.TimeRepeatViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeRepeatViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_time_repart, parent, false)
        return TimeRepeatViewHolder(view)
    }

    override fun onBindViewHolder(holder: TimeRepeatViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    inner class TimeRepeatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var edClo: TextView
        var edClo1: TextView
        var tvAdd: TextView? = null
        fun bind(pos: Int) {
            edClo.text = arrayList[pos]!!.starttime
            edClo1.text = arrayList[pos]!!.endtime
            edClo.setOnClickListener {
                val mcurrentTime = Calendar.getInstance()
                val hour = mcurrentTime[Calendar.HOUR_OF_DAY]
                val minute = mcurrentTime[Calendar.MINUTE]
                val mTimePicker: TimePickerDialog
                mTimePicker = TimePickerDialog(context, OnTimeSetListener { timePicker, selectedHour, selectedMinute ->
                    edClo.text = "$selectedHour:$selectedMinute"
                    arrayList[pos]!!.starttime = edClo.text.toString()
                }, hour, minute, true) //Yes 24 hour time
                mTimePicker.setTitle("Select Time")
                mTimePicker.show()
            }
            edClo1.setOnClickListener {
                val mcurrentTime = Calendar.getInstance()
                val hour = mcurrentTime[Calendar.HOUR_OF_DAY]
                val minute = mcurrentTime[Calendar.MINUTE]
                val mTimePicker: TimePickerDialog
                mTimePicker = TimePickerDialog(context, OnTimeSetListener { timePicker, selectedHour, selectedMinute ->
                    edClo1.text = "$selectedHour:$selectedMinute"
                    arrayList[pos]!!.endtime = edClo1.text.toString()
                }, hour, minute, true) //Yes 24 hour time
                mTimePicker.setTitle("Select Time")
                mTimePicker.show()
            }
        }

        init {
            edClo = itemView.findViewById(R.id.edClo)
            edClo1 = itemView.findViewById(R.id.edClo1)
        }
    }

}