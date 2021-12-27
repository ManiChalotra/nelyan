package com.nelyanlive.ui


import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.datastore.preferences.core.preferencesKey
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.nelyanlive.R
import com.nelyanlive.adapter.MyAddAdapter
import com.nelyanlive.adapter.MyAdsChildCareAdapter
import com.nelyanlive.adapter.MyAdsTraderAdapter
import com.nelyanlive.data.viewmodel.AppViewModel
import com.nelyanlive.db.DataStoragePreference
import com.nelyanlive.modals.myAd.*
import com.nelyanlive.utils.*
import kotlinx.android.synthetic.main.activity_myadd.*
import kotlinx.android.synthetic.main.alert_add_post_restiction.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.json.JSONObject
import kotlin.coroutines.CoroutineContext

class MyAddActivity : AppCompatActivity(), CoroutineScope, View.OnClickListener,
    MyAdsChildCareAdapter.OnDeleteEditClickListner,
    MyAddAdapter.OnActivitiesDeleteEditClickListner,
    MyAdsTraderAdapter.OnDeleteEditTradersAdClickListner {

    var ivAdd: ImageView? = null
    var ivBack: ImageView? = null

    private var myAddAdapter: MyAddAdapter? = null
    private var myAdsChildCareAdapter: MyAdsChildCareAdapter? = null
    private var myAdsTraderAdapter: MyAdsTraderAdapter? = null
    var recyclerview: RecyclerView? = null

    var job = Job()
    private var authorization = ""
    var categoryId = ""
    var activitiesPos = 0
    var tradersPos = 0
    private var childCarePos = 0
    private val dataList by lazy { ArrayList<GetActivitypostMyAds>() }
    private val childCareList by lazy { ArrayList<GetChildcarePostMyAds>() }
    private val traderList by lazy { ArrayList<GetTraderMyAds>() }
    private var userType = ""
    var consultantUserDialog: Dialog? = null
    private var selectedTypeAds = "1"

    val dataStoragePreference by lazy {
        DataStoragePreference(this)
    }

    val appViewModel by lazy {
        ViewModelProvider.AndroidViewModelFactory.getInstance(this.application)
            .create(AppViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_myadd)
        ivBack = findViewById(R.id.ivBack)
        ivAdd = findViewById(R.id.ivAdd)
        recyclerview = findViewById(R.id.recyclerview)

        ivAdd!!.setOnClickListener(this)
        tv_activitiess!!.setOnClickListener(this)
        tv_child_care!!.setOnClickListener(this)
        tv_trader!!.setOnClickListener(this)
        ivBack!!.setOnClickListener(this)

        launch(Dispatchers.Main.immediate) {
            userType =
                dataStoragePreference.emitStoredValue(preferencesKey<String>("typeLogin")).first()
        }

        Log.d(MyAddActivity::class.java.name, "MyAdsActivity_onCreate   ")
        checkMvvmResponse()
        hitMyAdsListApi()
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job


    private fun hitMyAdsListApi() {
        if (checkIfHasNetwork(this)) {
            authorization = AllSharedPref.restoreString(this, "auth_key")
            appViewModel.sendMyAdsListData(security_key, authorization,selectedTypeAds)
            myads_progressBar?.showProgressBar()
        } else {
            showSnackBar(this, getString(R.string.no_internet_error))
        }
    }

    private fun hitDeleteMyAdsApi(adID: String?, categoryId: String) {
        if (checkIfHasNetwork(this)) {
            authorization = AllSharedPref.restoreString(this, "auth_key")
            appViewModel.sendDeleteAdData(security_key, authorization, categoryId, adID)
        } else {
            showSnackBar(this, getString(R.string.no_internet_error))
        }
    }

    fun setAdaptor(activityPostMyAdsList: ArrayList<GetActivitypostMyAds>) {
        myAddAdapter = MyAddAdapter(this, activityPostMyAdsList, this)
        recyclerview!!.layoutManager = LinearLayoutManager(this)
        recyclerview!!.adapter = myAddAdapter
    }

    fun setChildCareAdaptor(activityPostMyAdsList: ArrayList<GetChildcarePostMyAds>) {
        myAdsChildCareAdapter = MyAdsChildCareAdapter(this, activityPostMyAdsList, this)
        recyclerview!!.layoutManager = LinearLayoutManager(this)
        recyclerview!!.adapter = myAdsChildCareAdapter
    }

    fun setTraderAdaptor(getTraderMyAdsList: ArrayList<GetTraderMyAds>) {
        myAdsTraderAdapter = MyAdsTraderAdapter(this, getTraderMyAdsList, this)
        recyclerview!!.layoutManager = LinearLayoutManager(this)
        recyclerview!!.adapter = myAdsTraderAdapter
    }

    private fun checkMvvmResponse() {
        appViewModel.observerMyAdsListResponse()!!
            .observe(this, androidx.lifecycle.Observer { response ->
                if (response!!.isSuccessful && response.code() == 200) {
                    if (response.body() != null) {
                        myads_progressBar?.hideProgressBar()
                        Log.d("myAdsResponse", "-------------" + Gson().toJson(response.body()))
                        val myAdsResponse =
                            Gson().fromJson(response.body().toString(), MyAdsResponsee::class.java)

                        recyclerview!!.visibility = View.GONE
                        tv_no_ad!!.visibility = View.VISIBLE

                        when (selectedTypeAds) {
                            "2" -> {
                                childCareList.clear()
                                childCareList.addAll(myAdsResponse.data.getChildcarePosts)
                                if (childCareList.size != 0) {
                                    recyclerview!!.visibility = View.VISIBLE
                                    tv_no_ad!!.visibility = View.GONE
                                    setChildCareAdaptor(childCareList)
                                }
                            }
                            "3" -> {
                                traderList.clear()
                                traderList.addAll(myAdsResponse.data.GetTrader)
                                if (traderList.size != 0) {
                                    recyclerview!!.visibility = View.VISIBLE
                                    tv_no_ad!!.visibility = View.GONE
                                    setTraderAdaptor(traderList)
                                }
                            }
                            "1" -> {
                                dataList.clear()
                                dataList.addAll(myAdsResponse.data.getActivitypost)
                                if (dataList.size != 0) {
                                    recyclerview!!.visibility = View.VISIBLE
                                    tv_no_ad!!.visibility = View.GONE
                                    setAdaptor(dataList)
                                }
                            }
                        }
                    }
                } else {
                    ErrorBodyResponse(response, this, myads_progressBar)
                }
            })

        appViewModel.observeDeleteAdResponse()!!.observe(this, Observer { response ->
            if (response!!.isSuccessful && response.code() == 200) {
                Log.d("deleteAdResBody", "----------" + Gson().toJson(response.body()))
                val mResponse = response.body().toString()
                val jsonObject = JSONObject(mResponse)
                val message = jsonObject.get("msg").toString()
                myCustomToast(message)
                when (categoryId) {
                    "2" -> {
//                        childCareList.removeAt(childCarePos)
//                        myAdsChildCareAdapter!!.notifyDataSetChanged()
//                        Log.e("deleteListSize", childCareList.size.toString())

                        if (childCareList.size == 0) {
                            tv_no_ad.visibility = View.VISIBLE
                            recyclerview!!.visibility = View.GONE
                        } else {
                            recyclerview!!.visibility = View.VISIBLE
                            tv_no_ad.visibility = View.GONE

                        }

                    }
                    "1" -> {

                        if (dataList.size == 0) {
                            tv_no_ad.visibility = View.VISIBLE
                            recyclerview!!.visibility = View.GONE
                        } else {
                            recyclerview!!.visibility = View.VISIBLE
                            tv_no_ad.visibility = View.GONE
                        }
                    }
                    "3" -> {
//                        traderList.removeAt(tradersPos)
//                        myAdsTraderAdapter!!.notifyDataSetChanged()
                        Log.e("deleteListSize", traderList.size.toString())

                        if (traderList.size == 0) {
                            tv_no_ad.visibility = View.VISIBLE
                            recyclerview!!.visibility = View.GONE
                        } else {
                            recyclerview!!.visibility = View.VISIBLE
                            tv_no_ad.visibility = View.GONE
                        }
                    }
                }
            } else {
                ErrorBodyResponse(response, this, myads_progressBar)
                myads_progressBar?.hideProgressBar()
            }
        })

        appViewModel.getException()!!.observe(this, androidx.lifecycle.Observer {
            this.myCustomToast(it)
            myads_progressBar?.hideProgressBar()
        })

    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.tv_activitiess -> {
                selectedTypeAds = "1"
                tv_activitiess.setTextColor(ContextCompat.getColor(this, R.color.app))
                tv_child_care.setTextColor(ContextCompat.getColor(this, R.color.black))
                tv_trader.setTextColor(ContextCompat.getColor(this, R.color.black))
                hitMyAdsListApi()
            }
            R.id.tv_child_care -> {
                selectedTypeAds = "2"
                tv_activitiess.setTextColor(ContextCompat.getColor(this, R.color.black))
                tv_child_care.setTextColor(ContextCompat.getColor(this, R.color.app))
                tv_trader.setTextColor(ContextCompat.getColor(this, R.color.black))
                hitMyAdsListApi()

            }
            R.id.tv_trader -> {
                selectedTypeAds = "3"
                tv_activitiess.setTextColor(ContextCompat.getColor(this, R.color.black))
                tv_child_care.setTextColor(ContextCompat.getColor(this, R.color.black))
                tv_trader.setTextColor(ContextCompat.getColor(this, R.color.app))
                hitMyAdsListApi()

            }
            R.id.ivAdd -> {
                if (userType == "2") {
                    val i = Intent(this@MyAddActivity, PubilerActivity::class.java)
                    startActivity(i)
                } else {
                    consultantUserDialogMethod()
                }
            }
            R.id.ivBack -> {
                onBackPressed()
            }
        }
    }

    override fun onChildCareDeleteAdClick(position: Int, adID: String?) {
        categoryId = "2"
        childCarePos = position

        hitDeleteMyAdsApi(adID, categoryId)
    }

    fun consultantUserDialogMethod() {
        consultantUserDialog = Dialog(this@MyAddActivity)
        consultantUserDialog!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        consultantUserDialog!!.setContentView(R.layout.alert_add_post_restiction)
        consultantUserDialog!!.setCancelable(false)
        consultantUserDialog!!.setCanceledOnTouchOutside(false)

        consultantUserDialog!!.iv_cross.setOnClickListener {
            consultantUserDialog!!.dismiss()
        }
        consultantUserDialog!!.rl_open_settings.setOnClickListener {
            OpenActivity(SettingsActivity::class.java) {
                putString("authorization", authorization)
                consultantUserDialog!!.dismiss()
            }
        }

        consultantUserDialog!!.show()
    }


    override fun onEditAdClick(
        position: Int,
        adID: String?,
        childTypeId: String,
        name: String,
        noofplaces: String,
        description: String,
        countryCode: String,
        phoneNumber: String,
        city: String,
        address: String,
        childCareImageList: ArrayList<ChildCareImageMyAds>,
        lattude: String,
        longitude: String
    ) {
        val intent = Intent(this, EditBabySitterActivity::class.java)
        intent.putExtra("adID", adID)
        intent.putExtra("childType", childTypeId)
        intent.putExtra("name", name)
        intent.putExtra("noofplaces", noofplaces)
        intent.putExtra("description", description)
        intent.putExtra("countryCode", countryCode)
        intent.putExtra("phoneNumber", phoneNumber)
        intent.putExtra("city", city)
        intent.putExtra("address", address)
        intent.putExtra("childCareImageList", childCareImageList)
        intent.putExtra("latitude", lattude)
        intent.putExtra("longitude", longitude)
        startActivity(intent)
    }


    override fun onActivitiesDeleteAdClick(position: Int, adID: String?) {
        categoryId = "1"
        activitiesPos = position
        hitDeleteMyAdsApi(adID, categoryId)

    }

    override fun onEditActivitiesAdClick(
        position: Int,
        adID: String?,
        activityTypeId: String,
        nameofShop: String,
        nameofActivity: String,
        description: String,
        website: String,
        countryCode: String,
        phoneNumber: String,
        address: String,
        minage: String,
        maxage: String,
        latti: String,
        longi: String,
        city: String,
        ageGroupListMyAds: ArrayList<AgeGroupMyAds>,
        ActivityimagesList: ArrayList<ActivityimageMyAds>,
        eventMyAdsList: ArrayList<EventMyAds>
    ) {

        val intent = Intent(this, EditActivity::class.java)
        intent.putExtra("adID", adID)
        intent.putExtra("countryCodedata", countryCode)
        intent.putExtra("activityTypeId", activityTypeId)
        intent.putExtra("nameofShop", nameofShop)
        intent.putExtra("nameofActivity", nameofActivity)
        intent.putExtra("descriptiondata", description)
        intent.putExtra("phoneNumber", phoneNumber)
        intent.putExtra("address", address)
        intent.putExtra("minage", minage)
        intent.putExtra("maxage", maxage)
        intent.putExtra("city", city)
        intent.putExtra("latti", latti)
        intent.putExtra("longi", longi)
        intent.putExtra("website", website)
        intent.putParcelableArrayListExtra("ageGroupList", ageGroupListMyAds)
        intent.putParcelableArrayListExtra("activityimagesList", ActivityimagesList)
        intent.putParcelableArrayListExtra("eventMyAdsList", eventMyAdsList)
        startActivity(intent)

    }


    override fun onTraderAdDeleteAdClick(position: Int, adID: String?) {
        categoryId = "3"
        tradersPos = position
        hitDeleteMyAdsApi(adID, categoryId)

    }


    override fun onEditTraderAdClick(
        position: Int,
        adID: String?,
        traderTypeId: String,
        nameofShop: String,
        description: String,
        countryCode: String,
        phoneNumber: String,
        address: String,
        city: String,
        lati: String,
        longi: String,
        email: String,
        website: String,
        traderImageList: ArrayList<TradersimageMyAds>,
        daytimeList: ArrayList<TraderDaysTimingMyAds>,
        traderProductList: ArrayList<TraderProductMyAds>
    ) {
        val intent = Intent(this, EditTraderActivity::class.java)
        intent.putExtra("adID", adID)
        intent.putExtra("traderTypeId", traderTypeId)
        intent.putExtra("nameofShop", nameofShop)
        intent.putExtra("description", description.toString())
        intent.putExtra("countryCode", countryCode)
        intent.putExtra("phoneNumber", phoneNumber)
        intent.putExtra("address", address)
        intent.putExtra("city", city)
        intent.putExtra("lati", lati)
        intent.putExtra("longi", longi)
        intent.putExtra("email", email)
        intent.putExtra("website", website)
        intent.putExtra("traderImageList", traderImageList)
        intent.putExtra("daytimeList", daytimeList)
        intent.putExtra("traderProductList", traderProductList)
        startActivity(intent)

    }

}