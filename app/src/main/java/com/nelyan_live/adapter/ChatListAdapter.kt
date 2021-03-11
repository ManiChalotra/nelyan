package com.nelyan_live.adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.nelyan_live.AppUtils
import com.nelyan_live.R

class ChatListAdapter(var context:Context, var listner:OnChatListItemClickListner) : RecyclerView.Adapter<ChatListAdapter.RecyclerViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {

        return RecyclerViewHolder(LayoutInflater.from(context).inflate(R.layout.list_activitylist, parent, false), listner)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        holder.initalize(position)
    }
    override fun getItemCount(): Int {
        return 3
    }

    inner class RecyclerViewHolder(view: View, var listner:OnChatListItemClickListner): RecyclerView.ViewHolder(view){
        fun initalize(position: Int){
            itemView.setOnClickListener {
                listner!!.onItemClickListner(position)
            }

        }
    }

    interface OnChatListItemClickListner{
        fun onItemClickListner(position: Int)
    }



}