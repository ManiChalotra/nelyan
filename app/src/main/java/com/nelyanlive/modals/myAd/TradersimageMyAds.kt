package com.nelyanlive.modals.myAd

import java.io.Serializable

data class TradersimageMyAds(
    /* val createdAt: String,*/
    val id: Int,
    val images: String,
    /*val mediaType: Int,*/
    val tradersId: Int
    /*val updatedAt: String*/
) : Serializable
