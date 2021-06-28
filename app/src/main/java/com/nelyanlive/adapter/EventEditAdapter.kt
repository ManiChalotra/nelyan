package com.nelyanlive.adapter

import android.app.DatePickerDialog
import android.app.TimePickerDialog
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
                       var listener: OnEventRecyclerViewItemClickListener) : RecyclerView.Adapter<EventEditAdapter.EventRepeatViewHolder>()
{

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventRepeatViewHolder {
        return EventRepeatViewHolder(LayoutInflater.from(context).inflate(R.layout.item_event_add_more, parent, false), listener)
    }

    override fun onBindViewHolder(holder: EventRepeatViewHolder, position: Int) {
        holder.initialize(list, position)
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


    inner class EventRepeatViewHolder(itemView: View, var listener: OnEventRecyclerViewItemClickListener) : RecyclerView.ViewHolder(itemView) {

        private val addButton = itemView.tvAddMore!!
        val image = itemView.ivEventimage!!
        val name = itemView.edtEventName!!
        val dateFrom = itemView.tv_cal!!
        val dateTo = itemView.tvCal1!!
        val timeFrom = itemView.edClo2!!
        val timeTo = itemView.edClo3!!
        val description = itemView.edtDesc!!
        val price = itemView.edtPrice!!
        val city = itemView.et_city_add_event!!

        var selectDate = ""
        var day = ""
        var year = ""
        var month = ""
        var dateTimeStamp = ""

        fun initialize(list: ArrayList<EventMyAds>, position: Int) {

            Glide.with(context).load(image_base_URl+
                    list[position].image).error(R.mipmap.no_image_placeholder).into(image)


            name.setText(list[position].name)
            description.setText(list[position].description)
            price.setText(list[position].price)
            dateFrom.text = list[position].dateFrom
            dateTo.text = list[position].dateTo
            timeFrom.text = list[position].startTime
            timeTo.text = list[position].endTime
            city.text = list[position].city

            image.setOnClickListener{
                listener.addCameraGalleryImage( position)
            }

            city.setOnClickListener {
                listener.cityAddEvent(list, position, city)
            }
            
            addButton.setOnClickListener {
                description.clearFocus()
                price.clearFocus()
                name.clearFocus()
                city.clearFocus()
                listener.onAddEventItem(list, position)
            }


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
            
            //text watcher  for name 
             name.addTextChangedListener(object : TextWatcher {
                            override fun afterTextChanged(s: Editable?) {
                                list[position].name = s.toString()
                            }
                            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                            }
                            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                           }
                        })

            dateFrom.setOnClickListener {
                selectDate(position,"1")
            }

            dateTo.setOnClickListener {
                selectDate(position,"2")
            }


                        timeFrom.setOnClickListener {
                            val currentTime = Calendar.getInstance()
                            val hour = currentTime[Calendar.HOUR_OF_DAY]
                            val minute = currentTime[Calendar.MINUTE]
                            val mTimePicker = TimePickerDialog(context, { timePicker, selectedHour, selectedMinute ->
                                timeFrom.text = String.format("%02d:%02d", selectedHour, selectedMinute)
                                list[position].startTime = "$selectedHour:$selectedMinute"
                            }, hour, minute, true)
                            mTimePicker.setTitle(context.getString(R.string.select_time))
                            mTimePicker.show()
                        }

                        timeTo.setOnClickListener {
                            val currentTime = Calendar.getInstance()
                            val hour = currentTime[Calendar.HOUR_OF_DAY]
                            val minute = currentTime[Calendar.MINUTE]
                            val mTimePicker =
                                TimePickerDialog(context, { timePicker, selectedHour, selectedMinute ->
                                timeTo.text = String.format("%02d:%02d", selectedHour, selectedMinute)
                                list[position].endTime = "$selectedHour:$selectedMinute"
                            }, hour, minute, true)
                            mTimePicker.setTitle(context.getString(R.string.select_time))
                            mTimePicker.show()
                        }

        }
        private fun selectDate( position: Int, type: String) {
            selectDate=""
            val mYear: Int
            val mMonth: Int
            val mDay: Int
            val currentDate = Calendar.getInstance()
            mYear = currentDate[Calendar.YEAR]
            mMonth = currentDate[Calendar.MONTH]
            mDay = currentDate[Calendar.DAY_OF_MONTH]


            val mDatePicker = DatePickerDialog(context,
                { _, selectedyear, selectedmonth, selectedday ->
                    day = if (selectedday.toString().length == 1)
                        "0$selectedday"
                    else
                        selectedday.toString()

                    month = if ((selectedmonth + 1).toString().length == 1)
                        "0" + (selectedmonth + 1).toString()
                    else (selectedmonth + 1).toString()

                    year = selectedyear.toString()

                    dateTimeStamp = dateToTimeStamp(
                        "$day-$month-$year"
                    ).toString()
                    Log.e("day", day)
                    Log.e("month", month)
                    Log.e("year", selectedyear.toString())
                    Log.e("dateTimeStamp", dateTimeStamp)
                    selectDate = "$day/$month/$selectedyear"


                    if (type == "1") {
                        list[position].dateFrom = selectDate
                        notifyDataSetChanged()
                    } else if (type == "2") {
                        list[position].dateTo = selectDate
                        notifyDataSetChanged()

                    }
                }, mYear, mMonth, mDay
            )
            mDatePicker.datePicker.minDate = System.currentTimeMillis()
            if (!mDatePicker.isShowing) {
                mDatePicker.show()
            }
        }
        private fun dateToTimeStamp(str_date: String?): Long {
            var timeStamp = java.lang.Long.valueOf(0)
            try {
                val formatter = SimpleDateFormat("dd-MM-yyyy")
                val date: Date = formatter.parse(str_date)
                timeStamp = date.time
            } catch (ex: ParseException) {
                ex.printStackTrace()
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
            timeStamp /= 1000
            return timeStamp
        }

    }



    interface OnEventRecyclerViewItemClickListener {
        fun onAddEventItem(list: ArrayList<EventMyAds>, position: Int)
        fun addCameraGalleryImage( position: Int)
        fun cityAddEvent(list: ArrayList<EventMyAds>, position: Int, city: TextView)

    }
}


