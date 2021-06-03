package com.nelyanlive.adapter

/*import com.nelyan_live.utils.image_url_local*/
import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nelyanlive.R
import com.nelyanlive.modals.homeSectorization.GetPublic
import com.nelyanlive.utils.from_admin_image_base_URl

class PublicSchoolAdapter(activity: FragmentActivity, internal var datalist: ArrayList<GetPublic>) : RecyclerView.Adapter<PublicSchoolAdapter.Vh>() {
    var a: Activity
    var context: Context? = null
    var rl_1: RelativeLayout? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        val v = LayoutInflater.from(a).inflate(R.layout.list_sectorization, parent, false)
        context = parent.context
        return Vh(v)
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {

        holder.bindMethod(datalist[position])

     }

    override fun getItemCount(): Int {
        return datalist.size
    }

    inner class Vh(itemView: View) : RecyclerView.ViewHolder(itemView) {
        lateinit var tv_school_name: TextView
        lateinit var tvSchoolType: TextView
        lateinit var tv_schooladdress: TextView
        lateinit var tv_schoolPhone: TextView
        lateinit var tv_schoolWebsite: TextView
        lateinit var tvSchoolTime: TextView

        lateinit var eStartdate: TextView
        lateinit var eEnddate: TextView
        lateinit var ivSchool_image: ImageView
       

        fun bindMethod(publicSchoolList: GetPublic) {

            tv_school_name = itemView.findViewById(R.id.tv_school_name)
            tvSchoolType = itemView.findViewById(R.id.tv_school_type)
            tv_schooladdress = itemView.findViewById(R.id.tv_address)
            tv_schoolPhone = itemView.findViewById(R.id.tv_school_phone)
            tv_schoolWebsite = itemView.findViewById(R.id.tv_school_website)
            tvSchoolTime = itemView.findViewById(R.id.tv_school_time)

            ivSchool_image = itemView.findViewById(R.id.iv_school_image)

            if (publicSchoolList.sectorizationimages !=null && publicSchoolList.sectorizationimages.size !=0)
                Glide.with(context!!).load(from_admin_image_base_URl +publicSchoolList.sectorizationimages).error(R.mipmap.no_image_placeholder).into(ivSchool_image)


            tv_school_name.text = publicSchoolList.school_name
            tv_schooladdress.text = publicSchoolList.school_address
            tvSchoolType.text = publicSchoolList.school_level
            tv_schoolPhone.text = publicSchoolList.phone
            tv_schoolWebsite.text = publicSchoolList.school_site


        }

    }

    init {
        a = activity
        this.datalist = datalist
    }
}