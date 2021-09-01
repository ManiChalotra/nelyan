package com.nelyanlive.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nelyanlive.R
import com.nelyanlive.modals.postDetails.PostDetailsEvents
import com.nelyanlive.ui.ActivityDetailsActivity

class DetailsUpcomingAdapter(val context: Context?, internal var activityDetailsActivity: ActivityDetailsActivity, internal  var listUpcomingEvents: ArrayList<PostDetailsEvents>)
    : RecyclerView.Adapter<DetailsUpcomingAdapter.RecyclerViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.row_upcoming_details, parent, false)
        return RecyclerViewHolder(v)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        holder.bindItems(listUpcomingEvents[position])
    }

    override fun getItemCount(): Int {
        return listUpcomingEvents.size
    }


    inner class RecyclerViewHolder(view: View?) : RecyclerView.ViewHolder(view!!){

        var tvEventName = itemView.findViewById(R.id.tv_event_namee) as TextView
        var tvDescription = itemView.findViewById(R.id.tv_event_description) as TextView
        var tvEventPrice = itemView.findViewById(R.id.tv_event_price) as TextView

        var tvAgeLimit = itemView.findViewById(R.id.tv_age_limit) as TextView
        var tvEventDates = itemView.findViewById(R.id.tv_event_dates) as TextView
        var tvEventTime = itemView.findViewById(R.id.tv_event_times) as TextView

         fun bindItems(postDetailsEvents: PostDetailsEvents) {
             tvEventName.text = postDetailsEvents.name
             tvDescription.text = postDetailsEvents.description
             tvEventPrice.text ="€ "+postDetailsEvents.price

             if (postDetailsEvents.eventstimings !=null && postDetailsEvents.eventstimings.size !=0){
                 tvEventDates.text = context!!.getString(R.string.date1)+" "+ postDetailsEvents.eventstimings[0].dateFrom+" "+context.getString(R.string.to2)+" "+ postDetailsEvents.eventstimings[0].dateTo
                 tvEventTime.text = context.getString(R.string.time1)+" "+ postDetailsEvents.eventstimings[0].startTime+" "+context.getString(R.string.to_only_a)+" "+ postDetailsEvents.eventstimings[0].endTime
             }
         }
    }


    init {

    }

}




