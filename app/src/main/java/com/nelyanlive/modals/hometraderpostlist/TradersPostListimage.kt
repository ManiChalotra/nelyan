package com.nelyanlive.modals.hometraderpostlist

import java.io.Serializable

data class TradersPostListimage(
    val createdAt: String,
    val id: Int,
    val images: String,
    val mediaType: Int,
    val tradersId: Int,
    val updatedAt: String
) :Serializable