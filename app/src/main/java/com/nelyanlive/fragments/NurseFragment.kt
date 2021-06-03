package com.nelyanlive.fragments

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.nelyanlive.R
import com.nelyanlive.modals.DetailsImageModal
import com.nelyanlive.ui.HomeActivity
import com.nelyanlive.ui.NurserieActivityy
import com.nelyanlive.utils.OpenActivity
import ru.tinkoff.scrollingpagerindicator.ScrollingPagerIndicator
import java.util.*

class NurseFragment : Fragment(), OnMapReadyCallback {


    lateinit  var mContext: Context
    lateinit  var v: View
    var rc: RecyclerView? = null
    var rc_detailsimg: RecyclerView? = null
    var iv_msg: ImageView? = null
    var ivBack: ImageView? = null
    var ivShare: ImageView? = null
    var btnModify: Button? = null
    var btnPublish: Button? = null
    var dialog: Dialog? = null
    var mMap: GoogleMap? = null
    var tvTitle: TextView? = null
    var indicator: ScrollingPagerIndicator? = null
    var datalist = ArrayList<DetailsImageModal>()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.activity_home_child_care_details, container, false)
        mContext = requireActivity()
        /*  Bundle bundle = getArguments();
        String message = bundle.getString("message");
        tvTitle.setText(message);*/
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)
        ivBack = v.findViewById(R.id.ivBack)
        tvTitle = v.findViewById(R.id.tvTitle)
        ivBack!!.setOnClickListener(View.OnClickListener { requireActivity().onBackPressed() })
        btnModify = v.findViewById(R.id.btnModify)
        btnModify!!.setOnClickListener(View.OnClickListener { requireActivity().onBackPressed() })
        btnPublish = v.findViewById(R.id.btnPublish)
        btnPublish!!.setOnClickListener(View.OnClickListener {

            requireActivity().OpenActivity(NurserieActivityy::class.java)
            //AppUtils.gotoFragment(mContext, com.nelyan_live.fragments.NurserieFragment(), R.id.frame_container, false)
        })
        ivShare = v.findViewById(R.id.ivShare)
        ivShare!!.setOnClickListener(View.OnClickListener { dailogDelete() })
        rc = v.findViewById(R.id.rc_detailsimg)
        val lm = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        rc!!.layoutManager = lm
        indicator = v.findViewById(R.id.indicator)
        datalist.add(DetailsImageModal(R.drawable.img_1))
        datalist.add(DetailsImageModal(R.drawable.img_4))
        datalist.add(DetailsImageModal(R.drawable.img_1))
        datalist.add(DetailsImageModal(R.drawable.img_4))
        datalist.add(DetailsImageModal(R.drawable.img_1))
       /* val ad = DetailsImageAdapter(requireActivity()!!, datalist)
        rc!!.setAdapter(ad)
        indicator!!.attachToRecyclerView(rc!!)
       */ return v
    }


    fun dailogDelete() {
        dialog = Dialog(mContext)
        dialog!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog!!.setContentView(R.layout.alert_share)
        dialog!!.setCancelable(true)
        val ll_1: LinearLayout
        ll_1 = dialog!!.findViewById(R.id.ll_public)
        ll_1.setOnClickListener {
            mContext.startActivity(Intent(mContext, HomeActivity::class.java))
            dialog!!.dismiss()
        }
        dialog!!.show()
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        googleMap.mapType = GoogleMap.MAP_TYPE_NORMAL
        val india = LatLng(48.946697, 2.153927)
        mMap!!.addMarker(MarkerOptions()
                .position(india)
                .title("Marker in Sydney"))
        mMap!!.moveCamera(CameraUpdateFactory.newLatLng(india))
    }


}