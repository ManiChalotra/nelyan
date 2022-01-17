package com.nelyanlive.modals.myAd

import java.io.Serializable

data class TraderProductMyAds(
    /*  val createdAt: String,*/
    var description: String,
    var id: Int,
    var image: String,
    var price: String,
    var title: String,
    var traderId: Int
    /*val updatedAt: String*/
) :Serializable
