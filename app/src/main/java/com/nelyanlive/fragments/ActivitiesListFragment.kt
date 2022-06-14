package com.nelyanlive.fragments

import android.app.Dialog
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import androidx.datastore.preferences.core.preferencesKey
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.nelyanlive.R
import com.nelyanlive.adapter.ActivityListAdapter
import com.nelyanlive.data.viewmodel.AppViewModel
import com.nelyanlive.db.DataStoragePreference
import com.nelyanlive.modals.homeactivitylist.HomeAcitivityResponseData
import com.nelyanlive.modals.homeactivitylist.HomeActivityResponse
import com.nelyanlive.ui.ActivitiesFilterActivity
import com.nelyanlive.ui.ActivityDetailsActivity
import com.nelyanlive.utils.*
import kotlinx.android.synthetic.main.fragment_activity_list2.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.util.*
import kotlin.coroutines.CoroutineContext


class ActivitiesListFragment : Fragment(), AdapterView.OnItemSelectedListener,
    ActivityListAdapter.OnHomeActivitiesRecyclerViewItemClickListner, CoroutineScope {

     var orderby: Spinner? = null
    var dialog: Dialog? = null
//    var listType: String? = null
    var activityListAdapter: ActivityListAdapter? = null
    var recyclerview: RecyclerView? = null
    var ivFavouritee: ImageView? = null
    var LAUNCH_SECOND_ACTIVITY = 1
    var dataString = ""
    var Age = ""
    var jsondata = ArrayList<JsonObject>()
    private val activitisDatalist by lazy { ArrayList<HomeAcitivityResponseData>() }

    private val appViewModel by lazy {
        ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
            .create(AppViewModel::class.java)
    }
    private val dataStoragePreference by lazy { DataStoragePreference(requireActivity()) }
    private var authKey: String? = ""

    private val job by lazy {
        Job()
    }
    var listType="1"

    private var latitude: String = "42.6026"
    private var longitude: String = "20.9030"
    private var locality: String = ""
    var minage: String = ""
    var maxage: String = ""
    override fun onResume() {
        super.onResume()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_activity_list2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        orderby = view.findViewById(R.id.trader_type)
         recyclerview = view.findViewById(R.id.rv_home_activities)

//        if (intent.extras != null) {
//            listType = intent.getStringExtra("type").toString()
//            Log.e("qwe", intent.getStringExtra("type").toString())
//        }


        checkMvvmResponse()

        tvFilter.setOnClickListener {

            if (tvFilter.text == getString(R.string.filter) || tvFilter.text ==
                getString(R.string.clear_filter)
            ) {
                val i = Intent(requireActivity(), ActivitiesFilterActivity::class.java).putExtra(
                        "name",
                        getString(R.string.activity)
                    )
                startActivityForResult(i, LAUNCH_SECOND_ACTIVITY)

            } else {

                if (checkIfHasNetwork(requireActivity())) {
                    tvFilter.text = getString(R.string.filter)
                    launch(Dispatchers.Main.immediate) {
                        val authKey =
                            dataStoragePreference.emitStoredValue(preferencesKey<String>("auth_key"))
                                .first()
                        appViewModel.sendFilterActivityListData(
                            security_key, authKey,
                            latitude, longitude, "", "", "",
                            locality, Age
                        )
                      //  tv_userCityOrZipcode.text = locality
                        activity_list_progressbar?.showProgressBar()
                    }
                } else {
                    showSnackBar(requireActivity(), getString(R.string.no_internet_error))
                }
            }
        }



        val orderByList = arrayOf<String?>(
            "", "Events in City",
            "Date Added",
            "Distance"
        )
        val adapter: ArrayAdapter<*> = ArrayAdapter<Any?>(requireActivity(), R.layout.customspinner, orderByList)
        orderby!!.adapter = adapter
        orderby!!.onItemSelectedListener = this@ActivitiesListFragment

        if ((tvFilter.text == getString(R.string.filter))) {
            launch(Dispatchers.Main.immediate) {
                latitude =
                    dataStoragePreference.emitStoredValue(preferencesKey<String>("latitudeLogin"))
                        .first()
                longitude =
                    dataStoragePreference.emitStoredValue(preferencesKey<String>("longitudeLogin"))
                        .first()

                val geocoder = Geocoder(requireActivity(), Locale.getDefault())
                var list: List<Address>
                Log.e("location_changed", "==2=ifdsfdsfdsffff=$latitude==$longitude===$locality")

                if (latitude.toDouble() != 0.0) {
                    list = geocoder.getFromLocation(latitude.toDouble(), longitude.toDouble(), 1)

                    locality = list[0].locality
                    Log.e("location_changed", "==dasfasdf=$latitude==$longitude===${list[0]}")

                    Log.e("location_changed", "==2=ifffff=$latitude==$longitude===$locality")
                    if (latitude != "0.0") {
//                        tv_userCityOrZipcode.text = locality
                        if (checkIfHasNetwork(requireActivity())) {
                            launch(Dispatchers.Main.immediate) {
                                val authKey =
                                    dataStoragePreference.emitStoredValue(
                                        preferencesKey<String>(
                                            "auth_key"
                                        )
                                    )
                                        .first()
                                appViewModel.sendFilterActivityListData(
                                    security_key, authKey,
                                    latitude, longitude, "", "", "",
                                    locality, Age
                                )
                                activity_list_progressbar?.showProgressBar()
                            }
                        } else {
                            showSnackBar(
                                requireActivity(),
                                getString(R.string.no_internet_error)
                            )
                        }

                    }
                }
            }

        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == LAUNCH_SECOND_ACTIVITY) {
            if (resultCode == 1213) {

                tvFilter.text = getString(R.string.clear_filter)

                val returnName = data!!.getStringExtra("name")
                val returnLocation = data.getStringExtra("location")
                val returnDistance = data.getStringExtra("distance")
                val returnLat = data.getStringExtra("latitude")
                val returnlng = data.getStringExtra("longitude")
                val typeId = data.getStringExtra("typeId")
//                tv_userCityOrZipcode.text = data.getStringExtra("location")
                minage = data.getStringExtra("minage")!!
                maxage = data.getStringExtra("maxage")!!
                var TypeActivity = data.getStringExtra("SelectValueactivity")!!
                Age = data.getStringExtra("age")!!

                AllSharedPref.save(requireActivity(), "returnName", returnName!!)
                AllSharedPref.save(requireActivity(), "returnLocation", returnLocation!!)
                AllSharedPref.save(requireActivity(), "returnDistance", returnDistance!!)
                AllSharedPref.save(requireActivity(), "minage", minage)
                AllSharedPref.save(requireActivity(), "maxage", maxage)
                AllSharedPref.save(requireActivity(), "SelectValueactivity", TypeActivity)
                AllSharedPref.save(requireActivity(), "Age", Age)

                Log.e(
                    "ActivityListActivity ",
                    "returnValues_Activity_selectvalue   " + AllSharedPref.restoreString(
                        requireActivity(), "SelectValueactivity"
                    )
                )

                val geocoder = Geocoder(requireActivity(), Locale.getDefault())
                Log.e("returnLat0", "--" + returnLat + "=====" + returnlng)
                val list: List<Address> =
                    geocoder.getFromLocation(returnLat!!.toDouble(), returnlng!!.toDouble(), 1)

                val filteredAddress = list[0].locality
                Log.e(
                    "=======",
                    "===$returnName====$returnLocation====$returnDistance====$returnLat====$returnlng====$typeId==="
                )
                if (checkIfHasNetwork(requireActivity())) {
                    launch(Dispatchers.Main.immediate) {
                        val authKey =
                            dataStoragePreference.emitStoredValue(preferencesKey<String>("auth_key"))
                                .first()
                        appViewModel.sendFilterActivityListData(
                            security_key,
                            authKey,
                            returnLat,
                            returnlng,
                            returnDistance!!,
                            returnName,
                            typeId,
                            filteredAddress!!,
                            Age
                            )
                        activity_list_progressbar?.showProgressBar()
                    }
                } else {
                    showSnackBar(requireActivity(), getString(R.string.no_internet_error))
                }
            } else if (resultCode == 1){
                (tvFilter.text == getString(R.string.filter))
                    launch(Dispatchers.Main.immediate) {
                       // returnLocation = data?.getStringExtra("location").toString()
                       // latitude = data?.getStringExtra("latitude").toString()
                     //   longitude = data?.getStringExtra("longitude").toString()

                        Age = ""

                        val geocoder = Geocoder(requireActivity(), Locale.getDefault())
                        var list: List<Address>
                        Log.e("location_changed", "==2=ifdsfdsfdsffff=$latitude==$longitude===$locality")

                        if (latitude.toDouble() != 0.0) {
                            list = geocoder.getFromLocation(latitude.toDouble(), longitude.toDouble(), 1)

                            locality = list[0].locality
                            Log.e("location_changed", "==dasfasdf=$latitude==$longitude===${list[0]}")

                            Log.e("location_changed", "==2=ifffff=$latitude==$longitude===$locality")
                            if (latitude != "0.0") {
//                                tv_userCityOrZipcode.text = locality
                                if (checkIfHasNetwork(requireActivity())) {
                                    launch(Dispatchers.Main.immediate) {
                                        val authKey =
                                            dataStoragePreference.emitStoredValue(
                                                preferencesKey<String>(
                                                    "auth_key"
                                                )
                                            )
                                                .first()
                                        appViewModel.sendFilterActivityListData(
                                            security_key, authKey,
                                            latitude, longitude, "", "", "",
                                            locality, Age
                                        )
                                        activity_list_progressbar?.showProgressBar()
                                    }
                                } else {
                                    showSnackBar(
                                        requireActivity(),
                                        getString(R.string.no_internet_error)
                                    )
                                }

                            }
                        }
                    }

            } else {
                checkMvvmResponse()
            }
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {}

    override fun onNothingSelected(parent: AdapterView<*>?) {}

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job


    private fun setAdaptor(activitisDatalist: ArrayList<HomeAcitivityResponseData>) {
        activityListAdapter = ActivityListAdapter(requireActivity(), activitisDatalist, this)
        recyclerview!!.layoutManager = LinearLayoutManager(requireActivity())
        recyclerview!!.adapter = activityListAdapter
    }

    private fun checkMvvmResponse() {
        appViewModel.observeHomeActivitiesResponse()!!
            .observe(requireActivity(), androidx.lifecycle.Observer { response ->
                if (response!!.isSuccessful && response.code() == 200) {
                    if (response.body() != null) {
                        activity_list_progressbar?.hideProgressBar()
                        Log.d("myadsResponse", "-------------" + Gson().toJson(response.body()))
                        dataString = response.body().toString()
//                        jsondata = response.body()
                        Log.d(
                            ActivitiesListFragment::class.java.name,
                            "ActivitiesList_dataString    " + dataString
                        )
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
                    ErrorBodyResponse(response, requireActivity(), activity_list_progressbar)
                    activity_list_progressbar?.hideProgressBar()
                }
            })

        appViewModel.observeFilterActivityListResponse()!!
            .observe(requireActivity(), androidx.lifecycle.Observer { response ->
                if (response!!.isSuccessful && response.code() == 200) {
                    if (response.body() != null) {
                        activity_list_progressbar?.hideProgressBar()
                        Log.d("myadsResponse", "-------------" + Gson().toJson(response.body()))
                        dataString = response.body().toString()
                        val homeAcitivitiesResponse = Gson().fromJson(
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
                    ErrorBodyResponse(response, requireActivity(), null)
                    activity_list_progressbar?.hideProgressBar()
                }
            })

        appViewModel.observeAddFavouritePostApiResponse()!!.observe(requireActivity(), Observer { response ->
            if (response!!.isSuccessful && response.code() == 200) {
                Log.d("addFavouriteResBody", "----------" + Gson().toJson(response.body()))
                activity_list_progressbar?.hideProgressBar()
                val mResponse = response.body().toString()
                val jsonObject = JSONObject(mResponse)
                val message = jsonObject.get("msg").toString()
                if (message == "You marked this post as your favourite.") {
                    requireActivity().myCustomToast(getString(R.string.marked_as_favourite))
                    ivFavouritee!!.setImageResource(R.drawable.heart)
                } else {
                    requireActivity().myCustomToast(getString(R.string.removed_from_favourite))
                    ivFavouritee!!.setImageResource(R.drawable.heart_purple)
                }
            } else {
                ErrorBodyResponse(response, requireActivity(), activity_list_progressbar)
                activity_list_progressbar?.hideProgressBar()
            }
        })

        appViewModel.getException()!!.observe(requireActivity(), Observer {
            requireActivity().myCustomToast(it)
            activity_list_progressbar?.hideProgressBar()
        })
    }

    override fun onAddFavoriteClick(eventID: String, ivFavourite: ImageView) {
        ivFavouritee = ivFavourite

        if (checkIfHasNetwork(requireActivity())) {
            launch(Dispatchers.Main.immediate) {
                authKey = dataStoragePreference.emitStoredValue(preferencesKey<String>("auth_key"))
                    .first()
                appViewModel.addFavouritePostApiData(security_key, authKey, eventID, "1")
            }
            activity_list_progressbar.showProgressBar()
        } else {
            showSnackBar(requireActivity(), getString(R.string.no_internet_error))
        }
    }

    override fun onHomeActivitiesItemClickListner(
        activityId: String,
        categoryId: String,
        postLatitude: String,
        postLongitude: String
    ) {
        requireActivity().OpenActivity(ActivityDetailsActivity::class.java) {
            putString("activityId", activityId)
            putString("categoryId", categoryId)
            putString("lati", postLatitude)
            putString("longi", postLongitude)
        }
    }


}


