package com.nelyanlive.modals.postDetails


data class PostDetailsEvents(
        val activityId: Int,
        val city: String,
        val createdAt: String,
        val description: String,
        val eventstimings: ArrayList<PostDetailsEventstiming>,
        val id: Int,
        val image: String,
        val latitude: String,
        val longitude: String,
        val name: String,
        val price: String,
        val status: Int,
        val updatedAt: String,
        val userId: Int,
        val minAge: String,
        val maxAge: String
)