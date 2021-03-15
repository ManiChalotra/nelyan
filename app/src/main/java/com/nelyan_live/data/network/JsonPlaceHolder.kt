package com.meherr.mehar.data.network

import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.nelyan_live.data.network.responsemodels.ImageUploadApiResponseModel
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
    fun getSignUp_withImage_APi(
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

    @POST("forgot_password")
    @FormUrlEncoded
    fun get_ForgetPassword_Api(@Header("security_key") securityKey: String?,
                               @Field("email") email: String?): Call<JsonObject>

    @GET("getHomeCategories")
    fun get_HomeCategory_Api(@Header("security_key") securityKey: String?,
                             @Header("auth_key") auth_key: String?): Call<JsonObject>

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


    @Multipart
    @POST("imageUplaod")
    fun get_ImageUpload_Api(@Part("type") type: RequestBody?,
                            @Part("folder") folder: RequestBody?,
                            @Part image: List<MultipartBody.Part>?): Call<ImageUploadApiResponseModel>


    @GET("activityTpe")
    fun get_ActivityType_Api(@Header("security_key") securityKey: String?,
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
                        @Field("message") message: String,
                        @Field("phone") phone: String,
                        @Field("address") address: String,
                        @Field("city") city: String,
                        @Field("latitude") latitude: String,
                        @Field("longitude") longitude: String,
                        @Field("ageGroup") ageGroup: String,// sending json Array
                        @Field("addEvent") addEvent: String,// sending json Array
                        @Field("media") media: String,// send json Array
    ): Call<JsonObject>

    @POST("social_login")
    @FormUrlEncoded
    fun get_SocialLogin_Api(@Header("security_key") securityKey: String?,
    @Field("social_id")social_id:String?,
    @Field("social_type")social_type:String?):Call<JsonObject>


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