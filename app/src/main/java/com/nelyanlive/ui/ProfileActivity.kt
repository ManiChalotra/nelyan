package com.nelyanlive.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.preferences.core.preferencesKey
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.nelyanlive.R
import com.nelyanlive.data.viewmodel.AppViewModel
import com.nelyanlive.db.DataStoragePreference
import com.nelyanlive.utils.*
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.json.JSONObject
import kotlin.coroutines.CoroutineContext

class ProfileActivity : AppCompatActivity(), View.OnClickListener, CoroutineScope{

    private  val job by lazy {
        Job()
    }
    private  val appViewModel by  lazy {
        ViewModelProvider.AndroidViewModelFactory.getInstance(this.application).create(AppViewModel::class.java)
    }
    private  val dataStoragePreference by lazy { DataStoragePreference(this@ProfileActivity) }

    private  var image = ""
    private  var name = ""
    private  var email = ""
    private  var city = ""
    private  var dataReady = false
    private  var authKey = ""



    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onResume() {
        super.onResume()

        launch (Dispatchers.Main.immediate){
             authKey = dataStoragePreference.emitStoredValue(preferencesKey<String>("auth_key")).first()
            appViewModel.sendprofileApiData(security_key,authKey)
            profileProgressBar?.showProgressBar()
            imageViewProgressBar?.showProgressBar()
            checkMvvmResponse()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        initialize()
        btnEdit.visibility = View.GONE

    }
    private  fun checkMvvmResponse(){
       appViewModel.observeProfileApiResponse()!!.observe(this, Observer { response->

           if(response!!.isSuccessful && response.code()==200){
               if(response.body()!= null){
                   Log.d("ProfileResponseBody","----------"+ Gson().toJson(response.body()))
                   val mResponse = response.body().toString()
                   val jsonObject = JSONObject(mResponse)
                   image = jsonObject.getJSONObject("data").get("image").toString()
                   name = jsonObject.getJSONObject("data").get("name").toString()
                    email = jsonObject.getJSONObject("data").get("email").toString()
                    city = jsonObject.getJSONObject("data").get("cityOrZipcode").toString()

                   Glide.with(this).asBitmap().load(from_admin_image_base_URl + image).error(R.mipmap.ic_user_place).into(iv_userProfile)
                   tv_usernameProfile.text = name
                   tv_emailProfile.text = email
                   tv_cityProfile.text = city

                   profileProgressBar.hideProgressBar()
                   imageViewProgressBar.hideProgressBar()
                   dataReady = true
                   btnEdit.visibility = View.VISIBLE

               }

           }else{
               ErrorBodyResponse(response, this, profileProgressBar)
           }
       })
       appViewModel.getException()!!.observe(this, Observer {
           myCustomToast(it)
           profileProgressBar?.hideProgressBar()
       })
   }

    private  fun initialize(){
        btnEdit.setOnClickListener(this)
        ivBack.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.btnEdit->{
                if(dataReady){
                    OpenActivity(EditProfileActivity::class.java){
                        putString("userImage", image)
                        putString("userName", name)
                        putString("userEmail", email)
                        putString("userCity", city)
                        putString("authorization", authKey)
                    }
                }else{
                    myCustomToast(getString(R.string.wait_a_while_to_load_all_data))
                }
            }
            R.id.ivBack->{
                onBackPressed()
            }

        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        OpenActivity(HomeActivity::class.java)
        finishAffinity()
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}