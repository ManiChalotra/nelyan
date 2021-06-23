package com.nelyanlive.modals.myAd

import java.io.Serializable

data class TraderDaysTimingMyAds(
    /* val createdAt: String,*/
    val day: String,
    val endTime: String,
    val id: Int,
    val secondEndTime: String,
    val secondStartTime: String,
    val startTime: String,
    val traderId: Int
    /* val updatedAt: String*/
) : Serializable
