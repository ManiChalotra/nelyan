package com.nelyan_live.modals.traderPostDetails

data class TraderProduct(
    val createdAt: String,
    val description: String,
    val id: Int,
    val image: String,
    val price: String,
    val title: String,
    val traderId: Int,
    val updatedAt: String
)