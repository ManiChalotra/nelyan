package com.nelyan_live.fragments

import android.app.Activity.RESULT_OK
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
import com.nelyan_live.R
import com.nelyan_live.R.layout
import com.nelyan_live.adapter.MyEventAdapter
import com.nelyan_live.data.viewmodel.AppViewModel
import com.nelyan_live.modals.HomeEventModel
import com.nelyan_live.modals.eventList.EventData
import com.nelyan_live.ui.ActivitiesFilterActivity
import com.nelyan_live.ui.ActivitiesOnMapActivity
import com.nelyan_live.ui.CommunicationListner
import com.nelyan_live.utils.*
import kotlinx.android.synthetic.main.fragment_event.*
import org.json.JSONObject


class EventFragment : Fragment(), OnItemSelectedListener, MyEventAdapter.OnEventItemClickListner {

    private var listner: CommunicationListner? = null

    private val appViewModel: AppViewModel by lazy {
        ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application).create(AppViewModel::class.java)
    }

    private val FILTER_ACTIVITY_REQUEST_CODE = 0

    private lateinit var mContext: Context
    private var ivLocation: ImageView? = null
    private var ivBack: ImageView? = null
    private var ivFavouritee: ImageView? = null
    private var title: TextView? = null
    private var tvFilter: TextView? = null
    private var tvNoEvent: TextView? = null
    private lateinit var v: View
    private var orderby: Spinner? = null
    private var rc: RecyclerView? = null
    private var authkey= ""
    private val eventData by lazy { ArrayList<EventData>() }

    // var eventDatalist = ArrayList<EventData>()
    // var eventDatalist = ArrayList<EventModel>()
    private val datalist by lazy { ArrayList<HomeEventModel>() }

    override fun onResume() {
        super.onResume()
        if (listner != null) {
            listner!!.onFargmentActive(5)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(layout.fragment_event, container, false)
        mContext = requireActivity()
        ivBack = v.findViewById(R.id.ivBack)
        ivBack!!.setOnClickListener(View.OnClickListener {
            val fm = requireActivity().supportFragmentManager
            val f = fm.findFragmentById(R.id.frame_container)
            fm.popBackStack()
        })

        ivLocation = v.findViewById(R.id.iv_bell)
        orderby = v.findViewById(R.id.trader_type)
        tvNoEvent = v.findViewById(R.id.tv_no_event)
        rc = v.findViewById(R.id.rc_event)
        tvFilter = v.findViewById(R.id.tvFilter)
        tvFilter!!.setOnClickListener(View.OnClickListener {

            val intent = Intent(requireContext(), ActivitiesFilterActivity::class.java)
            startActivityForResult(intent, FILTER_ACTIVITY_REQUEST_CODE)

            // requireActivity().OpenActivity(FilterActivity::class.java)
            //AppUtils.gotoFragment(mContext, ActivityFragment(), R.id.frame_container, false)
        })
        ivBack!!.visibility = View.VISIBLE
        //            title.setVisibility(View.VISIBLE);
        ivLocation!!.visibility = View.VISIBLE
        ivLocation!!.setImageResource(R.drawable.location_circle)
        val LM = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rc!!.layoutManager = LM
        /*eventDatalist.clear()
        eventDatalist.add(EventModel(R.drawable.img_1, "Danse", "Marechal 20KM", "10.12.2020 To 11.12.2020", "11:00 AM 3 Year", "01:00 PM 5 Year", 450.80, "During the summer,many cities show movies in the park. Bring a blanket, snacks, a light sweater, and enjoy the magic of cinema"))
        eventDatalist.add(EventModel(R.drawable.img_1, "Danse", "Marechal 20KM", "10.12.2020 To 11.12.2020", "11:00 AM 3 Year", "01:00 PM 5 Year", 450.80, "During the summer,many cities show movies in the park. Bring a blanket, snacks, a light sweater, and enjoy the magic of cinema"))
        */


        ivLocation!!.setOnClickListener(View.OnClickListener {
            val intent = Intent(activity, ActivitiesOnMapActivity::class.java)
            intent.putExtra("event", "activity")
            startActivity(intent)
            /*Intent i= new Intent(getActivity(), Activity2Activity.class);
                    startActivity(i);*/
        })
        val genderlist = arrayOf<String?>(
                "", "Events in City",
                "Date Added",
                "Distance")
        val adapter: ArrayAdapter<*> = ArrayAdapter<Any?>(
                requireActivity(), layout.customspinner, genderlist)

// Setting Adapter to the Spinner
        orderby!!.adapter = adapter

// Setting OnItemClickListener to the Spinner
        orderby!!.onItemSelectedListener = this@EventFragment
        return v
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {}
    override fun onNothingSelected(parent: AdapterView<*>?) {}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //hideKeyboard
        val inputManager = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(view.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)

        eventListAPI()
        checkMvvmResponse()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == FILTER_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) { // Activity.RESULT_OK

                // get String data from Intent
                val returnName = data!!.getStringExtra("name")
                val returnDistance = data.getStringExtra("distance")
                val returnLocation = data.getStringExtra("location")
                val returnDate = data.getStringExtra("date")
                val returnLat = data.getStringExtra("lat")
                val returnlng = data.getStringExtra("lng")

                if (checkIfHasNetwork(requireActivity())) {
                     authkey = AllSharedPref.restoreString(requireContext(), "auth_key")
                    //    appViewModel.sendFilterEventListData(security_key, authkey, "30.7046","30.7046")
                    appViewModel.sendFilterEventListData(security_key, authkey, returnLat, returnlng, returnDistance,
                            returnName, returnDate, returnLocation)
                    eventProgressBar?.showProgressBar()
                } else {
                    showSnackBar(requireActivity(), getString(R.string.no_internet_error))
                }

            }
        }
    }

    private fun eventListAPI() {
        if (checkIfHasNetwork(requireActivity())) {
             authkey = AllSharedPref.restoreString(requireContext(), "auth_key")
            appViewModel.sendEventListData(security_key, authkey, "30.7046", "30.7046")      //TODO set the current Lat-Lng of user
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
                                    description, isFav))
                        } else {
                            datalist.add(HomeEventModel(id, image, name, city, "", "", "", "",
                                    price, description, isFav))
                        }

                    }

                    if (mSizeOfData == 0) {
                        rc!!.visibility = View.GONE
                        tvNoEvent!!.visibility = View.VISIBLE
                    } else {
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

                if (message.equals("You marked this Event as Your Favourite")){
                    ivFavouritee!!.setImageResource(R.drawable.heart)
                }else {
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


                        if (datalist != null) {
                            datalist.clear()
                        }

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
                                        description, isFav))
                            } else {
                                datalist.add(HomeEventModel(id, image, name, city, "", "", "", "",
                                        price, description, isFav))
                            }

                        }

                        if (mSizeOfData == 0) {
                            rc!!.visibility = View.GONE
                            tv_no_event!!.visibility = View.VISIBLE
                        } else {
                            rc!!.visibility = View.VISIBLE
                            tv_no_event!!.visibility = View.GONE
                            setAdaptor(datalist)
                        }

                    }
                } else {
                    ErrorBodyResponse(response, requireActivity(), eventProgressBar)
                }
            }catch (e:Exception)
            {e.printStackTrace()}
        })


        appViewModel.getException()!!.observe(requireActivity(), androidx.lifecycle.Observer {
            requireActivity().myCustomToast(it)
            eventProgressBar?.hideProgressBar()
        })

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is CommunicationListner) {
            listner = context
        } else {

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
            eventProgressBar.showProgressBar()
        } else {
            showSnackBar(requireActivity(), getString(R.string.no_internet_error))
        }

    }

}