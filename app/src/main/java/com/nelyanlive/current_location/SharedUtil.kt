package com.nelyanlive.current_location

import android.content.Context
import android.location.Location
import androidx.core.content.edit
import com.nelyanlive.R

internal object SharedUtil {

    const val KEY_FOR_ENABLED = "trackFor"
    const val KEY_FOR_Update = "update"

    fun getLocationTrackingPref(context: Context): Boolean =
        context.getSharedPreferences(
            "preference_file_key", Context.MODE_PRIVATE)
            .getBoolean(KEY_FOR_ENABLED, false)

    fun saveLocationTrackingPref(context: Context, requestingLocationUpdates: Boolean) =
        context.getSharedPreferences(
            "preference_file_key",
            Context.MODE_PRIVATE).edit {
            putBoolean(KEY_FOR_ENABLED, requestingLocationUpdates)
        }


    fun getPref(context: Context): Boolean =
        context.getSharedPreferences(
            "update", Context.MODE_PRIVATE)
            .getBoolean(KEY_FOR_Update, false)

    fun savePref(context: Context, value: Boolean) =
        context.getSharedPreferences(
            "update",
            Context.MODE_PRIVATE).edit {
            putBoolean(KEY_FOR_Update, value)
        }
}