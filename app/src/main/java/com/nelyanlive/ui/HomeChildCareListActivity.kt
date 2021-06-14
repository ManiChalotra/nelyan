package com.nelyanlive.ui


import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
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
import com.nelyanlive.adapter.HomeChildCareListAdapter
import com.nelyanlive.current_location.GPSTracker
import com.nelyanlive.data.viewmodel.AppViewModel
import com.nelyanlive.db.DataStoragePreference
import com.nelyanlive.modals.homechildcare.HomeChiildCareREsponse
import com.nelyanlive.modals.homechildcare.HomeChildCareeData
import com.nelyanlive.utils.*
import kotlinx.android.synthetic.main.activity_home_child_care_list.*
import kotlinx.android.synthetic.main.activity_home_child_care_list.ivBack
import kotlinx.android.synthetic.main.activity_home_child_care_list.iv_map
import kotlinx.android.synthetic.main.activity_home_child_care_list.ll_public
import kotlinx.android.synthetic.main.activity_home_child_care_list.trader_type
import kotlinx.android.synthetic.main.activity_home_child_care_list.tvFilter
import kotlinx.android.synthetic.main.activity_home_child_care_list.tv_userCityOrZipcode
import kotlinx.android.synthetic.main.fragment_activity_list.*
import kotlinx.android.synthetic.main.fragment_child_care.*
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

class HomeChildCareListActivity : AppCompatActivity(), View.OnClickListener,
    AdapterView.OnItemSelectedListener,
    HomeChildCareListAdapter.OnChatListItemClickListner, CoroutineScope {

    private val job by lazy {
        Job()
    }
    private var listType: String? = null
    var LAUNCH_SECOND_ACTIVITY = 2

    private var homeChildCareListAdapter: HomeChildCareListAdapter? = null
    private var recyclerview: RecyclerView? = null
    private var ivFavouritee: ImageView? = null

    private val childCareDatalist by lazy { ArrayList<HomeChildCareeData>() }

    private val appViewModel by lazy {
        ViewModelProvider.AndroidViewModelFactory.getInstance(this.application)
            .create(AppViewModel::class.java)
    }
    private val dataStoragePreference by lazy { DataStoragePreference(this@HomeChildCareListActivity) }
    private var authkey: String? = null
    var dataString = ""

    private var gpsTracker: GPSTracker? = null
    private var latitude: Double = 42.6026
    private var longitude: Double = 20.9030
    private var locality: String = ""

    override fun onResume() {
            super.onResume()
           gpsTracker = GPSTracker(this)
        if((tvFilter.text=="Filter")) {
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
                    val geocoder = Geocoder(this@HomeChildCareListActivity, Locale.getDefault())
                    var list = listOf<Address>()

                    list = geocoder.getFromLocation(latitude, longitude, 1)

                    Log.e("location_changed", "==home==Foreground address: ${list[0].locality}==")
                    tv_userCityOrZipcode.text = list[0].locality
                    locality = list[0].locality
                    if (checkIfHasNetwork(this)) {
                        launch(Dispatchers.Main.immediate) {
                            val authKey =
                                dataStoragePreference.emitStoredValue(preferencesKey<String>("auth_key"))
                                    .first()
                            appViewModel.sendChildCareFilterData(
                                security_key,
                                authKey,
                                latitude.toString(),
                                longitude.toString(),
                                "",
                                "",
                                locality,
                                ""
                            )
                            child_care_list_progressbar?.hideProgressBar()
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
        setContentView(R.layout.activity_home_child_care_list)
        initalizeClicks()

        recyclerview = findViewById(R.id.recyclerview)


        //current location
        if (foregroundPermissionApproved()) {
            val subscribeToLocationUpdates = HomeActivity.locationService?.subscribeToLocationUpdates()

            Log.e("location_changed", "=========$subscribeToLocationUpdates======")

            if (subscribeToLocationUpdates != null) subscribeToLocationUpdates else Log.e("location_changed", "Service Not Bound")
        }
        else {
            requestPermissions()
        }


        if (intent.extras != null) {
            listType = intent.getStringExtra("type").toString()
            Log.e("qwe", intent.getStringExtra("type").toString())
        }

        // for setting order module listing
        val genderlist = arrayOf<String?>(
            "",
            "Date Added",
            "Avilable Place",
            "Distance"
        )
        val adapter: ArrayAdapter<*> = ArrayAdapter<Any?>(
            this, R.layout.customspinner, genderlist
        )

        // Setting Adapter to the Spinner
        trader_type!!.adapter = adapter
        // Setting OnItemClickListener to the Spinner
        trader_type!!.onItemSelectedListener = this@HomeChildCareListActivity


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
                        this@HomeChildCareListActivity,
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        PERMISSIONS_REQUEST_CODE
                    )
                }
                .show()
        } else {
            Log.d("TAG", "Request foreground only permission")
            ActivityCompat.requestPermissions(
                this@HomeChildCareListActivity,
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


    private fun setChildcareAdapter(childCareDatalist: ArrayList<HomeChildCareeData>) {
        homeChildCareListAdapter = HomeChildCareListAdapter(this, childCareDatalist, this)
        recyclerview!!.layoutManager = LinearLayoutManager(this)
        recyclerview!!.adapter = homeChildCareListAdapter
    }

    private fun initalizeClicks() {
        ivBack.setOnClickListener(this)
        ll_public.setOnClickListener(this)
        tvFilter.setOnClickListener(this)
        iv_map.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.ivBack -> {
                onBackPressed()
            }
            R.id.ll_public -> {
            }
            R.id.tvFilter -> {
                if (tvFilter.text == "Filter") {
                    val i = Intent(this, ChildCareFilterActivity::class.java)
                    startActivityForResult(i, LAUNCH_SECOND_ACTIVITY)
                } else {

                    if (checkIfHasNetwork(this)) {
                        tvFilter.text = "Filter"
                        launch(Dispatchers.Main.immediate) {
                            val authKey = dataStoragePreference.emitStoredValue(preferencesKey<String>("auth_key")).first()

                            tv_userCityOrZipcode.text = locality

                            appViewModel.sendChildCareFilterData(
                                security_key,
                                authKey,
                                latitude.toString(),
                                longitude.toString(),
                                "",
                                "",
                                locality,
                                ""
                            )
                            child_care_list_progressbar?.hideProgressBar() }
                    }
                    else {
                        showSnackBar(this, getString(R.string.no_internet_error))
                    }




                }
            }
            R.id.iv_map -> {
                if (dataString.isEmpty()) {
                    myCustomToast("Data not loaded yet")
                }
                else {
                    val i = Intent(this, HomeChildCareOnMapActivity::class.java)
                    i.putExtra("dataString", dataString)
                    i.putExtra("type", "childCare")
                    startActivity(i)
                }
            } }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == LAUNCH_SECOND_ACTIVITY) {
            if (resultCode == 1214) {
                tvFilter.text = "Clear Filter"
                val returnName = data!!.getStringExtra("name")
                val returnLocation = data.getStringExtra("location")
                val returnDistance = data.getStringExtra("distance")
                val returnLat = data.getStringExtra("latitude")
                val returnlng = data.getStringExtra("longitude")
                val childCareType = data.getStringExtra("childCareType")
                tv_userCityOrZipcode.text = data.getStringExtra("location")
                Log.e("=======","===$returnName====$returnLocation====$returnDistance====$returnLat====$returnlng====$childCareType===")



                if (checkIfHasNetwork(this)) {
                    launch(Dispatchers.Main.immediate) {
                        val authKey =
                            dataStoragePreference.emitStoredValue(preferencesKey<String>("auth_key"))
                                .first()
                        appViewModel.sendChildCareFilterData(
                            security_key,
                            authKey,
                            returnLat!!,
                            returnlng,
                            returnDistance!!,
                            returnName,
                            returnLocation!!,
                            childCareType!!
                        )
                        child_care_list_progressbar?.hideProgressBar() }
                }
                else {
                    showSnackBar(this, getString(R.string.no_internet_error))
                }
            } } }

    override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {}

    override fun onNothingSelected(parent: AdapterView<*>?) {}


    override fun onItemClickListner(position: Int) {
        OpenActivity(NurserieActivityy::class.java)
    }

    override fun onAddFavoriteClick(position: Int, postId: String?, ivFavourite: ImageView) {
        ivFavouritee = ivFavourite
        if (checkIfHasNetwork(this@HomeChildCareListActivity)) {
            appViewModel.addFavouritePostApiData(security_key, authkey, postId, "2")
            child_care_list_progressbar.showProgressBar()
        } else {
            showSnackBar(this@HomeChildCareListActivity, getString(R.string.no_internet_error))
        }
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private fun checkMvvmResponse() {
        appViewModel.observeHomeActivitiesResponse()!!
            .observe(this, androidx.lifecycle.Observer { response ->
                if (response!!.isSuccessful && response.code() == 200) {
                    if (response.body() != null) {
                        child_care_list_progressbar?.hideProgressBar()
                        Log.d("homeChilcCare", "-------------" + Gson().toJson(response.body()))
                        val mResponse = response.body().toString()
                        dataString = response.body().toString()
                        val jsonObject = JSONObject(mResponse)
                        val homeChildcareResponse = Gson().fromJson<HomeChiildCareREsponse>(
                            response.body().toString(), HomeChiildCareREsponse::class.java)
                        if (childCareDatalist != null) {
                            childCareDatalist.clear()
                            childCareDatalist.addAll(homeChildcareResponse.data)
                        }
                        if (childCareDatalist.size == 0) {
                            recyclerview!!.visibility = View.GONE
                            tv_no_childcare!!.visibility = View.VISIBLE
                        } else {
                            recyclerview!!.visibility = View.VISIBLE
                            tv_no_childcare!!.visibility = View.GONE
                            setChildcareAdapter(childCareDatalist)
                        }
                    }
                } else {
                    ErrorBodyResponse(response, this, child_care_list_progressbar)
                }
            })
        appViewModel.observeFilterChildCareListResponse()!!.observe(this, androidx.lifecycle.Observer { response ->
                if (response!!.isSuccessful && response.code() == 200) {
                    if (response.body() != null) {
                        child_care_list_progressbar?.hideProgressBar()
                        Log.d("homeChilcCare", "-------------" + Gson().toJson(response.body()))
                        val mResponse = response.body().toString()
                        dataString = response.body().toString()
                        val jsonObject = JSONObject(mResponse)
                        val homeChildcareResponse = Gson().fromJson(
                            response.body().toString(),
                            HomeChiildCareREsponse::class.java
                        )
                        if (childCareDatalist != null) {
                            childCareDatalist.clear()
                            childCareDatalist.addAll(homeChildcareResponse.data)
                        }

                        if (childCareDatalist.size == 0) {
                            recyclerview!!.visibility = View.GONE
                            tv_no_childcare!!.visibility = View.VISIBLE
                        } else {
                            recyclerview!!.visibility = View.VISIBLE
                            tv_no_childcare!!.visibility = View.GONE
                            setChildcareAdapter(childCareDatalist)
                        }
                    }
                } else {
                    ErrorBodyResponse(response, this, null)
                    child_care_list_progressbar?.hideProgressBar()
                }
            })

        appViewModel.observeAddFavouritePostApiResponse()!!.observe(this, Observer { response ->
            if (response!!.isSuccessful && response.code() == 200) {
                Log.d("addFavouriteResBody", "----------" + Gson().toJson(response.body()))
                child_care_list_progressbar?.hideProgressBar()
                val mResponse = response.body().toString()
                val jsonObject = JSONObject(mResponse)

                val message = jsonObject.get("msg").toString()
                if (message == "You marked this Post as Your Favourite") {
                    myCustomToast("You marked this post as your favourite")

                    ivFavouritee!!.setImageResource(R.drawable.heart)
                } else {
                    myCustomToast("You removed this post from your favourite")
                    ivFavouritee!!.setImageResource(R.drawable.heart_purple)
                }
            } else {
                ErrorBodyResponse(response, this, child_care_list_progressbar)
                child_care_list_progressbar?.hideProgressBar()
            }
        })

        appViewModel.getException()!!.observe(this, Observer {
            myCustomToast(it)
            child_care_list_progressbar?.hideProgressBar()
        })
    }


}