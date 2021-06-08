package com.nelyanlive.ui


import android.content.Intent
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
import kotlinx.android.synthetic.main.activity_home_child_care_list.ivBack
import kotlinx.android.synthetic.main.activity_home_child_care_list.iv_map
import kotlinx.android.synthetic.main.activity_home_child_care_list.ll_public
import kotlinx.android.synthetic.main.activity_home_child_care_list.trader_type
import kotlinx.android.synthetic.main.activity_home_child_care_list.tvFilter
import kotlinx.android.synthetic.main.activity_home_child_care_list.tv_userCityOrZipcode
import kotlinx.android.synthetic.main.fragment_activity_list.*
import kotlinx.android.synthetic.main.fragment_child_care.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.json.JSONObject
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
    private var authkey: String? = null
    var dataString = ""

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
        val genderlist = arrayOf<String?>(
            "",
            "Date Added",
            "Avilable Place",
            "Distance"
        )
        val adapter: ArrayAdapter<*> = ArrayAdapter<Any?>(
            this, R.layout.customspinner, genderlist
        )

        // Setting Adapter to the Spinner
        trader_type!!.adapter = adapter
        // Setting OnItemClickListener to the Spinner
        trader_type!!.onItemSelectedListener = this@HomeChildCareListActivity

        launch(Dispatchers.Main.immediate) {
            authkey =
                dataStoragePreference.emitStoredValue(preferencesKey<String>("auth_key")).first()
            tv_userCityOrZipcode.text =
                dataStoragePreference.emitStoredValue(preferencesKey<String>("cityLogin")).first()

            if (checkIfHasNetwork(this@HomeChildCareListActivity)) {
                appViewModel.sendHomeActivitiesData(security_key, authkey, listType)
                child_care_list_progressbar?.showProgressBar()
                checkMvvmResponse()

            } else {
                showSnackBar(this@HomeChildCareListActivity, getString(R.string.no_internet_error))
            }
        }
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
                if (tvFilter.text == "Filter") {
                    val i = Intent(this, ChildCareFilterActivity::class.java)
                    startActivityForResult(i, LAUNCH_SECOND_ACTIVITY)
                } else {
                    tvFilter.text = "Filter"
                    launch(Dispatchers.Main.immediate) {
                        authkey =
                            dataStoragePreference.emitStoredValue(preferencesKey<String>("auth_key"))
                                .first()
                        tv_userCityOrZipcode.text =
                            dataStoragePreference.emitStoredValue(preferencesKey<String>("cityLogin")).first()

                        if (checkIfHasNetwork(this@HomeChildCareListActivity)) {
                            appViewModel.sendHomeActivitiesData(security_key, authkey, listType)
                            child_care_list_progressbar?.showProgressBar()
                            checkMvvmResponse()
                        } else {
                            showSnackBar(
                                this@HomeChildCareListActivity,
                                getString(R.string.no_internet_error)
                            )
                        }
                    }
                }
            }
            R.id.iv_map -> {
                if (dataString.isEmpty()) {
                    myCustomToast("Data not loaded yet")
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
                tvFilter.text = "Clear Filter"
                val returnName = data!!.getStringExtra("name")
                val returnLocation = data.getStringExtra("location")
                val returnDistance = data.getStringExtra("distance")
                val returnLat = data.getStringExtra("latitude")
                val returnlng = data.getStringExtra("longitude")
                val childCareType = data.getStringExtra("childCareType")

                if (checkIfHasNetwork(this)) {
                    launch(Dispatchers.Main.immediate) {
                        val authKey =
                            dataStoragePreference.emitStoredValue(preferencesKey<String>("auth_key"))
                                .first()
                        appViewModel.sendChildCareFilterData(
                            security_key,
                            authKey,
                            returnLat!!,
                            returnlng,
                            returnDistance!!,
                            returnName,
                            returnLocation!!,
                            childCareType!!
                        )
                        child_care_list_progressbar?.hideProgressBar()
                    }
                }
                else {
                    showSnackBar(this, getString(R.string.no_internet_error))
                } } } }

    override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {}

    override fun onNothingSelected(parent: AdapterView<*>?) {}


    override fun onItemClickListner(position: Int) {
        OpenActivity(NurserieActivityy::class.java)
    }

    override fun onAddFavoriteClick(position: Int, postId: String?, ivFavourite: ImageView) {
        ivFavouritee = ivFavourite
        if (checkIfHasNetwork(this@HomeChildCareListActivity)) {
            appViewModel.addFavouritePostApiData(security_key, authkey, postId, "2")
            child_care_list_progressbar.showProgressBar()
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
                        child_care_list_progressbar?.hideProgressBar()
                        Log.d("homeChilcCare", "-------------" + Gson().toJson(response.body()))
                        val mResponse = response.body().toString()
                        dataString = response.body().toString()
                        val jsonObject = JSONObject(mResponse)
                        val homeChildcareResponse = Gson().fromJson<HomeChiildCareREsponse>(
                            response.body().toString(), HomeChiildCareREsponse::class.java)
                        if (childCareDatalist != null) {
                            childCareDatalist.clear()
                            childCareDatalist.addAll(homeChildcareResponse.data)
                        }
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
                    ErrorBodyResponse(response, this, child_care_list_progressbar)
                }
            })
        appViewModel.observeFilterChildCareListResponse()!!.observe(this, androidx.lifecycle.Observer { response ->
                if (response!!.isSuccessful && response.code() == 200) {
                    if (response.body() != null) {
                        child_care_list_progressbar?.hideProgressBar()
                        Log.d("homeChilcCare", "-------------" + Gson().toJson(response.body()))
                        val mResponse = response.body().toString()
                        dataString = response.body().toString()
                        val jsonObject = JSONObject(mResponse)
                        val homeChildcareResponse = Gson().fromJson<HomeChiildCareREsponse>(
                            response.body().toString(),
                            HomeChiildCareREsponse::class.java
                        )
                        if (childCareDatalist != null) {
                            childCareDatalist.clear()
                            childCareDatalist.addAll(homeChildcareResponse.data)
                        }

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
                    child_care_list_progressbar?.hideProgressBar()
                }
            })

        appViewModel.observeAddFavouritePostApiResponse()!!.observe(this, Observer { response ->
            if (response!!.isSuccessful && response.code() == 200) {
                Log.d("addFavouriteResBody", "----------" + Gson().toJson(response.body()))
                child_care_list_progressbar?.hideProgressBar()
                val mResponse = response.body().toString()
                val jsonObject = JSONObject(mResponse)

                val message = jsonObject.get("msg").toString()
                if (message == "You marked this Post as Your Favourite") {
                    myCustomToast("You marked this post as your favourite")

                    ivFavouritee!!.setImageResource(R.drawable.heart)
                } else {
                    myCustomToast("You removed this post from your favourite")
                    ivFavouritee!!.setImageResource(R.drawable.heart_purple)
                }
            } else {
                ErrorBodyResponse(response, this, child_care_list_progressbar)
                child_care_list_progressbar?.hideProgressBar()
            }
        })

        appViewModel.getException()!!.observe(this, Observer {
            myCustomToast(it)
            child_care_list_progressbar?.hideProgressBar()
        })
    }


}