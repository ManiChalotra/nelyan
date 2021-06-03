package com.nelyanlive.modals.homeSectorization

data class HomeSectorizationResponse(
        val code: Int,
        val data: HomeSectorizationResponseData,
        val msg: String,
        val success: Int
)