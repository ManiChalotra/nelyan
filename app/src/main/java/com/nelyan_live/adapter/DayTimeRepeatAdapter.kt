package com.nelyan_live.adapter

import android.app.TimePickerDialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nelyan_live.R
import com.nelyan_live.modals.DayTimeModel
import com.nelyan_live.modals.ModelPOJO
import com.nelyan_live.utils.CommonMethodsKotlin

import java.text.SimpleDateFormat
import java.util.*

class DayTimeRepeatAdapter(var context: Context, var list: ArrayList<DayTimeModel>, var listner: OnDayTimeRecyclerViewItemClickListner)
    : RecyclerView.Adapter<DayTimeRepeatAdapter.MyViewHolder>() {

    var end1TimeTimestamp = ""
    var end2TimeTimestamp = ""
    var startTime1Timestamp = ""
    var startTime2Timestamp = ""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_day_time_add_more, parent, false), listner)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.initalize(list, position)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class MyViewHolder(itemView: View, var listner: OnDayTimeRecyclerViewItemClickListner) : RecyclerView.ViewHolder(itemView) {

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
                startTime1Timestamp=""
                end1TimeTimestamp=""
                startTime2Timestamp=""
                end2TimeTimestamp=""
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

            val arrayAdapter = ArrayAdapter(context, R.layout.customspinner, days)
            spinnerDayss.adapter = arrayAdapter
//            val spinnerPosition: Int = arrayAdapter.getPosition(list.get(position).selectedDay)
            //           spinnerDayss.setSelection(spinnerPosition)

            tvMorningFromtime.setOnClickListener {
                timee("start1")
            }
            tvMorningTotime.setOnClickListener {
                timee("end1")
            }
            tvEveningFromtime.setOnClickListener {
                timee("start2")

            }
            tvEveningTotime.setOnClickListener {
                timee("end2")
            }

        }


        fun timee(type: String) {
            val cal = Calendar.getInstance()
            val c = Calendar.getInstance()
            val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)

                var a = SimpleDateFormat("HH:mm").format(cal.time)

              /*  if (cal.getTimeInMillis() >= c.getTimeInMillis()) {
              */      //it's after current
                    if (type.equals("start1")) {
                        tvMorningFromtime.text = SimpleDateFormat("HH:mm").format(cal.time)
                        startTime1Timestamp = (CommonMethodsKotlin.time_to_timestamp(tvMorningFromtime.text.toString(), "HH:mm")).toString()
                        list[adapterPosition].firstStarttime = tvMorningFromtime.text.toString()
                        Log.e("startTimeTimestamp", startTime1Timestamp)

                    } else if (type.equals("end1")) {
                        end1TimeTimestamp = (CommonMethodsKotlin.time_to_timestamp(a, "HH:mm")).toString()
                        if(!startTime1Timestamp.isNullOrEmpty() && startTime1Timestamp<end1TimeTimestamp){
                            tvMorningTotime.text = SimpleDateFormat("HH:mm").format(cal.time)
                            list[adapterPosition].firstEndtime = tvMorningTotime.text.toString()
                            Log.e("endTimeTimestamp", end1TimeTimestamp)
                        } else {
                            Toast.makeText(context, "Invalid Time", Toast.LENGTH_SHORT).show()
                            tvMorningTotime.text=""

                        }

                    }
                    else if (type.equals("start2")) {
                        startTime2Timestamp = (CommonMethodsKotlin.time_to_timestamp(a, "HH:mm")).toString()
                        if(!startTime1Timestamp.isNullOrEmpty() && !end1TimeTimestamp.isNullOrEmpty() && end1TimeTimestamp<startTime2Timestamp){
                            tvEveningFromtime.text = SimpleDateFormat("HH:mm").format(cal.time)
                            list[adapterPosition].secondStarttime = tvEveningFromtime.text.toString()
                            Log.e("endTimeTimestamp", startTime2Timestamp)
                        } else {
                            Toast.makeText(context, "Invalid Time", Toast.LENGTH_SHORT).show()
                            tvEveningFromtime.text=""

                        }
                    }
                    else if (type.equals("end2")) {
                        end2TimeTimestamp = (CommonMethodsKotlin.time_to_timestamp(a, "HH:mm")).toString()

                        if(!startTime1Timestamp.isNullOrEmpty() && !end1TimeTimestamp.isNullOrEmpty() &&
                                !startTime2Timestamp.isNullOrEmpty() && startTime2Timestamp<end2TimeTimestamp){
                            tvEveningTotime.text = SimpleDateFormat("HH:mm").format(cal.time)
                            list[adapterPosition].secondEndtime = tvEveningTotime.text.toString()
                            Log.e("endTimeTimestamp", end2TimeTimestamp)
                        } else {
                            Toast.makeText(context, "Invalid Time", Toast.LENGTH_SHORT).show()
                            tvEveningTotime.text=""
                        }



                    }

            //    }
            /*else {
                    //it's before current'
                    Toast.makeText(context, "Invalid Time", Toast.LENGTH_SHORT).show()
                }*/


/*
            if(a.equals("AM"))
            {
                typ="Am"
            }
            else
            {
                typ="Pm"
            }
*/
            }
            TimePickerDialog(context, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
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