package com.nelyanlive.modals.hometraderpostlist

import java.io.Serializable

data class TraderProduct(
    val createdAt: String,
    val description: String,
    val id: Int,
    val image: String,
    val price: String,
    val title: String,
    val traderId: Int,
    val updatedAt: String
) :Serializable