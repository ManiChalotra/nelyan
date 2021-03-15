package com.nelyan_live.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nelyan_live.R
import kotlinx.android.synthetic.main.item_trader_listing.view.*

class TraderListingAdapter(var context:Context, var listner:OnTraderItemClickListner):RecyclerView.Adapter<TraderListingAdapter.MyViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_trader_listing, parent, false), listner)   }

    override fun getItemCount(): Int {
return 10    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
holder.initalize(position)    }

    inner  class MyViewHolder(itemView:View, var listner: OnTraderItemClickListner):RecyclerView.ViewHolder(itemView){
        val favourite = itemView.iv_fav
        val parentView = itemView.parentView

        fun initalize(position: Int){
            parentView.setOnClickListener{
                listner!!.onTraderListItemClickListner(position)
            }
            favourite.setOnClickListener {
                listner!!.onFavouriteItemClickListner(position)
            }
        }
    }

    interface OnTraderItemClickListner{
        fun onTraderListItemClickListner(position: Int)
        fun onFavouriteItemClickListner(position: Int)
    }
}