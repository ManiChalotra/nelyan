package com.nelyan_live.modals.homeactivitylist

import java.io.Serializable

data class AgeGroup(
        val activityPostId: Int,
        val ageFrom: String,
        val ageTo: String,
        val createdAt: String,
        val days: String,
        val eventId: Int,
        val id: Int,
        val timeFrom: String,
        val timeTo: String,
        val updatedAt: String
):Serializable