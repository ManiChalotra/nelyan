package com.nelyan_live.modals.traderPostDetails

data class TraderPostDetailsResponse(
        val code: Int,
        val data: ArrayList<TraderPostDetailsData>,
        val msg: String,
        val success: Int
)