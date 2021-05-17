package com.nelyan_live.ui

import android.app.Activity
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
import com.nelyan_live.data.viewmodel.AppViewModel
import com.nelyan_live.R
import com.nelyan_live.adapter.ActivityListAdapter
import com.nelyan_live.db.DataStoragePreference
import com.nelyan_live.modals.homeactivitylist.HomeAcitivityResponseData
import com.nelyan_live.modals.homeactivitylist.HomeActivityResponse
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
    var ivFavouritee: ImageView? = null
    var LAUNCH_SECOND_ACTIVITY = 1

    private val activitisDatalist by lazy { ArrayList<HomeAcitivityResponseData>() }

    private val appViewModel by lazy { ViewModelProvider.AndroidViewModelFactory.getInstance(this.application).create(AppViewModel::class.java) }
    private val dataStoragePreference by lazy { DataStoragePreference(this@ActivitiesListActivity) }
    private var authkey: String? = null
    private var userCityOrZipcode: String? = null

    private val job by lazy {
        Job()
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
        ivBack!!.setOnClickListener(View.OnClickListener {
            onBackPressed()
        })

        launch(Dispatchers.Main.immediate) {
            authkey = dataStoragePreference?.emitStoredValue(preferencesKey<String>("auth_key"))?.first()
            tv_userCityOrZipcode.text = dataStoragePreference?.emitStoredValue(preferencesKey<String>("cityLogin"))?.first()
            appViewModel.sendHomeActivitiesData(security_key, authkey, "1")
            activity_list_progressbar?.showProgressBar()

        }
        checkMvvmResponse()

        //   tv_userCityOrZipcode.text = userCityOrZipcode

        tvFilter.setOnClickListener(View.OnClickListener {

           // OpenActivity(FilterActivity::class.java)

            val i = Intent(this, ActivitiesFilterActivity::class.java)
            startActivityForResult(i, LAUNCH_SECOND_ACTIVITY)
            })


        iv_map!!.setOnClickListener(View.OnClickListener { // navigationbar.setVisibility(View.GONE);
            val i = Intent(this, ActivitiesOnMapActivity::class.java)
            /*TODO pass the response of activity list*/
            i.putExtra("type", listType)
            startActivity(i)
        })

        val orderbylist = arrayOf<String?>(
                "", "Events in City",
                "Date Added",
                "Distance")
        val adapter: ArrayAdapter<*> = ArrayAdapter<Any?>(
                this, R.layout.customspinner, orderbylist)

// Setting Adapter to the Spinner
        orderby!!.setAdapter(adapter)

// Setting OnItemClickListener to the Spinner
        orderby!!.setOnItemSelectedListener(this@ActivitiesListActivity)

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode === LAUNCH_SECOND_ACTIVITY) {
            if (resultCode === Activity.RESULT_OK) {
                val activitisDatalistss: ArrayList<HomeAcitivityResponseData> = data!!.getSerializableExtra("filteredActivitisDatalist") as ArrayList<HomeAcitivityResponseData>

                if (activitisDatalistss.size == 0) {
                    recyclerview!!.visibility = View.GONE
                    tv_no_activities!!.visibility = View.VISIBLE
                } else {
                    recyclerview!!.visibility = View.VISIBLE
                    tv_no_activities!!.visibility = View.GONE
                    setAdaptor(activitisDatalistss)
                }

            }
            if (resultCode === Activity.RESULT_CANCELED) {
                // Write your code if there's no result
            }
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {}

    override fun onNothingSelected(parent: AdapterView<*>?) {}

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job


    private fun setAdaptor(activitisDatalist: ArrayList<HomeAcitivityResponseData>) {
        activityListAdapter = ActivityListAdapter(this, activitisDatalist, this)
        recyclerview!!.setLayoutManager(LinearLayoutManager(this))
        recyclerview!!.setAdapter(activityListAdapter)
    }

    private fun checkMvvmResponse() {
        appViewModel.observeHomeActivitiesResponse()!!.observe(this, androidx.lifecycle.Observer { response ->
            if (response!!.isSuccessful && response.code() == 200) {
                if (response.body() != null) {
                    activity_list_progressbar?.hideProgressBar()
                    Log.d("myadsResponse", "-------------" + Gson().toJson(response.body()))
                    val mResponse = response.body().toString()
                    val jsonObject = JSONObject(mResponse)
                    val homeAcitivitiesResponse = Gson().fromJson<HomeActivityResponse>(response.body().toString(), HomeActivityResponse::class.java)


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
                myCustomToast(message)
                if (message.equals("You marked this Post as Your Favourite")){
                    ivFavouritee!!.setImageResource(R.drawable.heart)
                }else {
                    ivFavouritee!!.setImageResource(R.drawable.heart_purple)
                }


            } else {
                ErrorBodyResponse(response, this, activity_list_progressbar)
                activity_list_progressbar?.hideProgressBar()
            }
        })

        appViewModel.getException()!!.observe(this, Observer {
            myCustomToast(it)
            activity_list_progressbar?.hideProgressBar()
        })
    }

    override fun onAddFavoriteClick(postId: String, ivFavourite: ImageView) {
        ivFavouritee = ivFavourite

        if (checkIfHasNetwork(this@ActivitiesListActivity)) {
            appViewModel.addFavouritePostApiData(security_key, authkey, postId, "1")
            activity_list_progressbar.showProgressBar()//  loginProgressBar?.showProgressBar()
        } else {
            showSnackBar(this@ActivitiesListActivity, getString(R.string.no_internet_error))
        }

    }

    override fun onHomeActivitiesItemClickListner(activityId: String, categoryId: String, latti: String, longi: String) {
        OpenActivity(ActivityDetailsActivity::class.java) {
            putString("activityId", activityId)
            putString("categoryId", categoryId)
            putString("lati", latti)
            putString("longi", longi)
        }
    }

}


