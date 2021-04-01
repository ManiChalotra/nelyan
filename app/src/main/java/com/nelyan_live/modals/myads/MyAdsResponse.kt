package com.nelyan_live.modals.myads

data class MyAdsResponse(
        val code: Int,
        val data: ArrayList<MyAdsData>,
        val msg: String,
        val success: Int
)