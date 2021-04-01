package com.nelyan_live.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.meherr.mehar.data.viewmodel.AppViewModel
import com.nelyan_live.R
import com.nelyan_live.R.layout
import com.nelyan_live.adapter.MyEventAdapter
import com.nelyan_live.modals.HomeEventModel
import com.nelyan_live.modals.eventList.EventData
import com.nelyan_live.ui.Activity2Activity
import com.nelyan_live.ui.ActivityFragmentActivity
import com.nelyan_live.ui.CommunicationListner
import com.nelyan_live.utils.*
import kotlinx.android.synthetic.main.fragment_event.*
import kotlinx.android.synthetic.main.fragment_home.*
import org.json.JSONObject
import java.lang.RuntimeException
import kotlin.collections.ArrayList

class EventFragment : Fragment(), OnItemSelectedListener {

    private  var listner: CommunicationListner?= null

    private val appViewModel: AppViewModel by lazy {
        ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application).
        create(AppViewModel::class.java)
    }

    private lateinit  var mContext: Context
    private var ivLocation: ImageView? = null
    private var ivBack: ImageView? = null
    private var title: TextView? = null
    private var tvFilter: TextView? = null
    private lateinit  var v: View
    private var orderby: Spinner? = null
    private var rc: RecyclerView? = null

    private val eventData by lazy { ArrayList<EventData>() }

   // var eventDatalist = ArrayList<EventData>()
   // var eventDatalist = ArrayList<EventModel>()
   private val datalist by lazy { ArrayList<HomeEventModel>() }

    override fun onResume() {
        super.onResume()
        if(listner!= null){
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
            val fm = requireActivity()!!.supportFragmentManager
            val f = fm.findFragmentById(R.id.frame_container)
            fm.popBackStack()
        })

        ivLocation = v.findViewById(R.id.iv_bell)
        orderby = v.findViewById(R.id.orderby)
        rc = v.findViewById(R.id.rc_event)
        tvFilter = v.findViewById(R.id.tvFilter)
        tvFilter!!.setOnClickListener(View.OnClickListener {
            requireActivity().OpenActivity(ActivityFragmentActivity::class.java)
            //AppUtils.gotoFragment(mContext, ActivityFragment(), R.id.frame_container, false)
        })
        ivBack!!.setVisibility(View.VISIBLE)
        //            title.setVisibility(View.VISIBLE);
        ivLocation!!.setVisibility(View.VISIBLE)
        ivLocation!!.setImageResource(R.drawable.location_circle)
        val LM = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rc!!.setLayoutManager(LM)
        /*eventDatalist.clear()
        eventDatalist.add(EventModel(R.drawable.img_1, "Danse", "Marechal 20KM", "10.12.2020 To 11.12.2020", "11:00 AM 3 Year", "01:00 PM 5 Year", 450.80, "During the summer,many cities show movies in the park. Bring a blanket, snacks, a light sweater, and enjoy the magic of cinema"))
        eventDatalist.add(EventModel(R.drawable.img_1, "Danse", "Marechal 20KM", "10.12.2020 To 11.12.2020", "11:00 AM 3 Year", "01:00 PM 5 Year", 450.80, "During the summer,many cities show movies in the park. Bring a blanket, snacks, a light sweater, and enjoy the magic of cinema"))
        */


        ivLocation!!.setOnClickListener(View.OnClickListener {
            val intent = Intent(activity, Activity2Activity::class.java)
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
                requireActivity()!!, layout.customspinner, genderlist)

// Setting Adapter to the Spinner
        orderby!!.setAdapter(adapter)

// Setting OnItemClickListener to the Spinner
        orderby!!.setOnItemSelectedListener(this@EventFragment)
        return v
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {}
    override fun onNothingSelected(parent: AdapterView<*>?) {}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        eventListAPI()
        checkMvvmResponse()
    }

    private fun eventListAPI () {
        if (checkIfHasNetwork(requireActivity())) {
            val authkey = AllSharedPref.restoreString(requireContext(), "auth_key")
            appViewModel.sendEventListData(security_key, authkey, "30.7046","76.7179")
            eventProgressBar?.showProgressBar()
        } else {
            showSnackBar(requireActivity(), getString(R.string.no_internet_error))
        }
    }

    private fun setAdaptor(datalist: ArrayList<HomeEventModel>){
        val myEventAdapter = MyEventAdapter(requireActivity(), datalist)
        //rc.setAdapter(null);
        rc!!.setAdapter(myEventAdapter)
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

                    if (datalist != null) {
                        datalist!!.clear()
                    }

                    for (i in 0 until mSizeOfData) {
                        val name = jsonObject.getJSONArray("data").getJSONObject(i).get("name").toString()
                        val image = jsonObject.getJSONArray("data").getJSONObject(i).get("image").toString()
                        val description = jsonObject.getJSONArray("data").getJSONObject(i).get("description").toString()
                        val latitude = jsonObject.getJSONArray("data").getJSONObject(i).get("latitude").toString()
                        val longitude = jsonObject.getJSONArray("data").getJSONObject(i).get("longitude").toString()
                        val price = jsonObject.getJSONArray("data").getJSONObject(i).get("price").toString()
                        val city = jsonObject.getJSONArray("data").getJSONObject(i).get("city").toString()

                        datalist!!.add(HomeEventModel(image, name, city, "", "", "", price,
                                description))
                    }

                    if (mSizeOfData == 0){
                        rc!!.visibility = View.GONE
                        tv_no_event!!.visibility = View.VISIBLE
                    }else{
                        rc!!.visibility = View.VISIBLE
                        tv_no_event!!.visibility = View.GONE
                    setAdaptor(datalist)
                    }

                }
            } else {
                ErrorBodyResponse(response, requireActivity(), eventProgressBar)
            }
        })
        appViewModel.getException()!!.observe(requireActivity(), androidx.lifecycle.Observer {
            requireActivity().myCustomToast(it)
            eventProgressBar?.hideProgressBar()
        })

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is CommunicationListner){
            listner = context as CommunicationListner
        }else{

            throw RuntimeException("Home Fragment not Attched")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listner = null
    }

}