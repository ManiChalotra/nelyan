package com.nelyan_live.utils.fcm

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.datastore.preferences.core.preferencesKey
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.nelyan_live.R
import com.nelyan_live.db.DataStoragePreference
import com.nelyan_live.ui.MainActivity
import com.nelyan_live.utils.AppController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.json.JSONObject
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

        val dataStoragePreference= DataStoragePreference(AppController.getInstance())
        launch {
            dataStoragePreference.save(refreshedToken, preferencesKey("device_token"))
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.e("new_message", "=======$remoteMessage")
        Log.e("new_message", "=======${remoteMessage.data}")


        val data1 = remoteMessage.data


        Log.e("new_message", "=======${data1["body"]!!}")
        Log.e("new_message", "=======${data1["data"]!!}")

        val jsonMain = JSONObject(data1["data"]!!.toString())


        val id  = jsonMain.getString("senderId")
        val name  = jsonMain.getString("senderName")
        val image  = jsonMain.getString("senderImage")
        val userId  = jsonMain.getString("receiverId")


        Log.e("new_message", "=======${userId}")


        // Log.e("new_message", "=======${remoteMessage.notification!!.title}")
        //  Log.e("new_message", "=======${remoteMessage.notification!!.title}")
        //  Log.e("new_message", "=======${remoteMessage.notification!!.body}")

        /* {
             "message": "A new movie test push again has been added.",
             "data": {
             "message": "A new movie test push again has been added.",
             "deviceType": 0,
             "deviceToken": "c1ob-ZUgTd6__baW1KOy8Q:APA91bE5lkwK5AzwQGFI2SOkueae6n4vSMMzrJcXw3SFQoJTTyIi4r2b_UwUPmNsNRiSGwlEouo7q7ZklxK6EuYyaMST7mCXtGbgjTYL7eMdla69GqCwlS4g8E67lElNGM4K4W4xb2t0",
             "receiverId": 108,
             "movie": {
             "created": "1609502644",
             "id": 34,
             "status": 1,
             "title": "test push again",
             "description": "test",
             "image": "http://localhost:8011/uploads/movies/aaa9ff9f-59d3-4a13-a05b-3cde82d02442.jpeg",
             "audio": "http://localhost:8011/uploads/movies/eeb77f0b-8a8c-42f6-b26e-9a215c2b5e19.mp3",
             "priceWithAdd": "20",
             "priceWithoutAdd": "10",
             "genreId": 5,
             "createdAt": "2021-01-01T12:04:04.000Z",
             "updatedAt": "2021-01-01T12:04:04.000Z"
         },
             "code": 1
         },
             "priority": "high"
         }*/

        /*{body=ani Sent You a Message, data={"readStatus":0,"created":1622098609,
            "groupId":0,"chatConstantId":22,"deletedId":0,
            "recieverName":"rohit sevenn",
            "message":"hello bhai 2","senderImage":"",
            "senderId":188,"senderName":"ani","receiverId":181,"messageType":0,"id":567,
            "recieverImage":"b98e0a97-2367-4804-89d5-65d9561e51fb.jpg","updated":1622098609}}*/

        //showSmallNotification(data1["message"]!!, data1["title"]!!)

        //  Log.e("msg", "onMessageReceived: " + remoteMessage.data["message"])


        //                                Intent(view.context, Chat1Activity::class.java)
        //                                        .putExtra("senderID", listChat[position].senderId)
        //                                        .putExtra("senderName", listChat[position].senderName)
        //                                        .putExtra("senderImage", listChat[position].senderImage)
        //                                        .putExtra("userId", userId)
        //                        )


        val intent = Intent(this, MainActivity::class.java)
                //.putExtra("senderID", id)
                //.putExtra("senderName", name)
               // .putExtra("senderImage", image)
               // .putExtra("userId", userId)
               // .putExtra("disconnect", userId)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)


       // SocketManager.disconnectSocketMain()

        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
        val channelId = "Default"

        val builder = NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(data1["body"]!!).setAutoCancel(true)
                .setContentIntent(pendingIntent)
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "Default channel", NotificationManager.IMPORTANCE_DEFAULT)
            manager.createNotificationChannel(channel)
        }

        val am: ActivityManager = AppController.mInstance.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val cn: String = am.getRunningTasks(1)[0].toString()


        Log.e("new_message", "==212=====${cn}")

        manager.notify(0, builder.build())

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
        private var i = 0
    }
}