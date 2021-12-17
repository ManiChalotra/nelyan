package com.nelyanlive.data.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.JsonObject
import com.nelyanlive.data.network.JsonPlaceHolder
import com.nelyanlive.data.network.responsemodels.ImageUploadApiResponseModel
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
        name: RequestBody?,
        email: RequestBody?,
        password: RequestBody?,
        role: RequestBody?,
        second: RequestBody?,
        city: RequestBody?,
        latituude: RequestBody?,
        longitude: RequestBody?,
        image: MultipartBody.Part?
    ) {
        JsonPlaceHolder().getSignUp_woithImage_APi(
            securityKey, deviceType, deviceToken,
            name, email, password, role, second, city, latituude, longitude, image
        )
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
        name: RequestBody?,
        email: RequestBody?,
        password: RequestBody?,
        role: RequestBody?,
        second: RequestBody?,
        city: RequestBody?,
        latituude: RequestBody?,
        longitude: RequestBody?
    ) {
        JsonPlaceHolder().getSignUp_withoutImage_APi(
            securityKey, deviceType, deviceToken, name, email,
            password, role, second, city, latituude, longitude
        )
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
        email: String?,
        password: String?,
        second: String?
    ) {
        JsonPlaceHolder().getz_Login_api(
            securityKey,
            deviceType,
            deviceToken,
            email,
            password,
            second
        )
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
            loginMutableLiveData = MutableLiveData()
        }
        return loginMutableLiveData
    }

    fun sendLogoutData(
        securityKey: String?, authkey: String?
    ) {
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


    // home Activities List api
    private var homeActivitiesMutableLiveData: MutableLiveData<Response<JsonObject>?>? = null

    fun observeHomeActivitiesResponse(): LiveData<Response<JsonObject>?>? {
        if (homeActivitiesMutableLiveData == null) {
            homeActivitiesMutableLiveData = MutableLiveData<Response<JsonObject>?>()
        }
        return homeActivitiesMutableLiveData
    }

    fun sendHomeActivitiesData(
        securityKey: String?, authkey: String?, categoryType: String?
    ) {
        JsonPlaceHolder().getHomeDataListing(securityKey, authkey, categoryType)
            .enqueue(object : retrofit2.Callback<JsonObject> {
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    exceptionLiveData!!.value = t.message + "\n" + t.localizedMessage
                }

                override fun onResponse(
                    call: Call<JsonObject>,
                    response: Response<JsonObject>
                ) {
                    homeActivitiesMutableLiveData?.value = response
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
        securityKey: String?, email: String?
    ) {
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

    fun sendHOmeCategoryData(securityKey: String?, authkey: String?) {
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


    // get myads list api
    private var myAdsListMutableLiveData: MutableLiveData<Response<JsonObject>?>? = null

    fun observerMyAdsListResponse(): LiveData<Response<JsonObject>?>? {
        if (myAdsListMutableLiveData == null) {
            myAdsListMutableLiveData = MutableLiveData()
        }
        return myAdsListMutableLiveData
    }

    fun sendMyAdsListData(securityKey: String?, authkey: String?) {
        JsonPlaceHolder().getmyAddListAPI(securityKey, authkey)
            .enqueue(object : retrofit2.Callback<JsonObject> {
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    exceptionLiveData!!.value = t.message + "\n" + t.localizedMessage
                }

                override fun onResponse(
                    call: Call<JsonObject>,
                    response: Response<JsonObject>
                ) {
                    myAdsListMutableLiveData?.value = response
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

    fun sendprofileApiData(securityKey: String?, authkey: String?) {
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

    // add favourite  api
    private var addFavouriteMutableLiveData: MutableLiveData<Response<JsonObject>?>? = null

    fun observeAddFavouriteApiResponse(): LiveData<Response<JsonObject>?>? {
        if (addFavouriteMutableLiveData == null) {
            addFavouriteMutableLiveData = MutableLiveData()
        }
        return addFavouriteMutableLiveData
    }

    fun addFavouriteApiData(securityKey: String?, authkey: String?, eventId: String?) {
        JsonPlaceHolder().addFavourite(securityKey, authkey, eventId)
            .enqueue(object : retrofit2.Callback<JsonObject> {
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    exceptionLiveData!!.value = t.message + "\n" + t.localizedMessage
                }

                override fun onResponse(
                    call: Call<JsonObject>,
                    response: Response<JsonObject>
                ) {
                    addFavouriteMutableLiveData?.value = response
                }
            })
    }

    // get Group Messages api
    private var groupMessageMutableLiveData: MutableLiveData<Response<JsonObject>?>? = null

    fun observeGroupMessageApiResponse(): LiveData<Response<JsonObject>?>? {
        if (groupMessageMutableLiveData == null) {
            groupMessageMutableLiveData = MutableLiveData()
        }
        return groupMessageMutableLiveData
    }

    fun groupMessageApiData(
        securityKey: String?,
        authkey: String?,
        cityName: String?,
        page: String?,
        limit: String?
    ) {
        JsonPlaceHolder().getGroupMessages(securityKey, authkey, cityName, page, limit)
            .enqueue(object : retrofit2.Callback<JsonObject> {
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    exceptionLiveData!!.value = t.message + "\n" + t.localizedMessage
                }

                override fun onResponse(
                    call: Call<JsonObject>,
                    response: Response<JsonObject>
                ) {
                    groupMessageMutableLiveData?.value = response
                }
            })
    }


    // get Group Notification check api
    private var groupGroupNotifyMutableLiveData: MutableLiveData<Response<JsonObject>?>? = null

    fun observeGroupNotifyApiResponse(): LiveData<Response<JsonObject>?>? {
        if (groupGroupNotifyMutableLiveData == null) {
            groupGroupNotifyMutableLiveData = MutableLiveData()
        }
        return groupGroupNotifyMutableLiveData
    }

    fun groupNotifyApiData(securityKey: String?, authkey: String?, groupId: String?) {
        JsonPlaceHolder().getGroupMessagesNotification(securityKey, authkey, groupId)
            .enqueue(object : retrofit2.Callback<JsonObject> {
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    exceptionLiveData!!.value = t.message + "\n" + t.localizedMessage
                }

                override fun onResponse(
                    call: Call<JsonObject>,
                    response: Response<JsonObject>
                ) {
                    groupGroupNotifyMutableLiveData?.value = response
                }
            })
    }

    // get Badge api
    private var badgeMutableLiveData: MutableLiveData<Response<JsonObject>?>? = null

    fun observeBadgeApiResponse(): LiveData<Response<JsonObject>?>? {
        if (badgeMutableLiveData == null) {
            badgeMutableLiveData = MutableLiveData()
        }
        return badgeMutableLiveData
    }

    fun badgeApiData(securityKey: String?, authKey: String?) {
        JsonPlaceHolder().getBadgeStatus(securityKey, authKey)
            .enqueue(object : retrofit2.Callback<JsonObject> {
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {

                    Log.e("observeBadgeApiResponse", "-ttt----$t--------")

                    exceptionLiveData!!.value = t.message + "\n" + t.localizedMessage
                }

                override fun onResponse(
                    call: Call<JsonObject>,
                    response: Response<JsonObject>
                ) {
                    Log.e("observeBadgeApiResponse", "--ress---$response--------")

                    badgeMutableLiveData?.value = response
                }
            })
    }

    // change Group Notification check api
    private var changeGroupNotifyMutableLiveData: MutableLiveData<Response<JsonObject>?>? = null

    fun observeChangeNotifyApiResponse(): LiveData<Response<JsonObject>?>? {
        if (changeGroupNotifyMutableLiveData == null) {
            changeGroupNotifyMutableLiveData = MutableLiveData()
        }
        return changeGroupNotifyMutableLiveData
    }

    fun changeNotifyApiData(
        securityKey: String?,
        authkey: String?,
        groupId: String?,
        status: String?
    ) {
        JsonPlaceHolder().changeGroupMessagesNotification(securityKey, authkey, groupId, status)
            .enqueue(object : retrofit2.Callback<JsonObject> {
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    exceptionLiveData!!.value = t.message + "\n" + t.localizedMessage
                }

                override fun onResponse(
                    call: Call<JsonObject>,
                    response: Response<JsonObject>
                ) {
                    changeGroupNotifyMutableLiveData?.value = response
                }
            })
    }

    // for event notifictaion

    fun observeChangeEventNotifyApiResponse(): LiveData<Response<JsonObject>?>? {
        if (eventPushModel == null) {
            eventPushModel = MutableLiveData()
        }
        return eventPushModel
    }

    fun changeEventNotiApiData(
        securityKey: String?,
        authkey: String?,
        type: String
    ) {
        JsonPlaceHolder().changeEventNotification(securityKey, authkey, type)
            .enqueue(object : retrofit2.Callback<JsonObject> {
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    exceptionLiveData!!.value = t.message + "\n" + t.localizedMessage
                }

                override fun onResponse(
                    call: Call<JsonObject>,
                    response: Response<JsonObject>
                ) {
                    eventPushModel?.value = response
                }
            })
    }

    // change Chat Regulation  api
    private var changeChatRegulationMutableLiveData: MutableLiveData<Response<JsonObject>?>? = null

    fun observeChatRegulationApiResponse(): LiveData<Response<JsonObject>?>? {
        if (changeChatRegulationMutableLiveData == null) {
            changeChatRegulationMutableLiveData = MutableLiveData()
        }
        return changeChatRegulationMutableLiveData
    }

    fun getChatRegulations(securityKey: String?, authkey: String?) {
        JsonPlaceHolder().getChatRegulation(securityKey, authkey)
            .enqueue(object : retrofit2.Callback<JsonObject> {
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    exceptionLiveData!!.value = t.message + "\n" + t.localizedMessage
                }

                override fun onResponse(
                    call: Call<JsonObject>,
                    response: Response<JsonObject>
                ) {
                    changeChatRegulationMutableLiveData?.value = response
                }
            })
    }

    // add favourite  post api
    private var addFavouritePostMutableLiveData: MutableLiveData<Response<JsonObject>?>? = null

    fun observeAddFavouritePostApiResponse(): LiveData<Response<JsonObject>?>? {
        if (addFavouritePostMutableLiveData == null) {
            addFavouritePostMutableLiveData = MutableLiveData()
        }
        return addFavouritePostMutableLiveData
    }

    fun addFavouritePostApiData(
        securityKey: String?,
        authkey: String?,
        postId: String?,
        type: String?
    ) {
        JsonPlaceHolder().addFavouritePost(securityKey, authkey, postId, type)
            .enqueue(object : retrofit2.Callback<JsonObject> {
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    exceptionLiveData!!.value = t.message + "\n" + t.localizedMessage
                }

                override fun onResponse(
                    call: Call<JsonObject>,
                    response: Response<JsonObject>
                ) {
                    addFavouritePostMutableLiveData?.value = response
                }
            })
    }

    // post details  api
    private var postDetailsMutableLiveData: MutableLiveData<Response<JsonObject>?>? = null

    fun observePostDetailApiResponse(): LiveData<Response<JsonObject>?>? {
        if (postDetailsMutableLiveData == null) {
            postDetailsMutableLiveData = MutableLiveData()
        }
        return postDetailsMutableLiveData
    }

    fun postDetailsApiData(
        securityKey: String?,
        authkey: String?,
        type: String?,
        postId: String?,
        categoryId: String?
    ) {
        JsonPlaceHolder().postDetail(securityKey, authkey, type, postId, categoryId)
            .enqueue(object : retrofit2.Callback<JsonObject> {
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    exceptionLiveData!!.value = t.message + "\n" + t.localizedMessage
                }

                override fun onResponse(
                    call: Call<JsonObject>,
                    response: Response<JsonObject>
                ) {
                    postDetailsMutableLiveData?.value = response
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

    fun sendEditProfileApiWithImageData(
        securityKey: String?,
        authkey: String?,
        name: RequestBody?,
        city: RequestBody?,
        lat: RequestBody?,
        longi: RequestBody?,
        utilization: RequestBody?,
        image: MultipartBody.Part?
    ) {
        JsonPlaceHolder().get_EditProfile_withImage_Api(
            securityKey,
            authkey,
            name,
            city,
            lat,
            longi,
            utilization,
            image
        )
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

    fun sendEditProfileApiWithoutImageData(
        securityKey: String?,
        authkey: String?,
        name: RequestBody?,
        city: RequestBody?,
        lat: RequestBody?,
        longi: RequestBody?,
        utilization: RequestBody?
    ) {
        JsonPlaceHolder().get_EditProfile_withoutImage_Api(
            securityKey,
            authkey,
            name,
            city,
            lat,
            longi,
            utilization
        )
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

    fun sendContatUsData(
        securityKey: String?,
        authkey: String?,
        name: String?,
        email: String?,
        comment: String?
    ) {
        JsonPlaceHolder().get_ContactUs_Api(securityKey, authkey, name, email, comment)
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

    fun sendNotificationEnableData(securityKey: String?, authkey: String?, type: String?) {
        JsonPlaceHolder().get_Notificatuion_Enable_Api(securityKey, authkey, type)
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


    // switch user API
    private var switchUserMutableLiveData: MutableLiveData<Response<JsonObject>?>? = null

    fun observeSwitchUserResponse(): LiveData<Response<JsonObject>?>? {
        if (switchUserMutableLiveData == null) {
            switchUserMutableLiveData = MutableLiveData<Response<JsonObject>?>()
        }
        return switchUserMutableLiveData
    }

    fun sendSwitchAccountData(securityKey: String?, authkey: String?, type: String?) {
        JsonPlaceHolder().switchAccount(securityKey, authkey, type)
            .enqueue(object : retrofit2.Callback<JsonObject> {
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    exceptionLiveData!!.value = t.message + "\n" + t.localizedMessage
                }

                override fun onResponse(
                    call: Call<JsonObject>,
                    response: Response<JsonObject>
                ) {
                    switchUserMutableLiveData?.value = response
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

    fun sendChangePasswordData(
        securityKey: String?,
        authkey: String?,
        oldPassword: String?,
        newpassword: String?
    ) {
        JsonPlaceHolder().get_ChangePassword_Api(securityKey, authkey, oldPassword, newpassword)
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

    fun sendGetContentApiData(securityKey: String?, authkey: String?, type: String?) {
        JsonPlaceHolder().get_GetContent_APi(securityKey, authkey, type)
            .enqueue(object : retrofit2.Callback<JsonObject> {
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    exceptionLiveData!!.value = t.message + "\n" + t.localizedMessage
                }

                override fun onResponse(
                    call: Call<JsonObject>,
                    response: Response<JsonObject>
                ) {

                    when (type) {
                        "1" -> {
                            getAboutUSMutableLiveData?.value = response

                        }
                        "2" -> {
                            getTermsConditionLiveData?.value = response

                        }
                        "3" -> {
                            getPrivacyPolicyLiveData?.value = response

                        }
                    }
                }
            })
    }


    // imageUpload
    private var imageUploadMutableLiveData: MutableLiveData<Response<ImageUploadApiResponseModel>?>? =
        null

    fun observeUploadImageResponse(): LiveData<Response<ImageUploadApiResponseModel>?>? {
        if (imageUploadMutableLiveData == null) {
            imageUploadMutableLiveData = MutableLiveData()
        }
        return imageUploadMutableLiveData
    }

    fun sendUploadImageData(
        type: RequestBody?,
        folder: RequestBody?,
        image: ArrayList<MultipartBody.Part>?
    ) {
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

    fun sendActivityTypeData(securityKey: String?, authkey: String?) {
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

    // get ChildCareType Api

    private var childcareTypeMutableLiveData: MutableLiveData<Response<JsonObject>?>? = null
    fun observeChildCareTypeResponse(): LiveData<Response<JsonObject>?>? {
        if (childcareTypeMutableLiveData == null) {
            childcareTypeMutableLiveData = MutableLiveData<Response<JsonObject>?>()
        }
        return childcareTypeMutableLiveData
    }

    fun sendChildCareTypeData(securityKey: String?, authkey: String?) {
        JsonPlaceHolder().getChildCareType(securityKey, authkey)
            .enqueue(object : retrofit2.Callback<JsonObject> {
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    exceptionLiveData!!.value = t.message + "\n" + t.localizedMessage
                }

                override fun onResponse(
                    call: Call<JsonObject>,
                    response: Response<JsonObject>
                ) {
                    childcareTypeMutableLiveData?.value = response
                }
            })

    }

    // get myFavourite Api

    private var myFavouriteMutableLiveData: MutableLiveData<Response<JsonObject>?>? = null
    fun observeFavourteResponse(): LiveData<Response<JsonObject>?>? {
        if (myFavouriteMutableLiveData == null) {
            myFavouriteMutableLiveData = MutableLiveData<Response<JsonObject>?>()
        }
        return myFavouriteMutableLiveData
    }

    fun sendMyFavouriteData(securityKey: String?, authkey: String?) {
        JsonPlaceHolder().myfavouriteAPI(securityKey, authkey)
            .enqueue(object : retrofit2.Callback<JsonObject> {
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    exceptionLiveData!!.value = t.message + "\n" + t.localizedMessage
                }

                override fun onResponse(
                    call: Call<JsonObject>,
                    response: Response<JsonObject>
                ) {
                    myFavouriteMutableLiveData?.value = response
                }
            })

    }

    // trader Activity spinner api

    private var activityTypeTraderMutableLiveData: MutableLiveData<Response<JsonObject>?>? = null
    fun observeActivityTypeTraderResponse(): LiveData<Response<JsonObject>?>? {
        if (activityTypeTraderMutableLiveData == null) {
            activityTypeTraderMutableLiveData = MutableLiveData()
        }
        return activityTypeTraderMutableLiveData
    }

    fun sendTraderTypeTraderData(securityKey: String?, authkey: String?) {
        JsonPlaceHolder().get_TraderActivity_Api(securityKey, authkey)
            .enqueue(object : retrofit2.Callback<JsonObject> {
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    exceptionLiveData!!.value = t.message + "\n" + t.localizedMessage
                }

                override fun onResponse(
                    call: Call<JsonObject>,
                    response: Response<JsonObject>
                ) {
                    activityTypeTraderMutableLiveData?.value = response
                }
            })

    }


    // add post Activity api

    private var addPostActivityMutableLiveData: MutableLiveData<Response<JsonObject>?>? = null
    fun observe_addPostActivity_Response(): LiveData<Response<JsonObject>?>? {
        if (addPostActivityMutableLiveData == null) {
            addPostActivityMutableLiveData = MutableLiveData()
        }
        return addPostActivityMutableLiveData
    }

    fun send_addPostActivity_Data(
        securityKey: String?,
        authkey: String?,
        type: String,
        activityType: String,
        shopname: String,
        activityName: String,
        description: String,
        phone: String,
        address: String,
        city: String,
        website: String,
        latitude: String,
        longitude: String,
        ageGroup: String,
        addEvent: String,
        media: String,
        country_code: String,
        typeEmpty: String,
        minage: String,
        maxage: String
    ) {

        when (typeEmpty) {
            "0" -> {
                JsonPlaceHolder().get_addPOSt_Activity_Api(
                    securityKey,
                    authkey,
                    type,
                    activityType,
                    shopname,
                    activityName,
                    description,
                    phone,
                    address,
                    city,
                    website,
                    latitude,
                    longitude,
                    ageGroup,
                    addEvent,
                    media,
                    country_code,
                    minage,
                    maxage
                )
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
            "1" -> {
                JsonPlaceHolder().get_addPOSt_withoutevent_Activity_Api(
                    securityKey,
                    authkey,
                    type,
                    activityType,
                    shopname,
                    activityName,
                    description,
                    phone,
                    address,
                    city,
                    website,
                    latitude,
                    longitude,
                    ageGroup,
                    media,
                    country_code,
                    minage,
                    maxage
                )
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
            "2" -> {
                JsonPlaceHolder().get_addPOSt_withoutevent_age_Activity_Api(
                    securityKey,
                    authkey,
                    type,
                    activityType,
                    shopname,
                    activityName,
                    description,
                    phone,
                    address,
                    city,
                    website,
                    latitude,
                    longitude,
                    media,
                    country_code,
                    minage,
                    maxage
                )
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
            "3" -> {
                JsonPlaceHolder().get_addPOSt_without_age_Activity_Api(
                    securityKey,
                    authkey,
                    type,
                    activityType,
                    shopname,
                    activityName,
                    description,
                    phone,
                    address,
                    city,
                    website,
                    latitude,
                    longitude,
                    addEvent,
                    media,
                    country_code,
                    minage,
                    maxage
                )
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
        }


    }

    // edit post Activity api

    private var editActivityMutableLiveData: MutableLiveData<Response<JsonObject>?>? = null
    fun observe_editActivity_Response(): LiveData<Response<JsonObject>?>? {
        if (editActivityMutableLiveData == null) {
            editActivityMutableLiveData = MutableLiveData()
        }
        return editActivityMutableLiveData
    }


    fun send_editActivity_Data(
        securityKey: String?,
        authkey: String?,
        postId: String,
        type: String,
        activityType: String,
        shopname: String,
        activityName: String,
        description: String,
        phone: String,
        address: String,
        minage: String,
        maxage: String,
        website: String,
        city: String,
        latitude: String,
        longitude: String,
        ageGroup: String,
        addEvent: String,
        country_code: String,
        img1: String,
        img2: String,
        img3: String,
        typeEmpty: String
    ) {
        when (typeEmpty) {
            "1" -> {
                Log.d(AppViewModel::class.java.name,"AppViewModel_1181    ")
                JsonPlaceHolder().editMyaddActivity(
                    securityKey,
                    authkey,
                    postId,
                    type,
                    activityType,
                    shopname,
                    activityName,
                    description,
                    website,
                    phone,
                    address,
                    minage,
                    maxage,
                    city,
                    latitude,
                    longitude,
                    ageGroup,
                    addEvent,
                    country_code,
                    img1,
                    img2,
                    img3
                )
                    .enqueue(object : retrofit2.Callback<JsonObject> {
                        override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                            exceptionLiveData!!.value = t.message + "\n" + t.localizedMessage
                        }

                        override fun onResponse(
                            call: Call<JsonObject>,
                            response: Response<JsonObject>
                        ) {
                            editActivityMutableLiveData?.value = response
                        }
                    })
            }
            "2" -> {
                Log.d(AppViewModel::class.java.name,"AppViewModel_1220    ")
                JsonPlaceHolder().editMyaddActivitywithoutAge(
                    securityKey,
                    authkey,
                    postId,
                    type,
                    activityType,
                    shopname,
                    activityName,
                    description,
                    website,
                    phone,
                    address,
                    minage,
                    maxage,
                    city,
                    latitude,
                    longitude,
                    addEvent,
                    country_code,
                    img1,
                    img2,
                    img3
                )
                    .enqueue(object : retrofit2.Callback<JsonObject> {
                        override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                            exceptionLiveData!!.value = t.message + "\n" + t.localizedMessage
                        }

                        override fun onResponse(
                            call: Call<JsonObject>,
                            response: Response<JsonObject>
                        ) {
                            editActivityMutableLiveData?.value = response
                        }
                    })
            }
            "3" -> {
                Log.d(AppViewModel::class.java.name,"AppViewModel_1258    ")
                JsonPlaceHolder().editMyaddActivitywithoutEvent(
                    securityKey,
                    authkey,
                    postId,
                    type,
                    activityType,
                    shopname,
                    activityName,
                    description,
                    website,
                    phone,
                    address,
                    minage,
                    maxage,
                    city,
                    latitude,
                    longitude,
                    ageGroup,
                    country_code,
                    img1,
                    img2,
                    img3
                )
                    .enqueue(object : retrofit2.Callback<JsonObject> {
                        override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                            exceptionLiveData!!.value = t.message + "\n" + t.localizedMessage
                        }

                        override fun onResponse(
                            call: Call<JsonObject>,
                            response: Response<JsonObject>
                        ) {
                            editActivityMutableLiveData?.value = response
                        }
                    })
            }
            "4" -> {
                Log.d(AppViewModel::class.java.name,"AppViewModel_1296    ")
                JsonPlaceHolder().editMyaddActivitywithoutAgeEvent(
                    securityKey,
                    authkey,
                    postId,
                    type,
                    activityType,
                    shopname,
                    activityName,
                    description,
                    website,
                    phone,
                    address,
                    minage,
                    maxage,
                    city,
                    latitude,
                    longitude,
                    country_code,
                    img1,
                    img2,
                    img3
                )
                    .enqueue(object : retrofit2.Callback<JsonObject> {
                        override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                            exceptionLiveData!!.value = t.message + "\n" + t.localizedMessage
                        }

                        override fun onResponse(
                            call: Call<JsonObject>,
                            response: Response<JsonObject>
                        ) {
                            editActivityMutableLiveData?.value = response
                        }
                    })
            }

        }

        /*JsonPlaceHolder().editMyaddActivity(securityKey, authkey, postId, type, activityType, shopname, activityName, description,phone,address,city,
                latitude,longitude,ageGroup,addEvent, country_code,img1,img2,img3)
                .enqueue(object : retrofit2.Callback<JsonObject> {
                    override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                        exceptionLiveData!!.value = t.message + "\n" + t.localizedMessage
                    }
                    override fun onResponse(
                            call: Call<JsonObject>,
                            response: Response<JsonObject>
                    ) {
                        editActivityMutableLiveData?.value = response
                    }
                })*/
    }

    // add post Trader api

    private var addPostTraderMutableLiveData: MutableLiveData<Response<JsonObject>?>? = null
    fun observeAddPostTraderResponse(): LiveData<Response<JsonObject>?>? {
        if (addPostTraderMutableLiveData == null) {
            addPostTraderMutableLiveData = MutableLiveData<Response<JsonObject>?>()
        }
        return addPostTraderMutableLiveData
    }

    fun send_addPostTraderData(
        securityKey: String?,
        authkey: String?,
        type: String,
        traderType: String,
        shopname: String,
        description: String,
        country_code: String,
        phone: String,
        address: String,
        city: String,
        latitude: String,
        longitude: String,
        email: String,
        website: String,
        selectDay: String,
        productDetail: String,
        media: String,
        DaysType: String,
        productType: String
    ) {
        Log.d(AppViewModel::class.java.name, "Daystype_1332   " + DaysType)
        Log.d(AppViewModel::class.java.name, "Daystype_1333   " + productType)
        when (DaysType) {
            "0" -> {

                if (productType == "0") {

                    JsonPlaceHolder().getAddTraderPostApiWithoutDaysWithoutProduct(
                        securityKey,
                        authkey,
                        type,
                        traderType,
                        shopname,
                        description,
                        country_code,
                        phone,
                        address,
                        city,
                        latitude,
                        longitude,
                        email,
                        website,
                        media
                    )
                        .enqueue(object : retrofit2.Callback<JsonObject> {
                            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                                exceptionLiveData!!.value = t.message + "\n" + t.localizedMessage
                            }

                            override fun onResponse(
                                call: Call<JsonObject>,
                                response: Response<JsonObject>
                            ) {
                                addPostTraderMutableLiveData?.value = response
                            }
                        })
                    Log.d(
                        AppViewModel::class.java.name,
                        "Daystype_withoutdaysandproduct   " + DaysType
                    )
                } else {
                    Log.d(AppViewModel::class.java.name, "Daystype_withoutdays   " + DaysType)
                    Log.d(AppViewModel::class.java.name, "Producttype_withoutdays   " + productType)
                    JsonPlaceHolder().getAddTraderPostApiWithoutDays(
                        securityKey,
                        authkey,
                        type,
                        traderType,
                        shopname,
                        description,
                        country_code,
                        phone,
                        address,
                        city,
                        latitude,
                        longitude,
                        email,
                        website,
                        productDetail,
                        media
                    )
                        .enqueue(object : retrofit2.Callback<JsonObject> {
                            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                                exceptionLiveData!!.value = t.message + "\n" + t.localizedMessage
                            }

                            override fun onResponse(
                                call: Call<JsonObject>,
                                response: Response<JsonObject>
                            ) {
                                addPostTraderMutableLiveData?.value = response
                            }
                        })
                }
            }
            "1" -> {
                Log.d(AppViewModel::class.java.name, "Daystype_1403   " + productType)
                if (productType == "1") {
                    Log.d(AppViewModel::class.java.name, "Daystype_TraderpostAPi   " + productType)
                    JsonPlaceHolder().getAddTraderPostApi(
                        securityKey,
                        authkey,
                        type,
                        traderType,
                        shopname,
                        description,
                        country_code,
                        phone,
                        address,
                        city,
                        latitude,
                        longitude,
                        email,
                        website,
                        selectDay,
                        productDetail,
                        media
                    )
                        .enqueue(object : retrofit2.Callback<JsonObject> {
                            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                                exceptionLiveData!!.value = t.message + "\n" + t.localizedMessage
                            }

                            override fun onResponse(
                                call: Call<JsonObject>,
                                response: Response<JsonObject>
                            ) {
                                addPostTraderMutableLiveData?.value = response
                            }
                        })
                } else {
                    Log.d(
                        AppViewModel::class.java.name,
                        "Daystype_withoutProducts   " + productType
                    )
                    JsonPlaceHolder().getAddTraderPostApiWithoutProduct(
                        securityKey,
                        authkey,
                        type,
                        traderType,
                        shopname,
                        description,
                        country_code,
                        phone,
                        address,
                        city,
                        latitude,
                        longitude,
                        email,
                        website,
                        selectDay,
                        media
                    )
                        .enqueue(object : retrofit2.Callback<JsonObject> {
                            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                                exceptionLiveData!!.value = t.message + "\n" + t.localizedMessage
                            }

                            override fun onResponse(
                                call: Call<JsonObject>,
                                response: Response<JsonObject>
                            ) {
                                addPostTraderMutableLiveData?.value = response
                            }
                        })
                }
            }
        }

    }


    // edit post Trader api

    private var editPostTraderMutableLiveData: MutableLiveData<Response<JsonObject>?>? = null
    fun observeEditPostTraderResponse(): LiveData<Response<JsonObject>?>? {
        if (editPostTraderMutableLiveData == null) {
            editPostTraderMutableLiveData = MutableLiveData<Response<JsonObject>?>()
        }
        return editPostTraderMutableLiveData
    }

    fun send_editPostTraderData(
        securityKey: String?,
        authkey: String?,
        type: String,
        traderType: String,
        shopname: String,
        description: String,
        country_code: String,
        phone: String,
        address: String,
        city: String,
        latitude: String,
        longitude: String,
        email: String,
        website: String,
        selectDay: String,
        productDetail: String,
        image1: String,
        image2: String,
        image3: String,
        postId: String,
        emptyType: String
    ) {
        when (emptyType) {
            "1" -> {
                JsonPlaceHolder().editTraderPost_ApiwithoutDay(
                    securityKey,
                    authkey,
                    type,
                    traderType,
                    shopname,
                    description,
                    country_code,
                    phone,
                    address,
                    city,
                    latitude,
                    longitude,
                    email,
                    website,
                    productDetail,
                    image1,
                    image2,
                    image3,
                    postId
                )
                    .enqueue(object : retrofit2.Callback<JsonObject> {
                        override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                            exceptionLiveData!!.value = t.message + "\n" + t.localizedMessage
                        }

                        override fun onResponse(
                            call: Call<JsonObject>,
                            response: Response<JsonObject>
                        ) {
                            editPostTraderMutableLiveData?.value = response
                        }
                    })
            }
            "2" -> {
                JsonPlaceHolder().editTraderPost_ApiWitoutProductandDay(
                    securityKey,
                    authkey,
                    type,
                    traderType,
                    shopname,
                    description,
                    country_code,
                    phone,
                    address,
                    city,
                    latitude,
                    longitude,
                    email,
                    website,
                    image1,
                    image2,
                    image3,
                    postId
                )
                    .enqueue(object : retrofit2.Callback<JsonObject> {
                        override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                            exceptionLiveData!!.value = t.message + "\n" + t.localizedMessage
                        }

                        override fun onResponse(
                            call: Call<JsonObject>,
                            response: Response<JsonObject>
                        ) {
                            editPostTraderMutableLiveData?.value = response
                        }
                    })
            }
            "0" -> {
                JsonPlaceHolder().editTraderPost_Api(
                    securityKey,
                    authkey,
                    type,
                    traderType,
                    shopname,
                    description,
                    country_code,
                    phone,
                    address,
                    city,
                    latitude,
                    longitude,
                    email,
                    website,
                    selectDay,
                    productDetail,
                    image1,
                    image2,
                    image3,
                    postId
                )
                    .enqueue(object : retrofit2.Callback<JsonObject> {
                        override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                            exceptionLiveData!!.value = t.message + "\n" + t.localizedMessage
                        }

                        override fun onResponse(
                            call: Call<JsonObject>,
                            response: Response<JsonObject>
                        ) {
                            editPostTraderMutableLiveData?.value = response
                        }
                    })
            }
            "3" -> {
                JsonPlaceHolder().editTraderPost_ApiWitoutProduct(
                    securityKey,
                    authkey,
                    type,
                    traderType,
                    shopname,
                    description,
                    country_code,
                    phone,
                    address,
                    city,
                    latitude,
                    longitude,
                    email,
                    website,
                    selectDay,
                    image1,
                    image2,
                    image3,
                    postId
                )
                    .enqueue(object : retrofit2.Callback<JsonObject> {
                        override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                            exceptionLiveData!!.value = t.message + "\n" + t.localizedMessage
                        }

                        override fun onResponse(
                            call: Call<JsonObject>,
                            response: Response<JsonObject>
                        ) {
                            editPostTraderMutableLiveData?.value = response
                        }
                    })
            }
        }
    }
    // add post Nursery api

    private var addNuseryPostMutableLiveData: MutableLiveData<Response<JsonObject>?>? = null
    fun observe_addNuseryPost_Response(): LiveData<Response<JsonObject>?>? {
        if (addNuseryPostMutableLiveData == null) {
            addNuseryPostMutableLiveData = MutableLiveData<Response<JsonObject>?>()
        }
        return addNuseryPostMutableLiveData
    }

    fun send_addNuseryPost_Data(
        securityKey: String?,
        authkey: String?,
        type: String,
        nurseryName: String,
        addInfo: String,
        noOfPlaces: String,
        countryCode: String,
        phone: String,
        address: String,
        description: String,
        city: String,
        latitude: String,
        longitude: String,
        media: String
    ) {
        JsonPlaceHolder().get_addNurseryPost_Api(
            securityKey, authkey, type, nurseryName, addInfo, noOfPlaces, countryCode, phone,
            address, description, city, latitude, longitude, media
        )
            .enqueue(object : retrofit2.Callback<JsonObject> {
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    exceptionLiveData!!.value = t.message + "\n" + t.localizedMessage
                }

                override fun onResponse(
                    call: Call<JsonObject>,
                    response: Response<JsonObject>
                ) {
                    addNuseryPostMutableLiveData?.value = response
                }
            })
    }


    // add post Maternal Assistant api

    private var addMaternalPostMutableLiveData: MutableLiveData<Response<JsonObject>?>? = null

    fun observe_addMaternalPost_Response(): LiveData<Response<JsonObject>?>? {
        if (addMaternalPostMutableLiveData == null) {
            addMaternalPostMutableLiveData = MutableLiveData<Response<JsonObject>?>()
        }
        return addMaternalPostMutableLiveData
    }

    fun sendMaternalPost_Data(
        securityKey: String?,
        authkey: String?,
        type: String,
        childCareTypeId: String,
        maternalName: String,
        email: String,
        noOfPlaces: String,
        countryCode: String,
        phone: String,
        address: String,
        description: String,
        city: String,
        latitude: String,
        longitude: String,
        media: String
    ) {
        JsonPlaceHolder().addMaternalPost_Api(
            securityKey,
            authkey,
            type,
            childCareTypeId,
            maternalName,
            email,
            noOfPlaces,
            countryCode,
            phone,
            address,
            description,
            city,
            latitude,
            longitude,
            media
        )
            .enqueue(object : retrofit2.Callback<JsonObject> {
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    exceptionLiveData!!.value = t.message + "\n" + t.localizedMessage
                }

                override fun onResponse(
                    call: Call<JsonObject>,
                    response: Response<JsonObject>
                ) {
                    addMaternalPostMutableLiveData?.value = response
                }
            })
    }


    // edit post Maternal Assistant api

    private var editMaternalPostMutableLiveData: MutableLiveData<Response<JsonObject>?>? = null

    fun observe_editMaternalPost_Response(): LiveData<Response<JsonObject>?>? {
        if (editMaternalPostMutableLiveData == null) {
            editMaternalPostMutableLiveData = MutableLiveData<Response<JsonObject>?>()
        }
        return editMaternalPostMutableLiveData
    }

    fun editMaternalPost_Data(
        securityKey: String?,
        authkey: String?,
        type: String,
        childCareTypeId: String,
        maternalName: String,
        email: String,
        website: String,
        noOfPlaces: String,
        countryCode: String,
        phone: String,
        address: String,
        description: String,
        city: String,
        latitude: String,
        longitude: String/*,  media:String*/,
        image1: String,
        image2: String,
        image3: String,
        postid: String
    ) {
        JsonPlaceHolder().editMaternalPost_Api(
            securityKey,
            authkey,
            type,
            childCareTypeId,
            maternalName,
            email,
            website,
            noOfPlaces,
            countryCode,
            phone,
            address,
            description,
            city,
            latitude,
            longitude,
            image1,
            image2,
            image3,
            postid
        )
            .enqueue(object : retrofit2.Callback<JsonObject> {
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    exceptionLiveData!!.value = t.message + "\n" + t.localizedMessage
                }

                override fun onResponse(
                    call: Call<JsonObject>,
                    response: Response<JsonObject>
                ) {
                    editMaternalPostMutableLiveData?.value = response
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

    fun sendSocialLoginData(
        securityKey: String?,
        deviceType: String,
        deviceToken: String,
        socialId: String?,
        socialtype: String
    ) {
        JsonPlaceHolder().get_SocialLogin_Api(
            securityKey,
            deviceType,
            deviceToken,
            socialId,
            socialtype
        )
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

    // Filter Activity api

    private var filterActivityListMutableLiveData: MutableLiveData<Response<JsonObject>?>? = null
    fun observeFilterActivityListResponse(): LiveData<Response<JsonObject>?>? {
        if (filterActivityListMutableLiveData == null) {
            filterActivityListMutableLiveData = MutableLiveData<Response<JsonObject>?>()
        }
        return filterActivityListMutableLiveData
    }

    fun sendFilterActivityListData(
        securityKey: String?,
        authkey: String,
        latitude: String,
        longitude: String?,
        distance: String,
        name: String?,
        typeId: String?,
        address: String,
        Age: String
    ) {
        JsonPlaceHolder().activityFilter_Api(
            securityKey,
            authkey,
            latitude,
            longitude,
            distance,
            name,
            typeId,
            address,
            Age
        )
            .enqueue(object : retrofit2.Callback<JsonObject> {
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    exceptionLiveData!!.value = t.message + "\n" + t.localizedMessage
                }

                override fun onResponse(
                    call: Call<JsonObject>,
                    response: Response<JsonObject>
                ) {
                    filterActivityListMutableLiveData?.value = response
                }
            })
    }

    // Filter Trader api

    private var filterTraderListMutableLiveData: MutableLiveData<Response<JsonObject>?>? = null
    fun observeFilterTraderListResponse(): LiveData<Response<JsonObject>?>? {
        if (filterTraderListMutableLiveData == null) {
            filterTraderListMutableLiveData = MutableLiveData<Response<JsonObject>?>()
        }
        return filterTraderListMutableLiveData
    }

    fun sendFilterTraderListData(
        securityKey: String?,
        authkey: String,
        latitude: String,
        longitude: String?,
        distance: String,
        name: String?,
        typeId: String?,
        address: String
    ) {
        JsonPlaceHolder().traderFilter_Api(
            securityKey,
            authkey,
            latitude,
            longitude,
            distance,
            name,
            typeId,
            address
        )
            .enqueue(object : retrofit2.Callback<JsonObject> {
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    exceptionLiveData!!.value = t.message + "\n" + t.localizedMessage
                }

                override fun onResponse(
                    call: Call<JsonObject>,
                    response: Response<JsonObject>
                ) {
                    filterTraderListMutableLiveData?.value = response
                }
            })
    }

    // Child Care Filter Activity api

    private var filterChildCareListMutableLiveData: MutableLiveData<Response<JsonObject>?>? = null
    fun observeFilterChildCareListResponse(): LiveData<Response<JsonObject>?>? {
        if (filterChildCareListMutableLiveData == null) {
            filterChildCareListMutableLiveData = MutableLiveData<Response<JsonObject>?>()
        }
        return filterChildCareListMutableLiveData
    }

    fun sendChildCareFilterData(
        securityKey: String?,
        authkey: String,
        latitude: String,
        longitude: String?,
        distance: String,
        name: String?,
        address: String,
        childCareTypeId: String
    ) {
        JsonPlaceHolder().childCareFilter_Api(
            securityKey,
            authkey,
            latitude,
            longitude,
            distance,
            name,
            address,
            childCareTypeId
        )
            .enqueue(object : retrofit2.Callback<JsonObject> {
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    exceptionLiveData!!.value = t.message + "\n" + t.localizedMessage
                }

                override fun onResponse(
                    call: Call<JsonObject>,
                    response: Response<JsonObject>
                ) {
                    filterChildCareListMutableLiveData?.value = response
                }
            })
    }

    // Event List api

    private var eventListMutableLiveData: MutableLiveData<Response<JsonObject>?>? = null
    private var eventPushModel: MutableLiveData<Response<JsonObject>?>? = null
    fun observeEventListResponse(): LiveData<Response<JsonObject>?>? {
        if (eventListMutableLiveData == null) {
            eventListMutableLiveData = MutableLiveData<Response<JsonObject>?>()
        }
        return eventListMutableLiveData
    }

    fun sendEventListData(securityKey: String?, authKey: String?, lati: String?, longi: String?) {
        JsonPlaceHolder().getEventList(securityKey, authKey, lati, longi)
            .enqueue(object : retrofit2.Callback<JsonObject> {
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    exceptionLiveData!!.value = t.message + "\n" + t.localizedMessage
                }

                override fun onResponse(
                    call: Call<JsonObject>,
                    response: Response<JsonObject>
                ) {
                    eventListMutableLiveData?.value = response
                }
            })
    }

    // Filtered Events List api

    private var filterEventListMutableLiveData: MutableLiveData<Response<JsonObject>?>? = null
    fun observeFilterEventListResponse(): LiveData<Response<JsonObject>?>? {
        if (filterEventListMutableLiveData == null) {
            filterEventListMutableLiveData = MutableLiveData<Response<JsonObject>?>()
        }
        return eventListMutableLiveData
    }

    fun sendFilterEventListData(
        securityKey: String?, authKey: String?, lati: String?, longi: String?, distance: String?,
        name: String?, address: String?,Age: String,
    ) {
        JsonPlaceHolder().getEventFilter(
            securityKey,
            authKey,
            lati,
            longi,
            distance,
            name,
            address,
            Age
        )
            .enqueue(object : retrofit2.Callback<JsonObject> {
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    exceptionLiveData!!.value = t.message + "\n" + t.localizedMessage
                }

                override fun onResponse(
                    call: Call<JsonObject>,
                    response: Response<JsonObject>
                ) {
                    eventListMutableLiveData?.value = response
                }
            })
    }

    // Delete Ad API

    private var deleteAdMutableLiveData: MutableLiveData<Response<JsonObject>?>? = null
    fun observeDeleteAdResponse(): LiveData<Response<JsonObject>?>? {
        if (deleteAdMutableLiveData == null) {
            deleteAdMutableLiveData = MutableLiveData<Response<JsonObject>?>()
        }
        return deleteAdMutableLiveData
    }

    fun sendDeleteAdData(
        securityKey: String?,
        authKey: String?,
        categoryId: String?,
        postId: String?
    ) {
        JsonPlaceHolder().deleteAd(securityKey, authKey, categoryId, postId)
            .enqueue(object : retrofit2.Callback<JsonObject> {
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    exceptionLiveData!!.value = t.message + "\n" + t.localizedMessage
                }

                override fun onResponse(
                    call: Call<JsonObject>,
                    response: Response<JsonObject>
                ) {
                    deleteAdMutableLiveData?.value = response
                }
            })
    }

    // get complete social Login profile api

    private var completeSocialLoginMutableLiveData: MutableLiveData<Response<JsonObject>?>? = null
    fun observeCompleteSociaLogin_Api_Response(): LiveData<Response<JsonObject>?>? {
        if (completeSocialLoginMutableLiveData == null) {
            completeSocialLoginMutableLiveData = MutableLiveData<Response<JsonObject>?>()
        }
        return completeSocialLoginMutableLiveData
    }

    fun sendcompleteSocialLogin_withImage_Data(
        securityKey: String?,
        socialid: String?,
        email: String?,
        name: String?,
        role: String?,
        lat: String?,
        longi: String?,
        second: String?,
        city: String?,
        image_type: String?,
        image: String?
    ) {
        JsonPlaceHolder().get_SocialCompleteApi_withImage(
            securityKey,
            socialid,
            email,
            name,
            role,
            lat,
            longi,
            second,
            city,
            image_type,
            image
        )
            .enqueue(object : retrofit2.Callback<JsonObject> {
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    exceptionLiveData!!.value = t.message + "\n" + t.localizedMessage
                }

                override fun onResponse(
                    call: Call<JsonObject>,
                    response: Response<JsonObject>
                ) {
                    completeSocialLoginMutableLiveData?.value = response
                }
            })
    }

    fun sendcompleteSocialLogin_withoutImage_Data(
        securityKey: String?,
        socialid: String?,
        email: String?,
        name: String?,
        role: String?,
        lat: String?,
        longi: String?,
        second: String?,
        city: String?,
        image_type: String?
    ) {
        JsonPlaceHolder().get_SocialCompleteApi_withoutImage(
            securityKey,
            socialid,
            email,
            name,
            role,
            lat,
            longi,
            second,
            city,
            image_type
        )
            .enqueue(object : retrofit2.Callback<JsonObject> {
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    exceptionLiveData!!.value = t.message + "\n" + t.localizedMessage
                }

                override fun onResponse(
                    call: Call<JsonObject>,
                    response: Response<JsonObject>
                ) {
                    completeSocialLoginMutableLiveData?.value = response
                }
            })

    }


}