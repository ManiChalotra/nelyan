package com.nelyan_live.ui


import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.preferences.core.preferencesKey
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.meherr.mehar.data.viewmodel.AppViewModel
import com.meherr.mehar.db.DataStoragePreference
import com.nelyan_live.AppUtils
import com.nelyan_live.R
import com.nelyan_live.adapter.HomeChildCareListAdapter
import com.nelyan_live.fragments.ChildCareFragment
import com.nelyan_live.modals.homechildcare.HomeChildCareResponseData
import com.nelyan_live.modals.homechildcare.HomeChildcareResponse
import com.nelyan_live.utils.*
import kotlinx.android.synthetic.main.activity_home_child_care_list.ivBack
import kotlinx.android.synthetic.main.activity_home_child_care_list.iv_map
import kotlinx.android.synthetic.main.activity_home_child_care_list.ll_1
import kotlinx.android.synthetic.main.activity_home_child_care_list.orderby
import kotlinx.android.synthetic.main.activity_home_child_care_list.tvFilter
import kotlinx.android.synthetic.main.fragment_activity_list.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.json.JSONObject
import kotlin.coroutines.CoroutineContext

class HomeChildCareListActivity : AppCompatActivity(),View.OnClickListener, AdapterView.OnItemSelectedListener,
        HomeChildCareListAdapter.OnChatListItemClickListner, CoroutineScope {

    private  val job by lazy {
        Job()
    }
    private var listType: String? = null

    private var homeChildCareListAdapter: HomeChildCareListAdapter? = null
    private var recyclerview: RecyclerView? = null

    private val childCareDatalist by lazy { ArrayList<HomeChildCareResponseData>() }

    private val appViewModel by lazy { ViewModelProvider.AndroidViewModelFactory.getInstance(this.application).create(AppViewModel::class.java) }
    private  val dataStoragePreference by lazy { DataStoragePreference(this@HomeChildCareListActivity) }
    private var authkey: String?= null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_child_care_list)
        initalizeClicks()


        recyclerview = findViewById(R.id.recyclerview)


        if (intent.extras !=null){
            listType = intent.getStringExtra("type").toString()
            Log.e("qwe", intent.getStringExtra("type").toString())
        }




      // for setting order module listing
        val genderlist = arrayOf<String?>(
                "",
                "Date Added",
                "Avilable Place",
                "Distance")
        val adapter: ArrayAdapter<*> = ArrayAdapter<Any?>(
                this, R.layout.customspinner, genderlist)

            // Setting Adapter to the Spinner
        orderby!!.setAdapter(adapter)
            // Setting OnItemClickListener to the Spinner
        orderby!!.setOnItemSelectedListener(this@HomeChildCareListActivity)


    }

    private fun setChildcareAdapter(childCareDatalist: ArrayList<HomeChildCareResponseData>) {
        homeChildCareListAdapter = HomeChildCareListAdapter(this, childCareDatalist, this)
        recyclerview!!.setLayoutManager(LinearLayoutManager(this))
        recyclerview!!.setAdapter(homeChildCareListAdapter)
    }

    private  fun initalizeClicks(){
        ivBack.setOnClickListener(this)
        ll_1.setOnClickListener(this)
        tvFilter.setOnClickListener(this)
        iv_map.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.ivBack->{
                onBackPressed()
            }
            R.id.ll_1->{
                dailogLocation()
            }
            R.id.tvFilter->{
                AppUtils.gotoFragment(this, ChildCareFragment(), R.id.childcare_container , false)

            }
            R.id.iv_map->{
                OpenActivity(HomeChildCareOnMapActivity::class.java)
            }

        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {}

    override fun onNothingSelected(parent: AdapterView<*>?) {}

    fun dailogLocation() {
       val  dialog = Dialog(this)
        dialog!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog!!.setContentView(R.layout.alert_location)
        dialog!!.setCancelable(true)
        val rlYes: RelativeLayout
        val rlNo: RelativeLayout
        rlNo = dialog!!.findViewById(R.id.rlNo)
        rlNo.setOnClickListener { //  mContext.startActivity(new Intent(mContext, HomeActivity.class));
            dialog!!.dismiss()
        }
        rlYes = dialog!!.findViewById(R.id.rlYes)
        rlYes.setOnClickListener { dialog!!.dismiss() }
        dialog!!.show()
    }

    override fun onItemClickListner(position: Int) {
        OpenActivity(NurserieActivityy::class.java)
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onResume() {
        super.onResume()

        launch (Dispatchers.Main.immediate){
            authkey = dataStoragePreference?.emitStoredValue(preferencesKey<String>("auth_key"))?.first()

            if (checkIfHasNetwork(this@HomeChildCareListActivity)) {
                appViewModel.sendHomeActivitiesData(security_key,authkey, listType)
                activity_list_progressbar?.showProgressBar()
                checkMvvmResponse()

            } else {
                showSnackBar(this@HomeChildCareListActivity, getString(R.string.no_internet_error))
            }


        }
    }

    private  fun checkMvvmResponse(){
        appViewModel.observeHomeActivitiesResponse()!!.observe(this, androidx.lifecycle.Observer { response ->
            if (response!!.isSuccessful && response.code() == 200) {
                if (response.body() != null) {
                    activity_list_progressbar?.hideProgressBar()
                    Log.d("homeChilcCare", "-------------" + Gson().toJson(response.body()))
                    val mResponse = response.body().toString()
                    val jsonObject = JSONObject(mResponse)
                    val homeChildcareResponse =  Gson().fromJson<HomeChildcareResponse>(response.body().toString(), HomeChildcareResponse::class.java)
                    if (childCareDatalist != null) {
                        childCareDatalist!!.clear()
                        childCareDatalist.addAll(homeChildcareResponse.data)
                    }


                    if (childCareDatalist.size == 0) {
                        recyclerview!!.visibility = View.GONE
                        tv_no_activities!!.visibility = View.VISIBLE
                    } else {
                        recyclerview!!.visibility = View.VISIBLE
                        tv_no_activities!!.visibility = View.GONE
                        setChildcareAdapter(childCareDatalist)
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


}