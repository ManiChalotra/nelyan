package com.nelyanlive.ui


import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.RelativeLayout
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
import kotlin.coroutines.CoroutineContext

class HomeChildCareListActivity : AppCompatActivity(),View.OnClickListener, AdapterView.OnItemSelectedListener,
        HomeChildCareListAdapter.OnChatListItemClickListner, CoroutineScope {

    private  val job by lazy {
        Job()
    }
    private var listType: String? = null
    var LAUNCH_SECOND_ACTIVITY = 1

    private var homeChildCareListAdapter: HomeChildCareListAdapter? = null
    private var recyclerview: RecyclerView? = null
    private var ivFavouritee: ImageView? = null

    private val childCareDatalist by lazy { ArrayList<HomeChildCareeData>() }

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
        trader_type!!.adapter = adapter
            // Setting OnItemClickListener to the Spinner
        trader_type!!.onItemSelectedListener = this@HomeChildCareListActivity

        launch (Dispatchers.Main.immediate){
            authkey = dataStoragePreference.emitStoredValue(preferencesKey<String>("auth_key")).first()
            tv_userCityOrZipcode.text =
                dataStoragePreference.emitStoredValue(preferencesKey<String>("cityLogin")).first()

            if (checkIfHasNetwork(this@HomeChildCareListActivity)) {
                appViewModel.sendHomeActivitiesData(security_key,authkey, listType)
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

    private  fun initalizeClicks(){
        ivBack.setOnClickListener(this)
        ll_public.setOnClickListener(this)
        tvFilter.setOnClickListener(this)
        iv_map.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.ivBack->{
                onBackPressed()
            }
            R.id.ll_public->{
                dailogLocation()
            }
            R.id.tvFilter->{
                val i = Intent(this, ChildCareFilterActivity::class.java)
                startActivityForResult(i, LAUNCH_SECOND_ACTIVITY)

            }
            R.id.iv_map->{
                OpenActivity(HomeChildCareOnMapActivity::class.java)
            }

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode === LAUNCH_SECOND_ACTIVITY) {
            if (resultCode === Activity.RESULT_OK) {
                val childCareeDatalist: ArrayList<HomeChildCareeData> = data!!.getSerializableExtra("filteredChildCareDatalist") as ArrayList<HomeChildCareeData>

                if (childCareeDatalist.size == 0) {
                    recyclerview!!.visibility = View.GONE
                    tv_no_childcare!!.visibility = View.VISIBLE
                } else {
                    recyclerview!!.visibility = View.VISIBLE
                    tv_no_childcare!!.visibility = View.GONE
                    setChildcareAdapter(childCareeDatalist)
                }
            }

        }
    }


    override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {}

    override fun onNothingSelected(parent: AdapterView<*>?) {}

    fun dailogLocation() {
       val  dialog = Dialog(this)
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.setContentView(R.layout.alert_location)
        dialog.setCancelable(true)
        val rlNo: RelativeLayout = dialog.findViewById(R.id.rlNo)
        rlNo.setOnClickListener {
            dialog.dismiss()
        }
        val rlYes: RelativeLayout = dialog.findViewById(R.id.rlYes)
        rlYes.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    override fun onItemClickListner(position: Int) {
        OpenActivity(NurserieActivityy::class.java)
    }

    override fun onAddFavoriteClick(position: Int, postId: String?, ivFavourite: ImageView) {
        ivFavouritee=ivFavourite
        if (checkIfHasNetwork(this@HomeChildCareListActivity)) {
            appViewModel.addFavouritePostApiData(security_key, authkey, postId, "2")
            child_care_list_progressbar.showProgressBar()
        } else {
            showSnackBar(this@HomeChildCareListActivity, getString(R.string.no_internet_error))
        }

    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private  fun checkMvvmResponse(){
        appViewModel.observeHomeActivitiesResponse()!!.observe(this, androidx.lifecycle.Observer { response ->
            if (response!!.isSuccessful && response.code() == 200) {
                if (response.body() != null) {
                    child_care_list_progressbar?.hideProgressBar()
                    Log.d("homeChilcCare", "-------------" + Gson().toJson(response.body()))
                    val mResponse = response.body().toString()
                    val jsonObject = JSONObject(mResponse)
                    val homeChildcareResponse =  Gson().fromJson<HomeChiildCareREsponse>(response.body().toString(), HomeChiildCareREsponse::class.java)
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

        appViewModel.observeAddFavouritePostApiResponse()!!.observe(this, Observer { response ->
            if (response!!.isSuccessful && response.code() == 200) {
                Log.d("addFavouriteResBody", "----------" + Gson().toJson(response.body()))
                child_care_list_progressbar?.hideProgressBar()
                val mResponse = response.body().toString()
                val jsonObject = JSONObject(mResponse)

                val message = jsonObject.get("msg").toString()
              //  myCustomToast(message)
                if (message == "You marked this Post as Your Favourite"){
                    myCustomToast("You marked this post as your favourite")

                    ivFavouritee!!.setImageResource(R.drawable.heart)
                }else {
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