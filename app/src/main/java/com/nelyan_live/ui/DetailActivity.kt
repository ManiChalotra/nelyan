package com.nelyan_live.ui

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
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
import com.nelyan_live.adapter.DetailsTimeAdapter
import com.nelyan_live.adapter.DetailsUpcomingAdapter
import com.nelyan_live.modals.DetailsImageModal
import com.nelyan_live.modals.DetailsTimeModal
import ru.tinkoff.scrollingpagerindicator.ScrollingPagerIndicator
import java.util.*

class DetailActivity : AppCompatActivity(), OnMapReadyCallback {
    var mContext: Context? = null
    var btnModify: Button? = null
    var btnPublish: Button? = null
    var iv_msg: ImageView? = null
    var iv_back: ImageView? = null
    var iv_share: ImageView? = null
    var mMap: GoogleMap? = null
    var rc: RecyclerView? = null
    var rc_detailstime: RecyclerView? = null
    var rc_upcomingevents: RecyclerView? = null
    var datalist: ArrayList<DetailsImageModal> = ArrayList<DetailsImageModal>()
    var datalisttime: ArrayList<DetailsTimeModal> = ArrayList<DetailsTimeModal>()
    var dialog: Dialog? = null
    var indicator: ScrollingPagerIndicator? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        mContext = this
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)
        iv_back = findViewById(R.id.iv_back)
        btnModify = findViewById(R.id.btnModify)
        btnPublish = findViewById(R.id.btnPublish)
        iv_back!!.setOnClickListener(View.OnClickListener { finish() })
        btnModify!!.setOnClickListener(View.OnClickListener { finish() })
        btnPublish!!.setOnClickListener(View.OnClickListener {
            val i = Intent(mContext, HomeActivity::class.java)
            i.putExtra("activity", "acti")
            startActivity(i)
        })
        iv_share = findViewById(R.id.iv_share)
        iv_share!!.setOnClickListener(View.OnClickListener { dailogDelete() })
        iv_msg = findViewById(R.id.iv_msg)
        iv_msg!!.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@DetailActivity, com.nelyan_live.ui.Chat1Activity::class.java)
            startActivity(intent)
        })
        rc = findViewById(R.id.rc_detailsimg)
        rc_detailstime = findViewById(R.id.rc_detailstime)
        rc_upcomingevents = findViewById(R.id.rc_upcomingevents)
        val lm = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val lm2 = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val lm3 = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rc!!.setLayoutManager(lm)
        rc_detailstime!!.setLayoutManager(lm2)
        rc_upcomingevents!!.setLayoutManager(lm3)
        indicator = findViewById(R.id.indicator)
        datalist.add(DetailsImageModal(R.drawable.img_1))
        datalist.add(DetailsImageModal(R.drawable.img_4))
        datalist.add(DetailsImageModal(R.drawable.img_1))
        datalist.add(DetailsImageModal(R.drawable.img_4))
        datalist.add(DetailsImageModal(R.drawable.img_1))
        datalisttime.add(DetailsTimeModal("11:00AM ", "3 Years", "01:00PM", "5 Years"))
        datalisttime.add(DetailsTimeModal("11:00AM ", "3 Years", "01:00PM", "5 Years"))
        datalisttime.add(DetailsTimeModal("11:00AM ", "3 Years", "01:00PM", "5 Years"))
        val ad = DetailsImageAdapter(this, datalist)
        val adt = DetailsTimeAdapter(this, datalisttime)
        val adu = DetailsUpcomingAdapter(this)
        rc!!.setAdapter(ad)
        indicator!!.attachToRecyclerView(rc!!)
        rc_detailstime!!.setAdapter(adt)
        rc_upcomingevents!!.setAdapter(adu)
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