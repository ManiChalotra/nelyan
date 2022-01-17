package com.nelyanlive.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nelyanlive.R
import com.nelyanlive.modals.myFavourite.FavouriteEvent
import com.nelyanlive.utils.image_base_URl

class MyFavoriteEventsAdapter(
    internal var favouriteEventList: ArrayList<FavouriteEvent>,
    internal var favListner: OnFavEventClickListner) : RecyclerView.Adapter<MyFavoriteEventsAdapter.RecyclerViewHolder>() {
    var context: Context? = null
    var inflater: LayoutInflater? = null
    var rl_1: RelativeLayout? = null

    inner class RecyclerViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var ivFavourtie = itemView.findViewById(R.id.iv_favourtie) as ImageView
        var ivEventImage = itemView.findViewById(R.id.iv_event_image) as ImageView
        var tvEvent_name = itemView.findViewById(R.id.tv_event_name) as TextView
        var tvLocation = itemView.findViewById(R.id.tv_address) as TextView
        var tvDescription = itemView.findViewById(R.id.tv_description) as TextView
        var tvMsg = itemView.findViewById(R.id.tv_activity_title) as TextView
        var tvAvailablePlace = itemView.findViewById(R.id.tv_available_place) as TextView

        fun bindMethod(favouriteEvent: FavouriteEvent) {
            if (favouriteEvent.event !=null)
            {
                tvEvent_name.text = favouriteEvent.event.name
                tvLocation.text = favouriteEvent.event.city
                tvDescription.text = favouriteEvent.event.description
                tvMsg.text = "$"+favouriteEvent.event.price

                Glide.with(context!!).load(image_base_URl+favouriteEvent.event.image).error(R.mipmap.no_image_placeholder).into(ivEventImage)
                if (favouriteEvent.event.eventstimings !=null && favouriteEvent.event.eventstimings.size !=0){
                    tvAvailablePlace.text = context!!.getString(R.string.startdate1)+" "+ favouriteEvent.event.eventstimings[0].dateFrom
                } }
            ivFavourtie.setOnClickListener {
                favListner.onAddFavoriteEventClick(adapterPosition, favouriteEvent.eventId.toString(), ivFavourtie)
                favouriteEventList.removeAt(adapterPosition)
                notifyItemRemoved(adapterPosition)

            } }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.list_favorite, parent, false)
        context = parent.context
        return RecyclerViewHolder(v)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        holder.bindMethod(favouriteEventList[position])
    }

    override fun getItemCount(): Int {
        return favouriteEventList.size
    }

    interface OnFavEventClickListner{
        fun onItemClickListner(position: Int)
        fun onAddFavoriteEventClick(position: Int, postId: String?, ivFavourite: ImageView)
    }
}