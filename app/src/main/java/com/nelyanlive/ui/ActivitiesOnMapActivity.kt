package com.nelyanlive.ui

import android.content.Context
import android.graphics.*
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.datastore.preferences.core.preferencesKey
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.nelyanlive.R
import com.nelyanlive.db.DataStoragePreference
import com.nelyanlive.utils.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_activities_map.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import kotlin.coroutines.CoroutineContext

class ActivitiesOnMapActivity : CheckLocationActivity(), OnMapReadyCallback, CoroutineScope, View.OnClickListener,
    GoogleMap.OnInfoWindowClickListener {
    var mContext: Context? = null
    var ivBack: ImageView? = null
    var ivFilter: ImageView? = null
    private var listType: String? = null
    var latitude = ""
    var longitude = ""
    private lateinit var markerView: View
    private var mMap: GoogleMap? = null
    var lat = 0.0
    var lng = 0.0
    private var currentLat = "0.0"
    private var currentLong = "0.0"
    private var dataString = ""
    val list  = mutableListOf<DataMap>()

    private val dataStoragePreference by lazy { DataStoragePreference(this@ActivitiesOnMapActivity) }
    private var authkey: String? = null
    private val job by lazy {
        Job()
    }

    override fun onPermissionGranted() {
    }


    override fun onInfoWindowClick(marker : Marker) {
     OpenActivity(ActivityDetailsActivity::class.java) {
            putString("activityId", list[marker.tag.toString().toInt()].activityId)
            putString("categoryId", list[marker.tag.toString().toInt()].categoryId)
            putString("lati", marker.position.latitude.toString())
         putString("longi", marker.position.longitude.toString())
        }
    }

    override fun onLocationGet(latitude: String?, longitude: String?) {
        currentLat= latitude!!
        currentLong= longitude!!

        Log.e("lat_lng", "$currentLat,$currentLong")
    }

    internal inner class CustomInfoWindowAdapter : GoogleMap.InfoWindowAdapter {

        // TextViews with id "title" and "snippet".
        private val window: View = layoutInflater.inflate(R.layout.custom_info_window, null)
        //private val contents: View = layoutInflater.inflate(R.layout.custom_info_contents, null)

        override fun getInfoWindow(marker: Marker): View {

            render(marker, window)
            return window
        }

        override fun getInfoContents(marker: Marker): View? {
                return null
        }

        private fun render(marker: Marker, view: View) {

            var url = marker.snippet.toString()
            url  = url.replaceBeforeLast("###","")

           val ivImage =  view.findViewById<ImageView>(R.id.ivImage)


            Picasso.get().load(image_base_URl+ url.substring(3)).resize(150,150)
                .placeholder(
                    ContextCompat.getDrawable(
                        ivImage.context,
                        R.drawable.placeholder
                    )!!).into(ivImage)


            // Set the title and snippet for the custom info window
            val title: String? = marker.title
            val titleUi = view.findViewById<TextView>(R.id.tvTitle)

            if (title != null) {
                // Spannable string allows us to edit the formatting of the text.
                titleUi.text = SpannableString(title).apply {
                    setSpan(ForegroundColorSpan(Color.BLACK), 0, length, 0)
                }
            }
            else {
                titleUi.text = ""
            }
            var snippet: String = marker.snippet.toString().replaceAfterLast("###","")
            snippet = snippet.substring(0,snippet.length-3)
            val snippetUi = view.findViewById<TextView>(R.id.tvSubTitle)

                snippetUi.text = SpannableString(snippet).apply {
                    setSpan(ForegroundColorSpan(Color.GRAY), 0, length, 0)
                }

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_activities_map)
        mContext = this
        ivBack = findViewById(R.id.ivBack)
        ivFilter = findViewById(R.id.ivFilter)

        if (intent.extras != null) {
            listType = intent.getStringExtra("type").toString()
            Log.e("qwe", intent.getStringExtra("type").toString())

            if(intent.hasExtra("dataString"))
            {
                dataString = intent.getStringExtra("dataString")!!

                val jsonObject = JSONObject(dataString)

                val jsonArray = jsonObject.getJSONArray("data")
                (0 until jsonArray.length()).map {
                    val json = jsonArray.getJSONObject(it)
                    if(json.getString("latitude").isNotEmpty() && json.getString("longitude").isNotEmpty()) {
                        list.add(
                            DataMap(
                                LatLng(
                                    json.getString("latitude").toString().toDouble(),
                                    json.getString("longitude").toString().toDouble()
                                ), json.getString("activityname"), json.getString("city")
                                ,getImageFromArray(json.getJSONArray("activityimages")),
                                json.getString("categoryId"),json.getString("id")
                            ))
                    }
                } }
        }
        ivBack!!.setOnClickListener(this)

        launch(Dispatchers.Main.immediate) {
            authkey = dataStoragePreference.emitStoredValue(preferencesKey<String>("auth_key")).first() }

        ivFilter!!.setOnClickListener { onBackPressed() }



        markerView = (mContext!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(R.layout.view_custom_marker, null)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)
    }

    private fun getImageFromArray(jsonArray: JSONArray): String {
        return  if(jsonArray.length()>0) jsonArray.getJSONObject(0).getString("images") else ""
    }

    override fun onMapReady(googleMap: GoogleMap) {

        mMap = googleMap

        if(list.isNotEmpty()) {
            // create bounds that encompass every location we reference
            val boundsBuilder = LatLngBounds.Builder()
            // include all places we have markers for on the map

            (list.indices).map {
                boundsBuilder.include(list[it].position)
            }

            val bounds = boundsBuilder.build()

            with(mMap!!) {
                // Hide the zoom controls as the button panel will cover it.
                uiSettings!!.isZoomControlsEnabled = false

                // Setting an info window adapter allows us to change the both the contents and
                setInfoWindowAdapter(CustomInfoWindowAdapter())

                setOnInfoWindowClickListener(this@ActivitiesOnMapActivity)

                // Ideally this string would be localised.
                setContentDescription("markers")

                moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 30))
            }

            // Add lots of markers to the googleMap.
            addMarkersToMap()
        }
    }

    private fun addMarkersToMap() {
        // place markers for each of the defined locations
        (list.indices).map {
            mMap!!.addMarker(
                MarkerOptions()
                    .position(list[it].position)
                    .title(list[it].title)
                    .snippet(list[it].snippet+"###"+list[it].url)
                    .icon(BitmapDescriptorFactory.defaultMarker())
                    .infoWindowAnchor(0.5F, 0F)
                    .draggable(false)
                    .zIndex(it.toFloat())).tag = it.toString()

        }
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.ivBack->{
                onBackPressed()
            }
        }
    }
}

class DataMap(
    val position: LatLng,
    val title: String = "",
    val snippet: String = "",
    val url: String = "",
    val categoryId: String = "",
    val activityId: String = ""
    )