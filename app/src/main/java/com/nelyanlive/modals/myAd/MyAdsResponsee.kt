package com.nelyanlive.modals.myAd

data class MyAdsResponsee(
        val code: Int,
        val data: MyAdResponseeData,
        val msg: String,
        val success: Int
)