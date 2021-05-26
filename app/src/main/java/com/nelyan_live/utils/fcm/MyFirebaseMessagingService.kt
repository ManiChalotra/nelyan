package com.nelyan_live.utils.fcm

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.datastore.preferences.core.preferencesKey
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.nelyan_live.R
import com.nelyan_live.db.DataStoragePreference
import com.nelyan_live.utils.AppController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class MyFirebaseMessagingService : FirebaseMessagingService() , CoroutineScope{
    var title: String? = ""
    var message: String? = ""

    //    for oreo
    var CHANNEL_ID = "" // The id of the channel.
    var CHANNEL_ONE_NAME = "Channel One"
    var notificationManager: NotificationManager? = null
    var notificationChannel: NotificationChannel? = null
    var notification_code: String? = null
    var notification: Notification? = null

    private  var job = Job()
    //private  var dataStoragePreference= DataStoragePreference(AppController.getInstance().applicationContext)

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job


    override fun onNewToken(refreshedToken: String) {
        super.onNewToken(refreshedToken)
        Log.d(TAG, "Refreshed token: $refreshedToken")
        Log.d("fcm_token", "-----$refreshedToken")

        val dataStoragePreference= DataStoragePreference(AppController.getInstance().applicationContext)
        launch {
            dataStoragePreference.save(refreshedToken, preferencesKey("device_token"))
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    override fun onMessageReceived(remoteMessage: RemoteMessage) {

        Log.e(TAG, "Notification_Message_Body  ========$remoteMessage")
        Log.e(TAG, "Notification_Message_Body  ========${remoteMessage.notification}")
        Log.e(TAG, "Notification_Message_Body" + remoteMessage.data)

        manager
        CHANNEL_ID = applicationContext.packageName
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_ONE_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationChannel!!.enableLights(true)
            notificationChannel!!.lightColor = Color.RED
            notificationChannel!!.setShowBadge(true)
            notificationChannel!!.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        }

        message = remoteMessage.data["message"]
        notification_code = remoteMessage.data["code"]
        title = remoteMessage.data["title"]
        try {
            if (notification_code == "1") {
                sendMessagePush()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private val manager: NotificationManager?
        get() {
            if (notificationManager == null) {
                notificationManager =
                    getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            }

            return notificationManager
        }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private fun sendMessagePush() {
        val intent: Intent? = null

        intent!!.flags =
            Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        val pendingIntent = PendingIntent.getActivity(this, i, intent, PendingIntent.FLAG_ONE_SHOT)

        val icon1 = BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher)
        val defaultSoundUri =
            RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder =
            Notification.Builder(applicationContext)
                .setSmallIcon(notificationIcon).setLargeIcon(icon1)
                .setStyle(Notification.BigTextStyle().bigText(message))
                .setColor(ContextCompat.getColor(applicationContext, R.color.colorAccent))
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationBuilder.setChannelId(CHANNEL_ID)
            notificationManager!!.createNotificationChannel(notificationChannel!!)
        }
        notification = notificationBuilder.build()
        notificationManager!!.notify(i++, notification)
    }

    private val notificationIcon: Int
        @SuppressLint("ObsoleteSdkInt")
        get() {
            val useWhiteIcon = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
            // return useWhiteIcon ? R.mipmap.ic_notification_trans : R.mipmap.ic_launcher;
            return if (useWhiteIcon) R.mipmap.ic_launcher else R.mipmap.ic_launcher
        }


    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    companion object {
        private const val TAG = "MyFirebaseMsgService"
        private var i = 0
    }
}