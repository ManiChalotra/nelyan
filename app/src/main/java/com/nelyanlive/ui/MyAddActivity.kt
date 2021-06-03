package com.nelyanlive.ui


import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Spinner
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
import org.json.JSONArray
import org.json.JSONObject
import kotlin.coroutines.CoroutineContext

class MyAddActivity : AppCompatActivity(), CoroutineScope, View.OnClickListener, MyAdsChildCareAdapter.OnDeleteEditClickListner,
        MyAddAdapter.OnActivitiesDeleteEditClickListner, MyAdsTraderAdapter.OnDeleteEditTradersAdClickListner {

    lateinit var mContext: Context
    var ivAdd: ImageView? = null
    var ivBack: ImageView? = null
    var orderby: Spinner? = null
    var myAddAdapter: MyAddAdapter? = null
    var myAdsChildCareAdapter: MyAdsChildCareAdapter? = null
    var myAdsTraderAdapter: MyAdsTraderAdapter? = null
    var recyclerview: RecyclerView? = null
    var rvChildMyads: RecyclerView? = null
    var rvTraderMyads: RecyclerView? = null
    var job = Job()
    private var authorization = ""
    var categoryId=""
    var actvitiesPos=0
    var tradersPos=0
    var childdcarePos=0
    private val datalist by lazy { ArrayList<GetActivitypostMyAds>() }
    private val childCarelist by lazy { ArrayList<GetChildcarePostMyAds>() }
    private val traderlist by lazy { ArrayList<GetTraderMyAds>() }
    private val childCareImageList by lazy { ArrayList<ChildCareImageMyAds>() }
    private val traderImageList by lazy { ArrayList<TradersimageMyAds>() }
    private var childCareImagesJsonArray: JSONArray = JSONArray()
    private var traderImagesJsonArray: JSONArray = JSONArray()
    private var userType = ""
    var consultantUserDialog: Dialog? = null
    private var selectedTypeAds = "1"

    val dataStoragePreference by lazy {
        DataStoragePreference(this)
    }

    val appViewModel by lazy {
        ViewModelProvider.AndroidViewModelFactory.getInstance(this.application).create(AppViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_myadd)
        mContext = this
        ivBack = findViewById(R.id.ivBack)
        ivAdd = findViewById(R.id.ivAdd)
        recyclerview = findViewById(R.id.recyclerview)

        ivAdd!!.setOnClickListener(this)
        tv_activitiess!!.setOnClickListener(this)
        tv_child_care!!.setOnClickListener(this)
        tv_trader!!.setOnClickListener(this)
        ivBack!!.setOnClickListener(this)

        launch(Dispatchers.Main.immediate) {
            userType = dataStoragePreference.emitStoredValue(preferencesKey<String>("typeLogin")).first()
        }

        checkMvvmResponse()
        hitMyadsListApi()
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job


    private fun hitMyadsListApi() {
        if (checkIfHasNetwork(this)) {
            authorization = AllSharedPref.restoreString(this, "auth_key")
            appViewModel.sendMyAdsListData(security_key, authorization)
            myads_progressBar?.showProgressBar()
          //  checkMvvmResponse()
        } else {
            showSnackBar(this, getString(R.string.no_internet_error))
        }
    }

    private fun hitDeleteMyadsApi(pos: Int, adID: String?, categoryId: String) {
        if (checkIfHasNetwork(this)) {
            authorization = AllSharedPref.restoreString(this, "auth_key")
            appViewModel.sendDeleteAdData(security_key, authorization, categoryId, adID)
            //myads_progressBar?.showProgressBar()
        }
        else {
            showSnackBar(this, getString(R.string.no_internet_error))
        }
    }

    fun setAdaptor(activitypostMyAdsList: ArrayList<GetActivitypostMyAds>) {
        myAddAdapter = MyAddAdapter(mContext, activitypostMyAdsList, this)
        recyclerview!!.layoutManager = LinearLayoutManager(mContext)
        recyclerview!!.adapter = myAddAdapter
    }

    fun setChildCareAdaptor(activitypostMyAdsList: ArrayList<GetChildcarePostMyAds>) {
        myAdsChildCareAdapter = MyAdsChildCareAdapter(mContext, activitypostMyAdsList, this)
        recyclerview!!.layoutManager = LinearLayoutManager(mContext)
        recyclerview!!.adapter = myAdsChildCareAdapter
    }

    fun setTraderAdaptor(getTraderMyAdsList: ArrayList<GetTraderMyAds>) {
        myAdsTraderAdapter = MyAdsTraderAdapter(mContext, getTraderMyAdsList, this)
        recyclerview!!.layoutManager = LinearLayoutManager(mContext)
        recyclerview!!.adapter = myAdsTraderAdapter
    }

    private fun checkMvvmResponse() {
        appViewModel.observerMyAdsListResponse()!!.observe(this, androidx.lifecycle.Observer { response ->
            if (response!!.isSuccessful && response.code() == 200) {
                if (response.body() != null) {
                    myads_progressBar?.hideProgressBar()
                    Log.d("myadsResponse", "-------------" + Gson().toJson(response.body()))
                    val mResponse = response.body().toString()
                    val jsonObject = JSONObject(mResponse)
                    val myAdsResponse = Gson().fromJson<MyAdsResponsee>(response.body().toString(), MyAdsResponsee::class.java)

                    val mSizeOfData = jsonObject.getJSONObject("data").getJSONArray("getActivitypost").length()
                    val mSizeOfChildCare = jsonObject.getJSONObject("data").getJSONArray("getChildcarePosts").length()
                    val mSizeOfTrader = jsonObject.getJSONObject("data").getJSONArray("GetTrader").length()

                    if (datalist != null) {
                        datalist.clear()
                        datalist.addAll(myAdsResponse.data.getActivitypost)
                    }

                    for (i in 0 until mSizeOfChildCare) {
                        if (mSizeOfChildCare != 0) {
                            val postID = jsonObject.getJSONObject("data").getJSONArray("getChildcarePosts")
                                    .getJSONObject(i).get("id").toString()
                            val userId = jsonObject.getJSONObject("data").getJSONArray("getChildcarePosts")
                                    .getJSONObject(i).get("userId").toString()
                            val type = jsonObject.getJSONObject("data").getJSONArray("getChildcarePosts")
                                    .getJSONObject(i).get("type").toString()
                            val childcareType = jsonObject.getJSONObject("data").getJSONArray("getChildcarePosts")
                                    .getJSONObject(i).get("ChildcareType").toString()
                            val name = jsonObject.getJSONObject("data").getJSONArray("getChildcarePosts")
                                    .getJSONObject(i).get("name").toString()
                            val availableplace = jsonObject.getJSONObject("data").getJSONArray("getChildcarePosts")
                                    .getJSONObject(i).get("availableplace").toString()
                            val description = jsonObject.getJSONObject("data").getJSONArray("getChildcarePosts")
                                    .getJSONObject(i).get("description").toString()
                            val phone = jsonObject.getJSONObject("data").getJSONArray("getChildcarePosts")
                                    .getJSONObject(i).get("phone").toString()
                            val city = jsonObject.getJSONObject("data").getJSONArray("getChildcarePosts")
                                    .getJSONObject(i).get("city").toString()
                            val countryCode = jsonObject.getJSONObject("data").getJSONArray("getChildcarePosts")
                                    .getJSONObject(i).get("countryCode").toString()

                            childCareImagesJsonArray = jsonObject.getJSONObject("data").getJSONArray("getChildcarePosts")
                                    .getJSONObject(i).getJSONArray("ChildCareImages")

                            if (childCarelist != null) { childCarelist.clear() }

                            if (childCareImagesJsonArray != null) {
                                for (j in 0 until childCareImagesJsonArray.length()) {

                                    val image = jsonObject.getJSONObject("data").getJSONArray("getChildcarePosts").getJSONObject(i)
                                            .getJSONArray("ChildCareImages").getJSONObject(j).get("image").toString()
                                    val id = jsonObject.getJSONObject("data").getJSONArray("getChildcarePosts").getJSONObject(i).
                                    getJSONArray("ChildCareImages").getJSONObject(j).get("id").toString().toInt()
                                    val postId = jsonObject.getJSONObject("data").getJSONArray("getChildcarePosts").getJSONObject(i).
                                    getJSONArray("ChildCareImages").getJSONObject(j).get("postId").toString().toInt()

                                    childCareImageList.add(ChildCareImageMyAds(id, image, postId))
                                } }

                            childCarelist.add(GetChildcarePostMyAds(childCareImageList, childcareType.toInt(), availableplace.toInt(), city, countryCode, description,
                                    postID.toInt(), name, phone, postID.toInt(), userId.toInt()))
                        } }

                    if (childCarelist != null) {
                        childCarelist.clear()
                        childCarelist.addAll(myAdsResponse.data.getChildcarePosts)
                    }

                    for (i in 0 until mSizeOfTrader) {
                        if (mSizeOfTrader != 0) {
                            val postID = jsonObject.getJSONObject("data").getJSONArray("GetTrader")
                                    .getJSONObject(i).get("id").toString()
                            val userId = jsonObject.getJSONObject("data").getJSONArray("GetTrader")
                                    .getJSONObject(i).get("userId").toString()
                            val categoryId = jsonObject.getJSONObject("data").getJSONArray("GetTrader")
                                    .getJSONObject(i).get("categoryId").toString()
                            val typeofTraderId = jsonObject.getJSONObject("data").getJSONArray("GetTrader")
                                    .getJSONObject(i).get("typeofTraderId").toString()
                            val tradername = jsonObject.getJSONObject("data").getJSONArray("GetTrader")
                                    .getJSONObject(i).get("tradername").toString()
                            val nameOfShop = jsonObject.getJSONObject("data").getJSONArray("GetTrader")
                                    .getJSONObject(i).get("nameOfShop").toString()
                            val description = jsonObject.getJSONObject("data").getJSONArray("GetTrader")
                                    .getJSONObject(i).get("description").toString()
                            val address = jsonObject.getJSONObject("data").getJSONArray("GetTrader")
                                    .getJSONObject(i).get("address").toString()
                            val email = jsonObject.getJSONObject("data").getJSONArray("GetTrader")
                                    .getJSONObject(i).get("email").toString()
                            val website = jsonObject.getJSONObject("data").getJSONArray("GetTrader")
                                    .getJSONObject(i).get("website").toString()
                            val phone = jsonObject.getJSONObject("data").getJSONArray("GetTrader")
                                    .getJSONObject(i).get("phone").toString()
                            val city = jsonObject.getJSONObject("data").getJSONArray("GetTrader")
                                    .getJSONObject(i).get("city").toString()
                            val countryCode = jsonObject.getJSONObject("data").getJSONArray("GetTrader")
                                    .getJSONObject(i).get("country_code").toString()

                            traderImagesJsonArray = jsonObject.getJSONObject("data").getJSONArray("GetTrader")
                                    .getJSONObject(i).getJSONArray("tradersimages")

                            if (traderlist != null) { traderlist.clear() }

                            if (traderImagesJsonArray != null) {
                                for (j in 0 until traderImagesJsonArray.length()) {

                                    val image = jsonObject.getJSONObject("data").getJSONArray("GetTrader").getJSONObject(i).getJSONArray("tradersimages").getJSONObject(j).get("images").toString()
                                    val id = jsonObject.getJSONObject("data").getJSONArray("GetTrader").getJSONObject(i).getJSONArray("tradersimages").getJSONObject(j).get("id").toString().toInt()
                                    val tradersId = jsonObject.getJSONObject("data").getJSONArray("GetTrader").getJSONObject(i).getJSONArray("tradersimages").getJSONObject(j).get("tradersId").toString().toInt()

                                    traderImageList.add(TradersimageMyAds(id, image, tradersId))
                                } }

                            traderlist.add(GetTraderMyAds(address, categoryId.toInt(), city, countryCode, description, email,
                                    postID.toInt(), nameOfShop, phone, tradername, traderImageList, typeofTraderId.toInt(), userId.toInt(), website))
                        } }

                    if (traderlist != null) {
                        traderlist.clear()
                        traderlist.addAll(myAdsResponse.data.GetTrader)
                    }

                    if (selectedTypeAds == "2"){
                        if (childCarelist.size !=0){
                            recyclerview!!.visibility = View.VISIBLE
                            tv_no_ad!!.visibility = View.GONE
                            setChildCareAdaptor(childCarelist)
                            myAdsChildCareAdapter!!.notifyDataSetChanged()
                        }
                        else{
                            recyclerview!!.visibility = View.GONE
                            tv_no_ad!!.visibility = View.VISIBLE
                        }

                    }
                    else if (selectedTypeAds == "3"){
                        if (traderlist.size !=0){
                            recyclerview!!.visibility = View.VISIBLE
                            tv_no_ad!!.visibility = View.GONE
                            setTraderAdaptor(traderlist)
                            myAdsTraderAdapter!!.notifyDataSetChanged()

                        }else{
                            recyclerview!!.visibility = View.GONE
                            tv_no_ad!!.visibility = View.VISIBLE
                        }
                    }
                    else if (selectedTypeAds == "1"){
                        if (datalist.size !=0){
                            recyclerview!!.visibility = View.VISIBLE
                            tv_no_ad!!.visibility = View.GONE
                            setAdaptor(datalist)
                            myAddAdapter!!.notifyDataSetChanged()

                        }
                        else
                        {
                            recyclerview!!.visibility = View.GONE
                            tv_no_ad!!.visibility = View.VISIBLE

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
               // myads_progressBar?.hideProgressBar()
                val mResponse = response.body().toString()
                val jsonObject = JSONObject(mResponse)
                val message = jsonObject.get("msg").toString()
                myCustomToast(message)
                if (categoryId == "2"){
                    childCarelist.removeAt(childdcarePos)
                    myAdsChildCareAdapter!!.notifyDataSetChanged()
                    Log.e("deleteListSize", childCarelist.size.toString())

                    if (childCarelist.size == 0) {
                        tv_no_ad.visibility = View.VISIBLE
                        recyclerview!!.visibility = View.GONE
                    } else {
                        recyclerview!!.visibility = View.VISIBLE
                        tv_no_ad.visibility = View.GONE

                    }

                }
                else if (categoryId == "1"){

                    datalist.removeAt(actvitiesPos)
                    myAddAdapter!!.notifyDataSetChanged()
                    Log.e("deleteListSize", datalist.size.toString())

                    if (datalist.size == 0) {
                        tv_no_ad.visibility = View.VISIBLE
                        recyclerview!!.visibility = View.GONE
                    } else {
                        recyclerview!!.visibility = View.VISIBLE
                        tv_no_ad.visibility = View.GONE

                    }


                }
                else if (categoryId == "3"){
                    traderlist.removeAt(tradersPos)
                    myAdsTraderAdapter!!.notifyDataSetChanged()
                    Log.e("deleteListSize", traderlist.size.toString())

                    if (traderlist.size == 0) {
                        tv_no_ad.visibility = View.VISIBLE
                        recyclerview!!.visibility = View.GONE
                    } else {
                        recyclerview!!.visibility = View.VISIBLE
                        tv_no_ad.visibility = View.GONE
                    }
                }
            }
            else {
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
                tv_activitiess.setTextColor(ContextCompat.getColor(mContext, R.color.app))
                tv_child_care.setTextColor(ContextCompat.getColor(mContext, R.color.black))
                tv_trader.setTextColor(ContextCompat.getColor(mContext, R.color.black))
                hitMyadsListApi()
            }
            R.id.tv_child_care -> {
                selectedTypeAds = "2"
                tv_activitiess.setTextColor(ContextCompat.getColor(mContext, R.color.black))
                tv_child_care.setTextColor(ContextCompat.getColor(mContext, R.color.app))
                tv_trader.setTextColor(ContextCompat.getColor(mContext, R.color.black))
                hitMyadsListApi()

            }
            R.id.tv_trader -> {
                selectedTypeAds = "3"
                tv_activitiess.setTextColor(ContextCompat.getColor(mContext, R.color.black))
                tv_child_care.setTextColor(ContextCompat.getColor(mContext, R.color.black))
                tv_trader.setTextColor(ContextCompat.getColor(mContext, R.color.app))
                hitMyadsListApi()

            }
            R.id.ivAdd -> {
                if (userType == "2") {
                    val i = Intent(this@MyAddActivity, PubilerActivity::class.java)
                    startActivity(i)
                }else {
                    consultantUserDialogMethod()
                }
            }
            R.id.ivBack -> {
                onBackPressed()
            }
        }
    }

    override fun onChildCareDeleteAdClick(position: Int, adID: String?) {
         categoryId="2"
        childdcarePos=position
        hitDeleteMyadsApi(position, adID, categoryId)
    }

    fun consultantUserDialogMethod() {
        consultantUserDialog = Dialog(this@MyAddActivity)
        consultantUserDialog!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        consultantUserDialog!!.setContentView(R.layout.alert_add_post_restiction)
        consultantUserDialog!!.setCancelable(false)
        consultantUserDialog!!.setCanceledOnTouchOutside(false)

        consultantUserDialog!!.iv_cross.setOnClickListener { //  mContext.startActivity(new Intent(mContext, HomeActivity.class));
            consultantUserDialog!!.dismiss()
        }
        consultantUserDialog!!.rl_open_settings.setOnClickListener { //  mContext.startActivity(new Intent(mContext, HomeActivity.class));
            OpenActivity(SettingsActivity::class.java) {
                putString("authorization", authorization)
                consultantUserDialog!!.dismiss()
            }
        }

        consultantUserDialog!!.show()
    }


    override fun onEditAdClick(position: Int, adID: String?) {

    }

    override fun onActivitiesDeleteAdClick(position: Int, adID: String?) {
        categoryId="1"
        actvitiesPos=position
        hitDeleteMyadsApi(position, adID, categoryId)

    }

    override fun onEditActivitiesAdClick(position: Int, adID: String?, activityTypeId: String, nameofShop: String,
                                         nameofActivity: String, description: String, countryCode: String, phoneNumber: String,
                                         address: String, city: String, ageGroupListMyAds: ArrayList<AgeGroupMyAds>,
                                         activityimagesList: ArrayList<ActivityimageMyAds>, eventMyAdsList: ArrayList<EventMyAds>) {

          var intent =Intent(mContext, EditActivity::class.java)
          intent.putExtra("adID", adID)
          intent.putExtra("activityTypeId", activityTypeId)
          intent.putExtra("nameofShop", nameofShop)
          intent.putExtra("nameofActivity", nameofActivity)
          intent.putExtra("description", description)
          intent.putExtra("countryCode", countryCode)
          intent.putExtra("phoneNumber", phoneNumber)
          intent.putExtra("address", address)
          intent.putParcelableArrayListExtra("ageGroupList", ageGroupListMyAds)
          intent.putParcelableArrayListExtra("activityimagesList", activityimagesList)
          intent.putParcelableArrayListExtra("eventMyAdsList", eventMyAdsList)
          startActivity(intent)
    }


    override fun onTraderAdDeleteAdClick(position: Int, adID: String?) {
        categoryId="3"
        tradersPos=position
        hitDeleteMyadsApi(position, adID, categoryId)

    }

    override fun onEditTraderAdClick(position: Int, adID: String?) {

    }
}