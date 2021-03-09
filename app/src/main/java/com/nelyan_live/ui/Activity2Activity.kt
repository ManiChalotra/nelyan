package com.nelyan_live.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.nelyan_live.R

class Activity2Activity : AppCompatActivity(), OnMapReadyCallback {
    var mContext: Context? = null
    var ivBack: ImageView? = null
    var ivFilter: ImageView? = null
    var rl_1: RelativeLayout? = null
    var mMap: GoogleMap? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_activity2)
        mContext = this
        ivBack = findViewById(R.id.ivBack)
        ivBack!!.setOnClickListener(View.OnClickListener { finish() })
        ivFilter = findViewById(R.id.ivFilter)
        ivFilter!!.setOnClickListener(View.OnClickListener {
            Log.e("vghgv", "aaaaa")
            val i = Intent(this@Activity2Activity, HomeActivity::class.java)
            i.putExtra("activity", "map")
            startActivity(i)
            //  AppUtils.gotoFragment(mContext, new ActivityFragment(), R.id.frame_container, false);
        })
        rl_1 = findViewById(R.id.rl_1)
        rl_1!!.setOnClickListener(View.OnClickListener {
            val i = Intent(this@Activity2Activity, DetailActivity::class.java)
            startActivity(i)
            //AppUtils.gotoFragment(mContext, new ActivityDetailsFragment(), R.id.frame_container, false);
        })
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)
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