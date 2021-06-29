package com.nelyanlive.ui

import android.app.Dialog
import android.content.*
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.preferences.core.preferencesKey
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.nelyanlive.R
import com.nelyanlive.adapter.ActivityListAdapter
import com.nelyanlive.data.viewmodel.AppViewModel
import com.nelyanlive.db.DataStoragePreference
import com.nelyanlive.modals.homeactivitylist.HomeAcitivityResponseData
import com.nelyanlive.modals.homeactivitylist.HomeActivityResponse
import com.nelyanlive.utils.*
import kotlinx.android.synthetic.main.fragment_activity_list.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList
import kotlin.coroutines.CoroutineContext


class ActivitiesListActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener,
    ActivityListAdapter.OnHomeActivitiesRecyclerViewItemClickListner, CoroutineScope {

    var iv_map: ImageView? = null
    var ivBack: ImageView? = null
    var orderby: Spinner? = null
    var dialog: Dialog? = null
    var listType: String? = null
    var activityListAdapter: ActivityListAdapter? = null
    var recyclerview: RecyclerView? = null
    var ivFavouritee: ImageView? = null
    var LAUNCH_SECOND_ACTIVITY = 1
    var dataString = ""
    var enabled = false

    private val activitisDatalist by lazy { ArrayList<HomeAcitivityResponseData>() }

    private val appViewModel by lazy {
        ViewModelProvider.AndroidViewModelFactory.getInstance(this.application)
            .create(AppViewModel::class.java)
    }
    private val dataStoragePreference by lazy { DataStoragePreference(this@ActivitiesListActivity) }
    private var authKey: String? = ""

    private val job by lazy {
        Job()
    }

    private var latitude: String = "42.6026"
    private var longitude: String = "20.9030"
    private var locality: String = ""

    override fun onResume() {
        super.onResume()

        if ((tvFilter.text == "Filter")) {
            launch(Dispatchers.Main.immediate) {
                latitude =
                    dataStoragePreference.emitStoredValue(preferencesKey<String>("latitudeLogin"))
                        .first()
                latitude =
                    dataStoragePreference.emitStoredValue(preferencesKey<String>("longitudeLogin"))
                        .first()
                locality =
                    dataStoragePreference.emitStoredValue(preferencesKey<String>("cityLogin"))
                        .first()

                Log.e("location_changed", "==2=ifffff=$latitude==$longitude===$locality")
                if (latitude != "0.0") {
                    tv_userCityOrZipcode.text = locality
                    if (checkIfHasNetwork(this@ActivitiesListActivity)) {
                        launch(Dispatchers.Main.immediate) {
                            val authKey =
                                dataStoragePreference.emitStoredValue(preferencesKey<String>("auth_key"))
                                    .first()
                            appViewModel.sendFilterActivityListData(
                                security_key, authKey,
                                latitude, longitude, "", "", "",
                                locality
                            )
                            activity_list_progressbar?.showProgressBar()
                        }
                    } else {
                        showSnackBar(
                            this@ActivitiesListActivity,
                            getString(R.string.no_internet_error)
                        )
                    }

                }
            }

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_activity_list)
        ivBack = findViewById(R.id.ivBack)
        orderby = findViewById(R.id.trader_type)
        iv_map = findViewById(R.id.iv_map)
        recyclerview = findViewById(R.id.rv_home_activities)

        if (intent.extras != null) {
            listType = intent.getStringExtra("type").toString()
            Log.e("qwe", intent.getStringExtra("type").toString())
        }

        ivBack!!.setOnClickListener {
            onBackPressed()
        }
        checkMvvmResponse()

        tvFilter.setOnClickListener {

            if (tvFilter.text == "Filter") {
                val i =
                    Intent(this, ActivitiesFilterActivity::class.java).putExtra("name", "Activity")
                startActivityForResult(i, LAUNCH_SECOND_ACTIVITY)
            } else {

                if (checkIfHasNetwork(this)) {
                    tvFilter.text = "Filter"
                    launch(Dispatchers.Main.immediate) {
                        val authKey =
                            dataStoragePreference.emitStoredValue(preferencesKey<String>("auth_key"))
                                .first()
                        appViewModel.sendFilterActivityListData(
                            security_key, authKey,
                            latitude, longitude, "", "", "",
                            locality
                        )
                        tv_userCityOrZipcode.text = locality
                        activity_list_progressbar?.showProgressBar()

                    }
                } else {
                    showSnackBar(this, getString(R.string.no_internet_error))
                }
            }
        }

        iv_map!!.setOnClickListener {
            if (dataString.isEmpty()) {
                myCustomToast("Data not loaded yet")
            } else {
                val i = Intent(this, ActivitiesOnMapActivity::class.java)
                i.putExtra("type", listType)
                i.putExtra("dataString", dataString)
                startActivity(i)
            }
        }

        val orderByList = arrayOf<String?>(
            "", "Events in City",
            "Date Added",
            "Distance"
        )
        val adapter: ArrayAdapter<*> = ArrayAdapter<Any?>(this, R.layout.customspinner, orderByList)
        // Setting Adapter to the Spinner
        orderby!!.adapter = adapter
        // Setting OnItemClickListener to the Spinner
        orderby!!.onItemSelectedListener = this@ActivitiesListActivity
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == LAUNCH_SECOND_ACTIVITY) {
            if (resultCode == 1213) {

                tvFilter.text = "Clear Filter"

                val returnName = data!!.getStringExtra("name")
                val returnLocation = data.getStringExtra("location")
                val returnDistance = data.getStringExtra("distance")
                val returnLat = data.getStringExtra("latitude")
                val returnlng = data.getStringExtra("longitude")
                val typeId = data.getStringExtra("typeId")
                tv_userCityOrZipcode.text = data.getStringExtra("location")
                Log.e("=======", "===$returnName====$returnLocation====$returnDistance====$returnLat====$returnlng====$typeId==="
                )


                if (checkIfHasNetwork(this)) {
                    launch(Dispatchers.Main.immediate) {
                        val authKey =
                            dataStoragePreference.emitStoredValue(preferencesKey<String>("auth_key"))
                                .first()
                        appViewModel.sendFilterActivityListData(
                            security_key,
                            authKey,
                            returnLat!!,
                            returnlng,
                            returnDistance!!,
                            returnName,
                            typeId,
                            returnLocation!!
                        )
                        activity_list_progressbar?.showProgressBar()

                    }
                } else {
                    showSnackBar(this, getString(R.string.no_internet_error))
                }
            }
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {}

    override fun onNothingSelected(parent: AdapterView<*>?) {}

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job


    private fun setAdaptor(activitisDatalist: ArrayList<HomeAcitivityResponseData>) {
        activityListAdapter = ActivityListAdapter(this, activitisDatalist, this)
        recyclerview!!.layoutManager = LinearLayoutManager(this)
        recyclerview!!.adapter = activityListAdapter
    }

    private fun checkMvvmResponse() {
        appViewModel.observeHomeActivitiesResponse()!!
            .observe(this, androidx.lifecycle.Observer { response ->
                if (response!!.isSuccessful && response.code() == 200) {
                    if (response.body() != null) {
                        activity_list_progressbar?.hideProgressBar()
                        Log.d("myadsResponse", "-------------" + Gson().toJson(response.body()))
                        dataString = response.body().toString()
                        val homeAcitivitiesResponse = Gson().fromJson<HomeActivityResponse>(
                            response.body().toString(),
                            HomeActivityResponse::class.java
                        )

                        activitisDatalist.clear()
                        activitisDatalist.addAll(homeAcitivitiesResponse.data)
                        if (activitisDatalist.size == 0) {
                            recyclerview!!.visibility = View.GONE
                            tv_no_activities!!.visibility = View.VISIBLE
                        } else {
                            recyclerview!!.visibility = View.VISIBLE
                            tv_no_activities!!.visibility = View.GONE
                            setAdaptor(activitisDatalist)
                        }
                    }
                } else {
                    ErrorBodyResponse(response, this, activity_list_progressbar)
                    activity_list_progressbar?.hideProgressBar()
                }
            })

        appViewModel.observeFilterActivityListResponse()!!
            .observe(this, androidx.lifecycle.Observer { response ->
                if (response!!.isSuccessful && response.code() == 200) {
                    if (response.body() != null) {
                        activity_list_progressbar?.hideProgressBar()
                        Log.d("myadsResponse", "-------------" + Gson().toJson(response.body()))
                        dataString = response.body().toString()
                        val homeAcitivitiesResponse = Gson().fromJson<HomeActivityResponse>(
                            response.body().toString(),
                            HomeActivityResponse::class.java
                        )

                        activitisDatalist.clear()
                        activitisDatalist.addAll(homeAcitivitiesResponse.data)
                        if (activitisDatalist.size == 0) {
                            recyclerview!!.visibility = View.GONE
                            tv_no_activities!!.visibility = View.VISIBLE
                        }
                        else {
                            recyclerview!!.visibility = View.VISIBLE
                            tv_no_activities!!.visibility = View.GONE
                            setAdaptor(activitisDatalist)
                        }
                    }
                } else {
                    ErrorBodyResponse(response, this, null)
                    activity_list_progressbar?.hideProgressBar()
                }
            })

        appViewModel.observeAddFavouritePostApiResponse()!!.observe(this, Observer { response ->
            if (response!!.isSuccessful && response.code() == 200) {
                Log.d("addFavouriteResBody", "----------" + Gson().toJson(response.body()))
                activity_list_progressbar?.hideProgressBar()
                val mResponse = response.body().toString()
                val jsonObject = JSONObject(mResponse)
                val message = jsonObject.get("msg").toString()
                if (message == "You marked this Post as Your Favourite") {
                    myCustomToast("You marked this post as your favourite")
                    ivFavouritee!!.setImageResource(R.drawable.heart)
                }
                else
                {
                    myCustomToast("You removed this post from your favourite")
                    ivFavouritee!!.setImageResource(R.drawable.heart_purple)
                }
            }
            else {
                ErrorBodyResponse(response, this, activity_list_progressbar)
                activity_list_progressbar?.hideProgressBar()
            }
        })

        appViewModel.getException()!!.observe(this, Observer {
            myCustomToast(it)
            activity_list_progressbar?.hideProgressBar()
        })
    }

    override fun onAddFavoriteClick(eventID: String, ivFavourite: ImageView) {
        ivFavouritee = ivFavourite

        if (checkIfHasNetwork(this@ActivitiesListActivity)) {
            launch(Dispatchers.Main.immediate) {
                authKey = dataStoragePreference.emitStoredValue(preferencesKey<String>("auth_key")).first()
                appViewModel.addFavouritePostApiData(security_key, authKey, eventID, "1")
            }
            activity_list_progressbar.showProgressBar()
        } else {
            showSnackBar(this@ActivitiesListActivity, getString(R.string.no_internet_error))
        }
    }

    override fun onHomeActivitiesItemClickListner(
        activityId: String,
        categoryId: String,
        postLatitude: String,
        postLongitude: String
    ) {
        OpenActivity(ActivityDetailsActivity::class.java) {
            putString("activityId", activityId)
            putString("categoryId", categoryId)
            putString("lati", postLatitude)
            putString("longi", postLongitude)
        }
    }


}


