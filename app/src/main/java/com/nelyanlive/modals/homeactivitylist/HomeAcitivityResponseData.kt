package com.nelyanlive.modals.homeactivitylist

import java.io.Serializable

data class HomeAcitivityResponseData(
        val activityimages: ArrayList<Activityimage>,
        val activityname: String,
        val adMsg: String,
        val address: String,
        val minAge: String,
        val maxAge: String,
        val ageGroups: ArrayList<AgeGroup>,
        val categoryId: Int,
        val city: String,
        val country_code: String,
        val createdAt: String,
        val description: String,
        val events: ArrayList<Event>,
        val id: Int,
        val isPublished: Int,
        val latitude: String,
        val longitude: String,
        val nameOfShop: String,
        val phone: String,
        val status: Int,
        val typeofActivityId: Int,
        val updatedAt: String,
        val isFav: Int,
        val userId: Int
):Serializable