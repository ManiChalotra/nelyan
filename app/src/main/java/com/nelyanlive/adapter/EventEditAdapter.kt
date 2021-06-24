package com.nelyanlive.adapter

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
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
        }
    }

    interface OnEventRecyclerViewItemClickListener {
        fun onAddEventItem(list: ArrayList<EventMyAds>, position: Int)
        fun addCameraGalleryImage( position: Int)
        fun cityAddEvent(list: ArrayList<EventMyAds>, position: Int, city: TextView)

    }
}


