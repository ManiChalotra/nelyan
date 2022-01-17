package com.nelyanlive.modals.traderPostDetails

data class TraderDaysTiming(
    val createdAt: String,
    val day: String,
    val endTime: String,
    val id: Int,
    val secondEndTime: String,
    val secondStartTime: String,
    val startTime: String,
    val traderId: Int,
    val updatedAt: String
)