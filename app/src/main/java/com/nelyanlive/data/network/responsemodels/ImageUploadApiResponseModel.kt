package com.nelyanlive.data.network.responsemodels

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName


class ImageUploadApiResponseModel {
    @SerializedName("status")
    @Expose
    var status: Int? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("data")
    @Expose
    var data: List<Datum>? = null

    class Datum {
        @SerializedName("image")
        @Expose
        var image: String? = null

        @SerializedName("thumbnail")
        @Expose
        var thumbnail: String? = null

        @SerializedName("fileName")
        @Expose
        var fileName: String? = null

        @SerializedName("folder")
        @Expose
        var folder: String? = null

        @SerializedName("file_type")
        @Expose
        var fileType: String? = null

    }

}