package com.nelyan_live.fragments

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.nelyan_live.R
import com.nelyan_live.adapter.DetailsImageAdapter
import com.nelyan_live.modals.DetailsImageModal
import com.nelyan_live.ui.HomeActivity
import ru.tinkoff.scrollingpagerindicator.ScrollingPagerIndicator
import java.util.*

class TraderPublishFragment : Fragment(), OnMapReadyCallback {

 lateinit   var v: View
    lateinit  var mContext: Context
    var dialog: Dialog? = null
    var ivShare: ImageView? = null
    var ivBack: ImageView? = null
    var mMap: GoogleMap? = null
    var indicator: ScrollingPagerIndicator? = null
    var rc: RecyclerView? = null
    var datalist = ArrayList<DetailsImageModal>()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_trader_publish, container, false)
        mContext = requireActivity()
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)



        ivBack = v.findViewById(R.id.ivBack)
        ivBack!!.setOnClickListener(View.OnClickListener {
            try {
                val bundle = arguments
                if (bundle!!.getString("activity") == "traderfragment") {
                    requireActivity()!!.onBackPressed()
                } else {
                    val fm = requireActivity()!!.supportFragmentManager
                    val f = fm.findFragmentById(R.id.frame_container)
                    fm.popBackStack()
                }
            } catch (e: Exception) {
                val fm = requireActivity()!!.supportFragmentManager
                val f = fm.findFragmentById(R.id.frame_container)
                fm.popBackStack()
            }
        })

        ivShare = v.findViewById(R.id.ivShare)
        ivShare!!.setOnClickListener(View.OnClickListener { dailogShare() })

        rc = v.findViewById(R.id.rc_detailsimg)
        val lm = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        rc!!.setLayoutManager(lm)


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

    fun dailogShare() {
        dialog = Dialog(mContext!!)
        dialog!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog!!.setContentView(R.layout.alert_share)
        dialog!!.setCancelable(true)
        val ll_1: LinearLayout
        ll_1 = dialog!!.findViewById(R.id.ll_public)
        ll_1.setOnClickListener {
            mContext!!.startActivity(Intent(mContext, HomeActivity::class.java))
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