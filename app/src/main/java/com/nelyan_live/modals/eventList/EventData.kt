package com.nelyan_live.modals.eventList

data class EventData(
    val activityId: Int,
    val createdAt: String,
    val description: String,
    val distance: Int,
    val id: Int,
    val image: String,
    val latitude: String,
    val longitude: String,
    val name: String,
    val price: String,
    val status: Int,
    val updatedAt: String,
    val userId: Int
)