package com.nelyan_live.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.meherr.mehar.data.viewmodel.AppViewModel
import com.nelyan_live.R
import com.nelyan_live.utils.*
import kotlinx.android.synthetic.main.activity_contact_us.*
import org.json.JSONObject

class ContactUsActivity : AppCompatActivity(), View.OnClickListener {

    private val appViewModel by lazy { ViewModelProvider.AndroidViewModelFactory.getInstance(this.application).create(AppViewModel::class.java) }
    private var authorization = ""

    override fun onResume() {
        super.onResume()
        authorization = intent?.extras?.getString("authorization").toString()
        Log.d("authhhhkey", "------" + authorization)
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_us)
        initalize()
        checkMvvmResponse()
    }



    private fun initalize() {
        btnSubmit.setOnClickListener(this)
    }



    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.btnSubmit -> {
                val name = et_nameContactUs.text.toString()
                val email = et_emailContactUs.text.toString()
                val comment = et_commentContactUs.text.toString()
                if (Validation.checkName(name, this)) {
                    if (Validation.checkEmail(email, this)) {
                        if(!comment.isEmpty()){
                            hitContactUsApi(name, email, comment)
                        }else{
                            myCustomToast("Please enter comment ")
                        }
                    }
                }
            }
        }
    }



    private fun hitContactUsApi(name: String, email: String, comment: String) {
        appViewModel.sendContatUsData(security_key, authorization, name, email, comment)
        contactUsProgressBar?.showProgressBar()
    }

    private fun checkMvvmResponse() {
        appViewModel.observeContactUsResponse()!!.observe(this, Observer { response ->
            if (response!!.isSuccessful && response.code() == 200) {
                if (response.body() != null) {
                    Log.d("contactusResponse", "---------------" + Gson().toJson(response.body()))
                    val mResponse = response.body().toString()
                    val jsonObject = JSONObject(mResponse)
                    val message = jsonObject.get("msg").toString()
                    myCustomToast(message)
                    onBackPressed()
                }

            } else {
                ErrorBodyResponse(response, this, contactUsProgressBar)
            }
        })

        appViewModel.getException()!!.observe(this, Observer {
            myCustomToast(it)
            contactUsProgressBar?.hideProgressBar()
        })
    }

    /* fun dailogSubmit() {
         dialog = Dialog(mContext!!)
         dialog!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)
         dialog!!.setContentView(R.layout.alert_contact)
         dialog!!.setCancelable(true)
         val btnYes: Button
         btnYes = dialog!!.findViewById(R.id.btnYes)
         btnYes.setOnClickListener { //  mContext.startActivity(new Intent(mContext, HomeActivity.class));
             dialog!!.dismiss()
         }
         dialog!!.show()
     }*/


}