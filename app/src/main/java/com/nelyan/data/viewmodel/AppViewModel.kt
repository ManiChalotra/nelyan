package com.meherr.mehar.data.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AppViewModel : ViewModel() {

    // for onFailure method response
    private var exceptionLiveData: MutableLiveData<String>? = null
    fun getException(): LiveData<String>? {
        if (exceptionLiveData == null) {
            exceptionLiveData = MutableLiveData()
        }
        return exceptionLiveData
    }


   /* // hit Login api
    private var loginMutableLiveData: MutableLiveData<Response<LoginResponseModel>?>? = null

    fun observeLoginResponse(): LiveData<Response<LoginResponseModel>?>? {
        if (loginMutableLiveData == null) {
            loginMutableLiveData = MutableLiveData<Response<LoginResponseModel>?>()
        }
        return loginMutableLiveData
    }

    fun SendLoginData(
        securityKey: String?,
        email: String?,
        password: String?,
        deviceType: String?,
        deviceToken: String?
    ) {
        JsonPlaceHolder().getLogin_Api(securityKey, email, password, deviceType, deviceToken)
            .enqueue(object : retrofit2.Callback<LoginResponseModel> {
                override fun onFailure(call: Call<LoginResponseModel>, t: Throwable) {
                    exceptionLiveData!!.value = t.message + "\n" + t.localizedMessage
                }

                override fun onResponse(
                    call: Call<LoginResponseModel>,
                    response: Response<LoginResponseModel>
                ) {
                    loginMutableLiveData?.value = response
                }
            })

    }
*/


}