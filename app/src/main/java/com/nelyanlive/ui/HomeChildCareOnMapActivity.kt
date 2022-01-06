package com.nelyanlive.ui

import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.nelyanlive.R
import com.nelyanlive.utils.AllSharedPref
import com.nelyanlive.utils.OpenActivity
import com.nelyanlive.utils.image_base_URl
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_child_care.*
import org.json.JSONArray
import org.json.JSONObject

class HomeChildCareOnMapActivity : AppCompatActivity(), OnMapReadyCallback,
    GoogleMap.OnInfoWindowClickListener {
    var ivBack: ImageView? = null
    var ivFilter: ImageView? = null
    var mMap: GoogleMap? = null

    var dataString = ""
    var type = ""
    val list = mutableListOf<DataMap>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_child_care)
        ivBack = findViewById(R.id.ivBack)
        ivBack!!.setOnClickListener { finish() }
        ivFilter = findViewById(R.id.ivFilter)
        ivFilter!!.setOnClickListener {
            onBackPressed()
        }


        if (intent.extras != null) {
            type = intent.getStringExtra("type")!!

            if(type.equals("childCare"))
            {
                tvTitlechildcare.text = getString(R.string.child_care)
            }
           else {
                tvTitlechildcare.text = getString(R.string.trader_artisant)
            }
        }
            dataString = AllSharedPref.restoreString(this, "dataString")
            val jsonObject = JSONObject(dataString)

            val jsonArray = jsonObject.getJSONArray("data")
            (0 until jsonArray.length()).map {
                val json = jsonArray.getJSONObject(it)
                if (json.getString("latitude").isNotEmpty() && json.getString("longitude")
                        .isNotEmpty()) {

                    list.add(
                        DataMap(
                            LatLng(json.getString("latitude").toString().toDouble(),
                                json.getString("longitude").toString().toDouble()),
                            json.getString( "nameOfShop" ),
                            try {
                                json.getString("typeofTraderId")
                            }
                            catch (e:Exception)
                            {
                                json.getString("typeofActivityId")
                            }

                           , json.getString("id"),
                            json.getString("city"),
                            try {
                                getImageFromArray(json.getJSONArray("tradersimages"))
                            }
                            catch (e:Exception)
                            {
                                getImageFromArray(json.getJSONArray("activityimages"))
                            }
                        )
                    )
            }
        }

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {

        mMap = googleMap
        if (list.isNotEmpty()) {
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
                setInfoWindowAdapter(ChildCareInfoWindowAdapter())

                setOnInfoWindowClickListener(this@HomeChildCareOnMapActivity)

                // Ideally this string would be localised.
                setContentDescription("Map with lots of markers.")

                moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 30))
            }

            // Add lots of markers to the googleMap.
            addMarkersToMap()

        }
    }

    override fun onInfoWindowClick(marker: Marker) {
        if (type == "childCare") {
            OpenActivity(HomeChildCareDetailsActivity::class.java) {
                putString("activityId", list[marker.tag.toString().toInt()].activityId)
                putString("categoryId", list[marker.tag.toString().toInt()].categoryId)
                putString("latti", marker.position.latitude.toString())
                putString("longi", marker.position.longitude.toString())
            }
        } else {
            OpenActivity(TraderPublishActivty::class.java) {
                putString("postId", list[marker.tag.toString().toInt()].activityId)
                putString("latti", marker.position.latitude.toString())
                putString("longi", marker.position.longitude.toString())
            }
        }
    }

    private fun addMarkersToMap() {

        // place markers for each of the defined locations
        (list.indices).map {
            mMap!!.addMarker(
                MarkerOptions()
                    .position(list[it].position)
                    .title(list[it].title)
                    .snippet(list[it].snippet + "###" + list[it].url)
                    .icon(BitmapDescriptorFactory.defaultMarker())
                    .infoWindowAnchor(0.5F, 0F)
                    .draggable(false)
                    .zIndex(it.toFloat())
            ).tag = it.toString()

        }
    }

    internal inner class ChildCareInfoWindowAdapter : GoogleMap.InfoWindowAdapter {

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
            url = url.replaceBeforeLast("###", "")

            val ivImage = view.findViewById<ImageView>(R.id.ivImage)

            Picasso.get().load(image_base_URl + url.substring(3)).resize(150, 150)
                .placeholder(
                    ContextCompat.getDrawable(
                        ivImage.context,
                        R.drawable.placeholder
                    )!!
                ).into(ivImage)

            // Set the title and snippet for the custom info window
            val title: String? = marker.title
            val titleUi = view.findViewById<TextView>(R.id.tvTitle)

            if (title != null) {
                // Spannable string allows us to edit the formatting of the text.
                titleUi.text = SpannableString(title).apply {
                    setSpan(ForegroundColorSpan(Color.BLACK), 0, length, 0)
                }
            } else {
                titleUi.text = ""
            }
            var snippet: String = marker.snippet.toString().replaceAfterLast("###", "")
            snippet = snippet.substring(0, snippet.length - 3)
            val snippetUi = view.findViewById<TextView>(R.id.tvSubTitle)

            snippetUi.text = SpannableString(snippet).apply {
                setSpan(ForegroundColorSpan(Color.GRAY), 0, length, 0)
            }

        }
    }

    private fun getImageFromArray(jsonArray: JSONArray): String {
        return if (jsonArray.length() > 0) jsonArray.getJSONObject(0).getString("images") else ""
    }

}