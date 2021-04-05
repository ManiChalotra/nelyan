package com.nelyan_live.ui

import android.app.Dialog
import android.content.Intent
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
import com.meherr.mehar.data.viewmodel.AppViewModel
import com.meherr.mehar.db.DataStoragePreference
import com.nelyan_live.R
import com.nelyan_live.adapter.ActivityListAdapter
import com.nelyan_live.modals.homeactivitylist.HomeAcitivityResponseData
import com.nelyan_live.modals.homeactivitylist.HomeActivityRespone
import com.nelyan_live.utils.*
import kotlinx.android.synthetic.main.fragment_activity_list.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.json.JSONObject
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

    private val activitisDatalist by lazy { ArrayList<HomeAcitivityResponseData>() }

    private val appViewModel by lazy { ViewModelProvider.AndroidViewModelFactory.getInstance(this.application).create(AppViewModel::class.java) }
    private val dataStoragePreference by lazy { DataStoragePreference(this@ActivitiesListActivity) }
    private var authkey: String? = null

    private val job by lazy {
        Job()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_activity_list)
        ivBack = findViewById(R.id.ivBack)
        orderby = findViewById(R.id.orderby)
        iv_map = findViewById(R.id.iv_map)
        recyclerview = findViewById(R.id.rv_home_activities)

        if (intent.extras != null) {
            listType = intent.getStringExtra("type").toString()
            Log.e("qwe", intent.getStringExtra("type").toString())
        }
        ivBack!!.setOnClickListener(View.OnClickListener {
            onBackPressed()
        })


        tvFilter.setOnClickListener(View.OnClickListener {

            OpenActivity(ActivityFragmentActivity::class.java)

            /* val frag: Fragment  = ActivityFragment()
             supportFragmentManager?.beginTransaction().replace(R.id.frame_container, frag)?.addToBackStack(null)?.commit()
            // AppUtils.gotoFragment(mContext, ActivityFragment(), R.id.frame_container, false)*/
        })


        iv_map!!.setOnClickListener(View.OnClickListener { // navigationbar.setVisibility(View.GONE);
            //  AppUtils.gotoFragment(mContext, new MapActivityFragment(), R.id.fullframe_container, false);
            val i = Intent(this, Activity2Activity::class.java)
            startActivity(i)
        })

        val genderlist = arrayOf<String?>(
                "", "Events in City",
                "Date Added",
                "Distance")
        val adapter: ArrayAdapter<*> = ArrayAdapter<Any?>(
                this, R.layout.customspinner, genderlist)

// Setting Adapter to the Spinner
        orderby!!.setAdapter(adapter)

// Setting OnItemClickListener to the Spinner
        orderby!!.setOnItemSelectedListener(this@ActivitiesListActivity)

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {}

    override fun onNothingSelected(parent: AdapterView<*>?) {}

    override fun onHomeActivitiesItemClickListner() {
        OpenActivity(ActivityDetailsActivity::class.java)
        //AppUtils.gotoFragment(this, ActivityDetailsFragment(), R.id.frame_container, false)
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onResume() {
        super.onResume()

        launch(Dispatchers.Main.immediate) {
            authkey = dataStoragePreference?.emitStoredValue(preferencesKey<String>("auth_key"))?.first()
            appViewModel.sendHomeActivitiesData(security_key, authkey, "1")
            activity_list_progressbar?.showProgressBar()

            checkMvvmResponse()
        }
    }

    private fun setAdaptor(activitisDatalist: ArrayList<HomeAcitivityResponseData>) {
        activityListAdapter = ActivityListAdapter(this, activitisDatalist, this)
        recyclerview!!.setLayoutManager(LinearLayoutManager(this))
        recyclerview!!.setAdapter(activityListAdapter)
    }

    private fun checkMvvmResponse() {
        appViewModel.observeAddFavouriteApiResponse()!!.observe(this, Observer { response ->
            if (response!!.isSuccessful && response.code() == 200) {
                Log.d("addFavouriteResBody", "----------" + Gson().toJson(response.body()))


            } else {
                ErrorBodyResponse(response, this, activity_list_progressbar)
            }
        })

        appViewModel.observeHomeActivitiesResponse()!!.observe(this, androidx.lifecycle.Observer { response ->
            if (response!!.isSuccessful && response.code() == 200) {
                if (response.body() != null) {
                    activity_list_progressbar?.hideProgressBar()
                    Log.d("myadsResponse", "-------------" + Gson().toJson(response.body()))
                    val mResponse = response.body().toString()
                    val jsonObject = JSONObject(mResponse)
                    val homeAcitivitiesResponse = Gson().fromJson<HomeActivityRespone>(response.body().toString(), HomeActivityRespone::class.java)
                    if (activitisDatalist != null) {
                        activitisDatalist!!.clear()
                        activitisDatalist.addAll(homeAcitivitiesResponse.data)
                    }


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
            }
        })


        appViewModel.getException()!!.observe(this, Observer {
            myCustomToast(it)
            activity_list_progressbar?.hideProgressBar()
        })
    }

    override fun onAddFavoriteClick() {

    }


}