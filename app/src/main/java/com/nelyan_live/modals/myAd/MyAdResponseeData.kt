package com.nelyan_live.modals.myAd

data class MyAdResponseeData(
        val GetTrader: ArrayList<GetTraderMyAds>,
        val getActivitypost: ArrayList<GetActivitypostMyAds>,
        val getChildcarePosts: ArrayList<GetChildcarePostMyAds>
)