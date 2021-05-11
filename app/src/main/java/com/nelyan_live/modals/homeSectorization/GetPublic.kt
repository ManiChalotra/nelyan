package com.nelyan_live.modals.homeSectorization

data class GetPublic(
        val city_id: Int,
        val cityname: String,
        val createdAt: String,
        val id: Int,
        val phone: String,
        val school_address: String,
        val school_id: Int,
        val school_level: String,
        val school_name: String,
        val school_site: String,
        val sectorizationimages: ArrayList<SectorizationimagePublic>,
        val status: Int,
        val typeOfSchool: Int,
        val updatedAt: String
)