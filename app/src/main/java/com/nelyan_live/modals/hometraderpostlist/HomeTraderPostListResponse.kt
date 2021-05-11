package com.nelyan_live.modals.hometraderpostlist

data class HomeTraderPostListResponse(
        val code: Int,
        val data: ArrayList<HomeTraderListData>,
        val msg: String,
        val success: Int
)