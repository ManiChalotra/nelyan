package com.nelyanlive.ui


import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.nelyanlive.R
import com.nelyanlive.adapter.MyFavoriteEventsAdapter
import com.nelyanlive.adapter.MyFavouritePostListAdapter
import com.nelyanlive.data.viewmodel.AppViewModel
import com.nelyanlive.modals.myFavourite.FavouriteEvent
import com.nelyanlive.modals.myFavourite.FavouritePost
import com.nelyanlive.modals.myFavourite.MyFavouriteListResponse
import com.nelyanlive.utils.*
import kotlinx.android.synthetic.main.fragment_favorite.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import org.json.JSONObject
import kotlin.coroutines.CoroutineContext

class FavouriteActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener,
        MyFavouritePostListAdapter.OnFavPostRecyclerViewItemClickListner, MyFavoriteEventsAdapter.OnFavEventClickListner, View.OnClickListener, CoroutineScope {


    var job = Job()
    private var authorization = ""
    private var selectedType = ""
    private var selectedFavType = ""
    lateinit var mContext: FavouriteActivity
    var myFavoriteEventsAdapter: MyFavoriteEventsAdapter? = null
    var myFavouritePostListAdapter: MyFavouritePostListAdapter? = null
    var ivFavouriteeEvent: ImageView? = null
    var ivFavouriteePost: ImageView? = null



    private val favouritePostlist by lazy { ArrayList<FavouritePost>() }
    private val favouriteEventlist by lazy { ArrayList<FavouriteEvent>() }

    val appViewModel by lazy {
        ViewModelProvider.AndroidViewModelFactory.getInstance(this.application).create(AppViewModel::class.java)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_favorite)
        mContext = this
        initalizeClicks()
        tv_ads.setOnClickListener(this)
        tv_events.setOnClickListener(this)
    }


    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private  fun initalizeClicks(){
        ivBack.setOnClickListener(this)
    }

    override fun onResume() {
        super.onResume()
        hitMyFavListApi()
        checkMvvmResponse()
    }

    private fun hitMyFavListApi() {
        if (checkIfHasNetwork(this)) {
            authorization = AllSharedPref.restoreString(this, "auth_key")
            appViewModel.sendMyFavouriteData(security_key, authorization)
            myfav_progressBar?.showProgressBar()
        } else {
            showSnackBar(this, getString(R.string.no_internet_error))
        }
    }

    fun setFavouriteEventAdaptor(datalist: ArrayList<FavouriteEvent>) {
        myFavoriteEventsAdapter = MyFavoriteEventsAdapter(datalist, this)
        rv_my_favourite!!.layoutManager = LinearLayoutManager(mContext)
        rv_my_favourite!!.adapter = myFavoriteEventsAdapter
    }

    fun setFavouritePostAdaptor(datalist: ArrayList<FavouritePost>) {
        myFavouritePostListAdapter = MyFavouritePostListAdapter(mContext, datalist, this)
        rv_my_favourite!!.layoutManager = LinearLayoutManager(mContext)
        rv_my_favourite!!.adapter = myFavouritePostListAdapter
    }


    private fun checkMvvmResponse() {
        appViewModel.observeFavourteResponse()!!.observe(this, androidx.lifecycle.Observer { response ->

           try {
               if (response!!.isSuccessful && response.code() == 200) {
                   if (response.body() != null) {
                       myfav_progressBar?.hideProgressBar()
                       Log.d("myFavResponse", "-------------" + Gson().toJson(response.body()))
                       val mResponse = response.body().toString()
                       val jsonObject = JSONObject(mResponse)
                       val myFavouriteRespone = Gson().fromJson<MyFavouriteListResponse>(response.body().toString(), MyFavouriteListResponse::class.java)

                       if (favouritePostlist != null) {
                           favouritePostlist.clear()
                           favouritePostlist.addAll(myFavouriteRespone.data.FavouritePost)
                       }
                       if (favouriteEventlist != null) {
                           favouriteEventlist.clear()
                           favouriteEventlist.addAll(myFavouriteRespone.data.FavouriteEvent)
                       }


                       if (selectedType == "2") {
                           if (favouriteEventlist.size == 0) {
                               rv_my_favourite!!.visibility = View.GONE
                               tv_no_fav!!.visibility = View.VISIBLE
                           } else {
                               rv_my_favourite!!.visibility = View.VISIBLE
                               tv_no_fav!!.visibility = View.GONE
                               setFavouriteEventAdaptor(favouriteEventlist)
                               myFavoriteEventsAdapter!!.notifyDataSetChanged()
                           }

                       } else {
                           if (favouritePostlist.size == 0) {
                               rv_my_favourite!!.visibility = View.GONE
                               tv_no_fav!!.visibility = View.VISIBLE
                           } else {
                               rv_my_favourite!!.visibility = View.VISIBLE
                               tv_no_fav!!.visibility = View.GONE
                               setFavouritePostAdaptor(favouritePostlist)
                           }
                       }
                   }
               } else {
                   ErrorBodyResponse(response, this, myfav_progressBar)
               }
           }
           catch (e:Exception)
           {
               tv_no_fav!!.visibility = View.VISIBLE
               e.printStackTrace()}
        })

        appViewModel.getException()!!.observe(this, androidx.lifecycle.Observer {
            this.myCustomToast(it)
            myfav_progressBar?.hideProgressBar()
        })
    }


    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.ivBack->{
                onBackPressed()
            }

            R.id.tv_ads->{
                selectedType = "1"
                tv_ads.setTextColor(ContextCompat.getColor(mContext, R.color.app))
                tv_events.setTextColor(ContextCompat.getColor(mContext, R.color.black))
                hitMyFavListApi()
            }
            R.id.tv_events->{
                selectedType = "2"
                tv_ads.setTextColor(ContextCompat.getColor(mContext, R.color.black))
                tv_events.setTextColor(ContextCompat.getColor(mContext, R.color.app))
                hitMyFavListApi()
            }

        }
    }


    override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {}
    override fun onNothingSelected(parent: AdapterView<*>?) {}
    override fun onFavouritePostItemClickListner(activityId: String, categoryId: String) {
        when (categoryId) {
            "1" -> {
                OpenActivity(ActivityDetailsActivity::class.java) {
                    putString("activityId", activityId)
                    putString("categoryId", categoryId)
                }

            }
            "2" -> {
                OpenActivity(HomeChildCareDetailsActivity::class.java) {
                    putString("activityId", activityId)
                    putString("categoryId", categoryId)
                }

            }
            "3" -> {
                OpenActivity(TraderPublishActivty::class.java) {
                    putString("postId", activityId)

                }

            }
        }
    }

    override fun onAddFavouritePostClick(position: Int, postId: String?, postType: String?, ivFavourite: ImageView) {
        ivFavouriteePost=ivFavourite
        if (checkIfHasNetwork(this@FavouriteActivity)) {
            appViewModel.addFavouritePostApiData(security_key, authorization, postId, postType)
            selectedFavType= "Ads"

        } else {
            showSnackBar(this@FavouriteActivity, getString(R.string.no_internet_error))
        }

    }

    override fun onItemClickListner(position: Int) {
    }

    override fun onAddFavoriteEventClick(position: Int, eventId: String?, ivFavourite: ImageView) {
        ivFavouriteeEvent=ivFavourite
        if (checkIfHasNetwork(this@FavouriteActivity)) {
            appViewModel.addFavouriteApiData(security_key, authorization, eventId)
            selectedFavType = "Event"
           // myfav_progressBar.showProgressBar()
        } else {
            showSnackBar(this@FavouriteActivity, getString(R.string.no_internet_error))
        }

    }
}