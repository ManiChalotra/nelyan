package com.nelyanlive.utils

import android.content.Context
import android.util.Log
import org.json.JSONObject
import retrofit2.Response


class ErrorBodyResponse<T> {

    private var response: Response<T>
    private var progressBar: com.tuyenmonkey.mkloader.MKLoader?
    private var context: Context

    constructor(response: Response<T>, context: Context, progressBar: com.tuyenmonkey.mkloader.MKLoader?) {
        this.response = response
        this.context = context
        this.progressBar = progressBar

        handleErrorResponse()

    }

    fun handleErrorResponse() {
        try {
            val jObjError = JSONObject(response.errorBody()!!.string())
            Log.e("responseError", jObjError.toString())
            val msg = jObjError.getString(MESSAGE)
            failureMethod(context, msg, progressBar)
        } catch (ex: Exception) {
            ex.printStackTrace()
            // comment
          //  failureMethod(context, ex.localizedMessage, progressBar)
        }
    }

}