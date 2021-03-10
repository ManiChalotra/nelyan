package com.nelyan_live.ui

import android.app.Dialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
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
import java.util.ArrayList

class TraderPublishActivty : AppCompatActivity() , OnMapReadyCallback {

    var ivShare: ImageView? = null
    var ivBack: ImageView? = null
    var mMap: GoogleMap? = null
    var indicator: ScrollingPagerIndicator? = null
    var rc: RecyclerView? = null
    var datalist = ArrayList<DetailsImageModal>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_trader_publish)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)
        ivBack = findViewById(R.id.ivBack)
        ivBack!!.setOnClickListener(View.OnClickListener {
          onBackPressed()
        })
        ivShare = findViewById(R.id.ivShare)
        ivShare!!.setOnClickListener(View.OnClickListener { dailogShare() })
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

    fun dailogShare() {
       val  dialog = Dialog(this)
        dialog!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog!!.setContentView(R.layout.alert_share)
        dialog!!.setCancelable(true)
        val ll_1: LinearLayout
        ll_1 = dialog!!.findViewById(R.id.ll_1)
        ll_1.setOnClickListener {
            startActivity(Intent(this@TraderPublishActivty, HomeActivity::class.java))
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