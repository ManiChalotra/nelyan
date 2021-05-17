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
import com.nelyan_live.modals.myAd.ActivityimageMyAds
import com.nelyan_live.modals.myAd.AgeGroupMyAds
import com.nelyan_live.modals.myAd.EventMyAds
import com.nelyan_live.modals.myAd.GetActivitypostMyAds

import com.nelyan_live.ui.ActivityDetailsActivity
import com.nelyan_live.ui.AddActivity
import com.nelyan_live.utils.OpenActivity
import com.nelyan_live.utils.image_base_URl
import kotlinx.android.synthetic.main.alert_chat_delete.*


class MyAddAdapter(var context: Context, internal var myadsActivitylist: ArrayList<GetActivitypostMyAds>,
                   internal var activitiesDeleteEditListener: OnActivitiesDeleteEditClickListner) :
        RecyclerView.Adapter<MyAddAdapter.RecyclerViewHolder>() {
    var inflater: LayoutInflater
    var dialog: Dialog? = null
    private var popupWindow: PopupWindow? = null
    var dialog1: Dialog? = null
    var isMenuOpend = "0"

    inner class RecyclerViewHolder(view: View) : RecyclerView.ViewHolder(view) {

     /* //  var tvEdit = itemView.findViewById(R.id.tvEdit) as TextView
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

        var tvAddress = itemView.findViewById(R.id.tv_address) as TextView
        var tvActivityname = itemView.findViewById(R.id.tv_activityname) as TextView
        var tvNameOfShop = itemView.findViewById(R.id.tv_nameOfShop) as TextView
        var tvDescription = itemView.findViewById(R.id.tv_description) as TextView
        var tvMsg = itemView.findViewById(R.id.tv_available_place) as TextView
        var ivActivityImage = itemView.findViewById(R.id.iv_activity_image) as ImageView
        var ivDot = itemView.findViewById(R.id.iv_favouritessss) as ImageView
        var rl_1 = itemView.findViewById(R.id.rl_1) as RelativeLayout
        var llActivityDetails = itemView.findViewById(R.id.ll_activity_details) as LinearLayout


        fun bind(myadsList: GetActivitypostMyAds) {
            ivDot.setImageResource(R.drawable.option_dot)

            ivDot.setOnClickListener {
                Log.e("Dot Click", "Clikkceckk")
                callPopup(ivDot, adapterPosition, myadsList.id, myadsList.activityname, myadsList.typeofActivityId,
                        myadsList.nameOfShop, myadsList.description, myadsList.address, myadsList.city, myadsList.country_code,
                        myadsList.phone, myadsList.ageGroups, myadsList.events, myadsList.activityimages, myadsList.latitude, myadsList.longitude)

            }

            tvActivityname.text = myadsList.activityname
            tvDescription.text = myadsList.description
            tvAddress.text = myadsList.address +" "+myadsList.city
            tvNameOfShop.text = myadsList.nameOfShop

            if (myadsList.typeofActivityId == 5){
                tvMsg.text = context!!.getString(R.string.type)+" "+context!!.getString(R.string.sports)

            } else if (myadsList.typeofActivityId == 9){
                tvMsg.text = context!!.getString(R.string.type)+" "+context!!.getString(R.string.dance)

            } else if (myadsList.typeofActivityId == 10){
                tvMsg.text = context!!.getString(R.string.type)+" "+context!!.getString(R.string.drawing)

            }else if (myadsList.typeofActivityId == 11){
                tvMsg.text = context!!.getString(R.string.type)+" "+context!!.getString(R.string.zumba)

            }else if (myadsList.typeofActivityId == 13){
                tvMsg.text = context!!.getString(R.string.type)+" "+context!!.getString(R.string.tutor_mother_subject)

            }


            if (myadsList.activityimages.size !=null && myadsList.activityimages.size !=0)
                    Glide.with(context).load(image_base_URl + myadsList.activityimages.get(0).images).error(R.mipmap.no_image_placeholder).into(ivActivityImage)
                else
                ivActivityImage.setImageResource(R.mipmap.no_image_placeholder)


            llActivityDetails!!.setOnClickListener{

                context?.OpenActivity(ActivityDetailsActivity::class.java){
                    putString("activityId", myadsList.id.toString())
                    putString("categoryId", myadsList.categoryId.toString())
                    putString("lati", myadsList.latitude)
                    putString("longi", myadsList.longitude)
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

    private fun callPopup(ivDot: ImageView, adapterPosition: Int, adId: Int, activityname: String, typeofActivityId: Int,
                          nameOfShop: String, description: String, address: String, city: String, countryCode: String, phone: String,
                          ageGroups: ArrayList<AgeGroupMyAds>, events: ArrayList<EventMyAds>, activityimages: ArrayList<ActivityimageMyAds>,
                          latitude: String, longitude: String)
    {
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
            activitiesDeleteEditListener.onEditActivitiesAdClick(adapterPosition, adId.toString(), typeofActivityId.toString(), nameOfShop, activityname,
                     description, countryCode, phone, address, city, ageGroups,  activityimages, events, latitude, longitude,  )
            popupWindow!!.dismiss()
        }

        deletee.setOnClickListener {
            dailogDelete(adapterPosition, adId.toString())
            popupWindow!!.dismiss()
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val v = inflater.inflate(R.layout.list_activitylist, parent, false)
        return RecyclerViewHolder(v)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        holder.bind(myadsActivitylist[position])
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
        return myadsActivitylist.size
    }

    fun dailogDelete(adapterPosition: Int, adId: String) {
        dialog1 = Dialog(context)
        dialog1!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog1!!.setContentView(R.layout.alert_chat_delete)
        dialog1!!.setCancelable(true)
        val rl_1: RelativeLayout
        rl_1 = dialog1!!.findViewById(R.id.rl_1)
        val tvMessage = dialog1!!.findViewById<TextView>(R.id.tvMessage)
        tvMessage.text = "Are you sure want to\n delete my add?"
        dialog1!!.tvNo.setOnClickListener { //  mContext.startActivity(new Intent(mContext, HomeActivity.class));
            activitiesDeleteEditListener.onActivitiesDeleteAdClick(adapterPosition, adId)
            dialog1!!.dismiss()
        }
        dialog1!!.tvNo.setOnClickListener { //  mContext.startActivity(new Intent(mContext, HomeActivity.class));
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
         //   dailogDelete(adapterPosition, adId.toString())
            dialog!!.dismiss()
        }
        dialog!!.show()
    }

    init {
        inflater = LayoutInflater.from(context)
    }

    interface OnActivitiesDeleteEditClickListner {
        fun onActivitiesDeleteAdClick(position: Int, adID: String? )
        fun onEditActivitiesAdClick(position: Int, adID: String?, activityTypeId: String, nameofShop: String, nameofActivity: String,
                                    description: String, countryCode: String, phoneNumber: String, address: String, city: String,
                                    ageGroupListMyAds: ArrayList<AgeGroupMyAds>, ActivityimagesList: ArrayList<ActivityimageMyAds>,
                                    eventMyAdsList: ArrayList<EventMyAds>,
                                    latitude: String,
                                    longitude: String)
    }

}