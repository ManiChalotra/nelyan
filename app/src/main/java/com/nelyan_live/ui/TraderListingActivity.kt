package com.nelyan_live.ui


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.preferences.core.preferencesKey
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.meherr.mehar.data.viewmodel.AppViewModel
import com.nelyan_live.R
import com.nelyan_live.adapter.TraderListingAdapter
import com.nelyan_live.db.DataStoragePreference
import com.nelyan_live.modals.homeactivitylist.HomeAcitivityResponseData
import com.nelyan_live.modals.hometraderpostlist.HomeTraderListData
import com.nelyan_live.modals.hometraderpostlist.HomeTraderPostListResponse
import com.nelyan_live.utils.*
import kotlinx.android.synthetic.main.fragment_activity_list.*
import kotlinx.android.synthetic.main.fragment_trader_listing.*
import kotlinx.android.synthetic.main.fragment_trader_listing.ivBack
import kotlinx.android.synthetic.main.fragment_trader_listing.tvFilter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.json.JSONObject
import kotlin.coroutines.CoroutineContext

class TraderListingActivity : AppCompatActivity(), View.OnClickListener, TraderListingAdapter.OnTraderItemClickListner, CoroutineScope {


    private  val job by lazy {
        Job()
    }
    lateinit var ivFavourite :ImageView

    private var listType: String? = null

    private var traderListingAdapter: TraderListingAdapter? = null
    private var rvTrader: RecyclerView? = null
    var LAUNCH_SECOND_ACTIVITY = 1

    private val traderDatalist by lazy { ArrayList<HomeTraderListData>() }

    private val appViewModel by lazy { ViewModelProvider.AndroidViewModelFactory.getInstance(this.application).create(AppViewModel::class.java) }
    private  val dataStoragePreference by lazy { DataStoragePreference(this@TraderListingActivity) }
    private var authkey: String?= null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_trader_listing)
        initalizeClicks()
     //   rv_traderListing.adapter = TraderListingAdapter(this, traderDatalist, this)
        rvTrader = findViewById(R.id.rv_traderListing)


        if (intent.extras !=null){
            listType = intent.getStringExtra("type").toString()
            Log.e("qwe", intent.getStringExtra("type").toString())
        }

        launch (Dispatchers.Main.immediate){
            authkey = dataStoragePreference?.emitStoredValue(preferencesKey<String>("auth_key"))?.first()
            tv_city_zipcode.text = dataStoragePreference?.emitStoredValue(preferencesKey<String>("cityLogin"))?.first()

            if (checkIfHasNetwork(this@TraderListingActivity)) {
                appViewModel.sendHomeActivitiesData(security_key,authkey, listType)
                trader_list_progressbar?.showProgressBar()
                checkMvvmResponse()

            } else {
                showSnackBar(this@TraderListingActivity, getString(R.string.no_internet_error))
            }
        }

    }

    private fun initalizeClicks() {
        ivBack.setOnClickListener(this)
        ivMap.setOnClickListener(this)
        tvFilter.setOnClickListener(this)

    }

    private fun setTraderAdapter(traderDatalist: ArrayList<HomeTraderListData>) {
        traderListingAdapter = TraderListingAdapter(this, traderDatalist, this)
        rvTrader!!.setLayoutManager(LinearLayoutManager(this))
        rvTrader!!.setAdapter(traderListingAdapter)
    }


    override fun onResume() {
        super.onResume()

    }


    private  fun checkMvvmResponse(){
        appViewModel.observeHomeActivitiesResponse()!!.observe(this, androidx.lifecycle.Observer { response ->
            if (response!!.isSuccessful && response.code() == 200) {
                if (response.body() != null) {
                    trader_list_progressbar?.hideProgressBar()
                    Log.d("homeTraderListRespone", "-------------" + Gson().toJson(response.body()))
                    val mResponse = response.body().toString()
                    val jsonObject = JSONObject(mResponse)
                    val homeTraderResponse =  Gson().fromJson<HomeTraderPostListResponse>(response.body().toString(), HomeTraderPostListResponse::class.java)
                    if (traderDatalist != null) {
                        traderDatalist!!.clear()
                        traderDatalist.addAll(homeTraderResponse.data)
                    }

                    if (traderDatalist.size == 0) {
                        rvTrader!!.visibility = View.GONE
                        tv_no_trader_list!!.visibility = View.VISIBLE
                    } else {
                        rvTrader!!.visibility = View.VISIBLE
                        tv_no_trader_list!!.visibility = View.GONE
                        setTraderAdapter(traderDatalist)
                    }
                }
            } else {
                ErrorBodyResponse(response, this, trader_list_progressbar)
            }
        })

        appViewModel.observeAddFavouritePostApiResponse()!!.observe(this, Observer { response ->
            if (response!!.isSuccessful && response.code() == 200) {
                Log.d("addFavouriteResBody", "----------" + Gson().toJson(response.body()))
                trader_list_progressbar?.hideProgressBar()
                val mResponse = response.body().toString()
                val jsonObject = JSONObject(mResponse)

                val message = jsonObject.get("msg").toString()
                myCustomToast(message)
                if (message.equals("You marked this Post as Your Favourite")){
                    ivFavourite.setImageResource(R.drawable.heart)
                }else {
                    ivFavourite.setImageResource(R.drawable.heart_purple)
                }

                //  myCustomToast(jsonObject.getJSONObject("msg").toString())
            } else {
                ErrorBodyResponse(response, this, trader_list_progressbar)
                trader_list_progressbar?.hideProgressBar()
            }
        })


        appViewModel.getException()!!.observe(this, Observer {
            myCustomToast(it)
            trader_list_progressbar?.hideProgressBar()
        })
    }


    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.ivBack -> {
                onBackPressed()
            }
            R.id.ivMap -> {
                OpenActivity(HomeChildCareOnMapActivity::class.java)
            }
            R.id.tvFilter -> {
              //  OpenActivity(TraderFilterActivity::class.java)
                val i = Intent(this, TraderFilterActivity::class.java)
                startActivityForResult(i, LAUNCH_SECOND_ACTIVITY)

            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode === LAUNCH_SECOND_ACTIVITY) {
            if (resultCode === Activity.RESULT_OK) {
                val traderDatalistss: ArrayList<HomeTraderListData> = data!!.getSerializableExtra("filteredTraderDatalist") as ArrayList<HomeTraderListData>

                if (traderDatalistss.size == 0) {
                    rvTrader!!.visibility = View.GONE
                    tv_no_trader_list!!.visibility = View.VISIBLE
                } else {
                    rvTrader!!.visibility = View.VISIBLE
                    tv_no_trader_list!!.visibility = View.GONE
                    setTraderAdapter(traderDatalistss)
                }
            }
            if (resultCode === Activity.RESULT_CANCELED) {
                // Write your code if there's no result
            }
        }
    }

    override fun onTraderListItemClickListner(position: Int, postId: String) {
        OpenActivity(TraderPublishActivty::class.java){
            putString("postId", postId)
        }
    }

    override fun onFavouriteItemClickListner(position: Int, postId: String, favourite: ImageView) {

         ivFavourite = favourite

        if (checkIfHasNetwork(this@TraderListingActivity)) {
            appViewModel.addFavouritePostApiData(security_key, authkey, postId, "3")
            trader_list_progressbar.showProgressBar()//  loginProgressBar?.showProgressBar()
        } else {
            showSnackBar(this@TraderListingActivity, getString(R.string.no_internet_error))
        }

/*
        OpenActivity(FavouriteActivity::class.java){
            putString("postId", postId)
        }
*/
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job
}