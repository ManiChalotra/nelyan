package com.nelyanlive.ui

import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.preferences.core.preferencesKey
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.facebook.share.model.ShareHashtag
import com.facebook.share.model.ShareLinkContent
import com.facebook.share.widget.ShareDialog
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.Gson
import com.nelyanlive.R
import com.nelyanlive.adapter.ActivitiesEventsDaysAdapter
import com.nelyanlive.adapter.DetailsImageAdapter
import com.nelyanlive.adapter.DetailsTimeAdapter
import com.nelyanlive.adapter.DetailsUpcomingAdapter
import com.nelyanlive.data.viewmodel.AppViewModel
import com.nelyanlive.db.DataStoragePreference
import com.nelyanlive.modals.ActivitiesEventsDaysModel
import com.nelyanlive.modals.DetailsTimeModal
import com.nelyanlive.modals.postDetails.Activityimage
import com.nelyanlive.modals.postDetails.PostDetailsEvents
import com.nelyanlive.modals.postDetails.PostDetailsEventstiming
import com.nelyanlive.utils.*
import kotlinx.android.synthetic.main.alert_share.*
import kotlinx.android.synthetic.main.fragment_activity_details.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import ru.tinkoff.scrollingpagerindicator.ScrollingPagerIndicator
import kotlin.coroutines.CoroutineContext

class ActivityDetailsActivity : AppCompatActivity(), View.OnClickListener, CoroutineScope, OnMapReadyCallback {

    var iv_msg: ImageView? = null
    var iv_share: ImageView? = null
    var rvActivtiesImages: RecyclerView? = null
    private var shareDialog: ShareDialog? = null
    private val dataStoragePreference by lazy { DataStoragePreference(this@ActivityDetailsActivity) }
    private var authkey: String? = null
    var rc_detailstime: RecyclerView? = null
    var rc_upcomingevents: RecyclerView? = null
    var rvDays: RecyclerView? = null
    var categoryId = ""
    var postId = ""
    var userId = ""
    var latitude = ""
    var longitude = ""
    var datalisttime = ArrayList<DetailsTimeModal>()
    var daysList : ArrayList<ActivitiesEventsDaysModel> = ArrayList()
    var mMap: GoogleMap? = null
    var dialog: Dialog? = null
    var indicator: ScrollingPagerIndicator? = null
    private var listActivityimage = ArrayList<Activityimage>()
    private var listUpcomingEvents = ArrayList<PostDetailsEvents>()
    private var listUpcomingEventsTimings = ArrayList<PostDetailsEventstiming>()

    private val job by lazy { Job() }

    private val appViewModel by lazy { ViewModelProvider.AndroidViewModelFactory.getInstance(this.application).create(AppViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_activity_details)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)
        mapFragment.getMapAsync(this)

        launch(Dispatchers.Main.immediate) {
            userId = dataStoragePreference.emitStoredValue(preferencesKey<String>("id")).first()
        }

        ivMapImage.setOnTouchListener(object : View.OnTouchListener {

            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event!!.action) {
                    MotionEvent.ACTION_DOWN -> {
                        nvsMain.requestDisallowInterceptTouchEvent(true)
                        // Disable touch on transparent view
                        return false
                    }

                    MotionEvent.ACTION_UP -> {
                        nvsMain.requestDisallowInterceptTouchEvent(false)
                        return true
                    }

                    MotionEvent.ACTION_MOVE -> {
                        nvsMain.requestDisallowInterceptTouchEvent(true)
                        return false
                    } }
                return true
            }
        })

        iv_msg = findViewById(R.id.iv_msg)
        iv_share = findViewById(R.id.iv_share)

        iv_back!!.setOnClickListener(this)
        iv_msg!!.setOnClickListener(this)
        iv_share!!.setOnClickListener(this)

        rvActivtiesImages = findViewById(R.id.rv_activties_images)
        rc_detailstime = findViewById(R.id.rc_detailstime)
        rc_upcomingevents = findViewById(R.id.rc_upcomingevents)
        rvDays = findViewById(R.id.rv_days)

        indicator = findViewById(R.id.indicator)
        shareDialog = ShareDialog(this)

        if (intent.extras != null) {
            categoryId = intent.getStringExtra("categoryId").toString()
            postId = intent.getStringExtra("activityId")!!
        }
        launch(Dispatchers.Main.immediate) {
            authkey = dataStoragePreference.emitStoredValue(preferencesKey<String>("auth_key")).first()
            appViewModel.postDetailsApiData(security_key, authkey, "1", postId, categoryId)
            activity_details_progressbar?.showProgressBar()
        }
        checkMvvmResponse()
    }

    fun dailogshare() {
        dialog = Dialog(this)
        dialog!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog!!.setContentView(R.layout.alert_share)
        dialog!!.setCancelable(true)

        dialog!!.ll_twitter_share.setOnClickListener {
            CommonMethodsKotlin.twitterShare(this)
            dialog!!.dismiss()
        }
        dialog!!.ll_instra_share.setOnClickListener {
            CommonMethodsKotlin.instaShare(this)
            dialog!!.dismiss()
        }
        dialog!!.ll_messanger_share.setOnClickListener {
            CommonMethodsKotlin.messageShare(this)
            dialog!!.dismiss()
        }
        dialog!!.ll_gmail_share.setOnClickListener {
            val email = Intent(Intent.ACTION_SEND)
            email.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.nelyan_app))
            email.putExtra(Intent.EXTRA_TEXT, "Nelyan.. social app. \n"+
                    "https://play.google.com/store/apps/details?id=com.nelyan")
            email.setPackage("com.google.android.gm")
            email.type = "message/rfc822"
            try {
                startActivity(email)
            } catch (ex: ActivityNotFoundException) {
                myCustomToast( getString(R.string.gmail_not_error))
            }

            dialog!!.dismiss()
        }
        dialog!!.ll_whatsapp_share.setOnClickListener {
            CommonMethodsKotlin.whatsAppShare(this)

            dialog!!.dismiss()
        }
        dialog!!.ll_fv_share.setOnClickListener {
            CommonMethodsKotlin.fbShare(this)

            val content =  ShareLinkContent.Builder()
                    .setContentUrl(Uri.parse("https://play.google.com/store/apps/details?id=com.nelyan"))
                    .setShareHashtag( ShareHashtag.Builder().setHashtag(getString(R.string.nelyan_app)).build())
                    .build()

            try {
                shareDialog!!.show(content) // Show ShareDialog

            } catch (ex: ActivityNotFoundException) {
                Toast.makeText(this, getString(R.string.facebook_have_not_installed), Toast.LENGTH_SHORT).show()
            }

            dialog!!.dismiss()
        }
        dialog!!.show()
    }


    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.iv_back -> { onBackPressed() }
            R.id.iv_msg -> { }
            R.id.iv_share -> { dailogshare() }

        }
    }

    private fun checkMvvmResponse() {

        appViewModel.observePostDetailApiResponse()!!.observe(this, androidx.lifecycle.Observer { response ->

            if (response!!.isSuccessful && response.code() == 200) {
                if (response.body() != null) {

                    activity_details_progressbar?.hideProgressBar()

                    val mResponse = response.body().toString()
                    val jsonObject = JSONObject(mResponse)
                    if(!jsonObject.getJSONObject("data").isNull("user")) {
                        setUserData(jsonObject.getJSONObject("data").getJSONObject("user"))
                    }

                    val message = jsonObject.get("msg").toString()
                    myCustomToast(message)

                    tv_activity_title.text = jsonObject.getJSONObject("data").get("nameOfShop").toString()
                    tv_activity_name.text = jsonObject.getJSONObject("data").get("activityname").toString()
                    tv_city.text = jsonObject.getJSONObject("data").get("city").toString()
                    tv_activitydesc.text = jsonObject.getJSONObject("data").get("description").toString()


                    tvWebsite.text = jsonObject.getJSONObject("data").get("website").toString()

                    if(jsonObject.getJSONObject("data").get("phone").toString().isNotBlank()) {
                        tv_phn.visibility = View.VISIBLE
                        tv_phntxt.visibility = View.VISIBLE
                        tv_phntxt.text = jsonObject.getJSONObject("data").get("country_code")
                            .toString() + "-" + jsonObject.getJSONObject("data").get("phone")
                            .toString()
                    }
                    else
                    {
                        tv_phn.visibility = View.GONE
                        tv_phntxt.visibility = View.GONE
                    }
                        tv_activitydesc.text = jsonObject.getJSONObject("data").get("description").toString()
                    tv_actvity_address.text = jsonObject.getJSONObject("data").get("address").toString()

                    longitude = jsonObject.getJSONObject("data").get("longitude").toString()
                    latitude = jsonObject.getJSONObject("data").get("latitude").toString()

                    val listArray: JSONArray = jsonObject.getJSONObject("data").getJSONArray("activityimages")
                    val listArrayAgeGroups: JSONArray = jsonObject.getJSONObject("data").getJSONArray("ageGroups")
                    val listArrayEvents: JSONArray = jsonObject.getJSONObject("data").getJSONArray("events")

                    val mSizeOfData: Int = listArray.length()
                    val mSizeOfAgeGroup: Int = listArrayAgeGroups.length()
                    val mSizeOfEvents: Int = listArrayEvents.length()

                    listActivityimage.clear()
                    for (i in 0 until mSizeOfData) {
                        val model = listArray.getJSONObject(i)
                        val images = model.get("images")
                        val mediaType = model.get("mediaType")
                        val activityId = model.get("activityId")
                        val id = model.get("id")
                        listActivityimage.add(Activityimage(activityId.toString().toInt(), id.toString().toInt(), images.toString(), mediaType.toString().toInt()))
                    }

                    if (mSizeOfData != 0) {
                        val ad = DetailsImageAdapter(this, listActivityimage)
                        rvActivtiesImages!!.adapter = ad
                        indicator!!.attachToRecyclerView(rvActivtiesImages!!)
                    }

                    if (datalisttime != null) {
                        datalisttime.clear()
                    } else {
                        datalisttime = ArrayList()
                    }

                    for (j in 0 until mSizeOfAgeGroup) {
                        val dayssModel = listArrayAgeGroups.getJSONObject(j)
                        val ageFrom = dayssModel.get("days")

                        daysList.add(ActivitiesEventsDaysModel(ageFrom.toString()))
                    }

                    if (mSizeOfAgeGroup != 0) {
                        val activitiesEventsDaysAdapter = ActivitiesEventsDaysAdapter(this, this@ActivityDetailsActivity, daysList)
                        val lmmanager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
                        rvDays!!.layoutManager = lmmanager
                        rvDays!!.adapter = activitiesEventsDaysAdapter

                        rvDays!!.visibility = View.VISIBLE
                        tv_no_days!!.visibility = View.GONE

                    } else {
                        rvDays!!.visibility = View.GONE
                        tv_no_days!!.visibility = View.VISIBLE
                    }

                    for (j in 0 until mSizeOfAgeGroup) {
                        val model = listArrayAgeGroups.getJSONObject(j)
                        val ageFrom = model.get("ageFrom")
                        val ageTo = model.get("ageTo")
                        val timeFrom = model.get("timeFrom")
                        val timeTo = model.get("timeTo")

                        datalisttime.add(DetailsTimeModal(timeFrom.toString(), ageFrom.toString(), timeTo.toString(), ageTo.toString()))
                    }

                    if (mSizeOfAgeGroup != 0) {
                        val detailsTimeAdapter = DetailsTimeAdapter(this, datalisttime)
                        val lm2 = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
                        rc_detailstime!!.layoutManager = lm2
                        rc_detailstime!!.adapter = detailsTimeAdapter

                        rc_detailstime!!.visibility = View.VISIBLE
                        tv_no_age_group!!.visibility = View.GONE

                    }
                    else {
                        rc_detailstime!!.visibility = View.GONE
                        tv_no_age_group!!.visibility = View.VISIBLE
                    }

                    if (listUpcomingEvents != null) {
                        listUpcomingEvents.clear()
                    } else {
                        listUpcomingEvents = ArrayList()
                    }

                    if (mSizeOfEvents != 0) {

                        if (listUpcomingEventsTimings != null) {
                            listUpcomingEventsTimings.clear()
                        } else {
                            listUpcomingEventsTimings = ArrayList()
                        }

                        for (k in 0 until mSizeOfEvents) {
                            val model = listArrayEvents.getJSONObject(k)
                            val eventId = model.get("id")
                            val userId = model.get("userId")
                            val activityId = model.get("activityId")
                            val name = model.get("name")
                            val description = model.get("description")
                            val price = model.get("price")
                            val city = model.get("city")
                            val latitude = model.get("latitude")
                            val longitude = model.get("longitude")
                            val image = model.get("image")
                            val status = model.get("status")
                            val createdAt = model.get("createdAt")
                            val updatedAt = model.get("updatedAt")
                            val listArrayEventsTimings: JSONArray = jsonObject.getJSONObject("data").getJSONArray("events")
                                    .getJSONObject(k).getJSONArray("eventstimings")


                            if (listArrayEventsTimings != null) {
                                for (l in 0 until listArrayEventsTimings.length()) {

                                    val id = jsonObject.getJSONObject("data").getJSONArray("events").getJSONObject(k)
                                            .getJSONArray("eventstimings").getJSONObject(l).get("id").toString()
                                    val activityId = jsonObject.getJSONObject("data").getJSONArray("events").getJSONObject(k)
                                            .getJSONArray("eventstimings").getJSONObject(l).get("activityId").toString()
                                    val eventId = jsonObject.getJSONObject("data").getJSONArray("events").getJSONObject(k)
                                            .getJSONArray("eventstimings").getJSONObject(l).get("eventId").toString()
                                    val dateFrom = jsonObject.getJSONObject("data").getJSONArray("events").getJSONObject(k)
                                            .getJSONArray("eventstimings").getJSONObject(l).get("dateFrom").toString()
                                    val dateTo = jsonObject.getJSONObject("data").getJSONArray("events").getJSONObject(k)
                                            .getJSONArray("eventstimings").getJSONObject(l).get("dateTo").toString()
                                    val startTime = jsonObject.getJSONObject("data").getJSONArray("events").getJSONObject(k)
                                            .getJSONArray("eventstimings").getJSONObject(l).get("startTime").toString()
                                    val endTime = jsonObject.getJSONObject("data").getJSONArray("events").getJSONObject(k)
                                            .getJSONArray("eventstimings").getJSONObject(l).get("endTime").toString()

                                    listUpcomingEventsTimings.add(PostDetailsEventstiming(activityId.toInt(), dateFrom, dateTo, endTime, eventId.toInt(), id.toInt(), startTime))
                                }
                            }

                            listUpcomingEvents.add(PostDetailsEvents(activityId.toString().toInt(), city.toString(), createdAt.toString(),
                                    description.toString(), listUpcomingEventsTimings, eventId.toString().toInt(), image.toString(), latitude.toString(),
                                    longitude.toString(), name.toString(), price.toString(), status.toString().toInt(),
                                    updatedAt.toString(), userId.toString().toInt()))
                        }

                        val detailsUpcomingAdapter = DetailsUpcomingAdapter(this, this, listUpcomingEvents )
                        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
                        rc_upcomingevents!!.layoutManager = layoutManager
                        rc_upcomingevents!!.adapter = detailsUpcomingAdapter

                        rc_upcomingevents!!.visibility = View.VISIBLE
                        tv_no_upcoming_events!!.visibility = View.GONE

                    } else {
                        rc_upcomingevents!!.visibility = View.GONE
                        tv_no_upcoming_events!!.visibility = View.VISIBLE
                    }
                }
            } else {
                ErrorBodyResponse(response, this, activity_details_progressbar)
                activity_details_progressbar?.hideProgressBar()
            }
        })

        appViewModel.getException()!!.observe(this, Observer {
            myCustomToast(it)
            activity_details_progressbar?.hideProgressBar()
        })
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        googleMap.mapType = GoogleMap.MAP_TYPE_NORMAL
        try {
            if (intent.extras !=null){
                val latti= intent.getStringExtra("lati")
                val longi= intent.getStringExtra("longi")

                if(!latti.isNullOrBlank()) {
                    val india = LatLng(latti.toDouble(), longi!!.toDouble())
                    mMap!!.addMarker(
                        MarkerOptions()
                            .position(india)
                            .title("Activity")
                    )
                    mMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(india, 15f))
                }
            }

        }
        catch (e: Exception) { }
    }

    private fun setUserData(jsonObject: JSONObject) {


        if(userId!=jsonObject.getString("id")) {
            iv_msg!!.setOnClickListener {
                startActivity(
                    Intent(this, Chat1Activity::class.java)
                        .putExtra("senderID", jsonObject.getString("id"))
                        .putExtra("senderName", jsonObject.getString("name"))
                        .putExtra("senderImage", jsonObject.getString("image"))
                        .putExtra("userId", userId)
                ) } } }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job
}