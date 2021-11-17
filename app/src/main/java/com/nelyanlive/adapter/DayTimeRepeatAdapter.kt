package com.nelyanlive.adapter

import android.app.TimePickerDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nelyanlive.R
import com.nelyanlive.modals.DayTimeModel
import java.util.*

class DayTimeRepeatAdapter(var context: Context, var list: ArrayList<DayTimeModel>, var listner: OnDayTimeRecyclerViewItemClickListner) : RecyclerView.Adapter<DayTimeRepeatAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_day_time_add_more, parent, false), listner)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.initalize(list, position)
    }

    override fun getItemCount(): Int {
        return list.size
    }


    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }


    inner class MyViewHolder(itemView: View, var listner: OnDayTimeRecyclerViewItemClickListner) : RecyclerView.ViewHolder(itemView) {

        var tvAddDay = itemView.findViewById(R.id.tvAddDay) as TextView
        var tvMorningFromtime = itemView.findViewById(R.id.tv_mornning_fromtime) as TextView
        var tvMorningTotime = itemView.findViewById(R.id.tv_morning_totime) as TextView
        var tvEveningFromtime = itemView.findViewById(R.id.tv_evening_fromtime) as TextView
        var tvEveningTotime = itemView.findViewById(R.id.tv_evening_totime) as TextView
        var spinnerDayss = itemView.findViewById(R.id.spinner_dayss) as Spinner


        fun initalize(dayTimeModelArrayList: ArrayList<DayTimeModel>, position: Int) {

            tvMorningFromtime.text = list[position].firstStarttime
            tvMorningTotime.text = list[position].firstEndtime
            tvEveningFromtime.text = list[position].secondStarttime
            tvEveningTotime.text = list[position].secondEndtime

            if (position == dayTimeModelArrayList.size - 1) {
                tvAddDay.visibility = View.VISIBLE
            } else {
                tvAddDay.visibility = View.GONE
            }

            tvAddDay.setOnClickListener {
                listner.dayTimeAdd(list, position)
            }

            var name = ""
            spinnerDayss.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View, pos: Int, id: Long) {
                    name = parentView?.getItemAtPosition(pos).toString()
                    list[position].selectedDay = name
                }

                override fun onNothingSelected(parentView: AdapterView<*>?) {
                }
            }


            val days: MutableList<String?> = ArrayList()
            days.add("")
            days.add(context.getString(R.string.monday))
            days.add(context.getString(R.string.tuesday))
            days.add(context.getString(R.string.wednesday))
            days.add(context.getString(R.string.thursday))
            days.add(context.getString(R.string.friday))
            days.add(context.getString(R.string.saturday))
            days.add(context.getString(R.string.sunday))

            val arrayAdapter = ArrayAdapter(context, R.layout.customspinner, days)
            spinnerDayss.adapter = arrayAdapter
            spinnerDayss.setSelection(arrayAdapter.getPosition(list[position].selectedDay))

            tvMorningFromtime.setOnClickListener {
                val mcurrentTime = Calendar.getInstance()
                val hour = mcurrentTime[Calendar.HOUR_OF_DAY]
                val minute = mcurrentTime[Calendar.MINUTE]
                val mTimePicker = TimePickerDialog(context, { timePicker, selectedHour, selectedMinute ->
                    tvMorningFromtime.text = String.format("%02d:%02d", selectedHour, selectedMinute)
                    list[position].firstStarttime = "$selectedHour:$selectedMinute"
                }, hour, minute, true)
                mTimePicker.setTitle(context.getString(R.string.select_time))
                mTimePicker.show()
            }
            tvMorningTotime.setOnClickListener {
                val mcurrentTime = Calendar.getInstance()
                val hour = mcurrentTime[Calendar.HOUR_OF_DAY]
                val minute = mcurrentTime[Calendar.MINUTE]
                val mTimePicker = TimePickerDialog(context, { timePicker, selectedHour, selectedMinute ->
                    tvMorningTotime.text = String.format("%02d:%02d", selectedHour, selectedMinute)
                    list[position].firstEndtime = "$selectedHour:$selectedMinute"
                }, hour, minute, true) //Yes 24 hour time
                mTimePicker.setTitle(context.getString(R.string.select_time))
                mTimePicker.show()

            }

            tvEveningFromtime.setOnClickListener {
                val mcurrentTime = Calendar.getInstance()
                val hour = mcurrentTime[Calendar.HOUR_OF_DAY]
                val minute = mcurrentTime[Calendar.MINUTE]
                val mTimePicker = TimePickerDialog(context, { timePicker, selectedHour, selectedMinute ->
                    tvEveningFromtime.text = String.format("%02d:%02d", selectedHour, selectedMinute)
                    list[position].secondStarttime = "$selectedHour:$selectedMinute"
                }, hour, minute, true)
                mTimePicker.setTitle(context.getString(R.string.select_time))
                mTimePicker.show()

            }
            tvEveningTotime.setOnClickListener {
                val mcurrentTime = Calendar.getInstance()
                val hour = mcurrentTime[Calendar.HOUR_OF_DAY]
                val minute = mcurrentTime[Calendar.MINUTE]
                val mTimePicker = TimePickerDialog(context, { timePicker, selectedHour, selectedMinute ->
                    tvEveningTotime.text = String.format("%02d:%02d", selectedHour, selectedMinute)
                    list[position].secondEndtime = "$selectedHour:$selectedMinute"
                }, hour, minute, true)
                mTimePicker.setTitle(context.getString(R.string.select_time))
                mTimePicker.show()

            }
        }
    }

    interface OnDayTimeRecyclerViewItemClickListner {
        fun dayTimeAdd(list: ArrayList<DayTimeModel>, position: Int)
    }

}