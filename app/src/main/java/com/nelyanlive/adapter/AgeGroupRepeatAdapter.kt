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


class AgeGroupRepeatAdapter(var context: Context, var list: ArrayList<ModelPOJO.AgeGroupDataModel>, var listner: OnAGeGroupRecyclerViewItemClickListner)
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

    inner class AgeGroupRepeatViewHolder(itemView: View, var listner: OnAGeGroupRecyclerViewItemClickListner) : RecyclerView.ViewHolder(itemView) {
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
            day.add("Monday")
            day.add("Tuesday")
            day.add("Wednesday")
            day.add("Thursday")
            day.add("Friday")
            day.add("Saturday")
            day.add("Sunday")

            val modeAdaptercity = ArrayAdapter(context, R.layout.customspinner, day)
            days.adapter = modeAdaptercity
            val spinnerPosition: Int = modeAdaptercity.getPosition(list.get(position).days)
            days.setSelection(spinnerPosition)

            // add text watcher  for age from
            ageFrom.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    list[position].ageFrom = s.toString()
                }
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }
            })

            // add text watcher  for age To
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
            // setting the data in spinner
            days.onItemSelectedListener = object : OnItemSelectedListener {
                override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View, pos: Int, id: Long) {
                    // your code here
                    name = parentView?.getItemAtPosition(pos).toString()
                    // context.toast(name)
                    list[position].days = name.toString()
                }

                override fun onNothingSelected(parentView: AdapterView<*>?) {
                    // your code here
                }
            }

            timeFrom.setOnClickListener {
                val mcurrentTime = Calendar.getInstance()
                val hour = mcurrentTime[Calendar.HOUR_OF_DAY]
                val minute = mcurrentTime[Calendar.MINUTE]
                val mTimePicker: TimePickerDialog
                mTimePicker = TimePickerDialog(context, TimePickerDialog.OnTimeSetListener { timePicker, selectedHour, selectedMinute ->
                  //  timeFrom.text = "$selectedHour:$selectedMinute"
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
                val mTimePicker: TimePickerDialog
                mTimePicker = TimePickerDialog(context, TimePickerDialog.OnTimeSetListener { timePicker, selectedHour, selectedMinute ->
                 //   timeTo.text = "$selectedHour:$selectedMinute"
                    timeTo.text = String.format("%02d:%02d", selectedHour, selectedMinute)
                    list[position].timeTo = "$selectedHour:$selectedMinute"
                }, hour, minute, true) //Yes 24 hour time
                mTimePicker.setTitle("Select Time")
                mTimePicker.show()
            }

            // button for adding new layout
            addButton.setOnClickListener {
                ageFrom.clearFocus()
                ageTo.clearFocus()
                days.clearFocus()
                listner.addAgeGroupItem(list, position)
            }
        }
    }


    /*  var edtAgeFrom: EditText
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
      }*/


    interface OnAGeGroupRecyclerViewItemClickListner {
        fun addAgeGroupItem(list: ArrayList<ModelPOJO.AgeGroupDataModel>, position: Int)

    }

    /*   companion object {
           fun hideKeyboard(context: Context, view: View?) {
               if (view != null) {
                   val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                   imm.hideSoftInputFromWindow(view.windowToken, 0)
               }
           }
       }*/

}