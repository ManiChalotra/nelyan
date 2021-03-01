package com.nelyan.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.recyclerview.widget.RecyclerView
import com.nelyan.R
import com.nelyan.ui.Chat1Activity

class MessageAdapter(var context: Context) : RecyclerView.Adapter<MessageAdapter.RecyclerViewHolder>() {
    var inflater: LayoutInflater
    var rl_1: RelativeLayout? = null

    inner class RecyclerViewHolder(view: View?) : RecyclerView.ViewHolder(view!!)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val v = inflater.inflate(R.layout.list_message, parent, false)
        val viewHolder = RecyclerViewHolder(v)
        rl_1 = v.findViewById(R.id.rl_1)
        rl_1!!.setOnClickListener(View.OnClickListener { context.startActivity(Intent(context, Chat1Activity::class.java)) })
        return viewHolder
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, i: Int) {}
    override fun getItemCount(): Int {
        return 5
    }

    init {
        inflater = LayoutInflater.from(context)
    }
}