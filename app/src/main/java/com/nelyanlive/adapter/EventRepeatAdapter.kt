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
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nelyanlive.R
import com.nelyanlive.modals.ModelPOJO
import com.nelyanlive.utils.image_base_URl
import kotlinx.android.synthetic.main.item_event_add_more.view.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class EventRepeatAdapter(
    var context: Context, var list: ArrayList<ModelPOJO.AddEventDataModel>,
    var listner: OnEventRecyclerViewItemClickListener
) : RecyclerView.Adapter<EventRepeatAdapter.EventRepeatViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventRepeatViewHolder {
        return EventRepeatViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_event_add_more, parent, false),
            listner
        )
    }

    override fun onBindViewHolder(holder: EventRepeatViewHolder, position: Int) {
        holder.initalize(list, position)

    }

    override fun getItemCount(): Int {
        Log.d(EventRepeatAdapter::class.java.name, "EventRepearAdapter_size   " + list.size)
        return list.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    inner class EventRepeatViewHolder(
        itemView: View,
        var listner: OnEventRecyclerViewItemClickListener
    ) : RecyclerView.ViewHolder(itemView) {

        val addButton = itemView.tvAddMore!!
        val image = itemView.ivEventimage!!
        val name = itemView.edtEventName!!
        val dateFrom = itemView.tv_cal!!
        val dateTo = itemView.tvCal1
        val timeFrom = itemView.edClo2
        val timeTo = itemView.edClo3
        val description = itemView.edtDesc
        val edtAgeFrom = itemView.edtAgeFrom
        val edtAgeTo = itemView.edtAgeTo
        val price = itemView.edtPrice
        val et_city_add = itemView.et_city_add
        val removeButton = itemView.ivdlt
        val llevent = itemView.ll_event


        //  for selecting the date
        var select_date = ""
        var day = ""
        var month = ""
        var year = ""
        var date_timestamp = ""
        var date_timestamp_toDate = ""

        fun initalize(list: ArrayList<ModelPOJO.AddEventDataModel>, position: Int) {

            Glide.with(context).asBitmap().load(image_base_URl + list[position].image).into(image)
            name.setText(list[position].name)
            description.setText(list[position].description)
            price.setText(list[position].price)
            et_city_add.setText(list[position].city)

            dateFrom.text = list[position].dateFrom
            dateTo.text = list[position].dateTo
            timeFrom.text = list[position].timeFrom
            timeTo.text = list[position].timeTo


            et_city_add.setOnClickListener {
                listner.cityAddEvent(list, position,et_city_add)
            }

            image.setOnClickListener {
                listner.addCameraGalleryImage(list, position)
            }

            addButton.setOnClickListener {
                description.clearFocus()
                price.clearFocus()
                name.clearFocus()
               // city.clearFocus()
                listner.onAddEventItem(list, position)
            }

            removeButton.setOnClickListener {
                list.removeAt(position)
                val ListSize = list.size
                listner.onRemoveEventItem(position, ListSize)

                notifyDataSetChanged()
                Log.e("EventRepearAdapter_size", "--RRRRR--" + list.size)

            }


            timeFrom.setOnClickListener {
                val mcurrentTime = Calendar.getInstance()
                val hour = mcurrentTime[Calendar.HOUR_OF_DAY]
                val minute = mcurrentTime[Calendar.MINUTE]
                val mTimePicker = TimePickerDialog(context, { timePicker, selectedHour, selectedMinute ->
                        timeFrom.text = String.format("%02d:%02d", selectedHour, selectedMinute)
                        list[position].timeFrom = "$selectedHour:$selectedMinute"
                    }, hour, minute, true)
                mTimePicker.setTitle(context.getString(R.string.select_time))
                mTimePicker.show()
            }

            timeTo.setOnClickListener {
                val mcurrentTime = Calendar.getInstance()
                val hour = mcurrentTime[Calendar.HOUR_OF_DAY]
                val minute = mcurrentTime[Calendar.MINUTE]
                val mTimePicker =
                    TimePickerDialog(context, { timePicker, selectedHour, selectedMinute ->
                        timeTo.text = String.format("%02d:%02d", selectedHour, selectedMinute)
                        list[position].timeTo = "$selectedHour:$selectedMinute"
                    }, hour, minute, true)
                mTimePicker.setTitle(context.getString(R.string.select_time))
                mTimePicker.show()
            }

            dateFrom.setOnClickListener {
                select_date_register_popup(position, "1")
            }

            dateTo.setOnClickListener {
                select_date_register_popup(position, "2")
            }

            price.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    list[position].price = s.toString()
                }

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }
            })

            name.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    list[position].name = s.toString()
                }

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }
            })

            description.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    list[position].description = s.toString()
                }

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }
            })

            edtAgeFrom.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    list[position].minAge = s.toString()
                }
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }
            })

            edtAgeTo.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    list[position].maxAge = s.toString()
                }

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }
            })

        }

        private fun select_date_register_popup(position: Int, type: String) {
            select_date = ""
            val mYear: Int
            val mMonth: Int
            val mDay: Int
            val currentDate = Calendar.getInstance()
            mYear = currentDate[Calendar.YEAR]
            mMonth = currentDate[Calendar.MONTH]
            mDay = currentDate[Calendar.DAY_OF_MONTH]

            val mDatePicker = DatePickerDialog(
                context,
                { datepicker, selectedyear, selectedmonth, selectedday ->
                    day = if (selectedday.toString().length == 1)
                        "0$selectedday"
                    else
                        selectedday.toString()
                    month = if ((selectedmonth + 1).toString().length == 1)
                        "0" + (selectedmonth + 1).toString()
                    else (selectedmonth + 1).toString()

                    year = selectedyear.toString()
                    if (type=="1") {
                        date_timestamp = calDateToTimeStamp(
                            "$day-$month-$year"
                        ).toString()
                    }else{
                        date_timestamp_toDate = calDateToTimeStamp(
                            "$day-$month-$year"
                        ).toString()
                    }
                    select_date = "$day/$month/$selectedyear"

                    when (type) {
                        "1" -> {
                            // check the cases if date is put after the end date
                            // check by pardeep sharma
                            if (date_timestamp_toDate != "") {
                                if (date_timestamp.toLong() < date_timestamp_toDate.toLong()) {
                                    list[position].dateFrom = select_date
                                    notifyDataSetChanged()
                                } else {
                                    date_timestamp = ""
                                    list[position].dateFrom = ""
                                    notifyDataSetChanged()
                                    Toast.makeText(
                                        context,
                                        "La date de début doit être inférieure à la date de fin",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            } else {
                                list[position].dateFrom = select_date
                                notifyDataSetChanged()
                            }
                        }
                        "2" -> {
                            // check the cases if date is put before the start date
                        if (date_timestamp!=""){
                            if (date_timestamp.toLong() < date_timestamp_toDate.toLong()) {
                                list[position].dateTo = select_date
                                notifyDataSetChanged()
                            }else{
                                // clear the values
                                date_timestamp_toDate = ""
                                list[position].dateTo = ""
                                notifyDataSetChanged()
                                Toast.makeText(
                                    context,
                                    "La date de fin doit être supérieur de la date de début",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }else {
                            list[position].dateTo = select_date
                            notifyDataSetChanged()
                        }
                        }
                    }
                }, mYear, mMonth, mDay
            )
            mDatePicker.datePicker.minDate = System.currentTimeMillis()
            if (!mDatePicker.isShowing) {
                mDatePicker.show()
            }
        }

        private fun calDateToTimeStamp(strDate: String?): Long {
            var timeStamp = java.lang.Long.valueOf(0)
            try {
                val formatter = SimpleDateFormat("dd-MM-yyyy")
                //SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
                val date: Date = formatter.parse(strDate)
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
        fun onAddEventItem(list: ArrayList<ModelPOJO.AddEventDataModel>, position: Int)
        fun addCameraGalleryImage(list: ArrayList<ModelPOJO.AddEventDataModel>, position: Int)
        fun cityAddEvent(
            list: ArrayList<ModelPOJO.AddEventDataModel>,
            position: Int,
            city: TextView
        )

        fun eventDataSet(type: String, value: String, position: Int)
        fun onRemoveEventItem(position: Int, list: Int)

        fun onSingleEventItem(list: ArrayList<ModelPOJO.AddEventDataModel>, position: Int)
    }
}


