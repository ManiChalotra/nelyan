package com.nelyan_live.modals.myAd

data class GetActivitypostMyAds(
        val activityimages: ArrayList<ActivityimageMyAds>,
        val activityname: String,
        /*val adMsg: String,*/
        val ageGroups: ArrayList<AgeGroupMyAds>,
        val categoryId: Int,
        val country_code: String,
       /* val createdAt: String,*/
        val description: String,
        val latitude: String,
        val longitude: String,
        val events: ArrayList<EventMyAds>,
        val id: Int,
        /*val isPublished: Int,*/
        val nameOfShop: String,
        val phone: String,
        /*val status: Int,*/
        val typeofActivityId: Int,
        /*val updatedAt: String,*/
        val userId: Int,
        val address: String,
        val city: String
)