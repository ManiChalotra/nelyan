package com.nelyanlive.adapter

import android.app.Dialog
import android.content.Context
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nelyanlive.R
import com.nelyanlive.modals.myAd.GetTraderMyAds
import com.nelyanlive.modals.myAd.TraderDaysTimingMyAds
import com.nelyanlive.modals.myAd.TraderProductMyAds
import com.nelyanlive.modals.myAd.TradersimageMyAds
import com.nelyanlive.ui.TraderPublishActivty
import com.nelyanlive.utils.OpenActivity
import com.nelyanlive.utils.image_base_URl
import kotlinx.android.synthetic.main.alert_chat_delete.*
import kotlinx.android.synthetic.main.item_trader_listing.view.*


class MyAdsTraderAdapter(var context: Context, internal var myadsTraderlist: ArrayList<GetTraderMyAds>, internal var deleteEditTradersAdListner: OnDeleteEditTradersAdClickListner) :
    RecyclerView.Adapter<MyAdsTraderAdapter.RecyclerViewHolder>() {
    var inflater: LayoutInflater = LayoutInflater.from(context)
    var dialog1: Dialog? = null
    private var popupWindow: PopupWindow? = null

    inner class RecyclerViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val ivDot = itemView.iv_fav
        val ivTraderImage = itemView.iv_trader_image
        val traderName = itemView.trader_name
        val tvShopType = itemView.tv_shop_type
        val tvEmail = itemView.tv_email
        val phone = itemView.phone
        val currently = itemView.currently
        val llTraderDetails = itemView.ll_trader_details
        val tvAddress = itemView.tv_address
        val tvTraderdesc = itemView.tv_traderdesc


        fun bind(myadsList: GetTraderMyAds) {
            ivDot.setImageResource(R.drawable.option_dot)
            ivDot.setOnClickListener {
                Log.e("Dot Click", "Clikkceckk")
                callPopup(ivDot, adapterPosition, myadsList.id, myadsList.typeofTraderId, myadsList.nameOfShop, myadsList.description,
                    myadsList.country_code, myadsList.phone, myadsList.address,myadsList.city,myadsList.latitude,myadsList.longitude, myadsList.email, myadsList.website, myadsList.tradersimages,
                    myadsList.traderDaysTimings, myadsList.traderProducts)
            }


            traderName.text = myadsList.nameOfShop
            val traderTypeId= myadsList.typeofTraderId.toString()

            when (traderTypeId) {
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


            if( myadsList.email.isNotBlank())
            {
                tvEmail.text = myadsList.email
                tvEmail.visibility =View.VISIBLE
            }
            else
            {
                tvEmail.visibility =View.GONE
            }


            tvTraderdesc.text = myadsList.description
            phone.text = myadsList.country_code+"-"+myadsList.phone
            tvAddress.text = myadsList.address

            if (myadsList.tradersimages.size >0)
                Glide.with(context).load(image_base_URl+myadsList.tradersimages.get(0).images).error(R.mipmap.no_image_placeholder).into(ivTraderImage)
            else
                ivTraderImage.setImageResource(R.mipmap.no_image_placeholder)


            llTraderDetails.setOnClickListener {
                context.OpenActivity(TraderPublishActivty::class.java){

                    Log.e("fafsdfa","=====${myadsList.latitude}=====${myadsList.longitude}=====")

                    putString("postId", myadsList.id.toString())
                    putString("latti", myadsList.latitude)
                    putString("longi", myadsList.longitude)
                }

            }
        }

    }



    private fun callPopup(ivDot: ImageView, adapterPosition: Int, id: Int, typeofTraderId: Int, nameOfShop: String,
                          description: String, countryCode: String, phone: String, address: String,city: String,lati: String,longi: String, email: String, website: String,
                          tradersimages: ArrayList<TradersimageMyAds>, traderDaysTimings: ArrayList<TraderDaysTimingMyAds>, traderProducts: ArrayList<TraderProductMyAds>) {
        val layoutInflater = context.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView: View = layoutInflater.inflate(R.layout.alert_dot, null)
        val editt = popupView.findViewById<View>(R.id.tvEdit) as TextView
        val deletee = popupView.findViewById<View>(R.id.tvDelete) as TextView
        popupWindow = PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true)

        popupWindow!!.isTouchable = true
        popupWindow!!.isOutsideTouchable = true


        popupWindow!!.showAsDropDown(ivDot, -250, 0, Gravity.END)

        editt.setOnClickListener {
            popupWindow!!.dismiss()
            deleteEditTradersAdListner.onEditTraderAdClick(adapterPosition, id.toString(), typeofTraderId.toString(), nameOfShop, description.toString(), countryCode,
                phone, address,city, lati, longi, email, website, tradersimages, traderDaysTimings, traderProducts)


        }

        deletee.setOnClickListener {
            dailogDelete(adapterPosition, id.toString() )
            popupWindow!!.dismiss()
//            deleteEditTradersAdListner.onTraderAdDeleteAdClick(adapterPosition, id.toString())
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val v = inflater.inflate(R.layout.item_trader_listing, parent, false)
        return RecyclerViewHolder(v)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {

        holder.bind(myadsTraderlist[position])

    }

    override fun getItemCount(): Int {
        return myadsTraderlist.size
    }

    fun dailogDelete(adapterPosition: Int, adId: String) {
        dialog1 = Dialog(context)
        dialog1!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog1!!.setContentView(R.layout.alert_chat_delete)
        dialog1!!.setCancelable(true)
        val tvMessage = dialog1!!.findViewById<TextView>(R.id.tvMessage)
        tvMessage.text = context.getString(R.string.are_you_sure_want_to_delete_this)
        dialog1!!.tvYes.setOnClickListener {
            dialog1!!.dismiss()
            myadsTraderlist.removeAt(adapterPosition)
            notifyItemRemoved(adapterPosition)
            deleteEditTradersAdListner.onTraderAdDeleteAdClick(adapterPosition, adId)
        }
        dialog1!!.tvNo.setOnClickListener {
            dialog1!!.dismiss()
        }
        dialog1!!.show()
    }

    interface OnDeleteEditTradersAdClickListner{
        fun onTraderAdDeleteAdClick(position: Int, adID: String?)
        fun onEditTraderAdClick(position: Int, adID: String?, traderTypeId: String, nameofShop: String,
                                description: String, countryCode: String, phoneNumber: String, address: String,city: String,lati: String,longi: String, email: String, website: String,
                                traderImageList : ArrayList<TradersimageMyAds>, daytimeList : ArrayList<TraderDaysTimingMyAds>,
                                traderProductList : ArrayList<TraderProductMyAds>)
    }

}
