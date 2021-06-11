package com.nelyanlive.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nelyanlive.R
import com.nelyanlive.modals.hometraderpostlist.HomeTraderListData
import com.nelyanlive.utils.image_base_URl
import kotlinx.android.synthetic.main.item_trader_listing.view.*

class TraderListingAdapter(var context: Context, private var traderPostList: ArrayList<HomeTraderListData>, var listner: OnTraderItemClickListner) : RecyclerView.Adapter<TraderListingAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_trader_listing, parent, false), listner)
    }

    override fun getItemCount(): Int {
        return traderPostList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.initalize(traderPostList[position])
    }

    inner class MyViewHolder(itemView: View, var listner: OnTraderItemClickListner) : RecyclerView.ViewHolder(itemView) {
        val favourite = itemView.iv_fav
        val ivTraderImage = itemView.iv_trader_image
        val traderName = itemView.trader_name
        val tvShopType = itemView.tv_shop_type
        val tvEmail = itemView.tv_email
        val phone = itemView.phone
        val currently = itemView.currently
        val llTraderDetails = itemView.ll_trader_details
        val tvAddress = itemView.tv_address
        val tvTraderdesc = itemView.tv_traderdesc

        fun initalize(traderPostList: HomeTraderListData) {
            traderName.text = traderPostList.nameOfShop

            when (traderPostList.typeofTraderId.toString()) {
                "15" -> {
                    tvShopType.text = context.getString(R.string.doctor)
                }
                "16" -> {
                    tvShopType.text = context.getString(R.string.food_court)
                }
                "17" -> {
                    tvShopType.text = context.getString(R.string.plant_nursury)
                }
                "18" -> {
                    tvShopType.text = context.getString(R.string.tutor_mathematics)
                }
                "19" -> {
                    tvShopType.text = context.getString(R.string.gym_trainer)
                }
                "20" -> {
                    tvShopType.text = context.getString(R.string.yoga_trainer)
                }
                "21" -> {
                    tvShopType.text = context.getString(R.string.gadget_repair)
                }
            }

            tvEmail.text = traderPostList.email
            tvTraderdesc.text = traderPostList.description
            if(traderPostList.phone.isNotBlank())
            {
                phone.text = traderPostList.country_code+"-"+traderPostList.phone

            }
            else
            {
                phone.visibility = View.GONE

            }

            tvAddress.text = traderPostList.address+" "+traderPostList.city

            if (traderPostList.tradersimages.size >0)
            Glide.with(context).load(image_base_URl+ traderPostList.tradersimages[0].images).error(R.mipmap.no_image_placeholder).into(ivTraderImage)
            else
                ivTraderImage.setImageResource(R.mipmap.no_image_placeholder)


            if (traderPostList.isFav ==1){
                favourite!!.setImageResource(R.drawable.heart)
            }else {
                favourite!!.setImageResource(R.drawable.heart_purple)

            }


            favourite.setOnClickListener {
                listner.onFavouriteItemClickListner(adapterPosition, traderPostList.id.toString(), favourite)
            }

            llTraderDetails.setOnClickListener {
                listner.onTraderListItemClickListner(adapterPosition, traderPostList.id.toString(), traderPostList.latitude, traderPostList.longitude)

            }
        }
    }

    interface OnTraderItemClickListner {
        fun onTraderListItemClickListner(
            position: Int,
            postId: String,
            latitude: String,
            longitude: String
        )
        fun onFavouriteItemClickListner(position: Int, postID: String, favourite: ImageView)
    }
}