package com.nelyan_live.modals.homechildcare

data class HomeChildcareResponse(
        val code: Int,
        val data: ArrayList<HomeChildCareResponseData>,
        val msg: String,
        val success: Int
)