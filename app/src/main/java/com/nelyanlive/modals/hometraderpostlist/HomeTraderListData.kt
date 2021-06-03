package com.nelyanlive.modals.hometraderpostlist

import java.io.Serializable

data class HomeTraderListData(
        val address: String,
        val categoryId: Int,
        val city: String,
        val country_code: String,
        val createdAt: String,
        val dateFrom: String,
        val dateTo: String,
        val description: String,
        val email: String,
        val id: Int,
        val isPublished: Int,
        val latitude: String,
        val longitude: String,
        val nameOfShop: String,
        val phone: String,
        val status: Int,
        val traderDaysTimings: ArrayList<TraderDaysTiming>,
        val traderProducts: ArrayList<TraderProduct>,
        val tradername: String,
        val tradersimages: ArrayList<TradersPostListimage>,
        val typeofTraderId: Int,
        val updatedAt: String,
        val userId: Int,
        val isFav: Int,
        val website: String
): Serializable