package com.nelyanlive.current_location

import android.Manifest
import android.app.*
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.location.Location
import android.os.Binder
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.gms.location.*
import java.util.concurrent.TimeUnit

class LocationService : Service() {

    private var configurationChange = false

    private var serviceRunningInForeground = false

    private val localBinder = LocalBinder()

    private lateinit var notificationManager: NotificationManager

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private lateinit var locationRequest: LocationRequest

    private lateinit var locationCallback: LocationCallback


    private var currentLocation: Location? = null

    override fun onCreate() {
        Log.e("location_changed", "onCreate()")

        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        if (PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION)) {

            fusedLocationProviderClient.lastLocation

            fusedLocationProviderClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    Log.e("location_changed", "==111=====${location}=======")
                    if (location != null) {
                        val intent = Intent(ACTION_FOREGROUND_ONLY_LOCATION_BROADCAST)
                        intent.putExtra(EXTRA_LOCATION, location)
                        LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)
                    }
                }
        }

        locationRequest = LocationRequest.create().apply {

            interval = TimeUnit.SECONDS.toMillis(30)

            fastestInterval = TimeUnit.SECONDS.toMillis(30)

            maxWaitTime = TimeUnit.MINUTES.toMillis(2)

            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)

                Log.e("location_changed", "=======${locationResult.lastLocation}=======")

                currentLocation = locationResult.lastLocation
                val intent = Intent(ACTION_FOREGROUND_ONLY_LOCATION_BROADCAST)
                intent.putExtra(EXTRA_LOCATION, currentLocation)
                LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)
            }
        }
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Log.e("location_changed", "onStartCommand()")

        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent): IBinder {
        Log.e("location_changed", "onBind()")
        stopForeground(true)
        serviceRunningInForeground = false
        configurationChange = false
        return localBinder
    }

    override fun onRebind(intent: Intent) {
        Log.e("location_changed", "onRebind()")

        stopForeground(true)
        serviceRunningInForeground = false
        configurationChange = false
        super.onRebind(intent)
    }

    override fun onUnbind(intent: Intent): Boolean {

        if (!configurationChange && SharedUtil.getLocationTrackingPref(this)) {
            Log.e("location_changed", "Start foreground service")

            serviceRunningInForeground = true
        }

        return true
    }

    override fun onDestroy() {
        Log.e("location_changed", "onDestroy()")
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        configurationChange = true
    }

    fun subscribeToLocationUpdates() {
        Log.e("location_changed", "subscribeToLocationUpdates()")

        SharedUtil.saveLocationTrackingPref(this, true)

        startService(Intent(applicationContext, LocationService::class.java))

        try {
            fusedLocationProviderClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )

        } catch (unlikely: SecurityException) {
            SharedUtil.saveLocationTrackingPref(this, false)
            Log.e(
                "location_changed",
                "Lost location permissions. Couldn't remove updates. $unlikely"
            )
        }
    }

    fun unsubscribeToLocationUpdates() {
        Log.e("location_changed", "unsubscribeToLocationUpdates()")

        try {
            val removeTask = fusedLocationProviderClient.removeLocationUpdates(locationCallback)
            removeTask.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.e("location_changed", "Location Callback removed.")
                    stopSelf()
                } else {
                    Log.e("location_changed", "Failed to remove Location Callback.")
                }
            }
            SharedUtil.saveLocationTrackingPref(this, false)

        } catch (unlikely: SecurityException) {
            SharedUtil.saveLocationTrackingPref(this, true)
            Log.e(
                "location_changed",
                "Lost location permissions. Couldn't remove updates. $unlikely"
            )
        }
    }


    inner class LocalBinder : Binder() {
        internal val service: LocationService
            get() = this@LocationService
    }

    companion object {

        private const val PACKAGE_NAME = "com.nelyanlive"

        internal const val ACTION_FOREGROUND_ONLY_LOCATION_BROADCAST =
            "$PACKAGE_NAME.action.FOREGROUND_ONLY_LOCATION_BROADCAST"

        internal const val EXTRA_LOCATION = "$PACKAGE_NAME.extra.LOCATION"


    }
}