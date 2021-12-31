package com.nelyanlive.utils.fcm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.datastore.preferences.core.preferencesKey
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.nelyanlive.R
import com.nelyanlive.db.DataStoragePreference
import com.nelyanlive.ui.Chat1Activity
import com.nelyanlive.ui.HomeActivity
import com.nelyanlive.utils.AppController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.util.*
import kotlin.coroutines.CoroutineContext

class MyFirebaseMessagingService : FirebaseMessagingService(), CoroutineScope {

    var title: String? = ""
    var message: String? = ""
    var notificationManager: NotificationManager? = null
    var notifyID = 1

    val dataStoragePreference by lazy { DataStoragePreference(this) }

    private var job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job


    override fun onNewToken(refreshedToken: String) {
        super.onNewToken(refreshedToken)
        Log.d(TAG, "Refreshed token: $refreshedToken")
        Log.d("fcm_token", "-----$refreshedToken")

        val dataStoragePreference = DataStoragePreference(AppController.getInstance())
        launch {
            dataStoragePreference.saveFCM(refreshedToken, preferencesKey("device_token"))
        }
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.e("new_message", "=======")
        Log.e("new_message", "=======$remoteMessage")
        Log.e("new_message", "=======${remoteMessage.data}")
        var myId = ""

        val data1 = remoteMessage.data

        Log.e("new_message", "=======${data1["body"]!!}")
        Log.e("new_message", "=======${data1["data"]!!}")


        var senderID=""
        var name =""
        var image =""
        var userId=""
        var groupId=1
        try {
            val jsonMain = JSONObject(data1["data"]!!.toString())
             senderID = jsonMain.getString("senderId")
             name = jsonMain.getString("senderName")
             image = jsonMain.getString("senderImage")
             userId = jsonMain.getString("receiverId")
             groupId = jsonMain.getInt("groupId")
        }catch (e:Exception)
        {

        }

        Log.e("new_message", "=======${userId}")
        Log.e("new_message", "=======${senderID}")
        var intent1 = Intent()
        if(!senderID.equals(""))
        {
            intent1 = Intent(this, Chat1Activity::class.java)
                .putExtra("senderID", senderID)
                .putExtra("senderName", name)
                .putExtra("senderImage", image)
                .putExtra("userId", userId)
                .putExtra("disconnect", userId)
        }
        else
        {

            intent1 = Intent(this, HomeActivity::class.java)
                .putExtra("eventfragment", "EventFragment")
        }

        intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)

        val pendingIntent = PendingIntent.getActivity(this, 0, if (groupId == 0) {
                intent1
            }
        else {
            val intent = Intent(this, HomeActivity::class.java)
              //  .putExtra("groupChat", "true")
                .putExtra("eventfragment", "EventFragment")
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            }, PendingIntent.FLAG_ONE_SHOT
        )


        val channelId = getString(R.string.default_notification_channel_id)
        var numMessages = 0;

        val builder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(getString(R.string.app_name))
            .setContentText(data1["body"]!!).setAutoCancel(true)
            .setContentIntent(pendingIntent)

//        builder.setContentText(currentText).setNumber(++numMessages);

        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Default channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            manager.createNotificationChannel(channel)

        }

        Log.e("new_message", "==212=222====${myId}")

        launch(Dispatchers.Main.immediate) {
            myId = DataStoragePreference(AppController.getInstance()).emitStoredValue(
                preferencesKey<String>("id")
            ).first()

            Log.e("new_message", "==212=1111111====${myId}")
            Log.e("new_message", "==212=324234====${currentChatUser}")
            Log.e("new_message", "==212=111324234231111====${senderID}")
            Log.e("new_message", "==212=111324234231111====${myId}")
            Log.e("new_message", "==212=111324234231111==564==${groupId}")

            if (groupId == 0) {
                chatNotification.value = "true"
            }

            if (senderID != myId && senderID != currentChatUser) {

                if (myChatVisible) {
                    manager.notify(((Date().time / 1000L % Int.MAX_VALUE).toInt()), builder.build())
                } else {
                    if (groupId == 0) {
                        manager.notify(((Date().time / 1000L % Int.MAX_VALUE).toInt()),
                            builder.build())
                    }
                }
            }
            else
            {
                manager.notify(((Date().time / 1000L % Int.MAX_VALUE).toInt()), builder.build())
            }
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

    companion object {
        private const val TAG = "MyFirebaseMsgService"
        var myChatVisible = true
        var currentChatUser = ""
        var chatNotification: MutableLiveData<String> = MutableLiveData()
        var chatNotifyLive: LiveData<String> = chatNotification

    }
}