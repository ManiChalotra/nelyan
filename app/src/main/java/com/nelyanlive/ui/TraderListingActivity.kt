package com.nelyanlive.ui

import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.preferences.core.preferencesKey
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.nelyanlive.R
import com.nelyanlive.adapter.TraderListingAdapter
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


class TraderListingActivity : AppCompatActivity(), View.OnClickListener,
    TraderListingAdapter.OnTraderItemClickListner, CoroutineScope {

    private val job by lazy {
        Job()
    }
    lateinit var ivFavourite: ImageView
    private var listType: String? = null

    private var traderListingAdapter: TraderListingAdapter? = null
    private var rvTrader: RecyclerView? = null
    var LAUNCH_SECOND_ACTIVITY = 3

    private val traderDatalist by lazy { ArrayList<HomeTraderListData>() }

    private val appViewModel by lazy {
        ViewModelProvider.AndroidViewModelFactory.getInstance(this.application)
            .create(AppViewModel::class.java)
    }
    private val dataStoragePreference by lazy { DataStoragePreference(this@TraderListingActivity) }
    private var authKey: String? = ""
    var dataString = ""

    private var latitude: String = "42.6026"
    private var longitude: String = "20.9030"
    private var locality: String = ""

    override fun onResume() {
        super.onResume()

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_trader_listing)
        initalizeClicks()
        rvTrader = findViewById(R.id.rv_traderListing)

        if (intent.extras != null) {
            listType = intent.getStringExtra("type").toString()
            Log.e("qwe", intent.getStringExtra("type").toString())
        }
        checkMvvmResponse()
        if (tvFilter.text == getString(R.string.filter)) {
            launch(Dispatchers.Main.immediate) {
                latitude =
                    dataStoragePreference.emitStoredValue(preferencesKey<String>("latitudeLogin"))
                        .first()
                longitude =
                    dataStoragePreference.emitStoredValue(preferencesKey<String>("longitudeLogin"))
                        .first()

                val geocoder = Geocoder(this@TraderListingActivity, Locale.getDefault())
                val list = geocoder.getFromLocation(latitude.toDouble(), longitude.toDouble(), 1)

                locality = if (!list[0].locality.isNullOrBlank()) {
                    list[0].locality
                } else {
                    dataStoragePreference.emitStoredValue(preferencesKey<String>("cityLogin"))
                        .first()
                }

                Log.e("location_changed", "==2=ifffff=$latitude==$longitude=")
                if (latitude != "0.0") {

                    tv_city_zipcode.text = locality
                    if (checkIfHasNetwork(this@TraderListingActivity)) {
                        launch(Dispatchers.Main.immediate) {
                            val authKey =
                                dataStoragePreference.emitStoredValue(preferencesKey<String>("auth_key"))
                                    .first()

                            appViewModel.sendFilterTraderListData(
                                security_key, authKey,
                                latitude, longitude,
                                "", "", "",
                                locality
                            )

                            trader_list_progressbar?.showProgressBar()
                        }
                    } else {
                        showSnackBar(
                            this@TraderListingActivity,
                            getString(R.string.no_internet_error)
                        )
                    }
                }
            }
        }

        Log.d(
            TraderListingActivity::class.java.name,
            "TraderOnCreate_Name  " + AllSharedPref.restoreString(this, "returnnametrade")
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

    private fun checkMvvmResponse() {
        appViewModel.observeHomeActivitiesResponse()!!
            .observe(this, androidx.lifecycle.Observer { response ->
                if (response!!.isSuccessful && response.code() == 200) {
                    if (response.body() != null) {
                        trader_list_progressbar?.hideProgressBar()
                        Log.d(
                            "homeTraderListRespone",
                            "-------------" + Gson().toJson(response.body())
                        )
                        dataString = response.body().toString()
                        val homeTraderResponse = Gson().fromJson<HomeTraderPostListResponse>(
                            response.body().toString(),
                            HomeTraderPostListResponse::class.java
                        )
                        traderDatalist.clear()
                        traderDatalist.addAll(homeTraderResponse.data)

                        if (traderDatalist.size == 0) {
                            rvTrader!!.visibility = View.GONE
                            tv_no_trader_list!!.visibility = View.VISIBLE
                        } else {
                            rvTrader!!.visibility = View.VISIBLE
                            tv_no_trader_list!!.visibility = View.GONE
                            setTraderAdapter(traderDatalist)
                        }
                    }
                } else {
                    ErrorBodyResponse(response, this, trader_list_progressbar)
                }
            })
        appViewModel.observeFilterTraderListResponse()!!
            .observe(this, androidx.lifecycle.Observer { response ->
                if (response!!.isSuccessful && response.code() == 200) {
                    if (response.body() != null) {
                        trader_list_progressbar?.hideProgressBar()
                        Log.d(
                            "homeTraderListRespone",
                            "-------------" + Gson().toJson(response.body())
                        )
                        dataString = response.body().toString()

                        val homeTraderResponse = Gson().fromJson<HomeTraderPostListResponse>(
                            response.body().toString(),
                            HomeTraderPostListResponse::class.java
                        )
                        traderDatalist.clear()
                        traderDatalist.addAll(homeTraderResponse.data)
                        if (traderDatalist.size == 0) {
                            rvTrader!!.visibility = View.GONE
                            tv_no_trader_list!!.visibility = View.VISIBLE
                        } else {
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
                if (message == "You marked this Post as Your Favourite") {
                    myCustomToast(getString(R.string.marked_as_fav))

                    ivFavourite.setImageResource(R.drawable.heart)
                } else {
                    myCustomToast(getString(R.string.removed_from_fav))

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
                if (dataString.isEmpty()) {
                    myCustomToast(getString(R.string.data_not_loaded))
                } else {
                    val i = Intent(this, HomeChildCareOnMapActivity::class.java)
//                    i.putExtra("dataString", dataString)
                    AllSharedPref.save(
                        this,
                        "dataString",
                        dataString
                    )
                    i.putExtra("type", "trader")
                    startActivity(i)
                }
            }
            R.id.tvFilter -> {
                if (tvFilter.text == getString(R.string.filter) || tvFilter.text ==
                    getString(R.string.clear_filter)
                ) {
                    val i = Intent(this, TraderFilterActivity::class.java)
                    startActivityForResult(i, LAUNCH_SECOND_ACTIVITY)
                } else {
                    tvFilter.text = getString(R.string.filter)
                    if (checkIfHasNetwork(this)) {
                        launch(Dispatchers.Main.immediate) {
                            val authKey =
                                dataStoragePreference.emitStoredValue(preferencesKey<String>("auth_key"))
                                    .first()
                            tv_city_zipcode.text = locality

                            appViewModel.sendFilterTraderListData(
                                security_key, authKey,
                                latitude, longitude,
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == LAUNCH_SECOND_ACTIVITY) {
            if (resultCode == 1215) {
//                tvFilter.text = getString(R.string.clear_filter)
                tvFilter.text = getString(R.string.clear_filter)
                val returnName = data!!.getStringExtra("name")
                val returnLocation = data.getStringExtra("location")
                val returnDistance = data.getStringExtra("distance")
                val returnLat = data.getStringExtra("latitude")
                val returnlng = data.getStringExtra("longitude")
                val typeId = data.getStringExtra("typeId")
                tv_city_zipcode.text = data.getStringExtra("location")
                var TypeActivity = data.getStringExtra("SelectValuetrade")!!

                Log.e(
                    "=======",
                    "===$returnName====$returnLocation====$returnDistance====$returnLat====$returnlng====$typeId==="
                )

                Log.d(
                    "TraderActivity ",
                    "returnValues_TypeActivity   " + TypeActivity
                )

                AllSharedPref.save(this, "returnnametrade", returnName!!)
                AllSharedPref.save(this, "returnlocationtrade", returnLocation!!)
                AllSharedPref.save(this, "returndistancetrade", returnDistance!!)
                AllSharedPref.save(this, "SelectValuetrade", TypeActivity!!)

                Log.d(
                    "TraderActivity ",
                    "returnValues_Trader   " + AllSharedPref.restoreString(
                        this,
                        "returnDistance"
                    ) + "  " +
                            AllSharedPref.restoreString(this, "SelectValuetrade")
                )


                val geocoder = Geocoder(this, Locale.getDefault())
                var list = listOf<Address>()

                list = geocoder.getFromLocation(returnLat!!.toDouble(), returnlng!!.toDouble(), 1)

                val filteredAddress = list[0].locality


                if (checkIfHasNetwork(this)) {
                    launch(Dispatchers.Main.immediate) {
                        val authKey =
                            dataStoragePreference.emitStoredValue(preferencesKey<String>("auth_key"))
                                .first()
                        appViewModel.sendFilterTraderListData(
                            security_key, authKey, returnLat!!, returnlng,
                            returnDistance!!, returnName, typeId,
                            filteredAddress!!
                        )
                        trader_list_progressbar?.showProgressBar()

                    }
                } else {
                    showSnackBar(this, getString(R.string.no_internet_error))
                }
            }
        }
    }

    override fun onTraderListItemClickListner(
        position: Int,
        postId: String,
        latitude: String,
        longitude: String
    ) {
        OpenActivity(TraderPublishActivty::class.java) {
            putString("postId", postId)
            putString("latti", latitude)
            putString("longi", longitude)
        }
    }

    override fun onFavouriteItemClickListner(position: Int, postID: String, favourite: ImageView) {

        ivFavourite = favourite

        if (checkIfHasNetwork(this@TraderListingActivity)) {

            launch(Dispatchers.Main.immediate) {
                authKey = dataStoragePreference.emitStoredValue(preferencesKey<String>("auth_key"))
                    .first()

                appViewModel.addFavouritePostApiData(security_key, authKey, postID, "3")
            }
            trader_list_progressbar.showProgressBar()
        } else {
            showSnackBar(this@TraderListingActivity, getString(R.string.no_internet_error))
        }
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job
}