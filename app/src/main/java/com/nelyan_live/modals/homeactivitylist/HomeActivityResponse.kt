package com.nelyan_live.modals.homeactivitylist

data class HomeActivityResponse(
    val code: Int,
    val data: ArrayList<HomeAcitivityResponseData>,
    val msg: String,
    val success: Int
)