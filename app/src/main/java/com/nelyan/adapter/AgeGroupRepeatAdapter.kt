package com.nelyan.adapter

import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.recyclerview.widget.RecyclerView
import com.nelyan.R
import java.util.*

class AgeGroupRepeatAdapter(var context: Context) : RecyclerView.Adapter<AgeGroupRepeatAdapter.AgeGroupRepeatViewHolder>() {
    var returnItemView = 1
  lateinit  var days: MutableList<String>
    var Selectedmonth = HashMap<String, String>()
    var SelectededtAgeFrom = HashMap<String, String>()
    var SelectededtAgeTo = HashMap<String, String>()
    var SelectedTIMEedClo = HashMap<String, String>()
    var SelectedTIMEedClo1 = HashMap<String, String>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AgeGroupRepeatViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_time_add_more, parent, false)
        return AgeGroupRepeatViewHolder(view)
    }

    override fun onBindViewHolder(holder: AgeGroupRepeatViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return returnItemView
    }

    inner class AgeGroupRepeatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var edtAgeFrom: EditText
        var edtAgeTo: EditText
        var orderby1: Spinner
        var edClo: TextView
        var edClo1: TextView
        var tvAddMore: TextView
        fun bind(pos: Int) {
            if (pos == returnItemView - 1) {
                tvAddMore.visibility = View.VISIBLE
            } else {
                tvAddMore.visibility = View.GONE
            }
            try {
                edtAgeFrom.setText(SelectededtAgeFrom[pos.toString()])
                edtAgeTo.setText(SelectededtAgeTo[pos.toString()])
                edClo.text = SelectedTIMEedClo[pos.toString()]
                edClo1.text = SelectedTIMEedClo1[pos.toString()]
            } catch (e: Exception) {
            }
            tvAddMore.setOnClickListener { view ->
                hideKeyboard(context, view)
                returnItemView = returnItemView + 1
                SelectededtAgeFrom[pos.toString()] = edtAgeFrom.text.toString()
                SelectededtAgeTo[pos.toString()] = edtAgeTo.text.toString()
                SelectedTIMEedClo[pos.toString()] = edClo.text.toString()
                SelectedTIMEedClo1[pos.toString()] = edClo1.text.toString()
                notifyDataSetChanged()
            }
            edClo.setOnClickListener {
                val mcurrentTime = Calendar.getInstance()
                val hour = mcurrentTime[Calendar.HOUR_OF_DAY]
                val minute = mcurrentTime[Calendar.MINUTE]
                val mTimePicker: TimePickerDialog
                mTimePicker = TimePickerDialog(context, OnTimeSetListener { timePicker, selectedHour, selectedMinute -> edClo.text = "$selectedHour:$selectedMinute" }, hour, minute, true) //Yes 24 hour time
                mTimePicker.setTitle("Select Time")
                mTimePicker.show()
            }
            edClo1.setOnClickListener {
                val mcurrentTime = Calendar.getInstance()
                val hour = mcurrentTime[Calendar.HOUR_OF_DAY]
                val minute = mcurrentTime[Calendar.MINUTE]
                val mTimePicker: TimePickerDialog
                mTimePicker = TimePickerDialog(context, OnTimeSetListener { timePicker, selectedHour, selectedMinute -> edClo1.text = "$selectedHour:$selectedMinute" }, hour, minute, true) //Yes 24 hour time
                mTimePicker.setTitle("Select Time")
                mTimePicker.show()
            }
            days = ArrayList()
            days.add("")
            days.add("Monday")
            days.add("Tuesday")
            days.add("Wednesday")
            days.add("Thursday")
            days.add("Friday")
            days.add("Saturday")
            days.add("Sunday")
            val modeAdaptercity = ArrayAdapter(context, R.layout.customspinner, days)
            orderby1.adapter = modeAdaptercity
            try {
                orderby1.setSelection(Selectedmonth[pos.toString()]!!.toInt())
            } catch (e: Exception) {
            }
            orderby1.onItemSelectedListener = object : OnItemSelectedListener {
                override fun onItemSelected(adapterView: AdapterView<*>?, view: View, jj: Int, l: Long) {
                    hideKeyboard(context, view)
                    Selectedmonth[pos.toString()] = jj.toString()
                }

                override fun onNothingSelected(adapterView: AdapterView<*>?) {}
            }
        }

        init {
            edtAgeFrom = itemView.findViewById(R.id.edtAgeFrom)
            edtAgeTo = itemView.findViewById(R.id.edtAgeTo)
            orderby1 = itemView.findViewById(R.id.orderby1)
            edClo = itemView.findViewById(R.id.edClo)
            edClo1 = itemView.findViewById(R.id.edClo1)
            tvAddMore = itemView.findViewById(R.id.tvAddMore)
        }
    }

    companion object {
        fun hideKeyboard(context: Context, view: View?) {
            if (view != null) {
                val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.windowToken, 0)
            }
        }
    }

}