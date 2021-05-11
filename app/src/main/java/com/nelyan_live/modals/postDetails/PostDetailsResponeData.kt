package com.nelyan_live.modals.postDetails

data class PostDetailsResponeData(
        val activityimages: ArrayList<Activityimage>,
        val activityname: String,
        val adMsg: String,
        val address: String,
        val ageGroups: ArrayList<AgeGroup>,
        val categoryId: Int,
        val city: String,
        val country_code: String,
        val createdAt: String,
        val description: String,
        val events: ArrayList<PostDetailsEvents>,
        val id: Int,
        val isPublished: Int,
        val latitude: String,
        val longitude: String,
        val nameOfShop: String,
        val phone: String,
        val status: Int,
        val typeofActivityId: Int,
        val updatedAt: String,
        val userId: Int
)