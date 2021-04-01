package com.nelyan_live.modals

import okhttp3.MultipartBody
import okhttp3.RequestBody

sealed  class ModelPOJO {
    data class AgeGroupDataModel(var ageFrom:String?, var ageTo:String?, var days:String?, var timeFrom:String?, var timeTo:String?)
    data class AddEventDataModel( var image:String?, var name:String?,  var dateFrom:String?, var dateTo:String?, var timeFrom:String?,
                                  var timeTo:String, var description:String?, var price :String?, var city:String?, var lati:String?, var longi:String?)
    data class  UploadImageUrlDataModel(var path: MultipartBody.Part?)
}

