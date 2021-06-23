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
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nelyanlive.R
import com.nelyanlive.modals.myAd.GetTraderMyAds
import com.nelyanlive.ui.TraderPublishActivty
import com.nelyanlive.utils.OpenActivity
import com.nelyanlive.utils.image_base_URl
import kotlinx.android.synthetic.main.item_trader_listing.view.*


class MyAdsTraderAdapter(var context: Context, private var myadsTraderlist: ArrayList<GetTraderMyAds>, internal var deleteEditTradersAdListner: OnDeleteEditTradersAdClickListner) :
        RecyclerView.Adapter<MyAdsTraderAdapter.RecyclerViewHolder>() {
    var inflater: LayoutInflater = LayoutInflater.from(context)
    var dialog: Dialog? = null
    private var popupWindow: PopupWindow? = null
    var dialog1: Dialog? = null
    var isMenuOpend = "0"

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
                callPopup(ivDot, adapterPosition, myadsList.id)
            }


            traderName.text = myadsList.nameOfShop

            when (myadsList.typeofTraderId.toString()) {
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

            tvEmail.text = myadsList.email
            tvTraderdesc.text = myadsList.description
            phone.text = myadsList.country_code+"-"+myadsList.phone
            tvAddress.text = myadsList.address

            if (myadsList.tradersimages.size >0)
                Glide.with(context).load(image_base_URl+ myadsList.tradersimages[0].images).error(R.mipmap.no_image_placeholder).into(ivTraderImage)
            else
                ivTraderImage.setImageResource(R.mipmap.no_image_placeholder)


            llTraderDetails.setOnClickListener {
                context.OpenActivity(TraderPublishActivty::class.java){
                    putString("postId", myadsList.id.toString())
                }

            }
        }

    }

    private fun callPopup(ivDot: ImageView, adapterPosition: Int, adId: Int) {
        val layoutInflater = context.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView: View = layoutInflater.inflate(R.layout.alert_dot, null)
        val editt = popupView.findViewById<View>(R.id.tvEdit) as TextView
        val deletee = popupView.findViewById<View>(R.id.tvDelete) as TextView
        popupWindow = PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true)

        popupWindow!!.isTouchable = true
        popupWindow!!.isOutsideTouchable = true


        popupWindow!!.showAsDropDown(ivDot, -250, 0, Gravity.RIGHT)

        editt.setOnClickListener {
            popupWindow!!.dismiss()

        }

        deletee.setOnClickListener {
            deleteEditTradersAdListner.onTraderAdDeleteAdClick(adapterPosition, adId.toString())
            popupWindow!!.dismiss()
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

    fun dailogDelete() {
        dialog1 = Dialog(context)
        dialog1!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog1!!.setContentView(R.layout.alert_chat_delete)
        dialog1!!.setCancelable(true)
        val rl_1: RelativeLayout = dialog1!!.findViewById(R.id.rl_1)
        val tvMessage = dialog1!!.findViewById<TextView>(R.id.tvMessage)
        tvMessage.text = "Are you sure want to\n delete my add?"
        rl_1.setOnClickListener {
            dialog1!!.dismiss()
        }
        dialog1!!.show()
    }


    interface OnDeleteEditTradersAdClickListner{
        fun onTraderAdDeleteAdClick(position: Int, adID: String?)
        fun onEditTraderAdClick(position: Int, adID: String?)
    }

}