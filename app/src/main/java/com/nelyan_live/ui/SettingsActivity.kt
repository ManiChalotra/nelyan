package com.nelyan_live.ui

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.CompoundButton
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.preferences.core.preferencesKey
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.nelyan_live.data.viewmodel.AppViewModel
import com.nelyan_live.R
import com.nelyan_live.db.DataStoragePreference
import com.nelyan_live.utils.*
import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.json.JSONObject
import kotlin.coroutines.CoroutineContext


class SettingsActivity : AppCompatActivity(), CoroutineScope, View.OnClickListener {

    private val appViewModel by lazy { ViewModelProvider.AndroidViewModelFactory.getInstance(this.application).create(AppViewModel::class.java) }
    private val job = Job()

    private val dataStoragePreference by lazy { DataStoragePreference(this@SettingsActivity) }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    // dialog
    private val progressDialog = ProgressDialog(this)

    private var noti_status = ""
    private var user_type = ""

    private var authKey = ""
    private var aboutUs_data = ""
    private var termsConditipon_data = ""
    private var privacyPolicy_data = ""
    var dialog: Dialog? = null

    override fun onResume() {
        super.onResume()
        authKey = intent?.extras?.getString("authorization").toString()

/*
        launch(Dispatchers.Main.immediate) {
            val status = dataStoragePreference.emitStoredValue(preferencesKey<String>("noti_status"))?.first()
            if(!status.isNullOrEmpty()){
                iv_notification_switch.isChecked = status.equals("1")
            }

        }
*/

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
        if (checkIfHasNetwork(this@SettingsActivity)) {
            appViewModel.sendNotificationEnableData(security_key, authKey, notiStatus)
            settingsProgressBar?.showProgressBar()
        }
    }

    private fun hitUserTypeApi(userType: String) {
        if (checkIfHasNetwork(this@SettingsActivity)) {
            appViewModel.sendSwitchAccountData(security_key, authKey, userType)
            settingsProgressBar?.showProgressBar()
        }
    }

    private fun initalizeClick() {
        // iv_notification_switch.setOnClickListener(this)
        ivBack.setOnClickListener(this)
        llLogout.setOnClickListener(this)
        llTerms.setOnClickListener(this)
        llChange.setOnClickListener(this)
        llContact.setOnClickListener(this)
        llAbout.setOnClickListener(this)
        llPrivacy.setOnClickListener(this)

        launch(Dispatchers.Main.immediate) {
            noti_status = dataStoragePreference.emitStoredValue(preferencesKey<String>("notificationStatusLogin")).first()
            user_type = dataStoragePreference.emitStoredValue(preferencesKey<String>("typeLogin")).first()

            if (noti_status != null) {
                if (noti_status.equals("1")) {
                    iv_notification_switch.isChecked = true
                } else {
                    iv_notification_switch.isChecked = false
                }
            }

            if (user_type != null) {
                if (user_type.equals("1")) {
                    tv_user_type.text = getString(R.string.switch_as_a_professional)
                    iv_professional_switch.isChecked = true
                } else {
                    tv_user_type.text = getString(R.string.switch_as_a_consultant)
                    iv_professional_switch.isChecked = false
                }

            }


            iv_professional_switch.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
                if (user_type.equals("1")) {
                    user_type = "2"
                    hitUserTypeApi(user_type)
                } else {
                    user_type = "1"
                    hitUserTypeApi(user_type)
                }

            })


            iv_notification_switch.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    !isChecked
                    noti_status = "1"
                    hitNotificationStatusApi(noti_status)
                } else {
                    isChecked
                    noti_status = "0"
                    hitNotificationStatusApi(noti_status)
                }

            })

        }

    }

    override fun onClick(v: View?) {
        when (v!!.id) {
/*
            R.id.iv_notification_switch->{
                if (iv_notification_switch.isChecked) {
                    noti_status = "1"
                    hitNotificationStatusApi(noti_status)
                } else {
                    noti_status = "2"
                    hitNotificationStatusApi(noti_status)
                }
            }
*/

            R.id.ivBack -> {
                onBackPressed()
            }

            R.id.llLogout -> {
                showDailog()
            }

            R.id.llTerms -> {
                OpenActivity(TermsActivity::class.java) {
                    putString("cmsData", termsConditipon_data)
                }
            }

            R.id.llChange -> {
                OpenActivity(ChangePasswordActivity::class.java) {
                    putString("authkey", authKey)
                }
            }

            R.id.llContact -> {
                OpenActivity(ContactUsActivity::class.java) {
                    putString("authorization", authKey)
                }
            }

            R.id.llAbout -> {
                OpenActivity(AboutActivity::class.java) {
                    putString("cmsData", aboutUs_data)
                }
            }

            R.id.llPrivacy -> {
                OpenActivity(PrivacyActivity::class.java) {
                    putString("cmsData", privacyPolicy_data)
                }
            }

        }
    }

    private fun checkMvvmResponse() {
        appViewModel.observeNotificationEnableResponse()!!.observe(this, Observer { response ->
            if (response!!.isSuccessful && response.code() == 200) {
                if (response.body() != null) {
                    settingsProgressBar?.hideProgressBar()
                    launch {
                        dataStoragePreference.save(noti_status, preferencesKey<String>("notificationStatusLogin"))
                    }
                }
            } else {
                ErrorBodyResponse(response, this@SettingsActivity, null)
            }
        })
        appViewModel.observeSwitchUserResponse()!!.observe(this, Observer { response ->
            if (response!!.isSuccessful && response.code() == 200) {
                if (response.body() != null) {
                    settingsProgressBar?.hideProgressBar()
                    launch {
                        dataStoragePreference.save(user_type, preferencesKey<String>("typeLogin"))
                    }

                    if (user_type != null) {
                        if (user_type.equals("1"))
                            tv_user_type.text = getString(R.string.switch_as_a_professional)
                        else
                            tv_user_type.text = getString(R.string.switch_as_a_consultant)
                    }


                }
            } else {
                ErrorBodyResponse(response, this@SettingsActivity, null)
            }
        })

        // for aboutUs
        appViewModel.observeAboutUsResponse()!!.observe(this, Observer { response ->
            if (response!!.isSuccessful && response.code() == 200) {
                if (response.body() != null) {
                    Log.d("getContent_aboutus", "---------" + Gson().toJson(response.body()))
                    val mResponse = response.body().toString()
                    val jsonObject = JSONObject(mResponse)
                    aboutUs_data = jsonObject.getJSONObject("data").get("content").toString()
                }
            } else {
                ErrorBodyResponse(response, this, null)
            }
        })


        // for terms conditipon
        appViewModel.observeTermsConditionResponse()!!.observe(this, Observer { response ->
            if (response!!.isSuccessful && response.code() == 200) {
                if (response.body() != null) {
                    Log.d("getContent_terms", "---------" + Gson().toJson(response.body()))

                    val mResponse = response.body().toString()
                    val jsonObject = JSONObject(mResponse)
                    termsConditipon_data = jsonObject.getJSONObject("data").get("content").toString()
                }
            } else {
                ErrorBodyResponse(response, this, null)
            }
        })

        //Logout API
        appViewModel.observeLogoutResponse()!!.observe(this, Observer { response ->
            if (response!!.isSuccessful && response.code() == 200) {
                if (response.body() != null) {
                    settingsProgressBar?.hideProgressBar()
                    val mResponse = response.body().toString()
                    val jsonObject = JSONObject(mResponse)
                    Log.d("logoutResponse", "---------" + Gson().toJson(response.body()))
                    val message = jsonObject.get("msg").toString()
                    myCustomToast(message)

                    launch(Dispatchers.Main.immediate) {
                        val i = Intent(this@SettingsActivity, LoginActivity::class.java)
                        i.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(i)
                        dialog!!.dismiss()
                        dataStoragePreference.deleteDataBase()
                    }

                }
            } else {
                ErrorBodyResponse(response, this, settingsProgressBar)
            }
        })


        // for  privacy policy
        appViewModel.observePrivacyPolicyResponse()!!.observe(this, Observer { response ->
            if (response!!.isSuccessful && response.code() == 200) {
                if (response.body() != null) {
                    Log.d("getContent_privacy", "---------" + Gson().toJson(response.body()))

                    val mResponse = response.body().toString()
                    val jsonObject = JSONObject(mResponse)
                    privacyPolicy_data = jsonObject.getJSONObject("data").get("content").toString()
                }
            } else {
                ErrorBodyResponse(response, this, null)
            }
        })

        appViewModel.getException()!!.observe(this, Observer {
            myCustomToast(it)
        })

    }

    private fun hitLogoutApi() {
        appViewModel.sendLogoutData(security_key, authKey)
        settingsProgressBar?.showProgressBar()
    }

    fun showDailog() {
        dialog = Dialog(this!!)
        dialog!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog!!.setContentView(R.layout.alert_logout)
        dialog!!.setCancelable(true)
        val rlYes: RelativeLayout
        val rlNo: RelativeLayout
        rlYes = dialog!!.findViewById(R.id.rlYes)
        rlYes.setOnClickListener {

            hitLogoutApi()

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