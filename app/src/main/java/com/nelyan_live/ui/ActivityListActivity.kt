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
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.meherr.mehar.data.viewmodel.AppViewModel
import com.meherr.mehar.db.DataStoragePreference
import com.nelyan_live.AppUtils
import com.nelyan_live.R
import com.nelyan_live.adapter.ActivityListAdapter
import com.nelyan_live.utils.*
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.fragment_activity_list.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.json.JSONObject
import kotlin.coroutines.CoroutineContext

class ActivityListActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener,
        ActivityListAdapter.OnMyEventRecyclerViewItemClickListner, CoroutineScope {

    var iv_map: ImageView? = null
    var ivBack: ImageView? = null
    var orderby: Spinner? = null
    var dialog: Dialog? = null
    var activityListAdapter: ActivityListAdapter? = null
    var recyclerview: RecyclerView? = null
    private val appViewModel by lazy { ViewModelProvider.AndroidViewModelFactory.getInstance(this.application).create(AppViewModel::class.java) }
    private  val dataStoragePreference by lazy { DataStoragePreference(this@ActivityListActivity) }
    private var authkey: String?= null
    private  val job by lazy {
        Job()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_activity_list)
        ivBack = findViewById(R.id.ivBack)
        orderby = findViewById(R.id.orderby)
        iv_map = findViewById(R.id.iv_map)
        recyclerview = findViewById(R.id.recyclerview)


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


        activityListAdapter = ActivityListAdapter(this, this@ActivityListActivity)
       recyclerview!!.setLayoutManager(LinearLayoutManager(this))
      recyclerview!!.setAdapter(activityListAdapter)
        val genderlist = arrayOf<String?>(
                "", "Events in City",
                "Date Added",
                "Distance")
        val adapter: ArrayAdapter<*> = ArrayAdapter<Any?>(
             this, R.layout.customspinner, genderlist)

// Setting Adapter to the Spinner
      orderby!!.setAdapter(adapter)

// Setting OnItemClickListener to the Spinner
      orderby!!.setOnItemSelectedListener(this@ActivityListActivity)

    }




    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {}
    override fun onNothingSelected(parent: AdapterView<*>?) {}
    override fun onMyEventItemClickListner() {
OpenActivity(ActivityDetailsActivity::class.java)
        //AppUtils.gotoFragment(this, ActivityDetailsFragment(), R.id.frame_container, false)

    }

    override val coroutineContext: CoroutineContext
    get() = Dispatchers.Main + job

/*
    override fun onResume() {
        super.onResume()

        launch (Dispatchers.Main.immediate){
            authkey = dataStoragePreference?.emitStoredValue(preferencesKey<String>("auth_key"))?.first()
            appViewModel.addFavouriteApiData(security_key,authkey, "")
            activity_list_progressbar?.showProgressBar()

            checkMvvmResponse()
        }
    }
*/

/*
    private  fun checkMvvmResponse(){
        appViewModel.observeAddFavouriteApiResponse()!!.observe(this, Observer { response->
            if(response!!.isSuccessful && response.code()==200){
                Log.d("addFavouriteResBody","----------"+ Gson().toJson(response.body()))


            }else{
                ErrorBodyResponse(response, this, profileProgressBar)
            }
        })
        appViewModel.getException()!!.observe(this, Observer {
            myCustomToast(it)
            activity_list_progressbar?.hideProgressBar()
        })
    }
*/


}