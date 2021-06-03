package com.nelyanlive.adapter

import android.app.Dialog
import android.content.Context
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nelyanlive.R
import com.nelyanlive.modals.myAd.GetChildcarePostMyAds
import com.nelyanlive.ui.HomeChildCareDetailsActivity
import com.nelyanlive.utils.OpenActivity
import com.nelyanlive.utils.image_base_URl


class MyAdsChildCareAdapter(var context: Context, internal var myadsChildCarelist: ArrayList<GetChildcarePostMyAds>, internal var deleteEditListner: OnDeleteEditClickListner) :
        RecyclerView.Adapter<MyAdsChildCareAdapter.RecyclerViewHolder>() {
    var inflater: LayoutInflater = LayoutInflater.from(context)
    var dialog: Dialog? = null
    private var popupWindow: PopupWindow? = null
    var dialog1: Dialog? = null
    var isMenuOpend = "0"

    inner class RecyclerViewHolder(view: View) : RecyclerView.ViewHolder(view) {



        var tvAddress = itemView.findViewById(R.id.tv_address) as TextView
        var tvActivityname = itemView.findViewById(R.id.tv_activityname) as TextView
        var tvNameOfShop = itemView.findViewById(R.id.tv_nameOfShop) as TextView
        var tvDescription = itemView.findViewById(R.id.tv_description) as TextView
        var tvMsg = itemView.findViewById(R.id.tv_available_place) as TextView
        var ivActivityImage = itemView.findViewById(R.id.iv_activity_image) as ImageView
        var ivDot = itemView.findViewById(R.id.iv_favouritessss) as ImageView
        var rl_1 = itemView.findViewById(R.id.rl_1) as RelativeLayout
        var llActivityDetails = itemView.findViewById(R.id.ll_activity_details) as LinearLayout


        fun bind(myadsList: GetChildcarePostMyAds) {

            ivDot.setImageResource(R.drawable.option_dot)

            ivDot.setOnClickListener {
                Log.e("Dot Click", "Clikkceckk")
                callPopup(ivDot, adapterPosition, myadsList.id)


            }

            tvActivityname.text= myadsList.name
            tvAddress.text= myadsList.city
            tvDescription.text= myadsList.description

            tvMsg.text = context.getString(R.string.available_place)+" "+myadsList.availableplace.toString()

            val childcareTypeId= myadsList.ChildcareType.toString()

            when (childcareTypeId) {
                "1" -> {
                    tvNameOfShop.text = context.getString(R.string.type1)+" "+ context.getString(R.string.nursery)
                }
                "2" -> {
                    tvNameOfShop.text = context.getString(R.string.type1)+" "+ context.getString(R.string.maternal_assistant)
                }
                "3" -> {
                    tvNameOfShop.text = context.getString(R.string.type1)+" "+ context.getString(R.string.babySitter)
                }
            }

            if (myadsList.ChildCareImages !=null && myadsList.ChildCareImages.size !=0){
                Glide.with(context).load(image_base_URl +myadsList.ChildCareImages.get(0).image).
                error(R.mipmap.no_image_placeholder).into(ivActivityImage)
            }

            llActivityDetails.setOnClickListener{
                context.OpenActivity(HomeChildCareDetailsActivity::class.java){
                    putString("activityId", myadsList.id.toString())
                    putString("categoryId", myadsList.type.toString())
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


        popupWindow!!.showAsDropDown(ivDot, -300, 0, Gravity.RIGHT)

        editt.setOnClickListener {
            deleteEditListner.onEditAdClick(adapterPosition, adId.toString())
            popupWindow!!.dismiss()
        }

        deletee.setOnClickListener {
            deleteEditListner.onChildCareDeleteAdClick(adapterPosition, adId.toString() )
            popupWindow!!.dismiss()

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val v = inflater.inflate(R.layout.list_activitylist, parent, false)
        return RecyclerViewHolder(v)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {

        holder.bind(myadsChildCarelist[position])

    }

    override fun getItemCount(): Int {
        return myadsChildCarelist.size
    }

    fun dailogDelete() {
        dialog1 = Dialog(context)
        dialog1!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog1!!.setContentView(R.layout.alert_chat_delete)
        dialog1!!.setCancelable(true)
        val rl_1: RelativeLayout = dialog1!!.findViewById(R.id.rl_1)
        val tvMessage = dialog1!!.findViewById<TextView>(R.id.tvMessage)
        tvMessage.text = "Are you sure want to\n delete my add?"
        rl_1.setOnClickListener { //  mContext.startActivity(new Intent(mContext, HomeActivity.class));
            dialog1!!.dismiss()
        }
        dialog1!!.show()
    }


    interface OnDeleteEditClickListner{
        fun onChildCareDeleteAdClick(position: Int, adID: String?)
        fun onEditAdClick(position: Int, adID: String?)
    }

}