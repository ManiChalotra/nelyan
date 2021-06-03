package com.nelyanlive.modals.childcarePostdetails

data class ChildCarePostDetailsResponse(
        val code: Int,
        val data: ChildCareResponseData,
        val msg: String,
        val success: Int
)