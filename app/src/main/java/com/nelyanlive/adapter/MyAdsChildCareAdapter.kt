package com.nelyanlive.adapter

import android.app.Dialog
import android.content.Context
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.content.Intent
import android.util.Log
import android.view.*
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nelyanlive.R
import com.nelyanlive.modals.myAd.ChildCareImageMyAds
import com.nelyanlive.modals.myAd.GetChildcarePostMyAds
import com.nelyanlive.ui.AddActivity
import com.nelyanlive.ui.HomeChildCareDetailsActivity
import com.nelyanlive.utils.OpenActivity
import com.nelyanlive.utils.image_base_URl


class MyAdsChildCareAdapter(var context: Context, private var myadsChildCarelist: ArrayList<GetChildcarePostMyAds>, internal var deleteEditListner: OnDeleteEditClickListner) :
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
                callPopup(ivDot, adapterPosition, myadsList.id, myadsList.ChildcareType, myadsList.name, myadsList.availableplace, myadsList.description, myadsList.countryCode,
                    myadsList.phone, myadsList.city,myadsList.address,  myadsList.ChildCareImages, myadsList.latitude, myadsList.longitude)

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

                    Log.e("fafsdfa","===HomeChildCareDetailsActivity==${myadsList.latitude}=====${myadsList.longitude}=====")

                    putString("activityId", myadsList.id.toString())
                    putString("categoryId", myadsList.type.toString())
                    putString("latti", myadsList.latitude)
                    putString("longi", myadsList.longitude)
                }
            }
        }
    }

    private fun callPopup(ivDot: ImageView, adapterPosition: Int, id: Int, childcareType: Int, name: String, availableplace: Int,
                          description: String, countryCode: String, phone: String, city: String,address: String, childCareImages: ArrayList<ChildCareImageMyAds>,
                          latitude: String, longitude: String) {
        val layoutInflater = context.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView: View = layoutInflater.inflate(R.layout.alert_dot, null)
        val editt = popupView.findViewById<View>(R.id.tvEdit) as TextView
        val deletee = popupView.findViewById<View>(R.id.tvDelete) as TextView
        popupWindow = PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true)

        popupWindow!!.isTouchable = true
        popupWindow!!.isOutsideTouchable = true


        popupWindow!!.showAsDropDown(ivDot, -250, 0, Gravity.END)

        editt.setOnClickListener {
            deleteEditListner.onEditAdClick(adapterPosition, id.toString(), childcareType.toString(), name, availableplace.toString(),
                description, countryCode, phone, city,address, childCareImages, latitude, longitude)
            popupWindow!!.dismiss()
        }

        deletee.setOnClickListener {
            deleteEditListner.onChildCareDeleteAdClick(adapterPosition, id.toString() )
            popupWindow!!.dismiss()

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val v = inflater.inflate(R.layout.list_activitylist, parent, false)
        return RecyclerViewHolder(v)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {

        holder.bind(myadsChildCarelist[position])

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
        return myadsChildCarelist.size
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

    interface OnDeleteEditClickListner{
        fun onChildCareDeleteAdClick(position: Int, adID: String?)
        fun onEditAdClick(position: Int, adID: String?, childTypeId: String, name: String, noofplaces: String,
                          description: String, countryCode: String, phoneNumber: String, city: String,address: String,
                          childCareImageList : ArrayList<ChildCareImageMyAds>, latitude: String, longitude: String)
    }

}
