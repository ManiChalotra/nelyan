package com.nelyanlive.modals

 data class HomeEventModel(var id: String, var img: String, var eventName: String, var eventLocation: String,
                           var eventStartDate: String,var eventEndDate: String,
                           var eventStartTime: String, var eventEndTime: String,
                           var eventPrice: String, var eventDesc: String, var isFav: String
                           , var activityId: String, var latitude: String, var longitude: String)