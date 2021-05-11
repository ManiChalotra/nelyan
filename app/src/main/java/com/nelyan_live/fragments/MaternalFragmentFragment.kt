package com.nelyan_live.fragments

import android.content.Context
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
import com.nelyan_live.R
import com.nelyan_live.adapter.DetailsImageAdapter
import com.nelyan_live.modals.DetailsImageModal
import ru.tinkoff.scrollingpagerindicator.ScrollingPagerIndicator
import java.util.*

class MaternalFragmentFragment : Fragment(), OnMapReadyCallback {
    var mContext: Context? = null
    var v: View? = null
    var ivBack: ImageView? = null
    var btnModify: Button? = null
    var btnPublish: Button? = null
    var tvTitle: TextView? = null
    var ll_btns: LinearLayout? = null
    var mMap: GoogleMap? = null
    var indicator: ScrollingPagerIndicator? = null
    var rc: RecyclerView? = null
    var datalist = ArrayList<DetailsImageModal>()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        v = inflater.inflate(R.layout.fragment_maternal_fragment, container, false)
        mContext = activity
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)
        ivBack = v!!.findViewById(R.id.ivBack)
        ll_btns = v!!.findViewById(R.id.ll_btns)
        tvTitle = v!!.findViewById(R.id.tvTitle)
        ivBack!!.setOnClickListener(View.OnClickListener { requireActivity()!!.onBackPressed() })
        btnModify = v!!.findViewById(R.id.btnModify)
        btnModify!!.setOnClickListener(View.OnClickListener { requireActivity()!!.onBackPressed() })
        btnPublish = v!!.findViewById(R.id.btnPublish)
        btnPublish!!.setOnClickListener(View.OnClickListener {
            ll_btns!!.setVisibility(View.GONE)
            //  AppUtils.gotoFragment(mContext, new NurserieFragment(), R.id.frame_container, false);
        })
        rc = v!!.findViewById(R.id.rc_detailsimg)
        val lm = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        rc!!.setLayoutManager(lm)
        indicator = v!!.findViewById(R.id.indicator)
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