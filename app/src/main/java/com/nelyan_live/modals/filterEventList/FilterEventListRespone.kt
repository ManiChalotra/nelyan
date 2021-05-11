package com.nelyan_live.modals.filterEventList

data class FilterEventListRespone(
        val code: Int,
        val data: ArrayList<FilterListResponseData>,
        val msg: String,
        val success: Int
)