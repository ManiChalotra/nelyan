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
import com.nelyanlive.modals.myAd.AgeGroupMyAds
import kotlinx.android.synthetic.main.item_time_add_more.view.*
import java.util.*
import kotlin.collections.ArrayList


class AgeGroupEditAdapter(var context: Context, var list: ArrayList<AgeGroupMyAds>, var listener: OnAgeGroupRecyclerViewItemClickListener)
    : RecyclerView.Adapter<AgeGroupEditAdapter.AgeGroupRepeatViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AgeGroupRepeatViewHolder {
        return AgeGroupRepeatViewHolder(LayoutInflater.from(context).inflate(R.layout.item_time_add_more, parent, false), listener)
    }
    override fun onBindViewHolder(holder: AgeGroupRepeatViewHolder, position: Int) {
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

    inner class AgeGroupRepeatViewHolder(itemView: View, var listener: OnAgeGroupRecyclerViewItemClickListener) : RecyclerView.ViewHolder(itemView) {
        private var addButton = itemView.tvAddMore!!
        var ageFrom = itemView.edtAgeFrom!!
        var ageTo = itemView.edtAgeTo!!
        var days = itemView.spinner_dayss!!
        var timeFrom = itemView.tv_mornning_fromtime!!
        var timeTo = itemView.tv_morning_totime!!

        fun initialize(list: ArrayList<AgeGroupMyAds>, position: Int) {
            timeFrom.text = list[position].timeFrom
            timeTo.text = list[position].timeTo
            ageFrom.setText(list[position].ageFrom)
            ageTo.setText(list[position].ageTo)

            val day: ArrayList<String> = ArrayList()
            day.add("")
            day.add(context.getString(R.string.monday))
            day.add(context.getString(R.string.tuesday))
            day.add(context.getString(R.string.wednesday))
            day.add(context.getString(R.string.thursday))
            day.add(context.getString(R.string.friday))
            day.add(context.getString(R.string.saturday))
            day.add(context.getString(R.string.sunday))


            val modeAdapterCity = ArrayAdapter(context, R.layout.customspinner, day)
            days.adapter = modeAdapterCity
            days.setSelection(modeAdapterCity.getPosition(list[position].days))

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

            days.onItemSelectedListener = object : OnItemSelectedListener {
                override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View, pos: Int, id: Long) {
                    list[position].days = parentView?.getItemAtPosition(pos).toString()
                }
                override fun onNothingSelected(parentView: AdapterView<*>?) {
                }
            }

            timeFrom.setOnClickListener {
                val mCurrentTime = Calendar.getInstance()
                val hour = mCurrentTime[Calendar.HOUR_OF_DAY]
                val minute = mCurrentTime[Calendar.MINUTE]
                val mTimePicker =
                    TimePickerDialog(context, { _, selectedHour, selectedMinute ->
                    timeFrom.text = String.format("%02d:%02d", selectedHour, selectedMinute)
                    list[position].timeFrom = "$selectedHour:$selectedMinute"
                }, hour, minute, true) 
                mTimePicker.setTitle(context.getString(R.string.select_time))
                mTimePicker.show()
            }

            timeTo.setOnClickListener {
                val mCurrentTime = Calendar.getInstance()
                val hour = mCurrentTime[Calendar.HOUR_OF_DAY]
                val minute = mCurrentTime[Calendar.MINUTE]
                val mTimePicker = TimePickerDialog(context, { _, selectedHour, selectedMinute ->
                    timeTo.text = String.format("%02d:%02d", selectedHour, selectedMinute)
                    list[position].timeTo = "$selectedHour:$selectedMinute"
                }, hour, minute, true)
                mTimePicker.setTitle(context.getString(R.string.select_time))
                mTimePicker.show()
            }

            addButton.setOnClickListener {
                ageFrom.clearFocus()
                ageTo.clearFocus()
                days.clearFocus()
                listener.addAgeGroupItem(list, position)
            }
        }
    }

    interface OnAgeGroupRecyclerViewItemClickListener {
        fun addAgeGroupItem(list: ArrayList<AgeGroupMyAds>, position: Int)

    }
}