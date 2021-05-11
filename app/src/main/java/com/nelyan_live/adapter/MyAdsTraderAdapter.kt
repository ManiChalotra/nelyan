package com.nelyan_live.adapter

import android.app.Dialog
import android.content.Context
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.content.Intent
import android.util.Log
import android.view.*
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nelyan_live.R
import com.nelyan_live.modals.myAd.GetTraderMyAds
import com.nelyan_live.ui.AddActivity
import com.nelyan_live.ui.TraderPublishActivty
import com.nelyan_live.utils.OpenActivity
import com.nelyan_live.utils.image_base_URl
import kotlinx.android.synthetic.main.item_trader_listing.view.*


class MyAdsTraderAdapter(var context: Context, internal var myadsTraderlist: ArrayList<GetTraderMyAds>, internal var deleteEditTradersAdListner: OnDeleteEditTradersAdClickListner) :
        RecyclerView.Adapter<MyAdsTraderAdapter.RecyclerViewHolder>() {
    var inflater: LayoutInflater
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


/*
        var tvEdit = itemView.findViewById(R.id.tvEdit) as TextView
        var ll_1 = itemView.findViewById(R.id.ll_public) as LinearLayout
        var tvAdName = itemView.findViewById(R.id.tv_ad_name) as TextView
        var tvLocation = itemView.findViewById(R.id.tv_location) as TextView
        var tvActivityname = itemView.findViewById(R.id.tv_activityname) as TextView
        var tvDescription = itemView.findViewById(R.id.tv_description) as TextView
        var tvMsg = itemView.findViewById(R.id.tv_msg) as TextView
        var tvDelete = itemView.findViewById(R.id.tvDelete) as TextView
        var ivDot = itemView.findViewById(R.id.iv_dot) as ImageView
        var imageAds = itemView.findViewById(R.id.image_ads) as ImageView
        // var iv_cncl = itemView.findViewById(R.id.tvDelete) as ImageView
*/

        fun bind(myadsList: GetTraderMyAds) {
            ivDot.setImageResource(R.drawable.option_dot)

            ivDot.setOnClickListener {
                Log.e("Dot Click", "Clikkceckk")
                callPopup(ivDot, adapterPosition, myadsList.id)
            }


            traderName.text = myadsList.nameOfShop
            var traderTypeId= myadsList.typeofTraderId.toString()

            if (traderTypeId.equals("15")) {
                tvShopType.text = context.getString(R.string.doctor)
            } else if (traderTypeId.equals("16")) {
                tvShopType.text = context.getString(R.string.food_court)
            } else if (traderTypeId.equals("17")) {
                tvShopType.text = context.getString(R.string.plant_nursury)
            } else if (traderTypeId.equals("18")) {
                tvShopType.text = context.getString(R.string.tutor_mathematics)
            } else if (traderTypeId.equals("19")) {
                tvShopType.text = context.getString(R.string.gym_trainer)
            } else if (traderTypeId.equals("20")) {
                tvShopType.text = context.getString(R.string.yoga_trainer)
            } else if (traderTypeId.equals("21")) {
                tvShopType.text = context.getString(R.string.gadget_repair)
            }

            tvEmail.text = myadsList.email
            tvTraderdesc.text = myadsList.description
            phone.text = myadsList.country_code+"-"+myadsList.phone
            tvAddress.text = myadsList.address

            if (myadsList.tradersimages.size >0)
                Glide.with(context).load(image_base_URl+myadsList.tradersimages.get(0).images).error(R.mipmap.no_image_placeholder).into(ivTraderImage)
            else
                ivTraderImage.setImageResource(R.mipmap.no_image_placeholder)


            llTraderDetails.setOnClickListener {
                context.OpenActivity(TraderPublishActivty::class.java){
                    putString("postId", myadsList.id.toString())
                }

            }
        }

    }

    /* private fun showPopup(view: View) {

         val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

         val layout: View = inflater.inflate(R.layout.alert_dot, findViewById(R.id.popup_element) as ViewGroup?, false)

         val pwindo = PopupWindow(layout, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true)

         //get txt view from "layout" which will be added into popup window
         //before it you tried to find view in activity container

         //get txt view from "layout" which will be added into popup window
         //before it you tried to find view in activity container
         val txt = layout.findViewById(R.id.txtView) as TextView
         txt.text = Html.fromHtml(getString(R.string.tos_text))

      *//*   val popup = PopupMenu(context, view)
        popup.inflate(R.menu.popupmenu)

        popup.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item: MenuItem? ->

            when (item!!.itemId) {
                R.id.edit_ad -> {
                    Toast.makeText(context, item.title, Toast.LENGTH_SHORT).show()
                }
                R.id.delete_ad -> {
                    Toast.makeText(context, item.title, Toast.LENGTH_SHORT).show()
                }
            }

            true
        })

        popup.show()*//*
    }
*/

    private fun callPopup(ivDot: ImageView, adapterPosition: Int, adId: Int) {
        val layoutInflater = context.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView: View = layoutInflater.inflate(R.layout.alert_dot, null)
        val editt = popupView.findViewById<View>(R.id.tvEdit) as TextView
        val deletee = popupView.findViewById<View>(R.id.tvDelete) as TextView
        popupWindow = PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true)

        popupWindow!!.setTouchable(true)
       // popupWindow!!.setFocusable(true)
        popupWindow!!.isOutsideTouchable = true


        popupWindow!!.showAsDropDown(ivDot, -300, 0, Gravity.RIGHT)

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

/*
        holder.tvEdit.setOnClickListener { //  holder.ll_1.setVisibility(View.GONE);
            context.startActivity(Intent(context, AddActivity::class.java))
        }
        holder.tvDelete.setOnClickListener { //  holder.ll_1.setVisibility(View.GONE);
            dailogDelete()
        }
*/
    }

    override fun getItemCount(): Int {
        return myadsTraderlist.size
    }

    fun dailogDelete() {
        dialog1 = Dialog(context)
        dialog1!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog1!!.setContentView(R.layout.alert_chat_delete)
        dialog1!!.setCancelable(true)
        val rl_1: RelativeLayout
        rl_1 = dialog1!!.findViewById(R.id.rl_1)
        val tvMessage = dialog1!!.findViewById<TextView>(R.id.tvMessage)
        tvMessage.text = "Are you sure want to\n delete my add?"
        rl_1.setOnClickListener { //  mContext.startActivity(new Intent(mContext, HomeActivity.class));
            dialog1!!.dismiss()
        }
        dialog1!!.show()
    }

    fun dailogDot() {
        dialog = Dialog(context)
        dialog!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog!!.setContentView(R.layout.alert_dot)
        dialog!!.setCancelable(true)
        dialog!!.setCanceledOnTouchOutside(true)

        val window = dialog!!.window
        val wlp = window!!.attributes
        wlp.gravity = Gravity.RIGHT or Gravity.TOP
        wlp.x = -10 //x position
        wlp.y = 0
        wlp.flags = wlp.flags and WindowManager.LayoutParams.FLAG_DIM_BEHIND.inv()
        window.attributes = wlp
        val tvEdit = dialog!!.findViewById<TextView>(R.id.tvEdit)
        val tvDelet = dialog!!.findViewById<TextView>(R.id.tvDelete)
        //  tvEdit = dialog!!.findViewById(R.id.tvEdit)
        tvEdit.setOnClickListener {
            context.startActivity(Intent(context, AddActivity::class.java))
            dialog!!.dismiss()
        }
        tvDelet.setOnClickListener {
            dailogDelete()
            dialog!!.dismiss()
        }
        dialog!!.show()
    }

    init {
        inflater = LayoutInflater.from(context)
    }

    interface OnDeleteEditTradersAdClickListner{
        fun onTraderAdDeleteAdClick(position: Int, adID: String?)
        fun onEditTraderAdClick(position: Int, adID: String?)
    }

}