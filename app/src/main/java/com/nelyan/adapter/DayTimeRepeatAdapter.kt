package com.nelyan.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nelyan.R
import com.nelyan.adapter.DayTimeRepeatAdapter.DayTimeRepeatViewHolder
import com.nelyan.modals.DayTimeModel
import java.util.*

class DayTimeRepeatAdapter(var context: Context, dayTimeModelArrayList: ArrayList<DayTimeModel>, dayTimeRepeatListener: DayTimeRepeatListener) : RecyclerView.Adapter<DayTimeRepeatViewHolder>() {
    var dayTimeModelArrayList: ArrayList<DayTimeModel>
    var dayTimeRepeatListener: DayTimeRepeatListener
    var Selectedmonth = HashMap<String, String>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayTimeRepeatViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_day_time_add_more, parent, false)
        return DayTimeRepeatViewHolder(view)
    }

    override fun onBindViewHolder(holder: DayTimeRepeatViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return dayTimeModelArrayList.size
    }

    inner class DayTimeRepeatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var orderby1: Spinner
        var rvTime: RecyclerView
        var tvAddDay: TextView
        var tvAddTime: TextView
        fun bind(pos: Int) {
            if (pos == dayTimeModelArrayList.size - 1) {
                tvAddDay.visibility = View.VISIBLE
            } else {
                tvAddDay.visibility = View.GONE
            }
            tvAddDay.setOnClickListener {
                dayTimeRepeatListener.dayTimeAdd(pos)
                /*notifyDataSetChanged();*/
            }
            tvAddTime.setOnClickListener { dayTimeRepeatListener.timeAdd(pos) }
            val days: MutableList<String?> = ArrayList()
            days.add("")
            days.add("Monday")
            days.add("Tuesday")
            days.add("Wednesday")
            days.add("Thursday")
            days.add("Friday")
            days.add("Saturday")
            days.add("Sunday")
            val arrayAdapter: ArrayAdapter<*> = ArrayAdapter<Any?>(context, R.layout.customspinner, days as List<Any?>)
            orderby1.adapter = arrayAdapter
            val timeRepeatAdapter: com.nelyan.adapter.TimeRepeatAdapter = com.nelyan.adapter.TimeRepeatAdapter(context, dayTimeModelArrayList[pos].selectTime)
            rvTime.adapter = timeRepeatAdapter
            rvTime.layoutManager = LinearLayoutManager(context)
            val modeAdaptercity = ArrayAdapter(context, R.layout.customspinner, days)
            orderby1.adapter = modeAdaptercity
            try {
                orderby1.setSelection(Selectedmonth[pos.toString()]!!.toInt())
            } catch (e: Exception) {
            }
            orderby1.onItemSelectedListener = object : OnItemSelectedListener {
                override fun onItemSelected(adapterView: AdapterView<*>?, view: View, jj: Int, l: Long) {
                    Selectedmonth[pos.toString()] = jj.toString()
                }

                override fun onNothingSelected(adapterView: AdapterView<*>?) {}
            }
        }

        init {
            orderby1 = itemView.findViewById(R.id.orderby1)
            rvTime = itemView.findViewById(R.id.rvTime)
            tvAddDay = itemView.findViewById(R.id.tvAddDay)
            tvAddTime = itemView.findViewById(R.id.tvAddTime)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    interface DayTimeRepeatListener {
        fun dayTimeAdd(pos: Int)
        fun timeAdd(pos: Int)
    }

    init {
        this.dayTimeModelArrayList = dayTimeModelArrayList
        this.dayTimeRepeatListener = dayTimeRepeatListener
    }


}