package com.nelyanlive.ui

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.nelyanlive.R
import com.nelyanlive.data.viewmodel.AppViewModel
import com.nelyanlive.utils.*
import kotlinx.android.synthetic.main.activity_change_password.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import org.json.JSONObject
import kotlin.coroutines.CoroutineContext

class ChangePasswordActivity : AppCompatActivity(), View.OnClickListener, CoroutineScope {

    private val appViewModel by lazy {
        ViewModelProvider.AndroidViewModelFactory.getInstance(this.application).create(AppViewModel::class.java)
    }

    private val job by lazy { Job() }
    private var authKey = ""


    override fun onResume() {
        super.onResume()
        authKey = intent?.extras?.getString("authkey").toString()

    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)
        initalizeClicks()
        checkMvvmResponse()

    }

    private fun initalizeClicks() {
        ivBack.setOnClickListener(this)
        btnSave.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.ivBack -> {
                onBackPressed()
            }
            R.id.btnSave -> {
                val oldPassword = et_oldPassword.text.toString()
                val newPassword = et_newPassword.text.toString()
                val confirmPassword = et_confirmPassword.text.toString()
                if (Validation.checkoldPassword(oldPassword, this)) {
                    if (Validation.checkNewPassword(newPassword, this)) {
                        if (confirmPassword.isNotEmpty()) {
                            if (newPassword == confirmPassword) {
                                hitChangePasswordApi(oldPassword, newPassword)
                            }
                            else {
                                myCustomToast(getString(R.string.password_donot_match_error))
                            }
                        } else {
                            myCustomToast(getString(R.string.confirm_password_missing_erro))
                        }
                    }
                }
            }

        }
    }

    private fun hitChangePasswordApi(oldPassword: String, newPassword: String) {
        appViewModel.sendChangePasswordData(security_key, authKey, oldPassword, newPassword)
        changepasswordProgressBar?.showProgressBar()

    }

    private fun checkMvvmResponse() {
        appViewModel.observeChangePasswordResponse()!!.observe(this, Observer { response ->
            if (response!!.isSuccessful && response.code() == 200) {
                if(response.body()!= null){
                    val mResponse= response.body().toString()
                    val jsonObject = JSONObject(mResponse)
                    val message = jsonObject.get("msg").toString()
                    myCustomToast(message)
                    showDailog()
                }

            } else {
                ErrorBodyResponse(response, this, changepasswordProgressBar)

            }
        })
        appViewModel.getException()!!.observe(this, Observer {
            myCustomToast(it)
            changepasswordProgressBar?.hideProgressBar()
        })
    }

    private fun showDailog() {
        val dialog = Dialog(this@ChangePasswordActivity)
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.setContentView(R.layout.alert_change_pass)
        dialog.setCanceledOnTouchOutside(false)
        dialog.setCancelable(false)
        val btnOk: Button = dialog.findViewById(R.id.btn_sectorization_ok)
        btnOk.setOnClickListener {
            finish()
            dialog.dismiss()
        }
        dialog.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

}