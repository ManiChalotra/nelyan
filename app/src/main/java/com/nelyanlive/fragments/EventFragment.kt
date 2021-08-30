package com.nelyanlive.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.nelyanlive.R
import com.nelyanlive.R.layout
import com.nelyanlive.adapter.MyEventAdapter
import com.nelyanlive.data.viewmodel.AppViewModel
import com.nelyanlive.modals.HomeEventModel
import com.nelyanlive.ui.*
import com.nelyanlive.utils.*
import kotlinx.android.synthetic.main.fragment_event.*
import kotlinx.android.synthetic.main.tookbar.*
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList


class EventFragment(var userLat: String, var userLong: String, var userLocation: String) :
    Fragment(), OnItemSelectedListener, MyEventAdapter.OnEventItemClickListner {

    private var listner: CommunicationListner? = null
    private val appViewModel: AppViewModel by lazy {
        ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
            .create(AppViewModel::class.java)
    }
    private val FILTER_ACTIVITY_REQUEST_CODE = 1122

    private lateinit var mContext: Context
    private var ivFavouritee: ImageView? = null
    private var tvFilter: TextView? = null
    private var tvNoEvent: TextView? = null
    private lateinit var v: View
    private var orderBy: Spinner? = null
    private var rc: RecyclerView? = null
    private var authKey = ""
    var dataString = ""
    private val dataList by lazy { ArrayList<HomeEventModel>() }
    private var latitude: String = "42.6026"
    private var longitude: String = "20.9030"
    private var locality: String = ""


    override fun onResume() {
        super.onResume()
        if (listner != null) {
            listner!!.onFargmentActive(5)
        }
        if (tvFilter!!.text == getString(R.string.filter)) {

            latitude = userLat
            longitude = userLong
            locality = userLocation
            Log.e("location_changed", "==2=ifffff=$latitude==$longitude=")
            if (latitude != "0.0") {

                (mContext as HomeActivity).tvTitleToolbar!!.text =
                    getString(R.string.upcoming_events) + "\n" + locality

                if (checkIfHasNetwork(requireActivity())) {
                    authKey = AllSharedPref.restoreString(requireContext(), "auth_key")
                    appViewModel.sendFilterEventListData(
                        security_key, authKey,
                        latitude, longitude, "",
                        "", locality
                    )
                    eventProgressBar?.showProgressBar()
                } else {
                    showSnackBar(requireActivity(), getString(R.string.no_internet_error))
                }


            }
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        v = inflater.inflate(layout.fragment_event, container, false)
        mContext = requireActivity()
        orderBy = v.findViewById(R.id.trader_type)
        tvNoEvent = v.findViewById(R.id.tv_no_event)
        rc = v.findViewById(R.id.rc_event)
        tvFilter = v.findViewById(R.id.tvFilter)
        tvFilter!!.setOnClickListener {
            if (tvFilter!!.text == getString(R.string.filter)) {
                val intent =
                    Intent(requireContext(), ActivitiesFilterActivity::class.java).putExtra(
                        "name",
                       it.context.getString(R.string.event)
                    )
                startActivityForResult(intent, FILTER_ACTIVITY_REQUEST_CODE)
            } else {
                tvFilter!!.text = getString(R.string.filter)
                (mContext as HomeActivity).tvTitleToolbar!!.text =
                    getString(R.string.upcoming_events) + "\n" + locality

                if (checkIfHasNetwork(requireActivity())) {
                    authKey = AllSharedPref.restoreString(requireContext(), "auth_key")
                    appViewModel.sendFilterEventListData(
                        security_key, authKey, userLat, userLong, "",
                        "", userLocation
                    )
                    eventProgressBar?.showProgressBar()
                } else {
                    showSnackBar(requireActivity(), getString(R.string.no_internet_error))
                }
            }
        }

        val lm = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rc!!.layoutManager = lm
        (mContext as HomeActivity).iv_bell!!.visibility = View.VISIBLE
        (mContext as HomeActivity).iv_bell!!.setOnClickListener {
            if (dataString.isEmpty()) {
                requireActivity().myCustomToast("Data is empty.")
            } else {
                val intent = Intent(activity, EventsOnMapActivity::class.java)
                intent.putExtra("dataString", dataString)
                startActivity(intent)
            }
        }
        val genderList = arrayOf<String?>(
            "", "Events in City",
            "Date Added",
            "Distance"
        )
        val adapter: ArrayAdapter<*> = ArrayAdapter<Any?>(
            requireActivity(), layout.customspinner, genderList
        )

        orderBy!!.adapter = adapter
        orderBy!!.onItemSelectedListener = this@EventFragment
        return v
    }


    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {}
    override fun onNothingSelected(parent: AdapterView<*>?) {}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val inputManager =
            view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(view.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        checkMvvmResponse()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == FILTER_ACTIVITY_REQUEST_CODE) {
            if (resultCode == 1212) {
                tvFilter!!.text = getString(R.string.clear_filter)

                val returnName = data!!.getStringExtra("name")
                val returnLocation = data.getStringExtra("location")
                val returnDistance = data.getStringExtra("distance")
                val returnLat = data.getStringExtra("latitude")
                val returnLng = data.getStringExtra("longitude")
                (mContext as HomeActivity).tvTitleToolbar!!.text =
                    getString(R.string.upcoming_events) + "\n" + returnLocation

                if (checkIfHasNetwork(requireActivity())) {
                    authKey = AllSharedPref.restoreString(requireContext(), "auth_key")
                    appViewModel.sendFilterEventListData(
                        security_key, authKey, returnLat, returnLng, returnDistance,
                        returnName, returnLocation
                    )
                    eventProgressBar?.showProgressBar()
                } else {
                    showSnackBar(requireActivity(), getString(R.string.no_internet_error))
                }
            }
        }
    }

    private fun setAdaptor(dataList: ArrayList<HomeEventModel>) {
        try {
            val myEventAdapter = MyEventAdapter(requireActivity(), dataList, this)
            rc!!.adapter = myEventAdapter
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun checkMvvmResponse() {
        appViewModel.observeEventListResponse()!!
            .observe(requireActivity(), androidx.lifecycle.Observer { response ->
                if (response!!.isSuccessful && response.code() == 200) {
                    if (response.body() != null) {
                        eventProgressBar?.hideProgressBar()
                        Log.d("homeEventResponse", "-------------" + Gson().toJson(response.body()))
                        val mResponse = response.body().toString()
                        dataString = response.body().toString()
                        val jsonObject = JSONObject(mResponse)
                        val listArray = jsonObject.getJSONArray("data")
                        val mSizeOfData = listArray.length()

                        dataList.clear()

                        for (i in 0 until mSizeOfData) {
                            val name = jsonObject.getJSONArray("data").getJSONObject(i).get("name")
                                .toString()
                            val id = jsonObject.getJSONArray("data").getJSONObject(i).get("id")
                                .toString()
                            val image =
                                jsonObject.getJSONArray("data").getJSONObject(i).get("image")
                                    .toString()
                            val description =
                                jsonObject.getJSONArray("data").getJSONObject(i).get("description")
                                    .toString()
                            val isFav =
                                jsonObject.getJSONArray("data").getJSONObject(i).get("isFav")
                                    .toString()
                            val latitude =
                                jsonObject.getJSONArray("data").getJSONObject(i).get("latitude")
                                    .toString()
                            val longitude =
                                jsonObject.getJSONArray("data").getJSONObject(i).get("longitude")
                                    .toString()
                            val price =
                                jsonObject.getJSONArray("data").getJSONObject(i).get("price")
                                    .toString()
                            val city = jsonObject.getJSONArray("data").getJSONObject(i).get("city")
                                .toString()
                            val activityId =
                                jsonObject.getJSONArray("data").getJSONObject(i).get("activityId")
                                    .toString()

                            if (jsonObject.getJSONArray("data").getJSONObject(i)
                                    .getJSONArray("eventstimings").length() != 0
                            ) {
                                val eventStartdate =
                                    jsonObject.getJSONArray("data").getJSONObject(i)
                                        .getJSONArray("eventstimings")
                                        .getJSONObject(0).get("dateFrom").toString()
                                val eventEndDate = jsonObject.getJSONArray("data").getJSONObject(i)
                                    .getJSONArray("eventstimings")
                                    .getJSONObject(0).get("dateTo").toString()
                                val eventStartTime =
                                    jsonObject.getJSONArray("data").getJSONObject(i)
                                        .getJSONArray("eventstimings")
                                        .getJSONObject(0).get("startTime").toString()
                                val eventEndTime = jsonObject.getJSONArray("data").getJSONObject(i)
                                    .getJSONArray("eventstimings")
                                    .getJSONObject(0).get("endTime").toString()
                                dataList.add(
                                    HomeEventModel(
                                        id,
                                        image,
                                        name,
                                        city,
                                        eventStartdate,
                                        eventEndDate,
                                        eventStartTime,
                                        eventEndTime,
                                        price,
                                        description,
                                        isFav,
                                        activityId,
                                        latitude,
                                        longitude
                                    )
                                )
                            }
                            else {
                                dataList.add(
                                    HomeEventModel(
                                        id, image, name, city, "", "", "", "",
                                        price, description, isFav, activityId, latitude, longitude
                                    )
                                )
                            }
                        }

                        if (mSizeOfData == 0) {
                            rc!!.visibility = View.GONE
                            tvNoEvent!!.visibility = View.VISIBLE
                        } else {
                            rc!!.visibility = View.VISIBLE
                            tvNoEvent!!.visibility = View.GONE
                            setAdaptor(dataList)
                        }

                    }
                } else {
                    ErrorBodyResponse(response, requireActivity(), eventProgressBar)
                }
            })

        appViewModel.observeAddFavouriteApiResponse()!!
            .observe(requireActivity(), androidx.lifecycle.Observer { response ->
                if (response!!.isSuccessful && response.code() == 200) {
                    Log.d("addFavouriteResBody", "----------" + Gson().toJson(response.body()))
                    eventProgressBar?.hideProgressBar()
                    val mResponse = response.body().toString()
                    val jsonObject = JSONObject(mResponse)

                    val message = jsonObject.get("msg").toString()

                    if (message == "You marked this Event as Your Favourite") {
                        ivFavouritee!!.setImageResource(R.drawable.heart)
                    } else {
                        ivFavouritee!!.setImageResource(R.drawable.heart_purple)
                    }

                } else {
                    ErrorBodyResponse(response, requireActivity(), eventProgressBar)
                    eventProgressBar?.hideProgressBar()
                }
            })


        appViewModel.observeFilterEventListResponse()!!
            .observe(requireActivity(), androidx.lifecycle.Observer { response ->
                try {
                    if (response!!.isSuccessful && response.code() == 200) {
                        if (response.body() != null) {
                            eventProgressBar?.hideProgressBar()
                            Log.d(
                                "homeFilterEventResponse",
                                "-------------" + Gson().toJson(response.body())
                            )
                            val mResponse = response.body().toString()
                            val jsonObject = JSONObject(mResponse)
                            val listArray = jsonObject.getJSONArray("data")
                            val mSizeOfData = listArray.length()

                            dataList.clear()

                            for (i in 0 until mSizeOfData) {
                                val name =
                                    jsonObject.getJSONArray("data").getJSONObject(i).get("name")
                                        .toString()
                                val id = jsonObject.getJSONArray("data").getJSONObject(i).get("id")
                                    .toString()
                                val image =
                                    jsonObject.getJSONArray("data").getJSONObject(i).get("image")
                                        .toString()
                                val description = jsonObject.getJSONArray("data").getJSONObject(i)
                                    .get("description").toString()
                                val isFav =
                                    jsonObject.getJSONArray("data").getJSONObject(i).get("isFav")
                                        .toString()
                                val latitude =
                                    jsonObject.getJSONArray("data").getJSONObject(i).get("latitude")
                                        .toString()
                                val longitude = jsonObject.getJSONArray("data").getJSONObject(i)
                                    .get("longitude").toString()
                                val price =
                                    jsonObject.getJSONArray("data").getJSONObject(i).get("price")
                                        .toString()
                                val city =
                                    jsonObject.getJSONArray("data").getJSONObject(i).get("city")
                                        .toString()
                                val activityId = jsonObject.getJSONArray("data").getJSONObject(i)
                                    .get("activityId").toString()

                                if (jsonObject.getJSONArray("data").getJSONObject(i)
                                        .getJSONArray("eventstimings").length() != 0
                                ) {
                                    val eventStartdate =
                                        jsonObject.getJSONArray("data").getJSONObject(i)
                                            .getJSONArray("eventstimings")
                                            .getJSONObject(0).get("dateFrom").toString()
                                    val eventEndDate =
                                        jsonObject.getJSONArray("data").getJSONObject(i)
                                            .getJSONArray("eventstimings")
                                            .getJSONObject(0).get("dateTo").toString()
                                    val eventStartTime =
                                        jsonObject.getJSONArray("data").getJSONObject(i)
                                            .getJSONArray("eventstimings")
                                            .getJSONObject(0).get("startTime").toString()
                                    val eventEndTime =
                                        jsonObject.getJSONArray("data").getJSONObject(i)
                                            .getJSONArray("eventstimings")
                                            .getJSONObject(0).get("endTime").toString()
                                    dataList.add(
                                        HomeEventModel(
                                            id,
                                            image,
                                            name,
                                            city,
                                            eventStartdate,
                                            eventEndDate,
                                            eventStartTime,
                                            eventEndTime,
                                            price,
                                            description,
                                            isFav,
                                            activityId,
                                            latitude,
                                            longitude
                                        )
                                    )
                                } else {
                                    dataList.add(
                                        HomeEventModel(
                                            id,
                                            image,
                                            name,
                                            city,
                                            "",
                                            "",
                                            "",
                                            "",
                                            price,
                                            description,
                                            isFav,
                                            activityId,
                                            latitude,
                                            longitude
                                        )
                                    )
                                }
                            }

                            if (mSizeOfData == 0) {
                                rc!!.visibility = View.GONE
                                tv_no_event!!.visibility = View.VISIBLE
                            } else {
                                rc!!.visibility = View.VISIBLE
                                tv_no_event!!.visibility = View.GONE
                                setAdaptor(dataList)
                            }
                        }
                    } else {
                        ErrorBodyResponse(response, requireActivity(), eventProgressBar)
                    }
                } catch (e: Exception) {
                    eventProgressBar?.hideProgressBar()
                    e.printStackTrace()
                }
            })

        appViewModel.getException()!!.observe(requireActivity(), androidx.lifecycle.Observer {
            try {
                requireActivity().myCustomToast(it)
                eventProgressBar?.hideProgressBar()
            } catch (e: Exception) {
                e.printStackTrace()
            }

        })
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is CommunicationListner) {
            listner = context
        } else {
            throw RuntimeException("Home Fragment not Attached")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listner = null
    }

    override fun onAddFavoriteClick(eventID: String, ivFavourite: ImageView) {
        ivFavouritee = ivFavourite

        if (checkIfHasNetwork(requireActivity())) {
            appViewModel.addFavouriteApiData(security_key, authKey, eventID)
        } else {
            showSnackBar(requireActivity(), getString(R.string.no_internet_error))
        }

    }

}