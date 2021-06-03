package com.nelyanlive.adapter

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nelyanlive.R
import com.nelyanlive.modals.myAd.EventMyAds
import com.nelyanlive.utils.image_base_URl
import kotlinx.android.synthetic.main.item_event_add_more.view.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class EventEditAdapter(var context: Context, var list: ArrayList<EventMyAds>,
                       var listner: OnEventRecyclerViewItemClickListner) : RecyclerView.Adapter<EventEditAdapter.EventRepeatViewHolder>()
{

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventRepeatViewHolder {
        return EventRepeatViewHolder(LayoutInflater.from(context).inflate(R.layout.item_event_add_more, parent, false), listner)
    }

    override fun onBindViewHolder(holder: EventRepeatViewHolder, position: Int) {
        holder.initalize(list, position)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class EventRepeatViewHolder(itemView: View, var listner: OnEventRecyclerViewItemClickListner) : RecyclerView.ViewHolder(itemView) {

        val addButton = itemView.tvAddMore
        val image = itemView.ivEventimage
        val name = itemView.edtEventName
        val dateFrom = itemView.tv_cal
        val dateTo = itemView.tvCal1
        val timeFrom = itemView.edClo2
        val timeTo = itemView.edClo3
        val description = itemView.edtDesc
        val price = itemView.edtPrice
        val city = itemView.et_city_add_event

        //  for selecting the date
        var select_date = ""
        var day = ""
        var month = ""
        var year = ""
        var date_timestamp = ""


        fun initalize(list: ArrayList<EventMyAds>, position: Int) {
            addButton.visibility = View.GONE
            // setting data  here
            Glide.with(context).load(image_base_URl+list.get(position).image).error(R.mipmap.no_image_placeholder).into(image)
            name.setText(list.get(position).name)
            description.setText(list.get(position).description)
            price.setText(list.get(position).price)
            dateFrom.text = list.get(position).dateFrom
            dateTo.text = list.get(position).dateTo
            timeFrom.text = list.get(position).startTime
            timeTo.text = list.get(position).endTime
            city.text = list.get(position).city

            image.setOnClickListener{
                listner.addCameraGelleryImage(list, position)
            }

            city.setOnClickListener {
                listner.cityinAddEvent(list, position, city)

            }


            addButton.setOnClickListener {
                description.clearFocus()
                price.clearFocus()
                name.clearFocus()
                city.clearFocus()
                listner.onAddEventItem(list, position)
            }


/*
            timeFrom.setOnClickListener {
                val mcurrentTime = Calendar.getInstance()
                val hour = mcurrentTime[Calendar.HOUR_OF_DAY]
                val minute = mcurrentTime[Calendar.MINUTE]
                val mTimePicker: TimePickerDialog
                mTimePicker = TimePickerDialog(context, TimePickerDialog.OnTimeSetListener { timePicker, selectedHour, selectedMinute ->
//                    timeFrom.text = "$selectedHour:$selectedMinute"
                    timeFrom.setText(String.format("%02d:%02d", selectedHour, selectedMinute))

                    list[position].timeFrom = "$selectedHour:$selectedMinute"
                }, hour, minute, true) //Yes 24 hour time
                mTimePicker.setTitle(context.getString(R.string.select_time))
                mTimePicker.show()
            }

            timeTo.setOnClickListener {
                val mcurrentTime = Calendar.getInstance()
                val hour = mcurrentTime[Calendar.HOUR_OF_DAY]
                val minute = mcurrentTime[Calendar.MINUTE]
                val mTimePicker: TimePickerDialog
                mTimePicker = TimePickerDialog(context, TimePickerDialog.OnTimeSetListener { timePicker, selectedHour, selectedMinute ->
                   // timeTo.text = "$selectedHour:$selectedMinute"
                    timeTo.setText(String.format("%02d:%02d", selectedHour, selectedMinute))
                    list[position].timeTo = "$selectedHour:$selectedMinute"
                }, hour, minute, true) //Yes 24 hour time
                mTimePicker.setTitle(context.getString(R.string.select_time))
                mTimePicker.show()
            }


            dateFrom.setOnClickListener {
                select_date_register_popup(position,"1")
            }

            dateTo.setOnClickListener {
               select_date_register_popup(position,"2")
            }
*/

            listner.currentEventDataList(list, position)

            // add text watcher  for age To
            price.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    list[position].price = s.toString()
                }
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }
            })

            description.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    list[position].description = s.toString()
                }
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }
            })
        }

        private fun select_date_register_popup( position: Int, type: String) {
             select_date=""
            val mYear: Int
            val mMonth: Int
            val mDay: Int
            val mcurrentDate = Calendar.getInstance()
            mYear = mcurrentDate[Calendar.YEAR]
            mMonth = mcurrentDate[Calendar.MONTH]
            mDay = mcurrentDate[Calendar.DAY_OF_MONTH]


            val mDatePicker = DatePickerDialog(
                context, OnDateSetListener { datepicker, selectedyear, selectedmonth, selectedday ->
                        if (selectedday.toString().length == 1)
                            day = "0$selectedday"
                        else
                            day = selectedday.toString()

                        if ((selectedmonth + 1).toString().length == 1)
                            month = "0" + (selectedmonth + 1).toString()
                        else month = (selectedmonth + 1).toString()

                        year = selectedyear.toString()

                        date_timestamp = calenderDateToTimeStamp(
                                day + "-" + month + "-" + year,
                                "dd-MM-yyyy"
                        ).toString()
                        Log.e("day", day.toString())
                        Log.e("month", month.toString())
                        Log.e("year", selectedyear.toString())
                        Log.e("date_timestamp", date_timestamp)
                        select_date = day + "/" + month + "/" + selectedyear


/*
                if (type.equals("1")){
                    list.get(position).dateFrom = select_date
                    notifyDataSetChanged()
                } else if (type.equals("2")){
                    list.get(position).dateTo = select_date
                    notifyDataSetChanged()

                }
*/
                    }, mYear, mMonth, mDay
            )
            mDatePicker.datePicker.minDate = System.currentTimeMillis()
            if (!mDatePicker.isShowing) {
                mDatePicker.show()
            }
        }


        fun calenderDateToTimeStamp(str_date: String?, date_formate: String?): Long {
            var time_stamp = java.lang.Long.valueOf(0)
            try {
                val formatter = SimpleDateFormat(date_formate)
                //SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
                val date: Date = formatter.parse(str_date)
                time_stamp = date.time
            } catch (ex: ParseException) {
                ex.printStackTrace()
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
            time_stamp = time_stamp / 1000
            return time_stamp
        }

    }


    interface OnEventRecyclerViewItemClickListner {
        fun onAddEventItem(list: ArrayList<EventMyAds>, position: Int)
        fun addCameraGelleryImage(list: ArrayList<EventMyAds>, position: Int)
        fun currentEventDataList(list: ArrayList<EventMyAds>, position: Int)
        fun cityinAddEvent(list: ArrayList<EventMyAds>, position: Int, city: TextView)

    }
}


