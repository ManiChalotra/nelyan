package com.nelyanlive.modals.homechildcare

import java.io.Serializable

data class HomeChildCareeData(
        internal val ChildCareImages: ArrayList<ChildCareImage>,
        internal val ChildcareType: Int,
        internal val availableplace: Int,
        internal val city: String,
        internal val countryCode: String,
        internal val createdAt: String,
        internal val description: String,
        internal val id: Int,
        internal val ispublished: Int,
        internal val latitude: String,
        internal val longitude: String,
        internal val name: String,
        internal val phone: String,
        internal val status: Int,
        internal val type: Int,
        internal val updatedAt: String,
        internal val userId: Int,
        internal val isFav: Int
) :Serializable