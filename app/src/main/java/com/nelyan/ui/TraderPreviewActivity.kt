package com.nelyan.ui

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
import com.nelyan.R
import com.nelyan.adapter.DetailsImageAdapter
import com.nelyan.modals.DetailsImageModal
import ru.tinkoff.scrollingpagerindicator.ScrollingPagerIndicator
import java.util.*

class TraderPreviewActivity : AppCompatActivity(), OnMapReadyCallback {
    var mContext: Context? = null
    var ivBack: ImageView? = null
    var btnModify: Button? = null
    var btnPublish: Button? = null
    var dialog: Dialog? = null
    var mMap: GoogleMap? = null
    var ivShare: ImageView? = null
    var indicator: ScrollingPagerIndicator? = null
    var rc: RecyclerView? = null
    var datalist = ArrayList<DetailsImageModal>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trader_preview)
        mContext = this
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)
        ivBack = findViewById(R.id.ivBack)
        ivBack!!.setOnClickListener(View.OnClickListener { finish() })
        ivShare = findViewById(R.id.ivShare)
        ivShare!!.setOnClickListener(View.OnClickListener { dailogShare() })
        btnModify = findViewById(R.id.btnModify)
        btnModify!!.setOnClickListener(View.OnClickListener { finish() })
        btnPublish = findViewById(R.id.btnPublish)
        btnPublish!!.setOnClickListener(View.OnClickListener { //  AppUtils.gotoFragment(mContext, new TraderPublishFragment(), R.id.frame_container, false);
            val i = Intent(this@TraderPreviewActivity, HomeActivity::class.java)
            i.putExtra("activity", "tarfrag")
            startActivity(i)
        })
        rc = findViewById(R.id.rc_detailsimg)
        val lm = LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false)
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