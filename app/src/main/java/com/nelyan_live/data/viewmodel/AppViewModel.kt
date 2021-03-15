package com.meherr.mehar.data.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.JsonObject
import com.meherr.mehar.data.network.JsonPlaceHolder
import com.nelyan_live.data.network.responsemodels.ImageUploadApiResponseModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
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
        JsonPlaceHolder().get_ForgetPassword_Api(securityKey, email)
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

    // get profile api
    private var profileMutableLiveData: MutableLiveData<Response<JsonObject>?>? = null

    fun observeProfileApiResponse(): LiveData<Response<JsonObject>?>? {
        if (profileMutableLiveData == null) {
            profileMutableLiveData = MutableLiveData<Response<JsonObject>?>()
        }
        return profileMutableLiveData
    }

    fun sendprofileApiData(securityKey: String?,authkey:String?) {
        JsonPlaceHolder().get_Profile_api(securityKey, authkey)
                .enqueue(object : retrofit2.Callback<JsonObject> {
                    override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                        exceptionLiveData!!.value = t.message + "\n" + t.localizedMessage
                    }
                    override fun onResponse(
                            call: Call<JsonObject>,
                            response: Response<JsonObject>
                    ) {
                        profileMutableLiveData?.value = response
                    }
                })
    }

    //  edit profile api
    private var editProfileMutableLiveData: MutableLiveData<Response<JsonObject>?>? = null

    fun observeEditProfileResponse(): LiveData<Response<JsonObject>?>? {
        if (editProfileMutableLiveData == null) {
            editProfileMutableLiveData = MutableLiveData<Response<JsonObject>?>()
        }
        return editProfileMutableLiveData
    }

    fun sendEditProfileApiWithImageData(securityKey: String?,authkey:String?, name:RequestBody?, city:RequestBody?, lat:RequestBody?, longi:RequestBody?, utilization:RequestBody?, image:MultipartBody.Part?) {
        JsonPlaceHolder().get_EditProfile_withImage_Api(securityKey, authkey,name,city,lat,longi,utilization,image)
                .enqueue(object : retrofit2.Callback<JsonObject> {
                    override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                        exceptionLiveData!!.value = t.message + "\n" + t.localizedMessage
                    }
                    override fun onResponse(
                            call: Call<JsonObject>,
                            response: Response<JsonObject>
                    ) {
                        editProfileMutableLiveData?.value = response
                    }
                })
    }

    fun sendEditProfileApiWithoutImageData(securityKey: String?,authkey:String?, name:RequestBody?, city:RequestBody?, lat:RequestBody?, longi:RequestBody?, utilization:RequestBody?) {
        JsonPlaceHolder().get_EditProfile_withoutImage_Api(securityKey, authkey,name,city,lat,longi,utilization)
                .enqueue(object : retrofit2.Callback<JsonObject> {
                    override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                        exceptionLiveData!!.value = t.message + "\n" + t.localizedMessage
                    }
                    override fun onResponse(
                            call: Call<JsonObject>,
                            response: Response<JsonObject>
                    ) {
                        editProfileMutableLiveData?.value = response
                    }
                })
    }

    // get contact us api

    private var contactUsMutableLiveData: MutableLiveData<Response<JsonObject>?>? = null

    fun observeContactUsResponse(): LiveData<Response<JsonObject>?>? {
        if (contactUsMutableLiveData == null) {
            contactUsMutableLiveData = MutableLiveData<Response<JsonObject>?>()
        }
        return contactUsMutableLiveData
    }

    fun sendContatUsData(securityKey: String?,authkey:String?, name:String?, email:String?, comment:String?) {
        JsonPlaceHolder().get_ContactUs_Api(securityKey, authkey,name,email, comment)
                .enqueue(object : retrofit2.Callback<JsonObject> {
                    override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                        exceptionLiveData!!.value = t.message + "\n" + t.localizedMessage
                    }
                    override fun onResponse(
                            call: Call<JsonObject>,
                            response: Response<JsonObject>
                    ) {
                        contactUsMutableLiveData?.value = response
                    }
                })
    }

    // notification enable
    private var enableNotificationMutableLiveData: MutableLiveData<Response<JsonObject>?>? = null

    fun observeNotificationEnableResponse(): LiveData<Response<JsonObject>?>? {
        if (enableNotificationMutableLiveData == null) {
            enableNotificationMutableLiveData = MutableLiveData<Response<JsonObject>?>()
        }
        return enableNotificationMutableLiveData
    }

    fun sendNotificationEnableData(securityKey: String?,authkey:String?, type:String?) {
        JsonPlaceHolder().get_Notificatuion_Enable_Api(securityKey, authkey,type)
                .enqueue(object : retrofit2.Callback<JsonObject> {
                    override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                        exceptionLiveData!!.value = t.message + "\n" + t.localizedMessage
                    }
                    override fun onResponse(
                            call: Call<JsonObject>,
                            response: Response<JsonObject>
                    ) {
                        enableNotificationMutableLiveData?.value = response
                    }
                })
    }


    // change password api
    private var changePasswordMutableLiveData: MutableLiveData<Response<JsonObject>?>? = null

    fun observeChangePasswordResponse(): LiveData<Response<JsonObject>?>? {
        if (changePasswordMutableLiveData == null) {
            changePasswordMutableLiveData = MutableLiveData<Response<JsonObject>?>()
        }
        return changePasswordMutableLiveData
    }

    fun sendChangePasswordData(securityKey: String?,authkey:String?, oldPassword:String?, newpassword:String?) {
        JsonPlaceHolder().get_ChangePassword_Api(securityKey, authkey,oldPassword, newpassword)
                .enqueue(object : retrofit2.Callback<JsonObject> {
                    override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                        exceptionLiveData!!.value = t.message + "\n" + t.localizedMessage
                    }
                    override fun onResponse(
                            call: Call<JsonObject>,
                            response: Response<JsonObject>
                    ) {
                        changePasswordMutableLiveData?.value = response
                    }
                })
    }


    // get getCOntent api
    private var getAboutUSMutableLiveData: MutableLiveData<Response<JsonObject>?>? = null
    private var getTermsConditionLiveData: MutableLiveData<Response<JsonObject>?>? = null
    private var getPrivacyPolicyLiveData: MutableLiveData<Response<JsonObject>?>? = null

    fun observeAboutUsResponse(): LiveData<Response<JsonObject>?>? {
        if (getAboutUSMutableLiveData == null) {
            getAboutUSMutableLiveData = MutableLiveData<Response<JsonObject>?>()
        }
        return getAboutUSMutableLiveData
    }

    fun observeTermsConditionResponse(): LiveData<Response<JsonObject>?>? {
        if (getTermsConditionLiveData == null) {
            getTermsConditionLiveData = MutableLiveData<Response<JsonObject>?>()
        }
        return getTermsConditionLiveData
    }


    fun observePrivacyPolicyResponse(): LiveData<Response<JsonObject>?>? {
        if (getPrivacyPolicyLiveData == null) {
            getPrivacyPolicyLiveData = MutableLiveData<Response<JsonObject>?>()
        }
        return getPrivacyPolicyLiveData
    }

    fun sendGetContentApiData(securityKey: String?,authkey:String?, type:String?) {
        JsonPlaceHolder().get_GetContent_APi(securityKey, authkey,type)
                .enqueue(object : retrofit2.Callback<JsonObject> {
                    override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                        exceptionLiveData!!.value = t.message + "\n" + t.localizedMessage
                    }
                    override fun onResponse(
                            call: Call<JsonObject>,
                            response: Response<JsonObject>
                    ) {

                        when(type){
                            "1"->{
                                getAboutUSMutableLiveData?.value = response

                            }
                            "2"->{
                                getTermsConditionLiveData?.value = response

                            }
                            "3"->{
                                getPrivacyPolicyLiveData?.value = response

                            }
                        }
                    }
                })
    }


    // imageUpload
    private var imageUploadMutableLiveData: MutableLiveData<Response<ImageUploadApiResponseModel>?>? = null

    fun observeUploadImageResponse(): LiveData<Response<ImageUploadApiResponseModel>?>? {
        if (imageUploadMutableLiveData == null) {
            imageUploadMutableLiveData = MutableLiveData<Response<ImageUploadApiResponseModel>?>()
        }
        return imageUploadMutableLiveData
    }

    fun sendUploadImageData(type:RequestBody?, folder:RequestBody?, image:List<MultipartBody.Part>?) {
        JsonPlaceHolder().get_ImageUpload_Api(type, folder, image)
                .enqueue(object : retrofit2.Callback<ImageUploadApiResponseModel> {
                    override fun onFailure(call: Call<ImageUploadApiResponseModel>, t: Throwable) {
                        exceptionLiveData!!.value = t.message + "\n" + t.localizedMessage
                    }
                    override fun onResponse(
                            call: Call<ImageUploadApiResponseModel>,
                            response: Response<ImageUploadApiResponseModel>
                    ) {
                        imageUploadMutableLiveData?.value = response
                    }
                })
    }

    // get activityType Api

    private var activityTypeMutableLiveData: MutableLiveData<Response<JsonObject>?>? = null
    fun observeActivityTypeResponse(): LiveData<Response<JsonObject>?>? {
        if (activityTypeMutableLiveData == null) {
            activityTypeMutableLiveData = MutableLiveData<Response<JsonObject>?>()
        }
        return activityTypeMutableLiveData
    }

    fun sendActivityTypeData(securityKey: String?,authkey:String?) {
        JsonPlaceHolder().get_ActivityType_Api(securityKey, authkey)
                .enqueue(object : retrofit2.Callback<JsonObject> {
                    override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                        exceptionLiveData!!.value = t.message + "\n" + t.localizedMessage
                    }
                    override fun onResponse(
                            call: Call<JsonObject>,
                            response: Response<JsonObject>
                    ) {
                        activityTypeMutableLiveData?.value = response
                    }
                })

    }


    // add post Activity api

    private var addPostActivityMutableLiveData: MutableLiveData<Response<JsonObject>?>? = null
    fun observe_addPostActivity_Response(): LiveData<Response<JsonObject>?>? {
        if (addPostActivityMutableLiveData == null) {
            addPostActivityMutableLiveData = MutableLiveData<Response<JsonObject>?>()
        }
        return addPostActivityMutableLiveData
    }

    fun send_addPostActivity_Data(securityKey: String?,authkey:String?, type: String, activityType:String, shopname:String, activityName:String, description:String, message:String, phone:String, address:String, city:String, latitude:String, longitude:String, ageGroup:String, addEvent:String, media:String) {
        JsonPlaceHolder().get_addPOSt_Activity_Api(securityKey, authkey,type, activityType, shopname, activityName, description,message,phone,address,city,latitude,longitude,ageGroup,addEvent,media)
                .enqueue(object : retrofit2.Callback<JsonObject> {
                    override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                        exceptionLiveData!!.value = t.message + "\n" + t.localizedMessage
                    }
                    override fun onResponse(
                            call: Call<JsonObject>,
                            response: Response<JsonObject>
                    ) {
                        addPostActivityMutableLiveData?.value = response
                    }
                })
    }

    // social Login api

    private var socialLoginMutableLiveData: MutableLiveData<Response<JsonObject>?>? = null
    fun observeSocialLoginResponse(): LiveData<Response<JsonObject>?>? {
        if (socialLoginMutableLiveData == null) {
            socialLoginMutableLiveData = MutableLiveData<Response<JsonObject>?>()
        }
        return socialLoginMutableLiveData
    }

    fun sendSocialLoginData(securityKey: String?,socialId:String?, socialtype: String) {
        JsonPlaceHolder().get_SocialLogin_Api(securityKey, socialId,socialtype )
                .enqueue(object : retrofit2.Callback<JsonObject> {
                    override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                        exceptionLiveData!!.value = t.message + "\n" + t.localizedMessage
                    }
                    override fun onResponse(
                            call: Call<JsonObject>,
                            response: Response<JsonObject>
                    ) {
                        socialLoginMutableLiveData?.value = response
                    }
                })
    }


}