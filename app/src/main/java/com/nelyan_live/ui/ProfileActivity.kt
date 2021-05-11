package com.nelyan_live.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.preferences.core.preferencesKey
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.meherr.mehar.data.viewmodel.AppViewModel
import com.nelyan_live.db.DataStoragePreference
import com.nelyan_live.R
import com.nelyan_live.utils.*
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
    private  var utilization = ""
    private  var dataReady = false
    private  var authkey = ""



    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onResume() {
        super.onResume()

        launch (Dispatchers.Main.immediate){
             authkey = dataStoragePreference?.emitStoredValue(preferencesKey<String>("auth_key"))?.first()
            appViewModel.sendprofileApiData(security_key,authkey)
            profileProgressBar?.showProgressBar()
            imageViewProgressBar?.showProgressBar()
            checkMvvmResponse()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        initalize()
        btnEdit.visibility = View.GONE


        /*  tvEdit!!.setOnClickListener(View.OnClickListener {
              val i = Intent(this@ProfileActivity, EditProfileActivity::class.java)
              startActivity(i)
          })

          tvDelete!!.setOnClickListener(View.OnClickListener { delDialog() })
  */

        /*ivPlus!!.setOnClickListener(View.OnClickListener {
            if (ll_1!!.getVisibility() == View.VISIBLE) {
                // Its visible
                ll_1!!.setVisibility(View.GONE)
            } else {
                // Either gone or invisible
                ll_1!!.setVisibility(View.VISIBLE)
            }
        })*/

    }

   /* fun delDialog() {
       val  dialog = Dialog(this)
        dialog!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog!!.setContentView(R.layout.alert_chat_delete)
        dialog!!.setCancelable(true)
        val rl_1: RelativeLayout
        rl_1 = dialog!!.findViewById(R.id.rl_1)
        rl_1.setOnClickListener { //  mContext.startActivity(new Intent(mContext, HomeActivity.class));
            dialog!!.dismiss()
        }
        dialog!!.show()

    }
*/

    
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
                     utilization = jsonObject.getJSONObject("data").get("utilization").toString()

                   Glide.with(this).asBitmap().load(from_admin_image_base_URl + image).error(R.mipmap.ic_user_place).into(iv_userProfile)
                   tv_usernameProfile.text = name
                   tv_emailProfile.text = email
                   tv_cityProfile.text = city
                   tv_utilizationProfile.text = utilization

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

    private  fun initalize(){
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
                        putString("userUtilization", utilization)
                        putString("authorization", authkey)
                    }
                }else{
                    myCustomToast("Please wait for a while to load all data from server ")
                }

            }
            R.id.ivBack->{
                onBackPressed()
            }

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}