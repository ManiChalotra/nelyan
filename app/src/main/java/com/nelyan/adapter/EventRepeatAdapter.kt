package com.nelyan.adapter

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nelyan.R
import com.nelyan.ui.ActivityFormActivity
import com.yanzhenjie.album.AlbumFile
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class EventRepeatAdapter(var context: Context, var image: HashMap<String, Bitmap>, eventRepeatListen: ActivityFormActivity, returnItemView: Int) : RecyclerView.Adapter<EventRepeatAdapter.EventRepeatViewHolder>() {
    var selectedImage = ""
    var mAlbumFiles = ArrayList<AlbumFile>()
    var file: File? = null
    var rl_Add: RelativeLayout? = null
    var returnItemView = 1
    var eventRepeatListen: ActivityFormActivity
    var myCalendar = Calendar.getInstance()
    var date: OnDateSetListener? = null
    var ArryedtEventName = HashMap<String, String>()
    var ArrydatetvCalfrom = HashMap<String, String>()
    var ArrydatetvCal1to = HashMap<String, String>()
    var ArrytimeedClo2From = HashMap<String, String>()
    var ArrytimeedClo3To = HashMap<String, String>()
    var ArryedtDesc = HashMap<String, String>()
    var ArredtPrice = HashMap<String, String>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventRepeatViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_event_add_more, parent, false)
        return EventRepeatViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventRepeatViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return returnItemView
    }

    inner class EventRepeatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var edtEventName: EditText
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
            ivEvent.setOnClickListener { eventRepeatListen.imageClick(pos, returnItemView) }
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
            }
        }

        private fun dateDialog() {
            val datePickerDialog = DatePickerDialog(context, R.style.datepicker, date, myCalendar[Calendar.YEAR], myCalendar[Calendar.MONTH], myCalendar[Calendar.DAY_OF_MONTH])
            datePickerDialog.show()
        }

        private fun updateDateLabel(): String {
            val dateFormat = "dd/MM/yy" //In which you need put here
            val sdf = SimpleDateFormat(dateFormat, Locale.US)
            return sdf.format(myCalendar.time)
        }

        init {
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
        }
    }

    interface EventRepeatListen {
        fun imageClick(pos: Int, returnItemView: Int)
    }

    init {
        this.returnItemView = returnItemView
        this.eventRepeatListen = eventRepeatListen
    }
}