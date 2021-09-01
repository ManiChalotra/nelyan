package com.nelyanlive.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nelyanlive.R
import com.nelyanlive.fullscreen.FullScreen
import com.nelyanlive.modals.traderPostDetails.TraderProduct
import com.nelyanlive.utils.image_base_URl
import kotlinx.android.synthetic.main.item_traders_products.view.*
import java.util.*

class TradersProductListAdapter(var context: Context, var traderProductList: ArrayList<TraderProduct>) : RecyclerView.Adapter<TradersProductListAdapter.TraderProductViewHolder>()
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
            Glide.with(context).asBitmap().load(image_base_URl+list[position].image).error(R.mipmap.no_image_placeholder).into(image)
            name.text = list[position].title
            tvProductDesc.text = list[position].description
            tvProductPrice.text = list[position].price + " €"

            image.setOnClickListener {
                (image.context as Activity).startActivity(
                    Intent(image.context, FullScreen::class.java)
                    .putExtra("productImage",image_base_URl+list[position].image))
            }
        }
    }


}


