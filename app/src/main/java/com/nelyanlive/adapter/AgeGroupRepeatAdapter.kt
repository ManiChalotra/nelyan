package com.nelyanlive.adapter

import android.app.TimePickerDialog
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nelyanlive.R
import com.nelyanlive.modals.ModelPOJO
import kotlinx.android.synthetic.main.item_time_add_more.view.*
import java.util.*
import kotlin.collections.ArrayList


class AgeGroupRepeatAdapter(var context: Context, var list: ArrayList<ModelPOJO.AgeGroupDataModel>, var listner: OnAgeGroupRecyclerViewItemClickListener)
    : RecyclerView.Adapter<AgeGroupRepeatAdapter.AgeGroupRepeatViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AgeGroupRepeatViewHolder {
        return AgeGroupRepeatViewHolder(LayoutInflater.from(context).inflate(R.layout.item_time_add_more, parent, false), listner)
    }

    override fun onBindViewHolder(holder: AgeGroupRepeatViewHolder, position: Int) {
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


    inner class AgeGroupRepeatViewHolder(itemView: View, var listner: OnAgeGroupRecyclerViewItemClickListener) : RecyclerView.ViewHolder(itemView) {
        val addButton = itemView.tvAddMore

        val ageFrom = itemView.edtAgeFrom
        val ageTo = itemView.edtAgeTo
        val days = itemView.spinner_dayss
        val timeFrom = itemView.tv_mornning_fromtime
        val timeTo = itemView.tv_morning_totime

        fun initalize(list: ArrayList<ModelPOJO.AgeGroupDataModel>, position: Int) {

            timeFrom.text = list.get(position).timeFrom
            timeTo.text = list.get(position).timeTo

            // setting the spinner for days
            val day: ArrayList<String> = ArrayList()
            day.add("")
            day.add(context.getString(R.string.monday))
            day.add(context.getString(R.string.tuesday))
            day.add(context.getString(R.string.wednesday))
            day.add(context.getString(R.string.thursday))
            day.add(context.getString(R.string.friday))
            day.add(context.getString(R.string.saturday))
            day.add(context.getString(R.string.sunday))

            val modeAdaptercity = ArrayAdapter(context, R.layout.customspinner, day)
            days.adapter = modeAdaptercity
            val spinnerPosition: Int = modeAdaptercity.getPosition(list.get(position).days)
            days.setSelection(spinnerPosition)

            ageFrom.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    list[position].ageFrom = s.toString()
                }
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }
            })
            ageTo.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    list[position].ageTo = s.toString()
                }
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }
            })

            var name = ""
            days.onItemSelectedListener = object : OnItemSelectedListener {
                override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View, pos: Int, id: Long) {
                    // your code here
                    name = parentView?.getItemAtPosition(pos).toString()
                    list[position].days = name
                }

                override fun onNothingSelected(parentView: AdapterView<*>?) {
                    // your code here
                }
            }

            timeFrom.setOnClickListener {
                val mcurrentTime = Calendar.getInstance()
                val hour = mcurrentTime[Calendar.HOUR_OF_DAY]
                val minute = mcurrentTime[Calendar.MINUTE]
                val mTimePicker =
                    TimePickerDialog(context, { timePicker, selectedHour, selectedMinute ->
                    timeFrom.text = String.format("%02d:%02d", selectedHour, selectedMinute)
                    list[position].timeFrom = "$selectedHour:$selectedMinute"
                }, hour, minute, true) //Yes 24 hour time
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
                }, hour, minute, true) //Yes 24 hour time
                mTimePicker.setTitle("Select Time")
                mTimePicker.show()
            }

            addButton.setOnClickListener {
                ageFrom.clearFocus()
                ageTo.clearFocus()
                days.clearFocus()
                listner.addAgeGroupItem(list, position)
            }
        }
    }

    interface OnAgeGroupRecyclerViewItemClickListener {
        fun addAgeGroupItem(list: ArrayList<ModelPOJO.AgeGroupDataModel>, position: Int)

    }

}