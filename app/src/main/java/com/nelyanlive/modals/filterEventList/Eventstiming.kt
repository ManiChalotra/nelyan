package com.nelyanlive.modals.filterEventList

data class Eventstiming(
    val activityId: Int,
    val createdAt: String,
    val dateFrom: String,
    val dateFromTimestamp: Int,
    val dateTo: String,
    val dateToTimestamp: Int,
    val endTime: String,
    val eventId: Int,
    val id: Int,
    val startTime: String,
    val updatedAt: String
)