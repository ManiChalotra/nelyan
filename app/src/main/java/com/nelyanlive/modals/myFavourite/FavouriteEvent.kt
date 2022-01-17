package com.nelyanlive.modals.myFavourite



data class FavouriteEvent(
        val createdAt: String,
        val event: Event,
        val eventId: Int,
        val id: Int,
        val updatedAt: String,
        val userId: Int,
        val image: Int
)