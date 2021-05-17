package com.nelyan_live.ui

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.preferences.core.preferencesKey
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager

import com.google.gson.Gson
import com.nelyan_live.data.viewmodel.AppViewModel
import com.nelyan_live.db.DataStoragePreference
import com.nelyan_live.R
import com.nelyan_live.adapter.PrivateSchoolAdapter
import com.nelyan_live.adapter.PublicSchoolAdapter

import com.nelyan_live.modals.homeSectorization.GetPrivate
import com.nelyan_live.modals.homeSectorization.GetPublic
import com.nelyan_live.modals.homeSectorization.HomeSectorizationResponse
import com.nelyan_live.utils.*

import kotlinx.android.synthetic.main.activity_home_sectorization.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.json.JSONObject
import kotlin.coroutines.CoroutineContext

class HomeSectorizationActivity : AppCompatActivity(), View.OnClickListener, CoroutineScope {

    val appViewModel by lazy {
        ViewModelProvider.AndroidViewModelFactory.getInstance(this.application).create(AppViewModel::class.java)
    }
    private val getPrivateSchoolDatalist by lazy { ArrayList<GetPrivate>() }
    private val getPublicSchoolDatalist by lazy { ArrayList<GetPublic>() }
    var privateSchoolAdapter: PrivateSchoolAdapter? = null
    var publicSchoolAdapter: PublicSchoolAdapter? = null

    private val dataStoragePreference by lazy { DataStoragePreference(this@HomeSectorizationActivity) }
    private var authkey: String? = null
    private var listType: String? = null
    private var sectorizationTYPE: String? = null

    var dialog: Dialog? = null
    var v: View? = null
    var job = Job()


    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_sectorization)
        initalize()

        if (intent.extras != null) {
            listType = intent.getStringExtra("type").toString()
            sectorizationTYPE = intent.getStringExtra("sectorizationType").toString()
        }
    }

    private fun initalize() {
        ivBack.setOnClickListener(this)
    }


    override fun onResume() {
        super.onResume()
        launch(Dispatchers.Main.immediate) {
            authkey = dataStoragePreference?.emitStoredValue(preferencesKey<String>("auth_key"))?.first()

            if (checkIfHasNetwork(this@HomeSectorizationActivity)) {
                appViewModel.sendHomeActivitiesData(security_key, authkey, listType)
                homeSectorizationProgressBar?.showProgressBar()
                checkMvvmresponse()

            } else {
                showSnackBar(this@HomeSectorizationActivity, getString(R.string.no_internet_error))
            }
        }

    }

    override fun onClick(v: View?) {
        when (v!!.id) {
         R.id.ivBack -> {
             onBackPressed()
         }
        }
    }

    private fun checkMvvmresponse() {

        appViewModel.observeHomeActivitiesResponse()!!.observe(this, androidx.lifecycle.Observer { response ->
            if (response!!.isSuccessful && response.code() == 200) {
                if (response.body() != null) {
                    homeSectorizationProgressBar?.hideProgressBar()
                    Log.d("homeChilcCare", "-------------" + Gson().toJson(response.body()))
                    val mResponse = response.body().toString()
                    val jsonObject = JSONObject(mResponse)
                    val homeSectorizationResponse = Gson().fromJson<HomeSectorizationResponse>(response.body().toString(), HomeSectorizationResponse::class.java)
                    if (getPrivateSchoolDatalist != null) {
                        getPrivateSchoolDatalist!!.clear()
                        getPrivateSchoolDatalist.addAll(homeSectorizationResponse.data.GetPrivate)
                    }

                    if (getPublicSchoolDatalist != null) {
                        getPublicSchoolDatalist!!.clear()
                        getPublicSchoolDatalist.addAll(homeSectorizationResponse.data.GetPublic)
                    }

                    if (sectorizationTYPE.equals("private")) {

                        tv_school_type.setText(getString(R.string.private_schools))
                        if (getPrivateSchoolDatalist.size == 0) {
                            rv_home_sectorization!!.visibility = View.GONE
                            tv_no_school!!.visibility = View.VISIBLE
                        } else {
                            rv_home_sectorization!!.visibility = View.VISIBLE
                            tv_no_school!!.visibility = View.GONE
                            setPrivateSchoolAdator(getPrivateSchoolDatalist)
                        }

                    } else if (sectorizationTYPE.equals("public")) {

                        tv_school_type.setText(getString(R.string.public_schools))
                        if (getPublicSchoolDatalist.size == 0) {
                            rv_home_sectorization!!.visibility = View.GONE
                            tv_no_school!!.visibility = View.VISIBLE
                        } else {
                            rv_home_sectorization!!.visibility = View.VISIBLE
                            tv_no_school!!.visibility = View.GONE
                            setPublicSchoolAdator(getPublicSchoolDatalist)
                        }

                    }
                }
            } else {
                ErrorBodyResponse(response, this, homeSectorizationProgressBar)
            }
        })

        appViewModel.getException()!!.observe(this, Observer {
            myCustomToast(it)
            homeSectorizationProgressBar?.hideProgressBar()
        })
    }

    private fun setPublicSchoolAdator(publicSchoolDatalist: ArrayList<GetPublic>) {
        publicSchoolAdapter = PublicSchoolAdapter(this, publicSchoolDatalist)
        rv_home_sectorization!!.setLayoutManager(LinearLayoutManager(this))
        rv_home_sectorization!!.setAdapter(publicSchoolAdapter)

    }

    private fun setPrivateSchoolAdator(privateSchoolDatalist: ArrayList<GetPrivate>) {
        privateSchoolAdapter = PrivateSchoolAdapter(this, privateSchoolDatalist)
        rv_home_sectorization!!.setLayoutManager(LinearLayoutManager(this))
        rv_home_sectorization!!.setAdapter(privateSchoolAdapter)
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}