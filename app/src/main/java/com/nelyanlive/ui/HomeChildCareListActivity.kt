package com.nelyanlive.ui


import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.preferences.core.preferencesKey
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.nelyanlive.R
import com.nelyanlive.adapter.HomeChildCareListAdapter
import com.nelyanlive.data.viewmodel.AppViewModel
import com.nelyanlive.db.DataStoragePreference
import com.nelyanlive.modals.homechildcare.HomeChiildCareREsponse
import com.nelyanlive.modals.homechildcare.HomeChildCareeData
import com.nelyanlive.utils.*
import kotlinx.android.synthetic.main.activity_home_child_care_list.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList
import kotlin.coroutines.CoroutineContext


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
        setContentView(R.layout.activity_home_child_care_list)
        initalizeClicks()

        recyclerview = findViewById(R.id.recyclerview)

        if (intent.extras != null) {
            listType = intent.getStringExtra("type").toString()
            Log.e("qwe", intent.getStringExtra("type").toString())
        }

        // for setting order module listing
        val genderList = arrayOf<String?>(
            "",
            "Date Added",
            "Available Place",
            "Distance"
        )
        val adapter: ArrayAdapter<*> = ArrayAdapter<Any?>(
            this, R.layout.customspinner, genderList
        )

        // Setting Adapter to the Spinner
        trader_type!!.adapter = adapter
        // Setting OnItemClickListener to the Spinner
        trader_type!!.onItemSelectedListener = this@HomeChildCareListActivity
        checkMvvmResponse()
        if((tvFilter.text==getString(R.string.filter))) {
            launch(Dispatchers.Main.immediate) {
                latitude = dataStoragePreference.emitStoredValue(preferencesKey<String>("latitudeLogin")).first()
                longitude =
                    dataStoragePreference.emitStoredValue(preferencesKey<String>("longitudeLogin"))
                        .first()
                val geocoder = Geocoder(this@HomeChildCareListActivity, Locale.getDefault())
                val list: List<Address>
                Log.e("location_changed", "==3=ifffff=$latitude==$longitude===$locality=")
                if (latitude != "0.0") {
                    list = geocoder.getFromLocation(latitude.toDouble(), longitude.toDouble(), 1)

                    locality = list[0].locality

                    tv_userCityOrZipcode.text = locality
                    if (checkIfHasNetwork(this@HomeChildCareListActivity)) {
                        launch(Dispatchers.Main.immediate) {
                            val authKey =
                                dataStoragePreference.emitStoredValue(preferencesKey<String>("auth_key"))
                                    .first()
                            appViewModel.sendChildCareFilterData(
                                security_key,
                                authKey,
                                latitude,
                                longitude,
                                "",
                                "",
                                locality,
                                ""
                            )
                            childProgressbar?.showProgressBar()
                        }
                    } else {
                        showSnackBar(this@HomeChildCareListActivity, getString(R.string.no_internet_error))
                    } } } }

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
                if (tvFilter.text == getString(R.string.filter)) {
                    val i = Intent(this, ChildCareFilterActivity::class.java)
                    startActivityForResult(i, LAUNCH_SECOND_ACTIVITY)
                } else {

                    if (checkIfHasNetwork(this)) {
                        tvFilter.text = getString(R.string.filter)
                        launch(Dispatchers.Main.immediate) {
                            val authKey = dataStoragePreference.emitStoredValue(preferencesKey<String>("auth_key")).first()

                            tv_userCityOrZipcode.text = locality

                            appViewModel.sendChildCareFilterData(
                                security_key,
                                authKey,
                                latitude,
                                longitude,
                                "",
                                "",
                                locality,
                                ""
                            )
                            childProgressbar?.showProgressBar() }
                    }
                    else {
                        showSnackBar(this, getString(R.string.no_internet_error))
                    }
                }
            }
            R.id.iv_map -> {
                if (dataString.isEmpty()) {
                    myCustomToast(getString(R.string.data_not_loaded))
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
                tvFilter.text = getString(R.string.clear_filter)
                val returnName = data!!.getStringExtra("name")
                val returnDistance = data.getStringExtra("distance")
                val returnLat = data.getStringExtra("latitude")
                val returnlng = data.getStringExtra("longitude")
                val childCareType = data.getStringExtra("childCareType")
                tv_userCityOrZipcode.text = data.getStringExtra("location")
                val geocoder = Geocoder(this, Locale.getDefault())

                val list: List<Address> =
                    geocoder.getFromLocation(returnLat!!.toDouble(), returnlng!!.toDouble(), 1)

                val filteredAddress = list[0].locality


                if (checkIfHasNetwork(this)) {
                    launch(Dispatchers.Main.immediate) {
                        val authKey =
                            dataStoragePreference.emitStoredValue(preferencesKey<String>("auth_key"))
                                .first()
                        appViewModel.sendChildCareFilterData(
                            security_key,
                            authKey,
                            returnLat,
                            returnlng,
                            returnDistance!!,
                            returnName,
                            filteredAddress!!,
                            childCareType!!
                        )
                        childProgressbar?.visibility = View.VISIBLE
                    }
                }
                else {
                    showSnackBar(this, getString(R.string.no_internet_error))
                }
            } } }

    override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {}

    override fun onNothingSelected(parent: AdapterView<*>?) {}


    override fun onItemClickListner(position: Int) {
    }

    override fun onAddFavoriteClick(position: Int, postId: String?, ivFavourite: ImageView) {
        ivFavouritee = ivFavourite
        if (checkIfHasNetwork(this@HomeChildCareListActivity)) {

            launch(Dispatchers.Main.immediate) {
                authKey = dataStoragePreference.emitStoredValue(preferencesKey<String>("auth_key")).first()
                appViewModel.addFavouritePostApiData(security_key, authKey, postId, "2")
            }
            childProgressbar.showProgressBar()
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
                        childProgressbar?.hideProgressBar()
                        Log.d("homeChilcCare", "-------------" + Gson().toJson(response.body()))
                        val mResponse = response.body().toString()
                        dataString = response.body().toString()
                        val homeChildcareResponse = Gson().fromJson<HomeChiildCareREsponse>(
                            response.body().toString(), HomeChiildCareREsponse::class.java)
                        childCareDatalist.clear()
                        childCareDatalist.addAll(homeChildcareResponse.data)
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
                    ErrorBodyResponse(response, this, childProgressbar)
                }
            })
        appViewModel.observeFilterChildCareListResponse()!!.observe(this, androidx.lifecycle.Observer { response ->
                if (response!!.isSuccessful && response.code() == 200) {
                    if (response.body() != null) {
                        childProgressbar?.hideProgressBar()
                        Log.d("homeChilcCare", "-------------" + Gson().toJson(response.body()))
                        val mResponse = response.body().toString()
                        dataString = response.body().toString()
                        val homeChildcareResponse = Gson().fromJson(
                            response.body().toString(),
                            HomeChiildCareREsponse::class.java
                        )
                        childCareDatalist.clear()
                        childCareDatalist.addAll(homeChildcareResponse.data)

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
                    childProgressbar?.hideProgressBar()
                }
            })

        appViewModel.observeAddFavouritePostApiResponse()!!.observe(this, Observer { response ->
            if (response!!.isSuccessful && response.code() == 200) {
                Log.d("addFavouriteResBody", "----------" + Gson().toJson(response.body()))
                childProgressbar?.hideProgressBar()
                val mResponse = response.body().toString()
                val jsonObject = JSONObject(mResponse)

                val message = jsonObject.get("msg").toString()
                if (message == "You marked this Post as Your Favourite") {
                    myCustomToast(getString(R.string.marked_as_favourite))

                    ivFavouritee!!.setImageResource(R.drawable.heart)
                } else {
                    myCustomToast(getString(R.string.removed_from_favourite))
                    ivFavouritee!!.setImageResource(R.drawable.heart_purple)
                }
            } else {
                ErrorBodyResponse(response, this, childProgressbar)
                childProgressbar?.hideProgressBar()
            }
        })

        appViewModel.getException()!!.observe(this, Observer {
            myCustomToast(it)
            childProgressbar?.hideProgressBar()
        })
    }


}