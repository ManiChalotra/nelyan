package com.nelyanlive.ui

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.text.Editable
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
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.gson.Gson
import com.nelyanlive.BuildConfig
import com.nelyanlive.HELPER.FacebookHelper
import com.nelyanlive.HELPER.FacebookHelper.*
import com.nelyanlive.HELPER.GoogleHelper
import com.nelyanlive.R
import com.nelyanlive.data.viewmodel.AppViewModel
import com.nelyanlive.db.DataStoragePreference
import com.nelyanlive.modals.FacebookCustomDataModel
import com.nelyanlive.utils.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import kotlin.coroutines.CoroutineContext

class LoginActivity : AppCompatActivity(), View.OnClickListener, CoroutineScope,
    FacebookHelperCallback {

    val appViewModel by lazy {
        ViewModelProvider.AndroidViewModelFactory.getInstance(this.application)
            .create(AppViewModel::class.java)
    }
    val dataStoragePreference by lazy { DataStoragePreference(this) }

    var facebookHelper: FacebookHelper? = null
    var isFb = ""
    var socialId = ""
    var socialType = ""
    var socialName = ""
    var socialEmail = ""
    var socialImage = ""
    var facebookCustomDataModel: FacebookCustomDataModel? = null
    private var deviceToken: String? = null
    private var savedUser: String = ""
    private var savedPassword: String = ""

    lateinit var googleHelper: GoogleHelper

    private var job = Job()
    private var clicked = false

    private val progressDialog = ProgressDialog(this)

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private var GOOGLE_SIGN_IN: Int = 2
    lateinit var mGoogleSignInClient: GoogleSignInClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppEventsLogger.activateApp(application)
        facebookHelper = FacebookHelper(this, this)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.AUTH_ID))
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)



        setContentView(R.layout.activity_login)
        initGoogleSignin()
        initalize()
        checkMVVMResponse()
        getSig(this, "SHA")
    }

    fun getSig(context: Context, key: String) {
        try {
            val info = context.packageManager.getPackageInfo(
                BuildConfig.APPLICATION_ID,
                PackageManager.GET_SIGNATURES
            )
            for (signature in info.signatures) {
                val md = MessageDigest.getInstance(key)
                md.update(signature.toByteArray())
                val digest = md.digest()
                val toRet = StringBuilder()
                for (i in digest.indices) {
                    if (i != 0) toRet.append(":")
                    val b = digest[i].toInt() and 0xff
                    val hex = Integer.toHexString(b)
                    if (hex.length == 1) toRet.append("0")
                    toRet.append(hex)
                }
                val s = toRet.toString()
                Log.d("AppConstants_GLOBAL_LOGTAG====", s)

            }
        } catch (e1: PackageManager.NameNotFoundException) {
            Log.e("name not found", e1.toString())
        } catch (e: NoSuchAlgorithmException) {
            Log.e("no such an algorithm", e.toString())
        } catch (e: Exception) {
            Log.e("exception", e.toString())
        }
    }

    private fun initalize() {
        tvForgotPass.setOnClickListener(this)
        tvSignup.setOnClickListener(this)
        btnLogin.setOnClickListener(this)
        iv_tickButton.setOnClickListener(this)
        ll_facebook_login.setOnClickListener(this)
        ll_google_login.setOnClickListener(this)

        launch(Dispatchers.Main.immediate) {
            deviceToken =
                dataStoragePreference.emitStoredFCMValue(preferencesKey<String>("device_token"))
                    .first()
            savedUser =
                dataStoragePreference.emitStoredFCMValue(preferencesKey<String>("userName")).first()
            savedPassword =
                dataStoragePreference.emitStoredFCMValue(preferencesKey<String>("password")).first()

            if (savedUser != "null" && savedUser != null)
                tv_emailLogin.text = Editable.Factory.getInstance().newEditable(savedUser)
            if (savedPassword != "null" && savedPassword != null) tv_passwordLogin.text =
                Editable.Factory.getInstance().newEditable(savedPassword)
        }
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.tvForgotPass -> {
                OpenActivity(ForgotPasswordActivity::class.java)
            }
            R.id.tvSignup -> {
                OpenActivity(SignupActivity::class.java) {
                    putString("socialLogin", "")
                    putString("socialName", "")
                    putString("socialEmail", "")
                    putString("socialImage", "")
                    putString("socialId", "")
                }
            }
            R.id.btnLogin -> {
                val email = tv_emailLogin.text.toString()
                val password = tv_passwordLogin.text.toString()
                if (Validation.checkEmail(email, this)) {
                    if (Validation.checkEmptyPassword(password, this)) {

                        val dataStoragePreference =
                            DataStoragePreference(AppController.getInstance())
                        launch {
                            dataStoragePreference.saveFCM(
                                if (clicked) {
                                    email
                                } else {
                                    ""
                                },
                                preferencesKey("userName")
                            )
                            dataStoragePreference.saveFCM(
                                if (clicked) {
                                    password
                                } else {
                                    ""
                                },
                                preferencesKey("password")
                            )

                        }
                        hitLoginApi(email, password)
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
            R.id.ll_facebook_login -> {
                if (checkIfHasNetwork(this@LoginActivity)) {
                    isFb = "fb"
                    facebookHelper!!.login(this)
                } else {
                    showSnackBar(this, getString(R.string.no_internet_error))
                }
            }

            R.id.ll_google_login -> {
                if (checkIfHasNetwork(this@LoginActivity)) {
                    isFb = "google"

                    val signInIntent = mGoogleSignInClient.signInIntent
                    startActivityForResult(signInIntent, GOOGLE_SIGN_IN)

                } else {
                    showSnackBar(this, getString(R.string.no_internet_error))
                }
            }
        }
    }

    private fun hitLoginApi(email: String, password: String) {

        val tsLong = System.currentTimeMillis() / 1000
        val currentTS = tsLong.toString()
        Log.e("deviceToken", "=====$deviceToken=====")

        if (checkIfHasNetwork(this@LoginActivity)) {
            appViewModel.sendLoginData(
                security_key,
                device_Type,
                deviceToken,
                email,
                password,
                currentTS
            )
            progressDialog.setProgressDialog()
        } else {
            showSnackBar(this@LoginActivity, getString(R.string.no_internet_error))
        }
    }

    private fun checkMVVMResponse() {
        appViewModel.observeLOginResponse()!!.observe(this, Observer { response ->
            if (response!!.isSuccessful && response.code() == 200) {
                if (response.body() != null) {
                    Log.d("" + "", "---------" + Gson().toJson(response.body()))
                    val mResponse: String = response.body().toString()
                    val jsonObject = JSONObject(mResponse)
                    val message = jsonObject.get("msg").toString()
                    myCustomToast(getString(R.string.login_successful))

                    val id = jsonObject.getJSONObject("data").get("id").toString()
                    val email = jsonObject.getJSONObject("data").get("email").toString()
                    val name = jsonObject.getJSONObject("data").get("name").toString()
                    val password = jsonObject.getJSONObject("data").get("password").toString()
                    val type = jsonObject.getJSONObject("data").get("type").toString()
                    val notificationStatus =
                        jsonObject.getJSONObject("data").get("notificationStatus").toString()
                    val image = jsonObject.getJSONObject("data").get("image").toString()
                    val phone = jsonObject.getJSONObject("data").get("phone").toString()
                    val authKey = jsonObject.getJSONObject("data").get("authKey").toString()
                    val cityOrZipcode =
                        jsonObject.getJSONObject("data").get("cityOrZipcode").toString()
                    val latitude = jsonObject.getJSONObject("data").get("lat").toString()
                    val longitude = jsonObject.getJSONObject("data").get("lng").toString()
                    val EventPush = jsonObject.getJSONObject("data").get("eventPush").toString()

                    Log.d(LoginActivity::class.java.name, "LoginActivity_EventPush    " + EventPush)

                    AllSharedPref.save(this, "auth_key", authKey)
                    AllSharedPref.save(this, "EventPush", EventPush)

                    launch(Dispatchers.IO) {

                        dataStoragePreference.save(id, preferencesKey("id"))
                        dataStoragePreference.save(email, preferencesKey("emailLogin"))
                        dataStoragePreference.save(name, preferencesKey("nameLogin"))
                        dataStoragePreference.save(password, preferencesKey("passwordLogin"))
                        dataStoragePreference.save(type, preferencesKey("typeLogin"))
                        dataStoragePreference.save(
                            notificationStatus,
                            preferencesKey("notificationStatusLogin")
                        )
                        dataStoragePreference.save(image, preferencesKey("imageLogin"))
                        dataStoragePreference.save(phone, preferencesKey("phoneLogin"))
                        dataStoragePreference.save(authKey, preferencesKey("auth_key"))
                        dataStoragePreference.save(cityOrZipcode, preferencesKey("cityLogin"))
                        dataStoragePreference.save(latitude, preferencesKey("latitudeLogin"))
                        dataStoragePreference.save(longitude, preferencesKey("longitudeLogin"))

                        Log.d(
                            "savedValues", "----------" +
                                    "\n\n" + "---email---" + email +
                                    "\n\n" + "---name---" + name +
                                    "\n\n" + "---password---" + password +
                                    "\n\n" + "---type---" + type +
                                    "\n\n" + "---image---" + image +
                                    "\n\n" + "---phone---" + phone +
                                    "\n\n" + "---authkey---" + authKey +
                                    "\n\n" + "---cityZipCode---" + cityOrZipcode +
                                    "\n\n" + "---latitude---" + latitude +
                                    "\n\n" + "---longitude---" + longitude
                        )
                    }

                    progressDialog.hidedialog()

                    OpenActivity(HomeActivity::class.java) {
                        putString("authorization", authKey)
                    }
                    finishAffinity()
                }

            } else {
                ErrorBodyResponse(response, this, null)
                progressDialog.hidedialog()

            }
        })

        appViewModel.observeSocialLoginResponse()!!.observe(this, Observer { response ->
            if (response!!.isSuccessful && response.code() == 200) {
                if (response.body() != null) {
                    progressDialog.hidedialog()

                    val mResponse: String = response.body().toString()
                    val jsonObject = JSONObject(mResponse)
                    val message = jsonObject.get("msg").toString()
                    val isAlready = jsonObject.getJSONObject("data").get("isAlready").toString()

                    if (isAlready == "0") {
                        OpenActivity(SignupActivity::class.java) {
                            putString("socialLogin", "SOCIAL_LOGIN")
                            putString("socialName", socialName)
                            putString("socialEmail", socialEmail)
                            putString("socialImage", socialImage)
                            putString("socialId", socialId)
                        }

                    } else {
                        myCustomToast(message)

                        val id = jsonObject.getJSONObject("data").get("id").toString()
                        val email = jsonObject.getJSONObject("data").get("email").toString()
                        val name = jsonObject.getJSONObject("data").get("name").toString()
                        val password = jsonObject.getJSONObject("data").get("password").toString()
                        val type = jsonObject.getJSONObject("data").get("type").toString()
                        val notificationStatus =
                            jsonObject.getJSONObject("data").get("notificationStatus").toString()
                        val image = jsonObject.getJSONObject("data").get("image").toString()
                        val phone = jsonObject.getJSONObject("data").get("phone").toString()
                        val authKey = jsonObject.getJSONObject("data").get("authKey").toString()
                        val cityOrZipcode =
                            jsonObject.getJSONObject("data").get("cityOrZipcode").toString()
                        val latitude = jsonObject.getJSONObject("data").get("lat").toString()
                        val longitude = jsonObject.getJSONObject("data").get("lng").toString()

                        AllSharedPref.save(this, "auth_key", authKey)

                        launch(Dispatchers.Main.immediate) {
                            dataStoragePreference.save(id, preferencesKey("id"))
                            dataStoragePreference.save(email, preferencesKey("emailLogin"))
                            dataStoragePreference.save(name, preferencesKey("nameLogin"))
                            dataStoragePreference.save(password, preferencesKey("passwordLogin"))
                            dataStoragePreference.save(type, preferencesKey("typeLogin"))
                            dataStoragePreference.save(
                                notificationStatus,
                                preferencesKey("notificationStatusLogin")
                            )
                            dataStoragePreference.save(image, preferencesKey("imageLogin"))
                            dataStoragePreference.save(phone, preferencesKey("phoneLogin"))
                            dataStoragePreference.save(authKey, preferencesKey("auth_key"))
                            dataStoragePreference.save(cityOrZipcode, preferencesKey("cityLogin"))
                            dataStoragePreference.save(latitude, preferencesKey("latitudeLogin"))
                            dataStoragePreference.save(longitude, preferencesKey("longitudeLogin"))
                        }

                        OpenActivity(HomeActivity::class.java) {
                            putString("authorization", authKey)
                        }
                        finishAffinity()

                    }
                }
            } else {
                ErrorBodyResponse(response, this, null)
                progressDialog.hidedialog()
            }
        })

        appViewModel.getException()!!.observe(this, Observer {
            myCustomToast(it)
            progressDialog.hidedialog()
        })
    }

    override fun onCancelFacebook() {
        Log.d("onCancel", "-----------------" + "cancel")
    }

    override fun onErrorFacebook(ex: FacebookException?) {
        Log.d("Facebook_exception", "-------------------$ex")

    }

    override fun onSuccessFacebook(bundle: Bundle?) {
        Log.d("OnSuccess method ", "-----------------------------------$bundle")
        try {
            val firstName = bundle?.getString(FIRST_NAME)
            val lastName = bundle?.getString(LAST_NAME)
            socialName = "$firstName $lastName"
            socialId = bundle?.getString(FACEBOOK_ID)!!
            socialEmail = if (bundle.getString(EMAIL) != null) {
                bundle.getString(EMAIL)!!
            } else {
                ""
            }
            socialImage = bundle.getString(PROFILE_PIC)!!
            Log.e("socialId", socialId)
            socialType = FOR_FACEBOOK_TYPE

            appViewModel.sendSocialLoginData(
                security_key,
                device_Type,
                deviceToken!!,
                socialId,
                FOR_FACEBOOK_TYPE
            )


            progressDialog.setProgressDialog()
            facebookCustomDataModel =
                FacebookCustomDataModel(socialName, socialId, socialEmail, socialImage, socialType)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }


    override fun onResume() {
        super.onResume()

        if (AccessToken.getCurrentAccessToken() == null) {
            return
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
            LoginManager.getInstance().logOut()
            facebookHelper!!.logout()
        } catch (e: Exception) {
        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (isFb == "fb") {
            facebookHelper!!.onResult(requestCode, resultCode, data)
        }

        if (isFb == "google") {

            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)


        }

    }

    private fun initGoogleSignin() {

        googleHelper = GoogleHelper(this, object : GoogleHelper.GoogleHelperCallback {

            override fun onSuccessGoogle(account: GoogleSignInAccount) {
                try {
                    var photo = ""
                    if (account.photoUrl != null) {
                        photo = account.photoUrl.toString()
                    }
                    try {
                        socialId = account.id!!
                        socialImage = photo
                        socialEmail = account.email!!
                        socialName = account.displayName!!
                        socialType = FOR_GOOGLE_TYPE

                        appViewModel.sendSocialLoginData(
                            security_key,
                            device_Type,
                            deviceToken!!,
                            socialId,
                            FOR_GOOGLE_TYPE
                        )

                    } catch (e: Exception) {
                    }
                } catch (ex: Exception) {
                    ex.localizedMessage
                }
            }

            override fun onErrorGoogle() {
                // myCustomToast("Cancel Google Login")
            }
        })

    }


    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            mGoogleSignInClient.signOut()
            var photo = ""
            if (account.photoUrl != null) {
                photo = account.photoUrl.toString()
            }
            try {
                socialId = account.id!!
                socialImage = photo
                socialEmail = account.email!!
                socialName = account.displayName!!
                //  socialType = FOR_GOOGLE_TYPE

                appViewModel.sendSocialLoginData(
                    security_key,
                    device_Type,
                    deviceToken!!,
                    socialId,
                    FOR_GOOGLE_TYPE
                )

            } catch (e: Exception) {
                Log.d("GoogleException", "-----------$e")
            }


        } catch (e: ApiException) {
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}