package com.nelyanlive.modals.childcarePostdetails

data class ChildCareResponseData(
    val ChildCareImages: ArrayList<ChildCareImage>,
    val ChildcareType: Int,
    val availableplace: Int,
    val city: String,
    val countryCode: String,
    val createdAt: String,
    val description: String,
    val id: Int,
    val ispublished: Int,
    val latitude: String,
    val longitude: String,
    val name: String,
    val phone: String,
    val status: Int,
    val type: Int,
    val updatedAt: String,
    val userId: Int
)