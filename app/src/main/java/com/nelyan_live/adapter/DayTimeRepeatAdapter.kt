package com.nelyan_live.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nelyan_live.R
import com.nelyan_live.modals.DayTimeModel
import java.util.*

class DayTimeRepeatAdapter(var context: Context, var list: ArrayList<DayTimeModel>) : RecyclerView.Adapter<DayTimeRepeatAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return  MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_day_time_add_more, parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.initalize(list, position)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun initalize(list: ArrayList<DayTimeModel>, position: Int) {

        }

        /*  fun bind(pos: Int) {
              if (pos == dayTimeModelArrayList.size - 1) {
                  tvAddDay.visibility = View.VISIBLE
              } else {
                  tvAddDay.visibility = View.GONE
              }
              tvAddDay.setOnClickListener {
                  dayTimeRepeatListener.dayTimeAdd(pos)
                  *//*notifyDataSetChanged();*//*
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
            val timeRepeatAdapter =  TimeRepeatAdapter(context, dayTimeModelArrayList[pos].selectTime)
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
        }*/
    }


}