package com.nelyanlive.modals.homeSectorization

data class GetPrivate(
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
        val sectorizationimages: ArrayList<SectorizationimagePrivate>,
        val status: Int,
        val typeOfSchool: Int,
        val updatedAt: String
)