package com.nelyan.ui

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.preferences.core.preferencesKey
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.meherr.mehar.data.viewmodel.AppViewModel
import com.meherr.mehar.db.DataStoragePreference
import com.nelyan.R
import com.nelyan.utils.ErrorBodyResponse
import com.nelyan.utils.OpenActivity
import com.nelyan.utils.myCustomToast
import com.nelyan.utils.security_key
import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.json.JSONObject
import kotlin.coroutines.CoroutineContext

class SettingsActivity : AppCompatActivity(), CoroutineScope , View.OnClickListener{

    private val appViewModel by lazy { ViewModelProvider.AndroidViewModelFactory.getInstance(this.application).create(AppViewModel::class.java) }
    private val job = Job()

    private val dataStoragePreference by lazy { DataStoragePreference(this@SettingsActivity) }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private var noti_status = "1"

    private var authKey = ""
    private  var aboutUs_data = ""
    private  var termsConditipon_data = ""
    private  var privacyPolicy_data =""

    override fun onResume() {
        super.onResume()
        authKey = intent?.extras?.getString("authorization").toString()

        launch(Dispatchers.Main.immediate) {
            val status = dataStoragePreference.emitStoredValue(preferencesKey<String>("noti_status"))?.first()
            if(!status.isNullOrEmpty()){
                iv_notification_switch.isChecked = status.equals("1")
            }

        }

        launch(Dispatchers.Main.immediate) {
            // for aboutUs
            appViewModel.sendGetContentApiData(security_key, authKey, "1")

        }
        launch(Dispatchers.Main.immediate) {

            // for terms condition
            appViewModel.sendGetContentApiData(security_key, authKey, "2")

        }
        launch(Dispatchers.Main.immediate) {

            // for privacy policy
            appViewModel.sendGetContentApiData(security_key, authKey, "3")
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        initalizeClick()
        checkMvvmResponse()
    }

    private fun hitNotificationStatusApi(notiStatus: String) {
        appViewModel.sendNotificationEnableData(security_key, authKey, notiStatus)
    }

    private  fun initalizeClick(){
        iv_notification_switch.setOnClickListener(this)
        ivBack.setOnClickListener(this)
        llLogout.setOnClickListener(this)
        llTerms.setOnClickListener(this)
        llChange.setOnClickListener(this)
        llContact.setOnClickListener(this)
        llAbout.setOnClickListener(this)
        llPrivacy.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v!!.id){

            R.id.iv_notification_switch->{
                if (iv_notification_switch.isChecked) {
                    noti_status = "1"
                    hitNotificationStatusApi(noti_status)
                } else {
                    noti_status = "2"
                    hitNotificationStatusApi(noti_status)
                }
            }

            R.id.ivBack->{
                onBackPressed()
            }
            R.id.llLogout->{
                showDailog()
            }
            R.id.llTerms->{
                OpenActivity(TermsActivity::class.java){
                    putString("cmsData", termsConditipon_data)
                }
            }
            R.id.llChange->{
                OpenActivity(ChangePasswordActivity::class.java){
                    putString("authkey", authKey)
                }
            }
            R.id.llContact->{
                OpenActivity(ContactUsActivity::class.java){
                    putString("authkey", authKey)
                }
            }
            R.id.llAbout->{
                OpenActivity(AboutActivity::class.java){
                    putString("cmsData", aboutUs_data)
                }
            }
            R.id.llPrivacy->{
                OpenActivity(PrivacyActivity::class.java){
                    putString("cmsData", privacyPolicy_data)
                }
            }

        }
    }

    private fun checkMvvmResponse() {

        appViewModel.observeNotificationEnableResponse()!!.observe(this, Observer { response ->
            if (response!!.isSuccessful && response.code() == 200) {
                if (response.body() != null) {

                    launch {
                        dataStoragePreference.save(noti_status, preferencesKey<String>("noti_status"))
                    }
                }
            } else {
                ErrorBodyResponse(response, this@SettingsActivity, null)
            }
        })

        // for aboutUs
        appViewModel.observeAboutUsResponse()!!.observe(this, Observer { response->
            if(response!!.isSuccessful && response.code()==200){
                if(response.body()!= null){
                    Log.d("getContent_aboutus", "---------"+ Gson().toJson(response.body()))
                    val mResponse=  response.body().toString()
                    val jsonObject = JSONObject(mResponse)
                 aboutUs_data   = jsonObject.getJSONObject("data").get("content").toString()
                }
            }else{
                ErrorBodyResponse(response, this, null)
            }
        })


        // for terms conditipon
        appViewModel.observeTermsConditionResponse()!!.observe(this, Observer { response->
            if(response!!.isSuccessful && response.code()==200){
                if(response.body()!= null){
                    Log.d("getContent_terms", "---------"+ Gson().toJson(response.body()))

                    val mResponse=  response.body().toString()
                    val jsonObject = JSONObject(mResponse)
                   termsConditipon_data  = jsonObject.getJSONObject("data").get("content").toString()
                }
            }else{
                ErrorBodyResponse(response, this, null)
            }
        })



        // for  privacy policy
        appViewModel.observePrivacyPolicyResponse()!!.observe(this, Observer { response->
            if(response!!.isSuccessful && response.code()==200){
                if(response.body()!= null){
                    Log.d("getContent_privacy", "---------"+ Gson().toJson(response.body()))

                    val mResponse=  response.body().toString()
                    val jsonObject = JSONObject(mResponse)
                     privacyPolicy_data = jsonObject.getJSONObject("data").get("content").toString()
                }
            }else{
                ErrorBodyResponse(response, this, null)
            }
        })

        appViewModel.getException()!!.observe(this, Observer {
            myCustomToast(it)
        })

    }

    fun showDailog() {

       val  dialog = Dialog(this!!)
        dialog!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog!!.setContentView(R.layout.alert_logout)
        dialog!!.setCancelable(true)
        val rlYes: RelativeLayout
        val rlNo: RelativeLayout
        rlYes = dialog!!.findViewById(R.id.rlYes)
        rlYes.setOnClickListener {
            val i = Intent(this, LoginActivity::class.java)
            i.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(i)
            dialog!!.dismiss()
        }
        rlNo = dialog!!.findViewById(R.id.rlNo)
        rlNo.setOnClickListener { dialog!!.dismiss() }
        dialog!!.show()

    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

}