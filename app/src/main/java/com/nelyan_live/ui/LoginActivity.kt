package com.nelyan_live.ui

import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.preferences.core.preferencesKey
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.facebook.AccessToken
import com.facebook.FacebookException
import com.facebook.GraphRequest
import com.facebook.HttpMethod
import com.facebook.appevents.AppEventsLogger
import com.facebook.login.LoginManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.gson.Gson
import com.meherr.mehar.data.viewmodel.AppViewModel
import com.meherr.mehar.db.DataStoragePreference
import com.nelyan_live.HELPER.FacebookHelper
import com.nelyan_live.HELPER.FacebookHelper.*
import com.nelyan_live.HELPER.GoogleHelper
import com.nelyan_live.R
import com.nelyan_live.fragments.HomeFragment
import com.nelyan_live.modals.FacebookCustomDataModel
import com.nelyan_live.utils.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject
import kotlin.coroutines.CoroutineContext

class LoginActivity : AppCompatActivity(), View.OnClickListener, CoroutineScope, FacebookHelperCallback {

    val appViewModel by lazy { ViewModelProvider.AndroidViewModelFactory.getInstance(this.application).create(AppViewModel::class.java) }
    val dataStoragePreference by lazy { DataStoragePreference(this@LoginActivity) }

    // for facebook
    var facebookHelper: FacebookHelper? = null
    var isFb = ""
    var socialId = ""
    var socialType = ""
    var socialName = ""
    var socialEmail = ""
    var socialImage = ""
    var facebookCustomDataModel: FacebookCustomDataModel? = null


    // for google gmail
    lateinit var googleHelper: GoogleHelper


    private var job = Job()
    private var clicked = false

    // dialog
    private  val progressDialog = ProgressDialog(this)


    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppEventsLogger.activateApp(getApplication());
        facebookHelper = FacebookHelper(this, this)

        setContentView(R.layout.activity_login)
        initGoogleSignin()
        initalize()
        checkMVVMResponse()


    }

    private fun initGoogleSignin() {

        googleHelper = GoogleHelper(this, object : GoogleHelper.GoogleHelperCallback {

            override fun onSuccessGoogle(account: GoogleSignInAccount) {
                try {
                    Log.i("GoogleHelper", "" + account)
                    var photo = ""
                    if (account.photoUrl != null) {
                        photo = account.photoUrl.toString()
                    }

                   // googleHelper.signOut()
                    try {
                        socialId = account.id!!
                        socialImage = photo
                        socialEmail = account.email!!
                        socialName = account.displayName!!
                        socialType = FOR_GOOGLE_TYPE

                        Log.d("googleDetail","-------------name------"+ socialName)
                        Log.d("googleDetail","-------------email------"+ socialEmail)
                        Log.d("googleDetail","-------------image------"+ socialImage)
                        Log.d("googleDetail","-------------id------"+ socialId)

                        appViewModel!!.sendSocialLoginData(security_key, socialId, FOR_GOOGLE_TYPE)

                    } catch (e: Exception) {
                        Log.d("GoogleException","-----------"+ e.toString())
                    }
                } catch (ex: Exception) {
                    ex.localizedMessage
                    Log.d("gooleException","---------"+ ex.localizedMessage)
                }
            }

            override fun onErrorGoogle() {

                myCustomToast("Cancel Google Login")
                // CommonMethods.failureMethod(mContext,"Cancel google login")


            }
        })

    }

    /* fun initGoogleSignin() {
         val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                 .requestEmail().requestIdToken(OAUTH_GOOGLE_CLIENT_ID).build()
         mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
     }*/

    private fun initalize() {
        tvForgotPass.setOnClickListener(this)
        tvSignup.setOnClickListener(this)
        btnLogin.setOnClickListener(this)
        iv_tickButton.setOnClickListener(this)
        iv_facebookLogin_button.setOnClickListener(this)
        iv_googleLogin_button.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.tvForgotPass -> {
                OpenActivity(ForgotPasswordActivity::class.java)
            }
            R.id.tvSignup -> {
                OpenActivity(SignupActivity::class.java)
            }
            R.id.btnLogin -> {
                val email = tv_emailLogin.text.toString()
                val password = tv_passwordLogin.text.toString()
                if (Validation.checkEmail(email, this)) {
                    if (Validation.checkPassword(password, this)) {
                        if (clicked) {
                            hitLoginApi(email, password)
                        } else {
                            myCustomToast("Please  tick the remember me Option for always  Logged in App ")
                        }
                    }
                }

            }
            R.id.iv_tickButton -> {
                val test: ImageView = iv_tickButton
                val bmap = (test.drawable as BitmapDrawable).bitmap
                val myDrawable = resources.getDrawable(R.drawable.fill_check)
                val myLogo = (myDrawable as BitmapDrawable).bitmap
                if (bmap.sameAs(myLogo)) {
                    clicked = true
                    iv_tickButton.setImageResource(R.drawable.tick)
                } else {
                    clicked = false
                    iv_tickButton.setImageResource(R.drawable.fill_check)

                }


            }
            R.id.iv_facebookLogin_button -> {
                if (checkIfHasNetwork(this@LoginActivity)) {
                    isFb = "fb"
                    facebookHelper!!.login(this)
                } else {
                    showSnackBar(this, "No Internet Connection")
                }
            }


            R.id.iv_googleLogin_button -> {


                if (checkIfHasNetwork(this@LoginActivity)) {
                    isFb="google"
                    googleHelper.signOut()
                    googleHelper.signIn()

                } else {
                    showSnackBar(this, "No Internet Connection")
                }
            }


        }
    }

    /*  private fun getGoogle() {
          val signInIntent = mGoogleSignInClient?.signInIntent
          startActivityForResult(signInIntent, 9001)
      }
  */

    private fun hitLoginApi(email: String, password: String) {

        appViewModel.sendLoginData(security_key, device_Type, "112", email, password, "123")
      progressDialog.setProgressDialog()//  loginProgressBar?.showProgressBar()

    }

    private fun checkMVVMResponse() {
        appViewModel!!.observeLOginResponse()!!.observe(this, Observer { response ->
            if (response!!.isSuccessful && response.code() == 200) {
                if (response.body() != null) {
                    Log.d("loginResponse", "---------" + Gson().toJson(response.body()))
                    val mResponse: String = response.body().toString()
                    val jsonObject = JSONObject(mResponse)

                    val message = jsonObject.get("msg").toString()
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

                    AllSharedPref.save(this, "auth_key", authKey)

                    launch(Dispatchers.Main.immediate) {
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

                    progressDialog.hidedialog()

                    OpenActivity(HomeActivity::class.java) {
                        putString("authorization", authKey)
                    }
                    finishAffinity()
                  //  loginProgressBar?.hideProgressBar()

                }

            } else {
                ErrorBodyResponse(response, this, null)
            }
        })


        // social Login api  response
        appViewModel.observeSocialLoginResponse()!!.observe(this, Observer { response->
            if(response!!.isSuccessful && response.code()==200){
                if(response.body()!= null){
                    progressDialog.hidedialog()

                    OpenActivity(SignupActivity::class.java){
                        putString("socialLogin", "SOCIAL_LOGIN")
                        putString("socialName", socialName)
                        putString("socialEmail", socialEmail)
                        putString("socialImage", socialImage)
                    }
                }
            }else{
                ErrorBodyResponse(response, this, null)
            }
        })




        appViewModel!!.getException()!!.observe(this, Observer {
            myCustomToast(it)
           progressDialog.hidedialog()// loginProgressBar?.hideProgressBar()
        })
    }


    override fun onCancelFacebook() {
        Log.d("onCancel", "-----------------" + "cancel")
    }

    override fun onErrorFacebook(ex: FacebookException?) {
        Log.d("Facebook_exception", "-------------------" + ex)

    }

    override fun onSuccessFacebook(bundle: Bundle?) {
        Log.d("OnSuccess method ", "-----------------------------------" + bundle)
        try {
            val firstName = bundle?.getString(FIRST_NAME)
            val lastName = bundle?.getString(LAST_NAME)
            socialName = firstName + " " + lastName
            socialId = bundle?.getString(FACEBOOK_ID)!!
            if (bundle.getString(EMAIL) != null) {
                socialEmail = bundle.getString(EMAIL)!!
            } else {
                socialEmail = ""
            }
            socialImage = bundle.getString(PROFILE_PIC)!!
            Log.e("socialId", socialId)
            socialType = FOR_FACEBOOK_TYPE

            //--------------hit api here------------------->
            appViewModel!!.sendSocialLoginData(security_key, socialId, FOR_FACEBOOK_TYPE)


          progressDialog.setProgressDialog()//  loginProgressBar?.showProgressBar()
            facebookCustomDataModel =
                    FacebookCustomDataModel(socialName, socialId, socialEmail, socialImage, socialType)
        } catch (e: Exception) {
            e.printStackTrace()
        }


    }


    override fun onResume() {
        super.onResume()



        //---------------for FACEBOOK--------------->
        if (AccessToken.getCurrentAccessToken() == null) {
            return  // already logged out
        }

        GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me/permissions/",
                null,
                HttpMethod.DELETE
        ) {
            LoginManager.getInstance().logOut()
        }.executeAsync()

        try {

            LoginManager.getInstance().logOut();
            facebookHelper!!.logout()
        } catch (e: Exception) {
        }
        //---------------------END---------------------->



    }

    /* private fun signOut() {

         // Google sign out
         mGoogleSignInClient!!.signOut().addOnCompleteListener {
             updateUI(null)
         }

     }*/


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        Log.d("onctivityResult","---rqcode--------"+ requestCode)
        Log.d("onctivityResult","------rlcode-----"+ resultCode)
        Log.d("onctivityResult","-------d----"+ data)

        if (isFb.equals("fb")) {
            facebookHelper!!.onResult(requestCode, resultCode, data)

        }

          /*if (requestCode == 9001) {
              val task = GoogleSignIn.getSignedInAccountFromIntent(data)
              handleSignInResult(task)
          }*/

        if (isFb.equals("google")) {
            googleHelper.onResult(requestCode, resultCode, data)
        }

    }

  /*  private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            updateUI(account)
        } catch (e: ApiException) {
            Log.d("TAG", "signInResult:failed code=" + e.statusCode)
        }
    }*/

    private fun updateUI(account: GoogleSignInAccount?) {

        try {
            Log.d("TAG", "gooogle" + account?.displayName.toString())
            Log.d("TAG", "gooogle" + account?.email)
            if (account != null) {
                val sId = account.id.toString()
                val sfname = account.displayName.toString()
                val semail = account.email.toString()
                //  Toast.makeText(context, "TOKEN :"+SharedPref(context).getTOKEN().toString(), Toast.LENGTH_LONG).show()

                //  Toast.makeText(context, "Success", Toast.LENGTH_LONG).show()

                //--------------hit api here----------------->
                /* sociallogin(semail, sId, "1", RetrofitClient.getClient().social_login(SECURITY_KEY, "1",
                         sId, FirebaseInstanceId.getInstance().getToken().toString(), "1"))*/

            }

        } catch (e: JSONException) {

            e.printStackTrace()
        }
    }




    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}