package com.nelyan_live.chat

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.github.nkzawa.emitter.Emitter
import com.github.nkzawa.socketio.client.IO
import com.github.nkzawa.socketio.client.Socket
import com.nelyan_live.R
import com.nelyan_live.ui.Chat1Activity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

class GroupChatVM :ViewModel() {

    lateinit var socket: Socket
    lateinit var ctx: Context
    lateinit var rvChat: RecyclerView
    var senderID =""
    var userId =""
    var groupName =""
    var block ="0"

    var message : ObservableField<String> = ObservableField("")

    var senderName : ObservableField<String> = ObservableField("")

    var senderImage : ObservableField<String> = ObservableField("")
    var noDataMessage : ObservableField<String> = ObservableField("Loading Chat...")

    val groupChatAdapter by lazy { RecyclerAdapter<ChatData>(R.layout.list_chat) }
     val listChat by lazy { ArrayList<ChatData>() }

    init {

        groupChatAdapter.setOnItemClick(object : RecyclerAdapter.OnItemClick {
            override fun onClick(view: View, position: Int, type: String) {

                when (type) {

                    "delete" -> {
                        dailogDelete(view.context,listChat[position].id,listChat[position].groupId,position)
                    }
                    "flag" -> {
                        showDailog(view.context,listChat[position].senderId,listChat[position].id,listChat[position].groupId)
                    }
                    "chat" -> {

                        disconnectSocket()

                        view.context.startActivity(
                                Intent(view.context, Chat1Activity::class.java)
                                        .putExtra("senderID", listChat[position].senderId)
                                        .putExtra("senderName", listChat[position].senderName)
                                        .putExtra("senderImage", listChat[position].senderImage)
                                        .putExtra("userId", userId)
                        )

                    }
                } } }) }


    fun dailogDelete(context: Context, id: String, groupId: String, position: Int) {
        dialog = Dialog(context)
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.setContentView(R.layout.alert_chat_delete)
        dialog.setCancelable(true)
        val tvYes: TextView = dialog.findViewById(R.id.tvYes)
        val tvNo: TextView = dialog.findViewById(R.id.tvNo)
        tvYes.setOnClickListener {
            dialog.dismiss()
            val json = JSONObject()
            try {
                json.put("userId", userId)
                json.put("messageId", id)
                json.put("groupId", groupId)

                Log.e("socket", "=======$json")
                socket.emit("delete_group_message", json)

                message.set("")
            } catch (e: Exception) {
                e.printStackTrace()
            }

            listChat.removeAt(position)
            groupChatAdapter.removeAtPosition(position)
           // groupChatAdapter.notifyItemRemoved(position)
            groupChatAdapter.notifyItemRangeChanged(position,listChat.size)


        }
        tvNo.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    fun showDailog(context: Context, senderId: String, id1: String, groupId: String) {
        dialog = Dialog(context)
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.setContentView(R.layout.alert_chat_flag)
        dialog.setCancelable(true)
        val btnSubmit: RelativeLayout = dialog.findViewById(R.id.btnSubmit)
        val etReport: EditText = dialog.findViewById(R.id.etReport)
        btnSubmit.setOnClickListener {

            if(etReport.text.toString().isEmpty())
            {
                Toast.makeText(ctx,"please enter message",Toast.LENGTH_SHORT).show()
            }
            else {
                dialog.dismiss()
                val json = JSONObject()
                try {
                    json.put("userId", userId)
                    json.put("user2Id", senderId)
                    json.put("messageId", id1)
                    json.put("groupId", groupId)
                    json.put("comment", etReport.text.toString())

                    Log.e("socket", "=======$json")
                    socket.emit("report_user", json)

                    message.set("")
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        dialog.show()
    }

    fun onClick(view: View, s:String)
    {
        when(s)
        {

            // "three"->{
            //CommonMethods.hideKeyboard(view)
            // threeDialog(view)
            // }
            "ivSend"->{
                if(message.get()!!.trim().isEmpty())
                {
                    Toast.makeText(view.context,"Please enter message", Toast.LENGTH_SHORT).show()

                    // CommonMethods.alertDialog(view.context,"please enter message")
                }
                else {
                    Toast.makeText(view.context,"enter message-----${message.get().toString().trim()}----", Toast.LENGTH_SHORT).show()

                    //  sendChatMessage()
                }
            }
        }
    }


    fun sendChatMessage() {

        val json = JSONObject()
        try
        {
            json.put("senderId", userId)
            json.put("groupName", groupName)
            json.put("messageType", 0)
            json.put("message", message.get().toString())

            Log.e("socket","=======$json")

            socket.emit("send_group_message",json)
            message.set("")
        }

        catch (e: Exception)
        {
            e.printStackTrace()
        }
    }
    fun updateLocation(lat:String,long:String,city:String) {

        val json = JSONObject()
        try
        {
            json.put("latitude", lat)
            json.put("longitude", long)
            json.put("city", city)
            json.put("userId", userId)

            Log.e("socket","=======$json")
            socket.emit("update_location_user",json)

        }

        catch (e: Exception)
        {
            e.printStackTrace()
        }
    }
    private fun clearChat() {

        val json = JSONObject()
        try
        {
            json.put("userId", userId)
            json.put("user2Id", senderID)
            socket.emit("delete_chat",json)

        }

        catch (e: Exception)
        {
            e.printStackTrace()
        }
    }

    fun sendMessage(view: View, rv: RecyclerView)
    {
        rvChat = rv

        if(message.get().toString().trim().isEmpty())
        {

            Toast.makeText(view.context,"Please enter message", Toast.LENGTH_SHORT).show()

            //CommonMethods.alertDialog(view.context,"please enter message")
        }
        else {

           // Toast.makeText(view.context,"enter message-----${message.get().toString().trim()}------", Toast.LENGTH_SHORT).show()

            sendChatMessage()

        }

    }

    fun connectSocket(context: Context) {


        Log.e("socket", "connectSocket connectSocket")

        ctx = context
        try{
            socket = IO.socket("http://3.13.214.27:1052")
            socket.on(Socket.EVENT_CONNECT, onConnect)
            socket.on(Socket.EVENT_DISCONNECT, onDisconnect)
            socket.on(Socket.EVENT_CONNECT_ERROR, onConnectError)
            socket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectTimeout)

            socket.on("connect_user", connectListener)
            socket.on("connect_listener", connectListener)
            socket.on("send_group_message", newMessage)
            socket.on("new_group_message", newMessage)
            socket.on("report_user", reportUser)
            socket.on("report_data", reportUser)
            socket.on("delete_group_message", deleteData)
            socket.on("delete_groupdata", deleteData)
            socket.on("update_location_user", updatedLocation)
            socket.on("get_location", updatedLocation)
            socket.connect()

            Log.e("socket", "sfdafdsfdsfsdf")

        }
        catch (e :Exception){e.printStackTrace()}

    }

    fun disconnectSocket() {
        socket.disconnect()
        Log.e("socket", "chat disconnectSocket")

        socket.off(Socket.EVENT_CONNECT, onConnect)
        socket.off(Socket.EVENT_DISCONNECT, onDisconnect)
        socket.off(Socket.EVENT_CONNECT_ERROR, onConnectError)
        socket.off(Socket.EVENT_CONNECT_TIMEOUT, onConnectTimeout)

        socket.off("connect_user", connectListener)
        socket.off("connect_listener", connectListener)
        socket.off("send_group_message", newMessage)
        socket.off("new_group_message", newMessage)
        socket.off("report_user", reportUser)
        socket.off("report_data", reportUser)
        socket.off("delete_group_message", deleteData)
        socket.off("delete_groupdata", deleteData)
        socket.off("update_location_user", updatedLocation)
        socket.off("get_location", updatedLocation)


    }

    private val onConnect = Emitter.Listener {
        Log.e("socket", "chat: onConnect")
        //connectUser()
        //getUserChat()
    }


    private val onDisconnect = Emitter.Listener  { Log.e("socket", "run: disconnect")  }


    private val onConnectError = Emitter.Listener  { Log.e("socket", "run: onConnectError")  }

    private val onConnectTimeout = Emitter.Listener  { Log.e("socket", "run: onConnectTimeout") }


    private val connectListener = Emitter.Listener{
        Log.e("socket", "chat   JOIN")
        Log.e("socket", it[0].toString())
        Log.e("socket", it[0].toString())
    }

    private val reportUser = Emitter.Listener{

        Log.e("socket", "chat    reportUser")
        Log.e("socket", it[0].toString())

        //{
        //  "success_message": "Report Added Successfully"
        //}

        val json = JSONObject(it[0].toString())
        GlobalScope.launch {

            withContext(Dispatchers.Main) {
                Toast.makeText(ctx, "Message reported successfully", Toast.LENGTH_SHORT).show()

            }}
    }
    private val updatedLocation = Emitter.Listener{

        Log.e("socket", "chat    reportUser")
        Log.e("socket", it[0].toString())


    }

    private val newMessage = Emitter.Listener{

        Log.e("socket", "chat    newMessage")
        Log.e("socket", it[0].toString())

        try {

            /*{
                "id": 389,
                "senderId": 188,
                "receiverId": 184,
                "chatConstantId": 1,
                "groupId": 0,
                "message": "hello bhai",
                "readStatus": 0,
                "messageType": 0,
                "deletedId": 0,
                "created": 1620997139,
                "updated": 1620997139,
                "senderName": "ani",
                "senderImage": "",
                "recieverImage": "",
                "recieverName": "Rohit nine"
            }*/


            val json = JSONObject(it[0].toString())
            Log.e("socket===", json.toString())

            val listData : ArrayList<ChatData> = ArrayList()

            listData.add(ChatData(
                    json.getString("id"),
                    json.getString("senderId"),
                    json.getString("receiverId"),
                    "",
                    "",
                    json.getString("message"),
                    json.getString("readStatus"),
                    json.getString("messageType"),
                    json.getString("deletedId"),
                    json.getString("created"),
                    json.getString("updated"),
                    "",
                    json.getString("recieverImage"),
                    json.getString("senderName"),
                    json.getString("senderImage"),
                    userId,"1"

            ))

            GlobalScope.launch {

                withContext(Dispatchers.Main) {

                    listChat.addAll(listData)
                    groupChatAdapter.addAtPosition(listChat,listChat.size-1)

                    rvChat.scrollToPosition(listChat.size-1)
                    Log.e("socket=gsdfgsdfg==chat", listChat.size.toString())

                    if(listChat.isEmpty()) noDataMessage.set("No chat found") else{noDataMessage.set("")}
                }
            }
        }
        catch (e:Exception)
        {e.printStackTrace()}

    }
    private val deleteData = Emitter.Listener{

        Log.e("socket", "chat    deleteData")
        Log.e("socket", it[0].toString())

        //{
        //  "success_message": "Report Added Successfully"
        //}

        val json = JSONObject(it[0].toString())
        GlobalScope.launch {

            withContext(Dispatchers.Main) {
                Toast.makeText(ctx, json.getString("success_message"), Toast.LENGTH_SHORT).show()

            }}
    }

    private fun connectUser() {
        val json = JSONObject()
        try{

            Log.e("socket=connectUser", userId)

            json.put("userId", userId)
            socket.emit("connect_user",json)

        }

        catch (e: Exception)
        {e.printStackTrace()}

    }



    lateinit var dialog: Dialog








}