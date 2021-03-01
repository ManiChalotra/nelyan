package com.meherr.mehar.data.network

import com.google.gson.GsonBuilder
import com.nelyan.utils.base_URL
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


interface JsonPlaceHolder {

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
                .addConverterFactory(
                    GsonConverterFactory.create(
                        GsonBuilder().setLenient().create()
                    )
                )
                .build()
                .create(JsonPlaceHolder::class.java)
        }


    }

}