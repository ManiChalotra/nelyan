package com.nelyanlive.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nelyanlive.R
import com.nelyanlive.modals.postDetails.PostDetailsEvents
import com.nelyanlive.ui.ActivityDetailsActivity

class DetailsUpcomingAdapter(
    val context: Context?,
    internal var activityDetailsActivity: ActivityDetailsActivity,
    internal var listUpcomingEvents: ArrayList<PostDetailsEvents>
) : RecyclerView.Adapter<DetailsUpcomingAdapter.RecyclerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.row_upcoming_details, parent, false)
        return RecyclerViewHolder(v)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        holder.bindItems(listUpcomingEvents[position], position)
    }

    override fun getItemCount(): Int {
        return listUpcomingEvents.size
    }


    inner class RecyclerViewHolder(view: View?) : RecyclerView.ViewHolder(view!!) {

        var tvEventName = itemView.findViewById(R.id.tv_event_namee) as TextView
        var tvDescription = itemView.findViewById(R.id.tv_event_description) as TextView
        var tvEventPrice = itemView.findViewById(R.id.tv_event_price) as TextView

        var tvAgeLimit = itemView.findViewById(R.id.tv_age_limit) as TextView
        var tvEventDates = itemView.findViewById(R.id.tv_event_dates) as TextView
        var tvEventTime = itemView.findViewById(R.id.tv_event_times) as TextView

        var tvMinAgeEvent = itemView.findViewById(R.id.txt_minage_event) as TextView
        var tvMaxAgeEvent = itemView.findViewById(R.id.txt_maxage_event) as TextView

        fun bindItems(postDetailsEvents: PostDetailsEvents, position: Int) {
            tvEventName.text = postDetailsEvents.name
            tvDescription.text = postDetailsEvents.description
            tvEventPrice.text = "€ " + postDetailsEvents.price
            tvMinAgeEvent.text = postDetailsEvents.minAge
            tvMaxAgeEvent.text = postDetailsEvents.maxAge

            if (postDetailsEvents.eventstimings != null && postDetailsEvents.eventstimings.size != 0) {
                Log.e("postDetailsEventsssssss", "==11====${postDetailsEvents.eventstimings}===")
                Log.e(
                    "postDetailsEventsssssss",
                    "==22====${postDetailsEvents.eventstimings[position].dateFrom}==="
                )
                tvEventDates.text =
                    context!!.getString(R.string.date1) + " " + postDetailsEvents.eventstimings[position].dateFrom + " " + context.getString(
                        R.string.to2
                    ) + " " + postDetailsEvents.eventstimings[position].dateTo
                tvEventTime.text =
                    context.getString(R.string.time1) + " " + postDetailsEvents.eventstimings[position].startTime + " " + context.getString(
                        R.string.to_only_a
                    ) + " " + postDetailsEvents.eventstimings[position].endTime
            }
        }
    }


    init {

    }

}




