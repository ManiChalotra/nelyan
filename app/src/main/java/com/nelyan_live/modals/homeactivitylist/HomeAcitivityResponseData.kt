package com.nelyan_live.modals.homeactivitylist

data class HomeAcitivityResponseData(
    val activityimages: ArrayList<Activityimage>,
    val activityname: String,
    val adMsg: String,
    val ageGroups: ArrayList<AgeGroup>,
    val categoryId: Int,
    val country_code: String,
    val createdAt: String,
    val description: String,
    val events: ArrayList<Event>,
    val id: Int,
    val isPublished: Int,
    val nameOfShop: String,
    val phone: String,
    val status: Int,
    val typeofActivityId: Int,
    val updatedAt: String,
    val userId: Int
)