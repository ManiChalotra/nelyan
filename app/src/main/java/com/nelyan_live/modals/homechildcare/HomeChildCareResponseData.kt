package com.nelyan_live.modals.homechildcare

data class HomeChildCareResponseData(
    val addInfo: String,
    val address: String,
    val availablePlace: String,
    val availableplace: String,
    val babysitterimages: ArrayList<Babysitterimage>,
    val categoryId: Int,
    val city: String,
    val countryCode: String,
    val createdAt: String,
    val description: String,
    val id: Int,
    val ispublished: Int,
    val latitude: String,
    val longitude: String,
    val name: String,
    val nurseryasistantimages: ArrayList<Nurseryasistantimage>,
    val nurseryimages: ArrayList<Nurseryimage>,
    val phone: String,
    val status: Int,
    val updatedAt: String,
    val userId: Int
)