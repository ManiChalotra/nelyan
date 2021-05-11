package com.nelyan_live.adapter

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.TimePickerDialog
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nelyan_live.R
import com.nelyan_live.modals.ModelPOJO
import com.nelyan_live.modals.traderPostDetails.TraderProduct
import kotlinx.android.synthetic.main.item_event_add_more.view.*
import kotlinx.android.synthetic.main.item_traders_products.view.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class TradersProductListAdapter(var context: Context, var traderProductList: ArrayList<TraderProduct>,
                                ) : RecyclerView.Adapter<TradersProductListAdapter.TraderProductViewHolder>()
{

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TraderProductViewHolder {
        return TraderProductViewHolder(LayoutInflater.from(context).inflate(R.layout.item_traders_products, parent, false))
    }

    override fun onBindViewHolder(holder: TraderProductViewHolder, position: Int) {
        holder.initalize(traderProductList, position)
    }

    override fun getItemCount(): Int {
        return traderProductList.size
    }

    inner class TraderProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image = itemView.iv_product_image
        val name = itemView.tv_product_name
        val tvProductPrice = itemView.tv_product_price
        val tvProductDesc = itemView.tv_product_desc

        fun initalize(list: ArrayList<TraderProduct>, position: Int) {
            // setting data  here
            Glide.with(context).asBitmap().load(list.get(position).image).error(R.mipmap.no_image_placeholder).into(image)
            name.setText(list.get(position).title)
            tvProductDesc.setText(list.get(position).description)
            tvProductPrice.setText(list.get(position).price)

        }


    }


}


