package com.nelyan_live.adapter

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
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
import com.nelyan_live.R
import com.nelyan_live.modals.ModelPOJO
import kotlinx.android.synthetic.main.item_event_add_more.view.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class EventRepeatAdapter(var context: Context, var list: ArrayList<ModelPOJO.AddEventDataModel>,
                         var listner: OnEventRecyclerViewItemClickListner) : RecyclerView.Adapter<EventRepeatAdapter.EventRepeatViewHolder>() 
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


        fun initalize(list: ArrayList<ModelPOJO.AddEventDataModel>, position: Int) {

            // setting data  here
            Glide.with(context).asBitmap().load(list.get(position).image).into(image)
            name.setText(list.get(position).name)
            description.setText(list.get(position).description)
            price.setText(list.get(position).price)

            dateFrom.text = list.get(position).dateFrom
            dateTo.text = list.get(position).dateTo
            timeFrom.text = list.get(position).timeFrom
            timeTo.text = list.get(position).timeTo
            city.text = list.get(position).city

            image.setOnClickListener{
                listner!!.addCameraGelleryImage(list, position)
            }

            city.setOnClickListener {
                listner.cityinAddEvent(list, position, city)

            }


            addButton.setOnClickListener {
                description.clearFocus()
                price.clearFocus()
                name.clearFocus()
                city.clearFocus()
                listner!!.onAddEventItem(list, position)
            }


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

            name.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    list[position].name = s.toString()
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


            val mDatePicker = DatePickerDialog(context!!, OnDateSetListener { datepicker, selectedyear, selectedmonth, selectedday ->
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


                if (type.equals("1")){
                    list.get(position).dateFrom = select_date
                    notifyDataSetChanged()
                } else if (type.equals("2")){
                    list.get(position).dateTo = select_date
                    notifyDataSetChanged()

                }
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



        /* var edtEventName: EditText
         var edtDesc: EditText
         var edtPrice: EditText
         var tvCal: TextView
         var tvCal1: TextView
         var edClo2: TextView
         var edClo3: TextView
         var tvAddMore: TextView
         var ivEvent: ImageView
         var ivCam: ImageView
         fun bind(pos: Int) {
             Log.e("kmdkmdkedcmk", "sszzzzeeeeee " + image.size)
             if (pos == returnItemView - 1) {
                 tvAddMore.visibility = View.VISIBLE
             } else {
                 tvAddMore.visibility = View.GONE
             }
             try {
                 ivEvent.setImageBitmap(image[pos.toString()])
             } catch (e: Exception) {
             }
             try {
                 edtEventName.setText(ArryedtEventName[pos.toString()])
             } catch (e: Exception) {
             }
             try {
                 tvCal.text = ArrydatetvCalfrom[pos.toString()]
             } catch (e: Exception) {
             }
             try {
                 tvCal1.text = ArrydatetvCal1to[pos.toString()]
             } catch (e: Exception) {
             }
             try {
                 edClo2.text = ArrytimeedClo2From[pos.toString()]
             } catch (e: Exception) {
             }
             try {
                 edClo3.text = ArrytimeedClo3To[pos.toString()]
             } catch (e: Exception) {
             }
             try {
                 edtDesc.setText(ArryedtDesc[pos.toString()])
             } catch (e: Exception) {
             }
             try {
                 edtPrice.setText(ArredtPrice[pos.toString()])
             } catch (e: Exception) {
             }
             tvAddMore.setOnClickListener {
                 ArryedtEventName[pos.toString()] = edtEventName.text.toString()
                 ArrydatetvCalfrom[pos.toString()] = tvCal.text.toString()
                 ArrydatetvCal1to[pos.toString()] = tvCal1.text.toString()
                 ArrytimeedClo2From[pos.toString()] = edClo2.text.toString()
                 ArrytimeedClo3To[pos.toString()] = edClo3.text.toString()
                 ArryedtDesc[pos.toString()] = edtDesc.text.toString()
                 ArredtPrice[pos.toString()] = edtPrice.text.toString()
                 returnItemView = returnItemView + 1
                 notifyDataSetChanged()
             }
             ivEvent.setOnClickListener {
                // eventRepeatListen.imageClick(pos, returnItemView)
             }
             tvCal.setOnClickListener {
                 date = OnDateSetListener { datePicker, year, monthOfYear, dayOfMonth ->
                     myCalendar[Calendar.YEAR] = year
                     myCalendar[Calendar.MONTH] = monthOfYear
                     myCalendar[Calendar.DAY_OF_MONTH] = dayOfMonth
                     val dateInString = updateDateLabel()
                     tvCal.text = dateInString
                 }
                 dateDialog()
             }
             tvCal1.setOnClickListener {
                 date = OnDateSetListener { datePicker, year, monthOfYear, dayOfMonth ->
                     myCalendar[Calendar.YEAR] = year
                     myCalendar[Calendar.MONTH] = monthOfYear
                     myCalendar[Calendar.DAY_OF_MONTH] = dayOfMonth
                     val dateInString = updateDateLabel()
                     tvCal1.text = dateInString
                 }
                 dateDialog()
             }
             edClo2.setOnClickListener {
                 val mcurrentTime = Calendar.getInstance()
                 val hour = mcurrentTime[Calendar.HOUR_OF_DAY]
                 val minute = mcurrentTime[Calendar.MINUTE]
                 val mTimePicker: TimePickerDialog
                 mTimePicker = TimePickerDialog(context, OnTimeSetListener { timePicker, selectedHour, selectedMinute -> edClo2.text = "$selectedHour:$selectedMinute" }, hour, minute, true) //Yes 24 hour time
                 mTimePicker.setTitle("Select Time")
                 mTimePicker.show()
             }
             edClo3.setOnClickListener {
                 val mcurrentTime = Calendar.getInstance()
                 val hour = mcurrentTime[Calendar.HOUR_OF_DAY]
                 val minute = mcurrentTime[Calendar.MINUTE]
                 val mTimePicker: TimePickerDialog
                 mTimePicker = TimePickerDialog(context, OnTimeSetListener { timePicker, selectedHour, selectedMinute -> edClo3.text = "$selectedHour:$selectedMinute" }, hour, minute, true) //Yes 24 hour time
                 mTimePicker.setTitle("Select Time")
                 mTimePicker.show()
             }*/
    }

    /*private fun dateDialog() {
        val datePickerDialog = DatePickerDialog(context, R.style.datepicker, date, myCalendar[Calendar.YEAR], myCalendar[Calendar.MONTH], myCalendar[Calendar.DAY_OF_MONTH])
        datePickerDialog.show()
    }

    private fun updateDateLabel(): String {
        val dateFormat = "dd/MM/yy" //In which you need put here
        val sdf = SimpleDateFormat(dateFormat, Locale.US)
        return sdf.format(myCalendar.time)
    }*/

    /* init {
         edtEventName = itemView.findViewById(R.id.edtEventName)
         tvCal = itemView.findViewById(R.id.tvCal)
         tvCal1 = itemView.findViewById(R.id.tvCal1)
         edClo2 = itemView.findViewById(R.id.edClo2)
         ivEvent = itemView.findViewById(R.id.ivEventimage)
         edClo3 = itemView.findViewById(R.id.edClo3)
         edtDesc = itemView.findViewById(R.id.edtDesc)
         edtPrice = itemView.findViewById(R.id.edtPrice)
         tvAddMore = itemView.findViewById(R.id.tvAddMore)
         ivCam = itemView.findViewById(R.id.ivCam)
     }*/


    interface OnEventRecyclerViewItemClickListner {
        fun onAddEventItem(list: ArrayList<ModelPOJO.AddEventDataModel>, position: Int)
        fun addCameraGelleryImage(list: ArrayList<ModelPOJO.AddEventDataModel>, position: Int)
        fun cityinAddEvent(list: ArrayList<ModelPOJO.AddEventDataModel>, position: Int, city: TextView)
    }
}


