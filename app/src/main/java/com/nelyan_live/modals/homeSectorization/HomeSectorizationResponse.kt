package com.nelyan_live.modals.homeSectorization

data class HomeSectorizationResponse(
        val code: Int,
        val data: ArrayList<HomeSectorizationResponseData>,
        val msg: String,
        val success: Int
)