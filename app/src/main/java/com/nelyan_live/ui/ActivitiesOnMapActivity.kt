package com.nelyan_live.ui

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.*
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.preferences.core.preferencesKey
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.Gson
import com.meherr.mehar.data.viewmodel.AppViewModel
import com.nelyan_live.R
import com.nelyan_live.db.DataStoragePreference
import com.nelyan_live.modals.homeactivitylist.Activityimage
import com.nelyan_live.modals.homeactivitylist.HomeAcitivityResponseData
import com.nelyan_live.modals.homeactivitylist.HomeActivityResponse
import com.nelyan_live.utils.*
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_activities_map.*

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import kotlin.coroutines.CoroutineContext

class ActivitiesOnMapActivity : CheckLocationActivity(), OnMapReadyCallback, CoroutineScope, View.OnClickListener {
    var mContext: Context? = null
    var ivBack: ImageView? = null
    var ivFilter: ImageView? = null
    var rl_1: RelativeLayout? = null
    var listType: String? = null
    private var activitisDatalist: ArrayList<HomeAcitivityResponseData>? = ArrayList()

    private var activitisImageslist: ArrayList<Activityimage>? = null

    var latitude = ""
    var longitude = ""
    lateinit var markerView: View

    private var mMap: GoogleMap? = null
    var lat = 0.0
    var lng = 0.0
    var currentLat = "0.0"
    var currentLong = "0.0"


    private val dataStoragePreference by lazy { DataStoragePreference(this@ActivitiesOnMapActivity) }
    private var authkey: String? = null
    private val job by lazy {
        Job()
    }


    override fun onPermissionGranted() {

    }

    override fun onLocationGet(latitude: String?, longitude: String?) {
        currentLat= latitude!!
        currentLong= longitude!!

        Log.e("lat_lng", currentLat+","+currentLong)
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_activities_map)
        mContext = this
        ivBack = findViewById(R.id.ivBack)
        ivFilter = findViewById(R.id.ivFilter)
        rl_1 = findViewById(R.id.rl_1)

        if (intent.extras != null) {
            activitisDatalist = intent.getSerializableExtra("activitisDatalist") as ArrayList<HomeAcitivityResponseData>
            Log.e("activitisDatalist", activitisDatalist!!.size.toString())
        }

        ivBack!!.setOnClickListener(this)

        launch(Dispatchers.Main.immediate) {
            authkey = dataStoragePreference?.emitStoredValue(preferencesKey<String>("auth_key"))?.first()
        }


        ivFilter!!.setOnClickListener(View.OnClickListener {
            Log.e("vghgv", "aaaaa")
            finish()
        })

        rl_1!!.setOnClickListener(View.OnClickListener {
            val i = Intent(this@ActivitiesOnMapActivity, ActivityDetailsActivity::class.java)
            startActivity(i)
            //AppUtils.gotoFragment(mContext, new ActivityDetailsFragment(), R.id.frame_container, false);
        })



        markerView = (mContext!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(
                        R.layout.view_custom_marker,
                        null
                )

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        checkPermissionLocation()

        if (mMap != null) {
            mMap!!.clear()


            Thread(Runnable {
                activitisDatalist = getUserBitmaps()

                this!!.runOnUiThread {

                    for (i in activitisDatalist!!.indices) {
                        var imagview: CircleImageView = markerView.findViewById(R.id.profile_images)

                        imagview.setImageBitmap(activitisDatalist!![i].activityimages[0].bitmap)

                        mMap!!.addMarker(
                                MarkerOptions()
                                        .position(
                                                LatLng(
                                                        activitisDatalist!![i].latitude.toDouble(),
                                                        activitisDatalist!![i].longitude.toDouble()
                                                )
                                        )
                                        .title(activitisDatalist!![i].activityname)
                                        .icon(
                                                BitmapDescriptorFactory.fromBitmap(
                                                        getMarkerBitmapFromView(
                                                                markerView
                                                        )
                                                )
                                        ).snippet("" + i)
                        )
                    }

                    val customWindow = CustomInfoWindowForActivities(this@ActivitiesOnMapActivity!!, activitisDatalist!!)
                    mMap!!.setInfoWindowAdapter(customWindow)
                    mMap!!.setOnInfoWindowClickListener {

                        var intents=Intent(this@ActivitiesOnMapActivity!!,ActivityDetailsActivity::class.java)
                        intents.putExtra("categoryId", activitisDatalist!![Integer.parseInt(it.snippet)].categoryId)
                        intents.putExtra("activityId", activitisDatalist!![Integer.parseInt(it.snippet)].id)

                        startActivity(intents)
                    }
                }
            }).start()


/*
            googleMap.mapType = GoogleMap.MAP_TYPE_NORMAL
        val india = LatLng(currentLat.toDouble(), currentLong.toDouble())
        mMap!!.addMarker(MarkerOptions()
                .position(india)
                .title("Marker in Sydney"))
        mMap!!.moveCamera(CameraUpdateFactory.newLatLng(india))
*/

        }



    }




    private fun getUserBitmaps(): ArrayList<HomeAcitivityResponseData> {
        for(i in activitisDatalist!!.indices) {
            if (activitisDatalist!![i].activityimages.size == 0) {
                    activitisDatalist!![i].activityimages[0].bitmap = BitmapFactory.decodeResource(resources,
                            R.mipmap.no_image_placeholder)
                } else {
                    activitisDatalist!![i].activityimages[0].bitmap = getBitmapFromURL(image_base_URl +
                            activitisDatalist!![i].activityimages.get(0).images)
                }

        }

        return activitisDatalist!!
    }


    fun getBitmapFromURL(src: String): Bitmap? {

        try {
            var url = URL(src)
            var connection = url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            var input = connection.inputStream
            var myBitmap = BitmapFactory.decodeStream(input)
            return myBitmap
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }
    }



    private fun getMarkerBitmapFromView(view: View): Bitmap {

//        mMarkerImageView.setImageBitmap(bitmap);
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        view.layout(0, 0, view.measuredWidth, view.measuredHeight)
        view.buildDrawingCache()
        var returnedBitmap: Bitmap = Bitmap.createBitmap(
                view.measuredWidth, view.measuredHeight,
                Bitmap.Config.ARGB_8888
        )
        var canvas: Canvas = Canvas(returnedBitmap)
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN)
        var drawable: Drawable = view.background
        if (drawable != null)
            drawable.draw(canvas)
        view.draw(canvas)
        return returnedBitmap

    }


    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.ivBack->{

            }
        }
    }
}