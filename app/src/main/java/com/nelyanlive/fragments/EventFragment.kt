package com.nelyanlive.fragments

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.nelyanlive.R
import com.nelyanlive.R.layout
import com.nelyanlive.adapter.MyEventAdapter
import com.nelyanlive.current_location.GPSTracker
import com.nelyanlive.data.viewmodel.AppViewModel
import com.nelyanlive.modals.HomeEventModel
import com.nelyanlive.ui.*
import com.nelyanlive.utils.*
import kotlinx.android.synthetic.main.fragment_activity_list.*
import kotlinx.android.synthetic.main.fragment_activity_list.frame_container
import kotlinx.android.synthetic.main.fragment_event.*
import kotlinx.android.synthetic.main.fragment_trader_listing.*
import kotlinx.android.synthetic.main.tookbar.*
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList

private const val PERMISSIONS_REQUEST_CODE = 34


class EventFragment(var userlat: String, var userlong: String,var userlocation: String) : Fragment(), OnItemSelectedListener, MyEventAdapter.OnEventItemClickListner {

    private var listner: CommunicationListner? = null

    private val appViewModel: AppViewModel by lazy {
        ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application).create(AppViewModel::class.java)
    }

    private val FILTER_ACTIVITY_REQUEST_CODE = 1122

    private lateinit var mContext: Context
    private var ivFavouritee: ImageView? = null
    private var tvFilter: TextView? = null
    private var tvNoEvent: TextView? = null
    private lateinit var v: View
    private var orderby: Spinner? = null
    private var rc: RecyclerView? = null
    private var authkey= ""
    var dataString = ""
    private val datalist by lazy { ArrayList<HomeEventModel>() }

    private var gpsTracker: GPSTracker? = null
    private var latitude: Double = 42.6026
    private var longitude: Double = 20.9030
    private var locality: String = ""



    override fun onResume() {
        super.onResume()
        if (listner != null) {
            listner!!.onFargmentActive(5)
        }
                gpsTracker = GPSTracker(mContext)
                if (tvFilter!!.text == "Filter") {
                    if (!gpsTracker!!.canGetLocation()) {
                        Log.e("location_changed", "==1=iff==" + gpsTracker!!.canGetLocation())
                        gpsTracker!!.showSettingsAlert()

                    } else {
                        Log.e("location_changed", "==2=iff==" + gpsTracker!!.canGetLocation())
                        val gpsTracker = GPSTracker(mContext)
                        latitude = gpsTracker.latitude
                        longitude = gpsTracker.longitude
                        Log.e("location_changed", "==2=ifffff=$latitude==$longitude=")
                        if (latitude != 0.0) {
                            val geocoder = Geocoder(mContext, Locale.getDefault())
                            var list = listOf<Address>()

                            list = geocoder.getFromLocation(latitude, longitude, 1)

                            Log.e("location_changed", "==home==Foreground address: ${list[0].locality}==")
                           // tv_city_zipcode.text = list[0].locality
                            (mContext as HomeActivity).tvTitleToolbar!!.text = getString(R.string.upcoming_events) + "\n" + list[0].locality

                            locality = list[0].locality
        if (checkIfHasNetwork(requireActivity())) {
            authkey = AllSharedPref.restoreString(requireContext(), "auth_key")
            appViewModel.sendFilterEventListData(security_key, authkey,
                latitude.toString(), longitude.toString(),"",
                "",  locality)
            eventProgressBar?.showProgressBar()
        } else {
            showSnackBar(requireActivity(), getString(R.string.no_internet_error))
        }

                        }
                    }
                }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        v = inflater.inflate(layout.fragment_event, container, false)
        mContext = requireActivity()
        if (foregroundPermissionApproved()) {
            val subscribeToLocationUpdates = HomeActivity.locationService?.subscribeToLocationUpdates()

            Log.e("location_changed", "=========$subscribeToLocationUpdates======")

            if (subscribeToLocationUpdates != null) subscribeToLocationUpdates else Log.e("location_changed", "Service Not Bound")
        }
        else {
            requestPermissions()
        }
        orderby = v.findViewById(R.id.trader_type)
        tvNoEvent = v.findViewById(R.id.tv_no_event)
        rc = v.findViewById(R.id.rc_event)
        tvFilter = v.findViewById(R.id.tvFilter)
        tvFilter!!.setOnClickListener {
            if(tvFilter!!.text=="Filter") {
                val intent =
                    Intent(requireContext(), ActivitiesFilterActivity::class.java).putExtra(
                        "name",
                        "Event"
                    )
                startActivityForResult(intent, FILTER_ACTIVITY_REQUEST_CODE)
            }
            else
            {
                tvFilter!!.text="Filter"
                if (checkIfHasNetwork(requireActivity())) {
                    authkey = AllSharedPref.restoreString(requireContext(), "auth_key")
                    appViewModel.sendFilterEventListData(security_key, authkey, userlat, userlong,"",
                        "",  userlocation)
                    eventProgressBar?.showProgressBar()
                } else {
                    showSnackBar(requireActivity(), getString(R.string.no_internet_error))
                }
            }
        }

        val LM = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rc!!.layoutManager = LM
        (mContext as HomeActivity).iv_bell!!.visibility = View.VISIBLE
        (mContext as HomeActivity).iv_bell!!.setOnClickListener {
            if(dataString.isEmpty())
            {
                requireActivity().myCustomToast("Data is empty.")
            }
            else {
            val intent = Intent(activity, EventsOnMapActivity::class.java)
            intent.putExtra("dataString", dataString)
            startActivity(intent)
        }
        }
        val genderlist = arrayOf<String?>(
                "", "Events in City",
                "Date Added",
                "Distance")
        val adapter: ArrayAdapter<*> = ArrayAdapter<Any?>(
                requireActivity(), layout.customspinner, genderlist)

        orderby!!.adapter = adapter
        orderby!!.onItemSelectedListener = this@EventFragment
        return v
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
                        (mContext as Activity),
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        PERMISSIONS_REQUEST_CODE
                    )
                }
                .show()
        } else {
            Log.d("TAG", "Request foreground only permission")
            ActivityCompat.requestPermissions(
                (mContext as Activity),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSIONS_REQUEST_CODE
            )
        }
    }

    private fun foregroundPermissionApproved(): Boolean {
        return PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(
            (mContext as Activity),
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }


    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {}
    override fun onNothingSelected(parent: AdapterView<*>?) {}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val inputManager = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(view.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        //eventListAPI()
        checkMvvmResponse()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == FILTER_ACTIVITY_REQUEST_CODE) {
            if (resultCode == 1212) {
                tvFilter!!.text="Clear Filter"

                val returnName = data!!.getStringExtra("name")
                val returnLocation = data.getStringExtra("location")
                val returnDistance = data.getStringExtra("distance")
                val returnLat = data.getStringExtra("latitude")
                val returnlng = data.getStringExtra("longitude")
                (mContext as HomeActivity).tvTitleToolbar!!.text = getString(R.string.upcoming_events) + "\n" + returnLocation

                if (checkIfHasNetwork(requireActivity())) {
                     authkey = AllSharedPref.restoreString(requireContext(), "auth_key")
                     appViewModel.sendFilterEventListData(security_key, authkey, returnLat, returnlng,returnDistance,
                            returnName,  returnLocation)
                    eventProgressBar?.showProgressBar()
                }
                else {
                    showSnackBar(requireActivity(), getString(R.string.no_internet_error))
                }
            } }
    }

    private fun eventListAPI() {
        if (checkIfHasNetwork(requireActivity())) {
             authkey = AllSharedPref.restoreString(requireContext(), "auth_key")
            appViewModel.sendFilterEventListData(security_key, authkey, userlat, userlong,"",
                "",  userlocation)
            eventProgressBar?.showProgressBar()
        } else {
            showSnackBar(requireActivity(), getString(R.string.no_internet_error))
        }

    }

    private fun setAdaptor(datalist: ArrayList<HomeEventModel>) {
        try {
            val myEventAdapter = MyEventAdapter(requireActivity(), datalist, this)
            rc!!.adapter = myEventAdapter
        }
        catch (e:Exception)
        {e.printStackTrace()}
    }

    private fun checkMvvmResponse() {
        appViewModel.observeEventListResponse()!!.observe(requireActivity(), androidx.lifecycle.Observer { response ->
            if (response!!.isSuccessful && response.code() == 200) {
                if (response.body() != null) {
                    eventProgressBar?.hideProgressBar()
                    Log.d("homeEventResponse", "-------------" + Gson().toJson(response.body()))
                    val mResponse = response.body().toString()
                    dataString = response.body().toString()
                    val jsonObject = JSONObject(mResponse)
                    val listArray = jsonObject.getJSONArray("data")
                    val mSizeOfData = listArray.length()

                    datalist.clear()

                    for (i in 0 until mSizeOfData) {
                        val name = jsonObject.getJSONArray("data").getJSONObject(i).get("name").toString()
                        val id = jsonObject.getJSONArray("data").getJSONObject(i).get("id").toString()
                        val image = jsonObject.getJSONArray("data").getJSONObject(i).get("image").toString()
                        val description = jsonObject.getJSONArray("data").getJSONObject(i).get("description").toString()
                        val isFav = jsonObject.getJSONArray("data").getJSONObject(i).get("isFav").toString()
                        val latitude = jsonObject.getJSONArray("data").getJSONObject(i).get("latitude").toString()
                        val longitude = jsonObject.getJSONArray("data").getJSONObject(i).get("longitude").toString()
                        val price = jsonObject.getJSONArray("data").getJSONObject(i).get("price").toString()
                        val city = jsonObject.getJSONArray("data").getJSONObject(i).get("city").toString()
                        val activityId = jsonObject.getJSONArray("data").getJSONObject(i).get("activityId").toString()

                        if (jsonObject.getJSONArray("data").getJSONObject(i).getJSONArray("eventstimings").length() != 0) {
                            val eventStartdate = jsonObject.getJSONArray("data").getJSONObject(i).getJSONArray("eventstimings")
                                    .getJSONObject(0).get("dateFrom").toString()
                            val eventEndDate = jsonObject.getJSONArray("data").getJSONObject(i).getJSONArray("eventstimings")
                                    .getJSONObject(0).get("dateTo").toString()
                            val eventStartTime = jsonObject.getJSONArray("data").getJSONObject(i).getJSONArray("eventstimings")
                                    .getJSONObject(0).get("startTime").toString()
                            val eventEndTime = jsonObject.getJSONArray("data").getJSONObject(i).getJSONArray("eventstimings")
                                    .getJSONObject(0).get("endTime").toString()
                            datalist.add(HomeEventModel(id, image, name, city, eventStartdate, eventEndDate, eventStartTime, eventEndTime, price,
                                    description, isFav,activityId,latitude,longitude))
                        }
                        else {
                            datalist.add(HomeEventModel(id, image, name, city, "", "", "", "",
                                    price, description, isFav,activityId, latitude, longitude))
                        }
                    }

                    if (mSizeOfData == 0) {
                        rc!!.visibility = View.GONE
                        tvNoEvent!!.visibility = View.VISIBLE
                    }
                    else {
                        rc!!.visibility = View.VISIBLE
                        tvNoEvent!!.visibility = View.GONE
                        setAdaptor(datalist)
                    }

                }
            } else {
                ErrorBodyResponse(response, requireActivity(), eventProgressBar)
            }
        })

        appViewModel.observeAddFavouriteApiResponse()!!.observe(requireActivity(), androidx.lifecycle.Observer { response ->
            if (response!!.isSuccessful && response.code() == 200) {
                Log.d("addFavouriteResBody", "----------" + Gson().toJson(response.body()))
                eventProgressBar?.hideProgressBar()
                val mResponse = response.body().toString()
                val jsonObject = JSONObject(mResponse)

                val message = jsonObject.get("msg").toString()

                if (message == "You marked this Event as Your Favourite"){
                    ivFavouritee!!.setImageResource(R.drawable.heart)
                }
                else {
                    ivFavouritee!!.setImageResource(R.drawable.heart_purple)
                }

            } else {
                ErrorBodyResponse(response, requireActivity(), eventProgressBar)
                eventProgressBar?.hideProgressBar()
            }
        })


        appViewModel.observeFilterEventListResponse()!!.observe(requireActivity(), androidx.lifecycle.Observer { response ->
            try {
                if (response!!.isSuccessful && response.code() == 200) {
                    if (response.body() != null) {
                        eventProgressBar?.hideProgressBar()
                        Log.d("homeFilterEventResponse", "-------------" + Gson().toJson(response.body()))
                        val mResponse = response.body().toString()
                        val jsonObject = JSONObject(mResponse)
                        val listArray = jsonObject.getJSONArray("data")
                        val mSizeOfData = listArray.length()

                        if (datalist != null) { datalist.clear() }

                        for (i in 0 until mSizeOfData) {
                            val name = jsonObject.getJSONArray("data").getJSONObject(i).get("name").toString()
                            val id = jsonObject.getJSONArray("data").getJSONObject(i).get("id").toString()
                            val image = jsonObject.getJSONArray("data").getJSONObject(i).get("image").toString()
                            val description = jsonObject.getJSONArray("data").getJSONObject(i).get("description").toString()
                            val isFav = jsonObject.getJSONArray("data").getJSONObject(i).get("isFav").toString()
                            val latitude = jsonObject.getJSONArray("data").getJSONObject(i).get("latitude").toString()
                            val longitude = jsonObject.getJSONArray("data").getJSONObject(i).get("longitude").toString()
                            val price = jsonObject.getJSONArray("data").getJSONObject(i).get("price").toString()
                            val city = jsonObject.getJSONArray("data").getJSONObject(i).get("city").toString()
                            val activityId = jsonObject.getJSONArray("data").getJSONObject(i).get("activityId").toString()

                            if (jsonObject.getJSONArray("data").getJSONObject(i).getJSONArray("eventstimings").length() != 0) {
                                val eventStartdate = jsonObject.getJSONArray("data").getJSONObject(i).getJSONArray("eventstimings")
                                        .getJSONObject(0).get("dateFrom").toString()
                                val eventEndDate = jsonObject.getJSONArray("data").getJSONObject(i).getJSONArray("eventstimings")
                                        .getJSONObject(0).get("dateTo").toString()
                                val eventStartTime = jsonObject.getJSONArray("data").getJSONObject(i).getJSONArray("eventstimings")
                                        .getJSONObject(0).get("startTime").toString()
                                val eventEndTime = jsonObject.getJSONArray("data").getJSONObject(i).getJSONArray("eventstimings")
                                        .getJSONObject(0).get("endTime").toString()
                                datalist.add(HomeEventModel(id, image, name, city, eventStartdate, eventEndDate, eventStartTime, eventEndTime, price,
                                        description, isFav, activityId, latitude, longitude))
                            }
                            else {
                                datalist.add(HomeEventModel(id, image, name, city, "", "", "", "",
                                        price, description, isFav, activityId, latitude, longitude))
                            }
                        }

                        if (mSizeOfData == 0) {
                            rc!!.visibility = View.GONE
                            tv_no_event!!.visibility = View.VISIBLE
                        }
                        else {
                            rc!!.visibility = View.VISIBLE
                            tv_no_event!!.visibility = View.GONE
                            setAdaptor(datalist)
                        }
                    }
                } else {
                    ErrorBodyResponse(response, requireActivity(), eventProgressBar)
                }
            }catch (e:Exception)
            {
                eventProgressBar?.hideProgressBar()
                e.printStackTrace()}
        })

        appViewModel.getException()!!.observe(requireActivity(), androidx.lifecycle.Observer {
            try {
                requireActivity().myCustomToast(it)
                eventProgressBar?.hideProgressBar()
            }
            catch (e:Exception)
            {e.printStackTrace()}

        })
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is CommunicationListner) {
            listner = context
        }
        else {
            throw RuntimeException("Home Fragment not Attched")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listner = null
    }

    override fun onAddFavoriteClick(eventID: String, ivFavourite: ImageView) {
        ivFavouritee = ivFavourite

        if (checkIfHasNetwork(requireActivity())) {
            appViewModel.addFavouriteApiData(security_key, authkey, eventID )
        }
        else {
            showSnackBar(requireActivity(), getString(R.string.no_internet_error))
        }

    }

}