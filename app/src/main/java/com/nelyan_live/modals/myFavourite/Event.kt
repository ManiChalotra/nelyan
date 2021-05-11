package com.nelyan_live.modals.myFavourite


data class Event(
        val activityId: Int,
        val city: String,
        val createdAt: String,
        val description: String,
        val eventstimings: ArrayList<Eventstiming>,
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