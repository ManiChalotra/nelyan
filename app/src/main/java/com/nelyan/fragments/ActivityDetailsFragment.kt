package com.nelyan.fragments

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.nelyan.R
import com.nelyan.adapter.DetailsImageAdapter
import com.nelyan.adapter.DetailsTimeAdapter
import com.nelyan.adapter.DetailsUpcomingAdapter
import com.nelyan.modals.DetailsImageModal
import com.nelyan.modals.DetailsTimeModal
import com.nelyan.ui.Chat1Activity
import com.nelyan.ui.HomeActivity
import ru.tinkoff.scrollingpagerindicator.ScrollingPagerIndicator
import java.util.*

class ActivityDetailsFragment : Fragment(), OnMapReadyCallback {
    var v: View? = null
    var mContext: Context? = null
    var tvMon: TextView? = null
    var tvTue: TextView? = null
    var tvWed: TextView? = null
    var tvThur: TextView? = null
    var tvFri: TextView? = null
    var tvSat: TextView? = null
    var tvSun: TextView? = null
    var iv_msg: ImageView? = null
    var iv_back: ImageView? = null
    var iv_share: ImageView? = null
    var rc: RecyclerView? = null
    var rc_detailstime: RecyclerView? = null
    var rc_upcomingevents: RecyclerView? = null
    var Recycler_scroll: RecyclerView? = null
    var datalist = ArrayList<DetailsImageModal>()
    var datalisttime = ArrayList<DetailsTimeModal>()
    var mMap: GoogleMap? = null
    var dialog: Dialog? = null
    var indicator: ScrollingPagerIndicator? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_activity_details, container, false)
        mContext = activity
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)
        /*    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);*/mapFragment.getMapAsync(this)
        tvMon = v!!.findViewById(R.id.tvMon)
        /* tvTue=v.findViewById(R.id.tvTue);
        tvWed=v.findViewById(R.id.tvWed);
        tvThur=v.findViewById(R.id.tvThur);
        tvFri=v.findViewById(R.id.tvFri);
        tvSat=v.findViewById(R.id.tvSat);
        tvSun=v.findViewById(R.id.tvSun);
        tvMon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvMon.setBackground(getResources().getDrawable(R.drawable.selected_outlinbg));
                tvTue.setBackground(getResources().getDrawable(R.drawable.text_outlinebg));
                tvWed.setBackground(getResources().getDrawable(R.drawable.text_outlinebg));
                tvThur.setBackground(getResources().getDrawable(R.drawable.text_outlinebg));
                tvFri.setBackground(getResources().getDrawable(R.drawable.text_outlinebg));
                tvSat.setBackground(getResources().getDrawable(R.drawable.text_outlinebg));
                tvSun.setBackground(getResources().getDrawable(R.drawable.text_outlinebg));
            }
        });
        tvTue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvMon.setBackground(getResources().getDrawable(R.drawable.text_outlinebg));
                tvTue.setBackground(getResources().getDrawable(R.drawable.selected_outlinbg));
                tvWed.setBackground(getResources().getDrawable(R.drawable.text_outlinebg));
                tvThur.setBackground(getResources().getDrawable(R.drawable.text_outlinebg));
                tvFri.setBackground(getResources().getDrawable(R.drawable.text_outlinebg));
                tvSat.setBackground(getResources().getDrawable(R.drawable.text_outlinebg));
                tvSun.setBackground(getResources().getDrawable(R.drawable.text_outlinebg));
            }
        }); tvWed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvMon.setBackground(getResources().getDrawable(R.drawable.text_outlinebg));
                tvTue.setBackground(getResources().getDrawable(R.drawable.text_outlinebg));
                tvWed.setBackground(getResources().getDrawable(R.drawable.selected_outlinbg));
                tvThur.setBackground(getResources().getDrawable(R.drawable.text_outlinebg));
                tvFri.setBackground(getResources().getDrawable(R.drawable.text_outlinebg));
                tvSat.setBackground(getResources().getDrawable(R.drawable.text_outlinebg));
                tvSun.setBackground(getResources().getDrawable(R.drawable.text_outlinebg));
            }
        });tvThur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvMon.setBackground(getResources().getDrawable(R.drawable.text_outlinebg));
                tvTue.setBackground(getResources().getDrawable(R.drawable.text_outlinebg));
                tvWed.setBackground(getResources().getDrawable(R.drawable.text_outlinebg));
                tvThur.setBackground(getResources().getDrawable(R.drawable.selected_outlinbg));
                tvFri.setBackground(getResources().getDrawable(R.drawable.text_outlinebg));
                tvSat.setBackground(getResources().getDrawable(R.drawable.text_outlinebg));
                tvSun.setBackground(getResources().getDrawable(R.drawable.text_outlinebg));
            }
        });tvFri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvMon.setBackground(getResources().getDrawable(R.drawable.text_outlinebg));
                tvTue.setBackground(getResources().getDrawable(R.drawable.text_outlinebg));
                tvWed.setBackground(getResources().getDrawable(R.drawable.text_outlinebg));
                tvThur.setBackground(getResources().getDrawable(R.drawable.text_outlinebg));
                tvFri.setBackground(getResources().getDrawable(R.drawable.selected_outlinbg));
                tvSat.setBackground(getResources().getDrawable(R.drawable.text_outlinebg));
                tvSun.setBackground(getResources().getDrawable(R.drawable.text_outlinebg));
            }
        });tvSat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvMon.setBackground(getResources().getDrawable(R.drawable.text_outlinebg));
                tvTue.setBackground(getResources().getDrawable(R.drawable.text_outlinebg));
                tvWed.setBackground(getResources().getDrawable(R.drawable.text_outlinebg));
                tvThur.setBackground(getResources().getDrawable(R.drawable.text_outlinebg));
                tvFri.setBackground(getResources().getDrawable(R.drawable.text_outlinebg));
                tvSat.setBackground(getResources().getDrawable(R.drawable.selected_outlinbg));
                tvSun.setBackground(getResources().getDrawable(R.drawable.text_outlinebg));
            }
        });tvSun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvMon.setBackground(getResources().getDrawable(R.drawable.text_outlinebg));
                tvTue.setBackground(getResources().getDrawable(R.drawable.text_outlinebg));
                tvWed.setBackground(getResources().getDrawable(R.drawable.text_outlinebg));
                tvThur.setBackground(getResources().getDrawable(R.drawable.text_outlinebg));
                tvFri.setBackground(getResources().getDrawable(R.drawable.text_outlinebg));
                tvSat.setBackground(getResources().getDrawable(R.drawable.text_outlinebg));
                tvSun.setBackground(getResources().getDrawable(R.drawable.selected_outlinbg));
            }
        });*/iv_back = v!!.findViewById(R.id.iv_back)
        iv_back!!.setOnClickListener(View.OnClickListener {
            try {
                val bundle = arguments
                if (bundle!!.getString("activity") == "Home") {
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
        iv_share = v!!.findViewById(R.id.iv_share)
        iv_share!!.setOnClickListener(View.OnClickListener { dailogDelete() })
        iv_msg = v!!.findViewById(R.id.iv_msg)
        iv_msg!!.setOnClickListener(View.OnClickListener {
            val intent = Intent(activity, Chat1Activity::class.java)
            startActivity(intent)
        })
        rc = v!!.findViewById(R.id.rc_detailsimg)
        rc_detailstime = v!!.findViewById(R.id.rc_detailstime)
        rc_upcomingevents = v!!.findViewById(R.id.rc_upcomingevents)
        val lm = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        val lm2 = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        val lm3 = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        rc!!.setLayoutManager(lm)
        rc_detailstime!!.setLayoutManager(lm2)
        rc_upcomingevents!!.setLayoutManager(lm3)
        indicator = v!!.findViewById(R.id.indicator)
        datalist.add(DetailsImageModal(R.drawable.img_1))
        datalist.add(DetailsImageModal(R.drawable.img_4))
        datalist.add(DetailsImageModal(R.drawable.img_1))
        datalist.add(DetailsImageModal(R.drawable.img_4))
        datalist.add(DetailsImageModal(R.drawable.img_1))
        datalisttime.add(DetailsTimeModal("11:00AM ", "3 Years", "01:00PM", "5 Years"))
        datalisttime.add(DetailsTimeModal("11:00AM ", "3 Years", "01:00PM", "5 Years"))
        datalisttime.add(DetailsTimeModal("11:00AM ", "3 Years", "01:00PM", "5 Years"))
        val ad = DetailsImageAdapter(requireActivity()!!, datalist)
        val adt = DetailsTimeAdapter(requireActivity(), datalisttime)
        val adu = DetailsUpcomingAdapter(requireActivity())
        rc!!.setAdapter(ad)
        indicator!!.attachToRecyclerView(rc!!)
        rc_detailstime!!.setAdapter(adt)
        rc_upcomingevents!!.setAdapter(adu)
        return v
    }

    fun dailogDelete() {
        dialog = Dialog(mContext!!)
        dialog!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog!!.setContentView(R.layout.alert_share)
        dialog!!.setCancelable(true)
        val ll_1: LinearLayout
        ll_1 = dialog!!.findViewById(R.id.ll_1)
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