package com.nelyanlive.modals.eventList

data class EventListResponse(
        val code: Int,
        val data: ArrayList<EventData>,
        val msg: String,
        val success: Int
)