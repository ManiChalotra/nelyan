package com.nelyan_live.modals.homeactivitylist

import android.graphics.Bitmap
import java.io.Serializable

data class Activityimage(
        val activityId: Int,
        val id: Int,
        val images: String,
        var bitmap: Bitmap?
) :Serializable