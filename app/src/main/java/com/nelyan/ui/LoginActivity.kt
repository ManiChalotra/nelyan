package com.nelyan.ui

import android.content.Context
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.preferences.core.preferencesKey
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.meherr.mehar.data.viewmodel.AppViewModel
import com.meherr.mehar.db.DataStoragePreference
import com.nelyan.R
import com.nelyan.utils.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.json.JSONObject
import kotlin.coroutines.CoroutineContext

class LoginActivity : AppCompatActivity(), View.OnClickListener, CoroutineScope {

    val appViewModel by lazy { ViewModelProvider.AndroidViewModelFactory.getInstance(this.application).create(AppViewModel::class.java) }
    val dataStoragePreference by lazy { DataStoragePreference(this@LoginActivity) }




    private  var job = Job()
    private  var  clicked= false

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initalize()
        checkMVVMResponse()
    }

    private  fun  initalize(){
        tvForgotPass.setOnClickListener(this)
        tvSignup.setOnClickListener(this)
        btnLogin.setOnClickListener(this)
        iv_tickButton.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.tvForgotPass->{
                OpenActivity(ForgotPasswordActivity::class.java)
            }
            R.id.tvSignup->{
                OpenActivity(SignupActivity::class.java)
            }
            R.id.btnLogin->{
                val email = tv_emailLogin.text.toString()
                val password = tv_passwordLogin.text.toString()
                if(Validation.checkEmail(email, this)){
                    if(Validation.checkPassword(password, this)){
                        if(clicked){
                            hitLoginApi(email, password)
                        }else{
                            myCustomToast("Please  tick the remember me Option for always  Logged in App ")
                        }
                    }
                }

            }
            R.id.iv_tickButton->{
                val test: ImageView = iv_tickButton
                val bmap = (test.drawable as BitmapDrawable).bitmap
                val myDrawable = resources.getDrawable(R.drawable.fill_check)
                val myLogo = (myDrawable as BitmapDrawable).bitmap
                if (bmap.sameAs(myLogo)) {
                    clicked = true
                    iv_tickButton.setImageResource(R.drawable.tick)
                } else {
                    clicked= false
                    iv_tickButton.setImageResource(R.drawable.fill_check)

                }


            }
        }
    }

    private fun hitLoginApi(email: String, password: String) {

        appViewModel.sendLoginData(security_key, device_Type, "112",email,password, "123")
        loginProgressBar?.showProgressBar()

    }

    private  fun checkMVVMResponse(){
        appViewModel!!.observeLOginResponse()!!.observe(this, Observer { response->
            if(response!!.isSuccessful && response.code()==200){
                if(response.body()!= null){
                    Log.d("loginResponse","---------"+ Gson().toJson(response.body()))
                    val  mResponse :String  = response.body().toString()
                    val jsonObject = JSONObject(mResponse)

                  val message =   jsonObject.get("msg").toString()
                          myCustomToast(message)
                    val email = jsonObject.getJSONObject("data").get("email").toString()
                    val password = jsonObject.getJSONObject("data").get("password").toString()
                    val type = jsonObject.getJSONObject("data").get("type").toString()
                    val image = jsonObject.getJSONObject("data").get("image").toString()
                    val phone = jsonObject.getJSONObject("data").get("phone").toString()
                    val authKey = jsonObject.getJSONObject("data").get("authKey").toString()
                    val cityOrZipcode = jsonObject.getJSONObject("data").get("cityOrZipcode").toString()
                    val latitude = jsonObject.getJSONObject("data").get("lat").toString()
                    val longitude = jsonObject.getJSONObject("data").get("lng").toString()

                    launch (Dispatchers.Main.immediate){
                        dataStoragePreference.save(email, preferencesKey<String>("emailLogin"))
                        dataStoragePreference.save(password, preferencesKey<String>("passwordLogin"))
                        dataStoragePreference.save(type, preferencesKey<String>("typeLogin"))
                        dataStoragePreference.save(image, preferencesKey<String>("imageLogin"))
                        dataStoragePreference.save(phone, preferencesKey<String>("phoneLogin"))
                        dataStoragePreference.save(authKey, preferencesKey<String>("auth_key"))
                        dataStoragePreference.save(cityOrZipcode, preferencesKey<String>("cityLogin"))
                        dataStoragePreference.save(latitude, preferencesKey<String>("latitudeLogin"))
                        dataStoragePreference.save(longitude, preferencesKey<String>("longitudeLogin"))

                    }

                    OpenActivity(HomeActivity::class.java){
                        putString("authorization", authKey)
                    }
                    finishAffinity()
                    loginProgressBar?.hideProgressBar()


                }

            }else{
                ErrorBodyResponse(response, this, loginProgressBar)
            }
        })
        appViewModel!!.getException()!!.observe(this, Observer {
            myCustomToast(it)
            loginProgressBar?.hideProgressBar()
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}