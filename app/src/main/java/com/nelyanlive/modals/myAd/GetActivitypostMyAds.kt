package com.nelyanlive.modals.myAd

data class GetActivitypostMyAds(
        val activityimages: ArrayList<ActivityimageMyAds>,
        val activityname: String,
        val ageGroups: ArrayList<AgeGroupMyAds>,
        val categoryId: Int,
        val country_code: String,
        val description: String,
        val website: String,
        val events: ArrayList<EventMyAds>,
        val id: Int,
        val nameOfShop: String,
        val minAge: String,
        val maxAge: String,
        val phone: String,
        val typeofActivityId: Int,
        val userId: Int,
        val address: String,
        val city: String,
        val latitude: String,
        val longitude: String
)