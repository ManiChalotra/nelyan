package com.nelyanlive.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.nelyanlive.R
import com.nelyanlive.data.viewmodel.AppViewModel
import com.nelyanlive.utils.*
import kotlinx.android.synthetic.main.activity_forgot_password.*
import org.json.JSONObject

class ForgotPasswordActivity : AppCompatActivity(), View.OnClickListener {
    val appViewModel: AppViewModel by lazy {
        ViewModelProvider.AndroidViewModelFactory.getInstance(this.application).create(AppViewModel::class.java)
    }

    private val progressDialog by lazy { ProgressDialog(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        initalize()
        checkMvvmResponse()

    }

    private fun initalize() {
        ivBack.setOnClickListener(this)
        btnSubmit.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.ivBack -> {
                onBackPressed()
            }
            R.id.btnSubmit -> {
                val email = tv_emailForgetPassword.text.toString()
                if (Validation.checkEmail(email, this)) {
                    hitForgetPassowrdApi(email)
                } } } }

    private fun hitForgetPassowrdApi(email: String) {
        if (checkIfHasNetwork(this@ForgotPasswordActivity)) {
            appViewModel.sendForgetPasswordData(security_key, email)
            progressDialog.setProgressDialog()
        }
        else {
            showSnackBar(this@ForgotPasswordActivity, getString(R.string.no_internet_error))
        } }

    private fun checkMvvmResponse() {
        appViewModel.observeForgetPasswordResponse()!!.observe(this, Observer { response ->
            if (response!!.isSuccessful && response.code() == 200) {
                if (response.body() != null) {
                    Log.d("forgetPasswordResponse", "-------------" + Gson().toJson(response.body()))

                    val mResponse = response.body()!!.toString()
                    val jsonObject = JSONObject(mResponse)
                    val message = jsonObject.get("msg").toString()
                    Log.d("dkjhfkjhdh", "-------$message")

                    progressDialog.hidedialog()
                    myCustomToast(getString(R.string.email_has_been_sent))
                    onBackPressed()
                } else {
                    progressDialog.hidedialog()
                    ErrorBodyResponse(response, this, null)
                } }
            else {
                progressDialog.hidedialog()
                ErrorBodyResponse(response, this, null)
            } })

        appViewModel.getException()!!.observe(this, Observer {
            myCustomToast(it)
            progressDialog.hidedialog()
        })

    }
}