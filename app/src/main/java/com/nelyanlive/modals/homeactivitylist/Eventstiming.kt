package com.nelyanlive.modals.homeactivitylist

import java.io.Serializable

data class Eventstiming(
    val activityId: Int,
    val createdAt: String,
    val dateFrom: String,
    val dateTo: String,
    val endTime: String,
    val eventId: Int,
    val id: Int,
    val startTime: String,
    val updatedAt: String
) :Serializable