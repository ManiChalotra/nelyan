package com.nelyanlive.chat

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.github.nkzawa.emitter.Emitter
import com.github.nkzawa.socketio.client.IO
import com.github.nkzawa.socketio.client.Socket
import com.nelyanlive.R
import com.nelyanlive.fullscreen.FullScreen
import com.nelyanlive.utils.socketBaseUrl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ChatVM :ViewModel() {

    lateinit var socket: Socket
    lateinit var ctx: Context
    lateinit var rvChat: RecyclerView
    var senderID =""
    var userId =""
    var block ="0"

    var chatRoom = ""

    /*init {
        socket = SocketManager.socket
    }*/


    var message : ObservableField<String> = ObservableField("")
    var senderName : ObservableField<String> = ObservableField("")
    var senderImage : ObservableField<String> = ObservableField("")
    var noDataMessage : ObservableField<String> = ObservableField("Loading Chat...")

    val chatAdapter by lazy { RecyclerAdapterChat<ChatData>(R.layout.chat_text_left,R.layout.chat_text_right,R.layout.chat_image_right,R.layout.chat_image_left) }
     val listChat by lazy { ArrayList<ChatData>() }


    init {

        chatAdapter.setOnItemClick(object : RecyclerAdapterChat.OnItemClick {
            override fun onClick(view: View, position: Int, type: String) {

                when (type) {
                    "fullscreen" -> {
                        // disconnectSocket()

                        (view.context as Activity).startActivity(
                            Intent(view.context, FullScreen::class.java)
                            .putExtra("image",listChat[position].message))

                    }
                } } }) }


    fun onClick(view: View, s:String)
    {
        when(s)
        {

            "ivSend"->{
                if(message.get().toString().isEmpty())
                {
                    Toast.makeText(view.context,"Please enter message",Toast.LENGTH_SHORT).show()
                }
                else {
                    sendChatMessage()
                } } } }

    fun sendChatMessage() {

        val json = JSONObject()
        try
        {
            json.put("senderId", userId)
            json.put("receiverId", senderID)
            json.put("messageType", 0)
            json.put("message", message.get().toString())

            Log.e("send_message","=======$json")

            socket.emit("send_message",json)

            message.set("")
        }

        catch (e: Exception)
        {
            e.printStackTrace()
        }
    }
    fun sendChatImageMessage(str: String) {

        val json = JSONObject()
        try
        {
            json.put("senderId", userId)
            json.put("receiverId", senderID)
            json.put("messageType", 1)
            json.put("message", str)

            Log.e("send_message","=======$json")

            socket.emit("send_message",json)

            message.set("")
        }

        catch (e: Exception)
        {
            e.printStackTrace()
        }
    }

    fun sendMessage(view: View,rv:RecyclerView)
    {
        rvChat = rv

        if(message.get().toString().trim().isEmpty())
        {

            Toast.makeText(view.context,"Please enter message",Toast.LENGTH_SHORT).show()

        }
        else {
            sendChatMessage()
        }

    }

    fun connectSocket(context: Context) {

        Log.e("socket", "connectSocket connectSocket")

        ctx = context
        try{
            socket = IO.socket(socketBaseUrl)

            Log.e("socket", "=======${socketBaseUrl}")
            Log.e("socket", "=======${socket.connected()}")

            socket.on(Socket.EVENT_CONNECT, onConnect)
            socket.on(Socket.EVENT_DISCONNECT, onDisconnect)
            socket.on(Socket.EVENT_CONNECT_ERROR, onConnectError)
            socket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectTimeout)
            socket.on("connect_user", connectListener)
            socket.on("connect_listener", connectListener)
            socket.on("get_message", chatList)
            socket.on("get_data_message", chatList)
            socket.on("send_message", newMessage)
            socket.on("new_message", newMessage)
            socket.on("block_user", blockData)
            socket.on("block_data", blockData)
            socket.on("delete_chat", deleteData)
            socket.on("delete_data", deleteData)
            socket.on("seen_unseen", seenMessages)
            socket.on("seen_unseen_msg", seenMessages)
            if(!socket.connected()) {
                socket.connect()
            }
            else
            {
                connectUser()
                getUserChat()
            }

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
        socket.off("get_message", chatList)
        socket.off("get_data_message", chatList)
        socket.off("send_message", newMessage)
        socket.off("new_message", newMessage)
        socket.off("block_user", blockData)
        socket.off("block_data", blockData)
        socket.off("delete_chat", deleteData)
        socket.off("delete_data", deleteData)
        socket.off("seen_unseen", seenMessages)
        socket.off("seen_unseen_msg", seenMessages)


    }

    private val onConnect = Emitter.Listener {
        Log.e("socket", "chat: onConnect")
        connectUser()
        getUserChat()

    }

    private val onDisconnect = Emitter.Listener  { Log.e("socket", "run: disconnect")  }

    private val onConnectError = Emitter.Listener  { Log.e("socket", "run: onConnectError")  }

    private val onConnectTimeout = Emitter.Listener  { Log.e("socket", "run: onConnectTimeout") }

    private val connectListener = Emitter.Listener{
        Log.e("socket", "chat   JOIN")
        Log.e("socket", it[0].toString())
        Log.e("socket", it[0].toString())
    }

    private val chatList = Emitter.Listener{

        Log.e("socket", "chat    chatList")
        Log.e("socket", it[0].toString())

        try {
            val obj = JSONObject("{\"body\":${it[0]}}")
            Log.e("socket===", obj.toString())

            val listData : ArrayList<ChatData> = ArrayList()
            val jsonArray = obj.getJSONArray("body")

            for(i in 0 until jsonArray.length())
            {
                val json = jsonArray.getJSONObject(i)

                /*[
                    {
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
                        "recieverName": "Rohit nine",
                        "recieverImage": "",
                        "senderName": "ani",
                        "senderImage": ""
                    }
                ]*/

              //  block = json.getString("blockCount")

                listData.add(ChatData(
                    json.getString("id"),
                    json.getString("senderId"),
                    json.getString("receiverId"),
                    json.getString("chatConstantId"),
                    "",
                    json.getString("message"),
                    json.getString("readStatus"),
                    json.getString("messageType"),
                    json.getString("deletedId"),
                    json.getString("created"),
                    json.getString("updated"),
                    json.getString("recieverName"),
                    json.getString("recieverImage"),
                    json.getString("senderName"),
                    if(json.getString("senderId") == userId){json.getString("senderImage")}else{json.getString("recieverImage")},
                    userId,
                        "",
                        if(i==0){true}
                        else{checkDateCompare(json.getString("created"),listData[i-1].created)}

                ))
            }

            GlobalScope.launch {

                withContext(Dispatchers.Main) {
                    listChat.clear()
                    listChat.addAll(listData)
                   // listChat.reverse()
                    chatAdapter.addItems(listChat)
                    rvChat.scrollToPosition(listChat.size-1)

                    Log.e("socket=gsdfgsdfg==chat", listChat.size.toString())

                    if(listChat.isEmpty()) noDataMessage.set("No chat found") else{noDataMessage.set("")}

                }
            }

        }
        catch (e:Exception)
        {e.printStackTrace()}

        setSeenStatus()

    }

    private fun checkDateCompare(created: String, created1: String): Boolean {


        val date1 = SimpleDateFormat("dd").format(Date(created.toLong() * 1000))
        val date2 = SimpleDateFormat("dd").format(Date(created1.toLong() * 1000))

        return date1!=date2
    }

    private val blockData = Emitter.Listener{

        Log.e("socket", "chat    blockData")
        Log.e("socket", it[0].toString())

        val json = JSONObject(it[0].toString())

        if(""==json.getString("userId"))  block = json.getString("block_data")


    }


    private val seenMessages = Emitter.Listener{

        if(mySeen)
        {
        Log.e("socket", "chat    seenMessages")
        Log.e("socket", it[0].toString())

        GlobalScope.launch {

            withContext(Dispatchers.Main) {

               // listChat.addAll(listData)

                for(i in 0 until listChat.size)
                {
                    if(listChat[i].readStatus=="0")
                    {
                        listChat[i].readStatus="1"
                        chatAdapter.notifyItemChanged(i)
                    }
                }

              //  chatAdapter.addAtPosition(listChat,listChat.size-1)

               // rvChat.scrollToPosition(listChat.size-1)
                Log.e("socket=gsdfgsdfg==chat", listChat.size.toString())

                if(listChat.isEmpty()) noDataMessage.set("No chat found") else{noDataMessage.set("")}
            }
        }}
        else
        {
            mySeen = true
        }

    }




    private val newMessage = Emitter.Listener{

        Log.e("socket", "chat    newMessage")
        Log.e("socket", it[0].toString())

        try {

            /*{
                "id": 451,
                "senderId": 188,
                "receiverId": 197,
                "chatConstantId": 9,
                "groupId": 0,
                "message": "hello 197",
                "readStatus": 0,
                "messageType": 0,
                "deletedId": 0,
                "created": 1621493605,
                "updated": 1621493605,
                "senderName": "ani",
                "senderImage": "",
                "recieverImage": "a9e5857f-0c7e-40ce-b87f-69cde43a844c.jpg",
                "recieverName": "ramu"
            }*/
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

            val newMessageRoomId = if(json.getString("senderId").toInt()<json.getString("receiverId").toInt())
                                   {"${json.getString("senderId")}${json.getString("receiverId")}"}else{"${json.getString("receiverId")}${json.getString("senderId")}"}

            if(newMessageRoomId==chatRoom) {
                val listData: ArrayList<ChatData> = ArrayList()



                listData.add(
                    ChatData(
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
                        userId

                    )
                )

                GlobalScope.launch {

                    withContext(Dispatchers.Main) {

                        listChat.addAll(listData)
                        chatAdapter.addAtPosition(listChat, listChat.size - 1)

                        rvChat.scrollToPosition(listChat.size - 1)
                        Log.e("socket=gsdfgsdfg==chat", listChat.size.toString())

                        if (listChat.isEmpty()) noDataMessage.set("No chat found") else {
                            noDataMessage.set("")
                        }
                    }
                }
            }

        }
        catch (e:Exception)
        {e.printStackTrace()}
        setSeenStatus()

    }
    private val deleteData = Emitter.Listener{

        Log.e("socket", "chat    deleteData")
        Log.e("socket", it[0].toString())

        GlobalScope.launch {

            withContext(Dispatchers.Main) {

                listChat.clear()
                chatAdapter.addItems(listChat)

                Log.e("socket=gsdfgsdfg==chat", listChat.size.toString())

                if(listChat.isEmpty()) noDataMessage.set("No chat found") else{noDataMessage.set("")}
            }
        }

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

    var mySeen = false

    private fun setSeenStatus() {
        val json = JSONObject()
        try{
            mySeen = false
            Log.e("socket=connectUser", userId)

            json.put("userId", userId)
            json.put("user2Id", senderID)
            socket.emit("seen_unseen",json)

        }

        catch (e: Exception)
        {e.printStackTrace()}

    }

    private fun getUserChat() {
        val json = JSONObject()
        try{

            chatRoom = if(userId.toInt()<senderID.toInt()){"$userId$senderID"}else{"$senderID$userId"}
            json.put("senderId", userId)
            json.put("receiverId", senderID)
            socket.emit("get_message",json)

        }

        catch (e: Exception)
        {e.printStackTrace()}

    }

    lateinit var dialog: Dialog


}