package com.nelyan_live.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.meherr.mehar.data.viewmodel.AppViewModel
import com.nelyan_live.R
import com.nelyan_live.adapter.MyAddAdapter
import com.nelyan_live.modals.MyadsEvent
import com.nelyan_live.modals.myads.MyAdsData
import com.nelyan_live.modals.myads.MyAdsResponse
import com.nelyan_live.utils.*
import kotlinx.android.synthetic.main.activity_myadd.*
import kotlinx.android.synthetic.main.fragment_event.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import org.json.JSONObject
import kotlin.coroutines.CoroutineContext

class MyAddActivity : AppCompatActivity(), CoroutineScope {
    lateinit var mContext: Context
    var ivAdd: ImageView? = null
    var ivBack: ImageView? = null
    var orderby: Spinner? = null
    var myAddAdapter: MyAddAdapter? = null
    var recyclerview: RecyclerView? = null
    var job = Job()
    private var authorization = ""

    private val datalist by lazy { ArrayList<MyAdsData>() }

    val appViewModel by lazy {
        ViewModelProvider.AndroidViewModelFactory.getInstance(this.application).create(AppViewModel::class.java)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_myadd)
        mContext = this
        ivBack = findViewById(R.id.ivBack)
        ivBack!!.setOnClickListener(View.OnClickListener { onBackPressed() })
        ivAdd = findViewById(R.id.ivAdd)
        ivAdd!!.setOnClickListener(View.OnClickListener {
            val i = Intent(this@MyAddActivity, com.nelyan_live.ui.PubilerActivity::class.java)
            startActivity(i)
        })
        recyclerview = findViewById(R.id.recyclerview)

        // authorization = intent?.extras?.getString("authorization").toString()


    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job


    override fun onResume() {
        super.onResume()
        hitMyadsListApi()
        checkMvvmResponse()
    }

    private fun hitMyadsListApi() {
        if (checkIfHasNetwork(this)) {
            authorization = AllSharedPref.restoreString(this, "auth_key")
            appViewModel.sendMyAdsListData(security_key, authorization)
            eventProgressBar?.showProgressBar()
        } else {
            showSnackBar(this, getString(R.string.no_internet_error))
        }
    }

    fun setAdaptor(datalist: ArrayList<MyAdsData>) {
        myAddAdapter = MyAddAdapter(mContext, datalist)
        recyclerview!!.setLayoutManager(LinearLayoutManager(mContext))
        recyclerview!!.setAdapter(myAddAdapter)
    }

    private fun checkMvvmResponse() {
        appViewModel.observerMyAdsListResponse()!!.observe(this, androidx.lifecycle.Observer { response ->
            if (response!!.isSuccessful && response.code() == 200) {
                if (response.body() != null) {
                    eventProgressBar?.hideProgressBar()
                    Log.d("myadsResponse", "-------------" + Gson().toJson(response.body()))
                    val mResponse = response.body().toString()
                    val jsonObject = JSONObject(mResponse)
                      val myAdsResponse =  Gson().fromJson<MyAdsResponse>(response.body().toString(),MyAdsResponse::class.java)
                    if (datalist != null) {
                        datalist!!.clear()
                        datalist.addAll(myAdsResponse.data)
                    }



/*
                    for (i in 0 until mSizeOfData) {
                        val activityName = jsonObject.getJSONArray("data").getJSONObject(i).get("activityname").toString()
                        val adMsg = jsonObject.getJSONArray("data").getJSONObject(i).get("adMsg").toString()
//                        val addInfo = jsonObject.getJSONArray("data").getJSONObject(i).get("addInfo").toString()
                        val availablePlace = jsonObject.getJSONArray("data").getJSONObject(i).get("availablePlace").toString()
                        val name = jsonObject.getJSONArray("data").getJSONObject(i).get("name").toString()
                        val image = jsonObject.getJSONArray("data").getJSONObject(i).get("image").toString()
                        val description = jsonObject.getJSONArray("data").getJSONObject(i).get("description").toString()
                        val address = jsonObject.getJSONArray("data").getJSONObject(i).get("address").toString()
                        val latitude = jsonObject.getJSONArray("data").getJSONObject(i).get("latitude").toString()
                        val longitude = jsonObject.getJSONArray("data").getJSONObject(i).get("longitude").toString()
                        val price = jsonObject.getJSONArray("data").getJSONObject(i).get("price").toString()
                     */
/*   datalist!!.add(MyadsEvent(activityName, adMsg,addInfo, address, availablePlace, image, name, "", "", "", "", price,
                                description, "", latitude, longitude))*//*

                    }
*/
                    if (datalist.size == 0) {
                        recyclerview!!.visibility = View.GONE
                        tv_no_ad!!.visibility = View.VISIBLE
                    } else {
                        recyclerview!!.visibility = View.VISIBLE
                        tv_no_ad!!.visibility = View.GONE
                        setAdaptor(datalist)
                    }
                }
            } else {
                ErrorBodyResponse(response, this, homeFargProgressBar)
            }
        })
        appViewModel.getException()!!.observe(this, androidx.lifecycle.Observer {
            this.myCustomToast(it)
            homeFargProgressBar?.hideProgressBar()
        })

    }
}