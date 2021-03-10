package com.nelyan_live.ui

import android.app.Dialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.nelyan_live.AppUtils
import com.nelyan_live.R
import com.nelyan_live.adapter.DetailsImageAdapter
import com.nelyan_live.modals.DetailsImageModal
import com.nelyan_live.utils.OpenActivity
import ru.tinkoff.scrollingpagerindicator.ScrollingPagerIndicator
import java.util.ArrayList

class NurseryActivity : AppCompatActivity(), OnMapReadyCallback {

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_nurse)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)
        ivBack = findViewById(R.id.ivBack)
        tvTitle = findViewById(R.id.tvTitle)
        ivBack!!.setOnClickListener(View.OnClickListener { onBackPressed() })
        btnModify = findViewById(R.id.btnModify)
        btnModify!!.setOnClickListener(View.OnClickListener { onBackPressed() })
        btnPublish = findViewById(R.id.btnPublish)
        btnPublish!!.setOnClickListener(View.OnClickListener {
            OpenActivity(NurserieActivityy::class.java )
           // AppUtils.gotoFragment(this, com.nelyan_live.fragments.NurserieFragment(), R.id.frame_container, false)
        })
        ivShare = findViewById(R.id.ivShare)
        ivShare!!.setOnClickListener(View.OnClickListener { dailogDelete() })
        rc = findViewById(R.id.rc_detailsimg)
        val lm = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rc!!.setLayoutManager(lm)
        indicator = findViewById(R.id.indicator)
        datalist.add(DetailsImageModal(R.drawable.img_1))
        datalist.add(DetailsImageModal(R.drawable.img_4))
        datalist.add(DetailsImageModal(R.drawable.img_1))
        datalist.add(DetailsImageModal(R.drawable.img_4))
        datalist.add(DetailsImageModal(R.drawable.img_1))
        val ad = DetailsImageAdapter(this, datalist)
        rc!!.setAdapter(ad)
        indicator!!.attachToRecyclerView(rc!!)


    }
    fun dailogDelete() {
        dialog = Dialog(this)
        dialog!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog!!.setContentView(R.layout.alert_share)
        dialog!!.setCancelable(true)
        val ll_1: LinearLayout
        ll_1 = dialog!!.findViewById(R.id.ll_1)
        ll_1.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
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