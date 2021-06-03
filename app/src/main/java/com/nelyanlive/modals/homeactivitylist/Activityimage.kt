package com.nelyanlive.modals.homeactivitylist

import java.io.Serializable

data class Activityimage(
        val activityId: Int,
        val id: Int,
        val images: String,
        val mediaType: Int
) :Serializable