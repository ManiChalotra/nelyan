package com.meherr.mehar.data.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.JsonObject
import com.meherr.mehar.data.network.JsonPlaceHolder
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response

class AppViewModel : ViewModel() {

    // for onFailure method response
    private var exceptionLiveData: MutableLiveData<String>? = null
    fun getException(): LiveData<String>? {
        if (exceptionLiveData == null) {
            exceptionLiveData = MutableLiveData()
        }
        return exceptionLiveData
    }



    // hit signup api
    private var signUpMutableLiveData: MutableLiveData<Response<JsonObject>?>? = null

    fun observeSignupResponse(): LiveData<Response<JsonObject>?>? {
        if (signUpMutableLiveData == null) {
            signUpMutableLiveData = MutableLiveData<Response<JsonObject>?>()
        }
        return signUpMutableLiveData
    }

    fun Send_SIGNUP_withIMAGE_Data(
            securityKey: String?,
            deviceType: String?,
            deviceToken: String?,
            name:RequestBody?,
            email:RequestBody?,
            password:RequestBody?,
            role:RequestBody?,
            second:RequestBody?,
            city:RequestBody?,
            latituude:RequestBody?,
            longitude:RequestBody?,
            image:MultipartBody.Part?,

    ) {
        JsonPlaceHolder().getSignUp_withImage_APi(securityKey, deviceType, deviceToken,name,email,password,role,second,city,latituude,longitude,image)
                .enqueue(object : retrofit2.Callback<JsonObject> {
                    override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                        exceptionLiveData!!.value = t.message + "\n" + t.localizedMessage
                    }

                    override fun onResponse(
                            call: Call<JsonObject>,
                            response: Response<JsonObject>
                    ) {
                        signUpMutableLiveData?.value = response
                    }
                })

    }

    fun Send_SIGNUP_withoutIMAGE_Data(
            securityKey: String?,
            deviceType: String?,
            deviceToken: String?,
            name:RequestBody?,
            email:RequestBody?,
            password:RequestBody?,
            role:RequestBody?,
            second:RequestBody?,
            city:RequestBody?,
            latituude:RequestBody?,
            longitude:RequestBody?
            ) {
        JsonPlaceHolder().getSignUp_withoutImage_APi(securityKey, deviceType, deviceToken,name,email,password,role,second,city,latituude,longitude)
                .enqueue(object : retrofit2.Callback<JsonObject> {
                    override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                        exceptionLiveData!!.value = t.message + "\n" + t.localizedMessage
                    }

                    override fun onResponse(
                            call: Call<JsonObject>,
                            response: Response<JsonObject>
                    ) {
                        signUpMutableLiveData?.value = response
                    }
                })

    }

    // hit login api
    private var loginMutableLiveData: MutableLiveData<Response<JsonObject>?>? = null

    fun observeLOginResponse(): LiveData<Response<JsonObject>?>? {
        if (loginMutableLiveData == null) {
            loginMutableLiveData = MutableLiveData<Response<JsonObject>?>()
        }
        return loginMutableLiveData
    }

    fun sendLoginData(
            securityKey: String?,
            deviceType: String?,
            deviceToken: String?,
            email:String?,
            password:String?,
            second:String?, ) {
        JsonPlaceHolder().getz_Login_api(securityKey, deviceType, deviceToken,email,password,second)
                .enqueue(object : retrofit2.Callback<JsonObject> {
                    override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                        exceptionLiveData!!.value = t.message + "\n" + t.localizedMessage
                    }

                    override fun onResponse(
                            call: Call<JsonObject>,
                            response: Response<JsonObject>
                    ) {
                        loginMutableLiveData?.value = response
                    }
                })

    }

    // logout api
    private var logoutMutableLiveData: MutableLiveData<Response<JsonObject>?>? = null

    fun observeLogoutResponse(): LiveData<Response<JsonObject>?>? {
        if (loginMutableLiveData == null) {
            loginMutableLiveData = MutableLiveData<Response<JsonObject>?>()
        }
        return loginMutableLiveData
    }

    fun sendLogoutData(
            securityKey: String?,authkey:String?) {
        JsonPlaceHolder().get_Logout_api(securityKey, authkey)
                .enqueue(object : retrofit2.Callback<JsonObject> {
                    override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                        exceptionLiveData!!.value = t.message + "\n" + t.localizedMessage
                    }

                    override fun onResponse(
                            call: Call<JsonObject>,
                            response: Response<JsonObject>
                    ) {
                        loginMutableLiveData?.value = response
                    }
                })

    }

    // forget password api
    private var forgetPasswordMutableLiveData: MutableLiveData<Response<JsonObject>?>? = null

    fun observeForgetPasswordResponse(): LiveData<Response<JsonObject>?>? {
        if (forgetPasswordMutableLiveData == null) {
            forgetPasswordMutableLiveData = MutableLiveData<Response<JsonObject>?>()
        }
        return forgetPasswordMutableLiveData
    }

    fun sendForgetPasswordData(
            securityKey: String?,email:String?) {
        JsonPlaceHolder().get_Logout_api(securityKey, email)
                .enqueue(object : retrofit2.Callback<JsonObject> {
                    override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                        exceptionLiveData!!.value = t.message + "\n" + t.localizedMessage
                    }

                    override fun onResponse(
                            call: Call<JsonObject>,
                            response: Response<JsonObject>
                    ) {
                        forgetPasswordMutableLiveData?.value = response
                    }
                })

    }

    // get homeCategory api
    private var homeCategoryMutableLiveData: MutableLiveData<Response<JsonObject>?>? = null

    fun observeHomeCategoryResponse(): LiveData<Response<JsonObject>?>? {
        if (homeCategoryMutableLiveData == null) {
            homeCategoryMutableLiveData = MutableLiveData<Response<JsonObject>?>()
        }
        return homeCategoryMutableLiveData
    }

    fun sendHOmeCategoryData(securityKey: String?,authkey:String?) {
        JsonPlaceHolder().get_HomeCategory_Api(securityKey, authkey)
                .enqueue(object : retrofit2.Callback<JsonObject> {
                    override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                        exceptionLiveData!!.value = t.message + "\n" + t.localizedMessage
                    }

                    override fun onResponse(
                            call: Call<JsonObject>,
                            response: Response<JsonObject>
                    ) {
                        homeCategoryMutableLiveData?.value = response
                    }
                })

    }






}