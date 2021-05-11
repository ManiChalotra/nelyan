package com.nelyan_live.adapter

import android.app.TimePickerDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nelyan_live.R
import com.nelyan_live.modals.DayTimeModel
import com.nelyan_live.modals.ModelPOJO
import java.util.*

class DayTimeRepeatAdapter(var context: Context, var list: ArrayList<DayTimeModel>, var listner: OnDayTimeRecyclerViewItemClickListner) : RecyclerView.Adapter<DayTimeRepeatAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return  MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_day_time_add_more, parent, false), listner)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.initalize(list, position)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class MyViewHolder(itemView: View, var listner: OnDayTimeRecyclerViewItemClickListner)  : RecyclerView.ViewHolder(itemView) {

        var rvTime = itemView.findViewById(R.id.rvTime) as RecyclerView
        var tvAddDay = itemView.findViewById(R.id.tvAddDay) as TextView
        var tvAddTime = itemView.findViewById(R.id.tvAddTime) as TextView
        var tvMorningFromtime = itemView.findViewById(R.id.tv_mornning_fromtime) as TextView
        var tvMorningTotime = itemView.findViewById(R.id.tv_morning_totime) as TextView
        var tvEveningFromtime = itemView.findViewById(R.id.tv_evening_fromtime) as TextView
        var tvEveningTotime = itemView.findViewById(R.id.tv_evening_totime) as TextView
        var spinnerDayss = itemView.findViewById(R.id.spinner_dayss) as Spinner

        fun initalize(dayTimeModelArrayList: ArrayList<DayTimeModel>, position: Int) {
            if (position == dayTimeModelArrayList.size - 1) {
                tvAddDay.visibility = View.VISIBLE
            } else {
                tvAddDay.visibility = View.GONE
            }

            tvAddDay.setOnClickListener {
                listner.dayTimeAdd(list, position)
                //*notifyDataSetChanged();*//*
            }

            var name = ""
            // setting the data in spinner
            spinnerDayss.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View, pos: Int, id: Long) {
                    // your code here
                    name = parentView?.getItemAtPosition(pos).toString()
                    // context.toast(name)
                    list[position].selectedDay = name.toString()
                }
                override fun onNothingSelected(parentView: AdapterView<*>?) {
                    // your code here
                }
            })


            /*val timeRepeatAdapter =  TimeRepeatAdapter(context, dayTimeModelArrayList[position].selectTime)
            rvTime.adapter = timeRepeatAdapter
            rvTime.layoutManager = LinearLayoutManager(context)*/

            val days: MutableList<String?> = ArrayList()
            days.add("")
            days.add("Monday")
            days.add("Tuesday")
            days.add("Wednesday")
            days.add("Thursday")
            days.add("Friday")
            days.add("Saturday")
            days.add("Sunday")

            val arrayAdapter = ArrayAdapter(context, R.layout.customspinner, days )
            spinnerDayss.adapter = arrayAdapter
//            val spinnerPosition: Int = arrayAdapter.getPosition(list.get(position).selectedDay)
 //           spinnerDayss.setSelection(spinnerPosition)

            tvMorningFromtime.setOnClickListener {
                val mcurrentTime = Calendar.getInstance()
                val hour = mcurrentTime[Calendar.HOUR_OF_DAY]
                val minute = mcurrentTime[Calendar.MINUTE]
                val mTimePicker: TimePickerDialog
                mTimePicker = TimePickerDialog(context, TimePickerDialog.OnTimeSetListener { timePicker, selectedHour, selectedMinute ->
                    //  timeFrom.text = "$selectedHour:$selectedMinute"
                    tvMorningFromtime.setText(String.format("%02d:%02d", selectedHour, selectedMinute))
                    list[position].firstStarttime = "$selectedHour:$selectedMinute"
                }, hour, minute, true) //Yes 24 hour time
                mTimePicker.setTitle(context.getString(R.string.select_time))
                mTimePicker.show()
            }
            tvMorningTotime.setOnClickListener {
                val mcurrentTime = Calendar.getInstance()
                val hour = mcurrentTime[Calendar.HOUR_OF_DAY]
                val minute = mcurrentTime[Calendar.MINUTE]
                val mTimePicker: TimePickerDialog
                mTimePicker = TimePickerDialog(context, TimePickerDialog.OnTimeSetListener { timePicker, selectedHour, selectedMinute ->
                    //  timeFrom.text = "$selectedHour:$selectedMinute"
                    tvMorningTotime.setText(String.format("%02d:%02d", selectedHour, selectedMinute))
                    list[position].firstEndtime = "$selectedHour:$selectedMinute"
                }, hour, minute, true) //Yes 24 hour time
                mTimePicker.setTitle(context.getString(R.string.select_time))
                mTimePicker.show()

            }

            tvEveningFromtime.setOnClickListener {
                val mcurrentTime = Calendar.getInstance()
                val hour = mcurrentTime[Calendar.HOUR_OF_DAY]
                val minute = mcurrentTime[Calendar.MINUTE]
                val mTimePicker: TimePickerDialog
                mTimePicker = TimePickerDialog(context, TimePickerDialog.OnTimeSetListener { timePicker, selectedHour, selectedMinute ->
                    //  timeFrom.text = "$selectedHour:$selectedMinute"
                    tvEveningFromtime.setText(String.format("%02d:%02d", selectedHour, selectedMinute))
                    list[position].secondStarttime = "$selectedHour:$selectedMinute"
                }, hour, minute, true) //Yes 24 hour time
                mTimePicker.setTitle(context.getString(R.string.select_time))
                mTimePicker.show()

            }
            tvEveningTotime.setOnClickListener {
                val mcurrentTime = Calendar.getInstance()
                val hour = mcurrentTime[Calendar.HOUR_OF_DAY]
                val minute = mcurrentTime[Calendar.MINUTE]
                val mTimePicker: TimePickerDialog
                mTimePicker = TimePickerDialog(context, TimePickerDialog.OnTimeSetListener { timePicker, selectedHour, selectedMinute ->
                    //  timeFrom.text = "$selectedHour:$selectedMinute"
                    tvEveningTotime.setText(String.format("%02d:%02d", selectedHour, selectedMinute))
                    list[position].secondEndtime = "$selectedHour:$selectedMinute"
                }, hour, minute, true) //Yes 24 hour time
                mTimePicker.setTitle(context.getString(R.string.select_time))
                mTimePicker.show()

            }

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

        */

        init {

         }
    }

    interface OnDayTimeRecyclerViewItemClickListner {
        fun dayTimeAdd(list: ArrayList<DayTimeModel>, position: Int)
    }

}