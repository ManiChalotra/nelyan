package com.meherr.mehar.data.network

import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.nelyan_live.data.network.responsemodels.ImageUploadApiResponseModel
import com.nelyan_live.data.network.responsemodels.trader_type.TraderTypeResponse
import com.nelyan_live.modals.eventList.EventListResponse
import com.nelyan_live.utils.base_URL
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit


interface JsonPlaceHolder {

    @Multipart
    @POST("signup")
    fun getSignUp_woithImage_APi(
            @Header("security_key") securityKey: String?,
            @Header("device_type") device_type: String?,
            @Header("device_token") device_token: String?,
            @Part("name") name: RequestBody?,
            @Part("email") email: RequestBody?,
            @Part("password") password: RequestBody?,
            @Part("role") role: RequestBody?,
            @Part("second") second: RequestBody?,
            @Part("city") city: RequestBody?,
            @Part("lat") lat: RequestBody?,
            @Part("longi") longi: RequestBody?,
            @Part image: MultipartBody.Part?): Call<JsonObject>


    @Multipart
    @POST("signup")
    fun getSignUp_withoutImage_APi(
            @Header("security_key") securityKey: String?,
            @Header("device_type") device_type: String?,
            @Header("device_token") device_token: String?,
            @Part("name") name: RequestBody?,
            @Part("email") email: RequestBody?,
            @Part("password") password: RequestBody?,
            @Part("role") role: RequestBody?,
            @Part("second") second: RequestBody?,
            @Part("city") city: RequestBody?,
            @Part("lat") lat: RequestBody?,
            @Part("longi") longi: RequestBody?): Call<JsonObject>


    @POST("login")
    @FormUrlEncoded
    fun getz_Login_api(@Header("security_key") securityKey: String?,
                       @Header("device_type") device_type: String?,
                       @Header("device_token") device_token: String?,
                       @Field("email") email: String?,
                       @Field("password") password: String?,
                       @Field("second") second: String?
    ): Call<JsonObject>

    @GET("logout")
    fun get_Logout_api(@Header("security_key") securityKey: String?,
                       @Header("auth_key") auth_key: String?): Call<JsonObject>

    @POST("homeDataListing")
    @FormUrlEncoded
    fun getHomeDataListing(@Header("security_key") securityKey: String?,
                           @Header("auth_key") auth_key: String?, @Field("type") type: String?): Call<JsonObject>

    @POST("forgot_password")
    @FormUrlEncoded
    fun get_ForgetPassword_Api(@Header("security_key") securityKey: String?,
                               @Field("email") email: String?): Call<JsonObject>

    @POST("add_fav")
    @FormUrlEncoded
    fun addFavourite(@Header("security_key") securityKey: String?,
                     @Header("auth_key") authKey: String?,
                     @Field("eventId") eventId: String?): Call<JsonObject>

    @POST("addPost_fav")
    @FormUrlEncoded
    fun addFavouritePost(@Header("security_key") securityKey: String?,
                         @Header("auth_key") authKey: String?,
                         @Field("postId") postId: String?, @Field("type") type: String?): Call<JsonObject>

    @POST("post_detail")
    @FormUrlEncoded
    fun postDetail(@Header("security_key") securityKey: String?,
                   @Header("auth_key") authKey: String?,
                   @Field("type") type: String?, @Field("postid") postid: String?,
                   @Field("categoryId") categoryId: String?): Call<JsonObject>

    @GET("getHomeCategories")
    fun get_HomeCategory_Api(@Header("security_key") securityKey: String?,
                             @Header("auth_key") auth_key: String?): Call<JsonObject>

    @GET("myAddList")
    fun getmyAddListAPI(@Header("security_key") securityKey: String?,
                        @Header("auth_key") auth_key: String?): Call<JsonObject>

    @FormUrlEncoded
    @POST("post_detail")
    fun getPostDetailAPI(@Header("security_key") securityKey: String?,
                         @Header("auth_key") auth_key: String?,
                         @Field("type") type: String?,
                         @Field("postid") postid: String?,
    ): Call<JsonObject>

    @GET("get_profile")
    fun get_Profile_api(@Header("security_key") securityKey: String?,
                        @Header("auth_key") auth_key: String?): Call<JsonObject>

    @Multipart
    @POST("edit_profile")
    fun get_EditProfile_withImage_Api(@Header("security_key") securityKey: String?,
                                      @Header("auth_key") auth_key: String?,
                                      @Part("name") name: RequestBody?,
                                      @Part("city") city: RequestBody?,
                                      @Part("lat") lat: RequestBody?,
                                      @Part("longi") longi: RequestBody?,
                                      @Part("types_of_utilisation") utilization: RequestBody?,
                                      @Part image: MultipartBody.Part?): Call<JsonObject>

    @Multipart
    @POST("edit_profile")
    fun get_EditProfile_withoutImage_Api(@Header("security_key") securityKey: String?,
                                         @Header("auth_key") auth_key: String?,
                                         @Part("name") name: RequestBody?,
                                         @Part("city") city: RequestBody?,
                                         @Part("lat") lat: RequestBody?,
                                         @Part("longi") longi: RequestBody?,
                                         @Part("types_of_utilisation") utilization: RequestBody?): Call<JsonObject>

    @POST("contact_us")
    @FormUrlEncoded
    fun get_ContactUs_Api(@Header("security_key") securityKey: String?,
                          @Header("auth_key") auth_key: String?,
                          @Field("name") name: String?,
                          @Field("email") email: String?,
                          @Field("comment") comment: String?): Call<JsonObject>

    @POST("enable_notifications")
    @FormUrlEncoded
    fun get_Notificatuion_Enable_Api(@Header("security_key") securityKey: String?,
                                     @Header("auth_key") auth_key: String?,
                                     @Field("type") type: String?): Call<JsonObject>

    @POST("switchAccount")
    @FormUrlEncoded
    fun switchAccount(@Header("security_key") securityKey: String?,
                      @Header("auth_key") auth_key: String?,
                      @Field("type") type: String?): Call<JsonObject>

    @POST("change_password")
    @FormUrlEncoded
    fun get_ChangePassword_Api(@Header("security_key") securityKey: String?,
                               @Header("auth_key") auth_key: String?,
                               @Field("old_password") old_password: String?,
                               @Field("new_password") new_password: String?
    ): Call<JsonObject>

    @POST("getContent")
    @FormUrlEncoded
    fun get_GetContent_APi(@Header("security_key") securityKey: String?,
                           @Header("auth_key") auth_key: String?,
                           @Field("type") type: String?): Call<JsonObject>

    @POST("delete_ad")
    @FormUrlEncoded
    fun deleteAd(@Header("security_key") securityKey: String?,
                 @Header("auth_key") auth_key: String?,
                 @Field("categoryId") categoryId: String?,
                 @Field("postId") postId: String?): Call<JsonObject>


    @Multipart
    @POST("imageUplaod")
    fun get_ImageUpload_Api(@Part("type") type: RequestBody?,
                            @Part("folder") folder: RequestBody?,
                            @Part image: ArrayList<MultipartBody.Part>?): Call<ImageUploadApiResponseModel>


    @GET("activityTpe")
    fun get_ActivityType_Api(@Header("security_key") securityKey: String?,
                             @Header("auth_key") auth_key: String?): Call<JsonObject>

    @GET("getChildcareActivity")
    fun getChildCareType(@Header("security_key") securityKey: String?,
                         @Header("auth_key") auth_key: String?): Call<JsonObject>

    @GET("myfavourite")
    fun myfavouriteAPI(@Header("security_key") securityKey: String?,
                       @Header("auth_key") auth_key: String?): Call<JsonObject>

    @GET("traderactivity")
    fun get_TraderActivity_Api(@Header("security_key") securityKey: String?,
                               @Header("auth_key") auth_key: String?): Call<JsonObject>

    @POST("addPost")
    @FormUrlEncoded
    fun get_addPOSt_Activity_Api(@Header("security_key") securityKey: String?,
                                 @Header("auth_key") auth_key: String?,
                                 @Field("type") type: String,
                                 @Field("activity_type") activity_type: String,
                                 @Field("shop_name") shop_name: String,
                                 @Field("activity_name") activity_name: String,
                                 @Field("description") description: String,
            /*@Field("message") message: String,*/
                                 @Field("phone") phone: String,
                                 @Field("address") address: String,
                                 @Field("city") city: String,
                                 @Field("latitude") latitude: String,
                                 @Field("longitude") longitude: String,
                                 @Field("ageGroup") ageGroup: String,// sending json Array
                                 @Field("addEvent") addEvent: String,// sending json Array
                                 @Field("media") media: String,// send json Array
                                 @Field("country_code") country_code: String
    ): Call<JsonObject>

    @POST("edit_myadd")
    @FormUrlEncoded
    fun editMyaddActivity(@Header("security_key") securityKey: String?,
                          @Header("auth_key") auth_key: String?,
                          @Field("postId") postId: String,
                          @Field("type") type: String,
                          @Field("activity_type") activity_type: String,
                          @Field("shop_name") shop_name: String,
                          @Field("activity_name") activity_name: String,
                          @Field("description") description: String,
                          @Field("phone") phone: String,
                          @Field("address") address: String,
                          @Field("city") city: String,
                          @Field("latitude") latitude: String,
                          @Field("longitude") longitude: String,
                          @Field("ageGroup") ageGroup: String,// sending json Array
                          @Field("addEvent") addEvent: String,// sending json Array
            /*@Field("media") media: String,// send json Array*/
                          @Field("country_code") country_code: String,
                          @Field("image1") image1: String,
                          @Field("image2") image2: String,
                          @Field("image3") image3: String
    ): Call<JsonObject>

    @POST("addPost")
    @FormUrlEncoded
    fun getAddTraderPostApi(@Header("security_key") securityKey: String?,
                            @Header("auth_key") auth_key: String?,
                            @Field("type") type: String,
                            @Field("trader_type") traderType: String,
                            @Field("shop_name") shop_name: String,
                            @Field("description") description: String,
                            @Field("country_code") country_code: String,
                            @Field("phone") phone: String,
                            @Field("address") address: String,
                            @Field("city") city: String,
                            @Field("latitude") latitude: String,
                            @Field("longitude") longitude: String,
                            @Field("email") email: String,
                            @Field("website") website: String,
                            @Field("selectDay") selectDay: String,// sending json Array
                            @Field("productDetail") productDetail: String,// sending json Array
                            @Field("media") media: String,// send json Array

    ): Call<JsonObject>

    @POST("addPost")
    @FormUrlEncoded
    fun get_addNurseryPost_Api(@Header("security_key") securityKey: String?,
                               @Header("auth_key") auth_key: String?,
                               @Field("type") type: String,
                               @Field("nursery_name") nurseryName: String,
                               @Field("add_info") addInfo: String,
                               @Field("no_of_places") noOfPlaces: String,
                               @Field("country_code") country_code: String,
                               @Field("phone") phone: String,
                               @Field("address") address: String,
                               @Field("description") description: String,
                               @Field("city") city: String,
                               @Field("latitude") latitude: String,
                               @Field("longitude") longitude: String,
                               @Field("media") media: String,// send json Array

    ): Call<JsonObject>

    @FormUrlEncoded
    @POST("addPost")
    fun addMaternalPost_Api(@Header("security_key") securityKey: String?,
                            @Header("auth_key") auth_key: String?,
                            @Field("type") type: String,
                            @Field("ChildCareId") childCareId: String,
                            @Field("name") materialName: String,
                            @Field("email") email: String,
                            @Field("no_of_places") noOfPlaces: String,
                            @Field("country_code") country_code: String,
                            @Field("phone") phone: String,
                            @Field("address") address: String,
                            @Field("description") description: String,
                            @Field("city") city: String,
                            @Field("latitude") latitude: String,
                            @Field("longitude") longitude: String,
                            @Field("media") media: String,// send json Array


    ): Call<JsonObject>

    @FormUrlEncoded
    @POST("edit_myadd")
    fun editMaternalPost_Api(@Header("security_key") securityKey: String?,
                             @Header("auth_key") auth_key: String?,
                             @Field("type") type: String,
                             @Field("ChildCareId") childCareId: String,
                             @Field("name") materialName: String,
                             @Field("email") email: String,
                             @Field("website") website: String,
                             @Field("no_of_places") noOfPlaces: String,
                             @Field("country_code") country_code: String,
                             @Field("phone") phone: String,
                             @Field("address") address: String,
                             @Field("description") description: String,
                             @Field("city") city: String,
                             @Field("latitude") latitude: String,
                             @Field("longitude") longitude: String,
                             @Field("image1") image1: String,
                             @Field("image2") image2: String,
                             @Field("image3") image3: String,
                             @Field("postId") postId: String

    ): Call<JsonObject>

    @FormUrlEncoded
    @POST("edit_myadd")
    fun editTraderPost_Api(@Header("security_key") securityKey: String?,
                           @Header("auth_key") auth_key: String?,
                           @Field("type") type: String,
                           @Field("trader_type") traderType: String,
                           @Field("shop_name") shop_name: String,
                           @Field("description") description: String,
                           @Field("country_code") country_code: String,
                           @Field("phone") phone: String,
                           @Field("address") address: String,
                           @Field("city") city: String,
                           @Field("latitude") latitude: String,
                           @Field("longitude") longitude: String,
                           @Field("email") email: String,
                           @Field("website") website: String,
                           @Field("selectDay") selectDay: String,// sending json Array
                           @Field("productDetail") productDetail: String,
                           @Field("image1") image1: String,
                           @Field("image2") image2: String,
                           @Field("image3") image3: String,
                           @Field("postId") postId: String

    ): Call<JsonObject>

    @POST("social_login")
    @FormUrlEncoded
    fun get_SocialLogin_Api(@Header("security_key") securityKey: String?,
                            @Header("device_type") deviceType: String?,
                            @Header("device_token") deviceToken: String?,
                            @Field("social_id") social_id: String?,
                            @Field("social_type") social_type: String?): Call<JsonObject>

    @POST("activityFilter")
    @FormUrlEncoded
    fun activityFilter_Api(@Header("security_key") securityKey: String?,
                           @Header("auth_key") authKey: String?,
                           @Field("lat") lat: String?,
                           @Field("long") longitude: String?,
                           @Field("distance") distance: String?,
                           @Field("name") name: String?,
                           @Field("address") address: String?): Call<JsonObject>

    @POST("TraderFilter")
    @FormUrlEncoded
    fun traderFilter_Api(@Header("security_key") securityKey: String?,
                         @Header("auth_key") authKey: String?,
                         @Field("lat") lat: String?,
                         @Field("long") longitude: String?,
                         @Field("distance") distance: String?,
                         @Field("name") name: String?,
                         @Field("address") address: String?): Call<JsonObject>

    @POST("childCareFilter")
    @FormUrlEncoded
    fun childCareFilter_Api(@Header("security_key") securityKey: String?,
                            @Header("auth_key") authKey: String?,
                            @Field("lat") lat: String?,
                            @Field("long") longitude: String?,
                            @Field("distance") distance: String?,
                            @Field("name") name: String?,
                            @Field("address") address: String?,
                            @Field("childCareId") childCareId: String?, ): Call<JsonObject>

    @POST("eventList")
    @FormUrlEncoded
    fun getEventList(@Header("security_key") securityKey: String?,
                     @Header("auth_key") auth_key: String?,
                     @Field("lat") latitude: String?,
                     @Field("long") longitude: String?): Call<JsonObject>

    @POST("getFilter")
    @FormUrlEncoded
    fun getEventFilter(@Header("security_key") securityKey: String?,
                       @Header("auth_key") auth_key: String?,
                       @Field("lat") latitude: String?,
                       @Field("long") longitude: String?,
                       @Field("distance") distance: String?,
                       @Field("name") name: String?,
                       @Field("date") date: String?,
                       @Field("address") address: String?): Call<JsonObject>

    @POST("completeprofile_sociallogin")
    @FormUrlEncoded
    fun get_SocialCompleteApi_withImage(@Header("security_key") securityKey: String?,
                                        @Field("social_id") social_id: String?,
                                        @Field("email") email: String?,
                                        @Field("name") name: String?,
                                        @Field("role") role: String?,
                                        @Field("lat") lat: String?,
                                        @Field("longi") longi: String?,
                                        @Field("second") second: String?,
                                        @Field("city") city: String?,
                                        @Field("image_type") image_type: String?,
                                        @Field("image") image: String?
    ): Call<JsonObject>

    @POST("completeprofile_sociallogin")
    @FormUrlEncoded
    fun get_SocialCompleteApi_withoutImage(@Header("security_key") securityKey: String?,
                                           @Field("social_id") social_id: String?,
                                           @Field("email") email: String?,
                                           @Field("name") name: String?,
                                           @Field("role") role: String?,
                                           @Field("lat") lat: String?,
                                           @Field("longi") longi: String?,
                                           @Field("second") second: String?,
                                           @Field("city") city: String?,
                                           @Field("image_type") image_type: String?
    ): Call<JsonObject>


    companion object {

        operator fun invoke(): JsonPlaceHolder {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY

            val httpClient: OkHttpClient.Builder = OkHttpClient.Builder()
                    .callTimeout(100, TimeUnit.MINUTES)
                    .connectTimeout(100, TimeUnit.SECONDS)
                    .readTimeout(100, TimeUnit.SECONDS)
                    .writeTimeout(100, TimeUnit.SECONDS)
                    .addInterceptor(interceptor)

            httpClient.addInterceptor(object : Interceptor {

                override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
                    val request: Request
                    request = chain.request().newBuilder()
                            .addHeader("Accept", "application/json")
                            .build()
                    return chain.proceed(request)
                }

            })

            return Retrofit.Builder()
                    .baseUrl(base_URL)
                    .client(httpClient.build())
                    .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
                    .build()
                    .create(JsonPlaceHolder::class.java)
        }

    }

}