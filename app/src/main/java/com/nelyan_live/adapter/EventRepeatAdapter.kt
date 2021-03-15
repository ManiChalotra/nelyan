package com.nelyan_live.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nelyan_live.R
import com.nelyan_live.modals.ModelPOJO
import kotlinx.android.synthetic.main.item_event_add_more.view.*

class EventRepeatAdapter(var context: Context, var list: ArrayList<ModelPOJO.AddEventDataModel>, var listner:OnEventRecyclerViewItemClickListner) : RecyclerView.Adapter<EventRepeatAdapter.EventRepeatViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventRepeatViewHolder {
        return EventRepeatViewHolder(LayoutInflater.from(context).inflate(R.layout.item_event_add_more, parent, false), listner)
    }

    override fun onBindViewHolder(holder: EventRepeatViewHolder, position: Int) {
        holder.initalize(list, position)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class EventRepeatViewHolder(itemView: View, var listner:OnEventRecyclerViewItemClickListner) : RecyclerView.ViewHolder(itemView) {

        val addButton = itemView.tvAddMore

        fun initalize(list: ArrayList<ModelPOJO.AddEventDataModel>, position: Int) {
            addButton.setOnClickListener {
                listner!!.onAddEventItem(list, position)
            }
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
    }
}


