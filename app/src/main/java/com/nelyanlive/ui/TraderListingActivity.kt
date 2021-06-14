package com.nelyanlive.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.datastore.preferences.core.preferencesKey
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.nelyanlive.R
import com.nelyanlive.adapter.TraderListingAdapter
import com.nelyanlive.current_location.GPSTracker
import com.nelyanlive.data.viewmodel.AppViewModel
import com.nelyanlive.db.DataStoragePreference
import com.nelyanlive.modals.hometraderpostlist.HomeTraderListData
import com.nelyanlive.modals.hometraderpostlist.HomeTraderPostListResponse
import com.nelyanlive.utils.*
import kotlinx.android.synthetic.main.fragment_trader_listing.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList
import kotlin.coroutines.CoroutineContext

private const val PERMISSIONS_REQUEST_CODE = 34

class TraderListingActivity : AppCompatActivity(), View.OnClickListener, TraderListingAdapter.OnTraderItemClickListner, CoroutineScope {


    private  val job by lazy {
        Job()
    }
    lateinit var ivFavourite :ImageView
    private var listType: String? = null

    private var traderListingAdapter: TraderListingAdapter? = null
    private var rvTrader: RecyclerView? = null
    var LAUNCH_SECOND_ACTIVITY = 3

    private val traderDatalist by lazy { ArrayList<HomeTraderListData>() }

    private val appViewModel by lazy { ViewModelProvider.AndroidViewModelFactory.getInstance(this.application).create(AppViewModel::class.java) }
    private  val dataStoragePreference by lazy { DataStoragePreference(this@TraderListingActivity) }
    private var authkey: String?= null
    var dataString = ""

    private var gpsTracker: GPSTracker? = null
    private var latitude: Double = 42.6026
    private var longitude: Double = 20.9030
    private var locality: String = ""

    override fun onResume() {
                super.onResume()
               gpsTracker = GPSTracker(this)

        if (tvFilter.text == "Filter") {
            if (!gpsTracker!!.canGetLocation()) {
                Log.e("location_changed", "==1=iff==" + gpsTracker!!.canGetLocation())
                gpsTracker!!.showSettingsAlert()

            } else {
                Log.e("location_changed", "==2=iff==" + gpsTracker!!.canGetLocation())
                val gpsTracker = GPSTracker(this)
                latitude = gpsTracker.latitude
                longitude = gpsTracker.longitude
                Log.e("location_changed", "==2=ifffff=$latitude==$longitude=")
                if (latitude != 0.0) {
                    val geocoder = Geocoder(this, Locale.getDefault())
                    var list = listOf<Address>()

                    list = geocoder.getFromLocation(latitude, longitude, 1)

                    Log.e("location_changed", "==home==Foreground address: ${list[0].locality}==")
                    tv_city_zipcode.text = list[0].locality
                    locality = list[0].locality
                    if (checkIfHasNetwork(this)) {
                        launch(Dispatchers.Main.immediate) {
                            val authKey =
                                dataStoragePreference.emitStoredValue(preferencesKey<String>("auth_key"))
                                    .first()

                            appViewModel.sendFilterTraderListData(
                                security_key, authKey,
                                latitude.toString(), longitude.toString(),
                                "", "", "",
                                locality
                            )

                            trader_list_progressbar?.showProgressBar()
                        }
                    } else {
                        showSnackBar(this, getString(R.string.no_internet_error))
                    }

                }
            }
        }
                }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_trader_listing)
        initalizeClicks()
        rvTrader = findViewById(R.id.rv_traderListing)

        if (intent.extras !=null){
            listType = intent.getStringExtra("type").toString()
            Log.e("qwe", intent.getStringExtra("type").toString())
        }

        //current location
        if (foregroundPermissionApproved()) {
            val subscribeToLocationUpdates = HomeActivity.locationService?.subscribeToLocationUpdates()

            Log.e("location_changed", "=========$subscribeToLocationUpdates======")

            if (subscribeToLocationUpdates != null) subscribeToLocationUpdates else Log.e("location_changed", "Service Not Bound")
        }
        else {
            requestPermissions()
        }

        checkMvvmResponse()
    }


    private fun requestPermissions() {
        val provideRationale = foregroundPermissionApproved()

        // If the user denied a previous request, but didn't check "Don't ask again", provide
        // additional rationale.
        if (provideRationale) {
            Snackbar.make(
                frame_container,
                R.string.permission,
                Snackbar.LENGTH_LONG
            )
                .setAction(R.string.ok) {
                    // Request permission
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        PERMISSIONS_REQUEST_CODE
                    )
                }
                .show()
        } else {
            Log.d("TAG", "Request foreground only permission")
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSIONS_REQUEST_CODE
            )
        }
    }

    private fun foregroundPermissionApproved(): Boolean {
        return PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }


    private fun initalizeClicks() {
        ivBack.setOnClickListener(this)
        ivMap.setOnClickListener(this)
        tvFilter.setOnClickListener(this)
    }

    private fun setTraderAdapter(traderDatalist: ArrayList<HomeTraderListData>) {
        traderListingAdapter = TraderListingAdapter(this, traderDatalist, this)
        rvTrader!!.layoutManager = LinearLayoutManager(this)
        rvTrader!!.adapter = traderListingAdapter
    }

    private  fun checkMvvmResponse(){
        appViewModel.observeHomeActivitiesResponse()!!.observe(this, androidx.lifecycle.Observer { response ->
            if (response!!.isSuccessful && response.code() == 200) {
                if (response.body() != null) {
                    trader_list_progressbar?.hideProgressBar()
                    Log.d("homeTraderListRespone", "-------------" + Gson().toJson(response.body()))
                    val mResponse = response.body().toString()
                    dataString = response.body().toString()

                    val jsonObject = JSONObject(mResponse)
                    val homeTraderResponse =  Gson().fromJson<HomeTraderPostListResponse>(response.body().toString(), HomeTraderPostListResponse::class.java)
                    if (traderDatalist != null) {
                        traderDatalist.clear()
                        traderDatalist.addAll(homeTraderResponse.data)
                    }

                    if (traderDatalist.size == 0) {
                        rvTrader!!.visibility = View.GONE
                        tv_no_trader_list!!.visibility = View.VISIBLE
                    }
                    else {
                        rvTrader!!.visibility = View.VISIBLE
                        tv_no_trader_list!!.visibility = View.GONE
                        setTraderAdapter(traderDatalist)
                    }
                }
            } else {
                ErrorBodyResponse(response, this, trader_list_progressbar)
            }
        })
        appViewModel.observeFilterTraderListResponse()!!.observe(this, androidx.lifecycle.Observer { response ->
            if (response!!.isSuccessful && response.code() == 200) {
                if (response.body() != null) {
                    trader_list_progressbar?.hideProgressBar()
                    Log.d("homeTraderListRespone", "-------------" + Gson().toJson(response.body()))
                    val mResponse = response.body().toString()
                    dataString = response.body().toString()

                    val jsonObject = JSONObject(mResponse)
                    val homeTraderResponse =  Gson().fromJson<HomeTraderPostListResponse>(response.body().toString(), HomeTraderPostListResponse::class.java)
                    if (traderDatalist != null) {
                        traderDatalist.clear()
                        traderDatalist.addAll(homeTraderResponse.data)
                    }
                    if (traderDatalist.size == 0) {
                        rvTrader!!.visibility = View.GONE
                        tv_no_trader_list!!.visibility = View.VISIBLE
                    }
                    else {
                        rvTrader!!.visibility = View.VISIBLE
                        tv_no_trader_list!!.visibility = View.GONE
                        setTraderAdapter(traderDatalist)
                    }
                }
            } else {
                ErrorBodyResponse(response, this, null)
                trader_list_progressbar?.hideProgressBar()
            }
        })

        appViewModel.observeAddFavouritePostApiResponse()!!.observe(this, Observer { response ->
            if (response!!.isSuccessful && response.code() == 200) {
                Log.d("addFavouriteResBody", "----------" + Gson().toJson(response.body()))
                trader_list_progressbar?.hideProgressBar()
                val mResponse = response.body().toString()
                val jsonObject = JSONObject(mResponse)

                val message = jsonObject.get("msg").toString()
                if (message == "You marked this Post as Your Favourite"){
                    myCustomToast("You marked this post as your favourite")

                    ivFavourite.setImageResource(R.drawable.heart)
                }else {
                    myCustomToast("You removed this post from your favourite")

                    ivFavourite.setImageResource(R.drawable.heart_purple)
                }
            } else {
                ErrorBodyResponse(response, this, trader_list_progressbar)
                trader_list_progressbar?.hideProgressBar()
            }
        })

        appViewModel.getException()!!.observe(this, Observer {
            myCustomToast(it)
            trader_list_progressbar?.hideProgressBar()
        })
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.ivBack -> {
                onBackPressed()
            }
            R.id.ivMap -> {
                if(dataString.isEmpty())
                {
                    myCustomToast("Data not loaded yet")
                }
                else {
                    val i = Intent(this, HomeChildCareOnMapActivity::class.java)
                    i.putExtra("dataString", dataString)
                    i.putExtra("type", "trader")
                    startActivity(i)
                }
            }
            R.id.tvFilter -> {
                if (tvFilter.text == "Filter") {
                    val i = Intent(this, TraderFilterActivity::class.java)
                    startActivityForResult(i, LAUNCH_SECOND_ACTIVITY)
                }
                else
                {
                    tvFilter.text = "Filter"
                    if (checkIfHasNetwork(this)) {
                        launch(Dispatchers.Main.immediate) {
                            val authKey = dataStoragePreference.emitStoredValue(preferencesKey<String>("auth_key")).first()
                            tv_city_zipcode.text = locality

                            appViewModel.sendFilterTraderListData(security_key, authKey,
                                latitude.toString(), longitude.toString(),
                                "", "","",
                                locality )

                            trader_list_progressbar?.showProgressBar()
                        }
                    }
                    else {
                        showSnackBar(this, getString(R.string.no_internet_error))
                    }

                }
            } } }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == LAUNCH_SECOND_ACTIVITY) {
            if (resultCode == 1215) {
                tvFilter.text = "Clear Filter"
                val returnName = data!!.getStringExtra("name")
                val returnLocation = data.getStringExtra("location")
                val returnDistance = data.getStringExtra("distance")
                val returnLat = data.getStringExtra("latitude")
                val returnlng = data.getStringExtra("longitude")
                val typeId = data.getStringExtra("typeId")
                tv_city_zipcode.text = data.getStringExtra("location")

                Log.e("=======","===$returnName====$returnLocation====$returnDistance====$returnLat====$returnlng====$typeId===")

                if (checkIfHasNetwork(this)) {
                    launch(Dispatchers.Main.immediate) {
                       val authKey = dataStoragePreference.emitStoredValue(preferencesKey<String>("auth_key")).first()
                        appViewModel.sendFilterTraderListData(security_key, authKey, returnLat!!, returnlng,
                            returnDistance!!, returnName,typeId,
                            returnLocation!! )
                        trader_list_progressbar?.showProgressBar()

                    }
                }
                else {
                    showSnackBar(this, getString(R.string.no_internet_error))
                }
            } } }

    override fun onTraderListItemClickListner(
        position: Int,
        postId: String,
        latitude: String,
        longitude: String
    ) {
        OpenActivity(TraderPublishActivty::class.java){
            putString("postId", postId)
            putString("latti", latitude)
            putString("longi", longitude)
        }
    }

    override fun onFavouriteItemClickListner(position: Int, postID: String, favourite: ImageView) {

         ivFavourite = favourite

        if (checkIfHasNetwork(this@TraderListingActivity)) {
            appViewModel.addFavouritePostApiData(security_key, authkey, postID, "3")
            trader_list_progressbar.showProgressBar()
        } else {
            showSnackBar(this@TraderListingActivity, getString(R.string.no_internet_error))
        }
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job
}