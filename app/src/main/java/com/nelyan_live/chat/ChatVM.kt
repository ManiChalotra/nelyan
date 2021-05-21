package com.nelyan_live.chat

import android.app.Dialog
import android.content.Context
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.github.nkzawa.emitter.Emitter
import com.github.nkzawa.socketio.client.IO
import com.github.nkzawa.socketio.client.Socket
import com.nelyan_live.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

class ChatVM :ViewModel() {

    lateinit var socket: Socket
    lateinit var ctx: Context
    lateinit var rvChat: RecyclerView
    var senderID =""
    var userId =""
    var block ="0"

    var message : ObservableField<String> = ObservableField("")
    var senderName : ObservableField<String> = ObservableField("")
    var senderImage : ObservableField<String> = ObservableField("")
    var noDataMessage : ObservableField<String> = ObservableField("Loading Chat...")

    val chatAdapter by lazy { RecyclerAdapter<ChatData>(R.layout.list_chat) }
     val listChat by lazy { ArrayList<ChatData>() }

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
    private fun blockUser(s: String) {

        val json = JSONObject()
        try
        {
            json.put("userId", userId)
            json.put("user2Id", senderID)
            json.put("status", if(s=="Block") "1" else "0")
            socket.emit("block_user",json)

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

            //CommonMethods.alertDialog(view.context,"please enter message")
        }
        else {
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
            socket.on("get_message", chatList)
            socket.on("get_data_message", chatList)
            socket.on("send_message", newMessage)
            socket.on("new_message", newMessage)
            socket.on("block_user", blockData)
            socket.on("block_data", blockData)
            socket.on("delete_chat", deleteData)
            socket.on("delete_data", deleteData)
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
        socket.off("get_message", chatList)
        socket.off("get_data_message", chatList)
        socket.off("send_message", newMessage)
        socket.off("new_message", newMessage)
        socket.off("block_user", blockData)
        socket.off("block_data", blockData)
        socket.off("delete_chat", deleteData)
        socket.off("delete_data", deleteData)


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
                    userId

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

    }
    private val blockData = Emitter.Listener{

        Log.e("socket", "chat    blockData")
        Log.e("socket", it[0].toString())

        val json = JSONObject(it[0].toString())

        if(""==json.getString("userId"))  block = json.getString("block_data")


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
                    userId

            ))

            GlobalScope.launch {

                withContext(Dispatchers.Main) {

                    listChat.addAll(listData)
                    chatAdapter.addAtPosition(listChat,listChat.size-1)

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

    private fun getUserChat() {
        val json = JSONObject()
        try{

            json.put("senderId", userId)
            json.put("receiverId", senderID)
            socket.emit("get_message",json)

        }

        catch (e: Exception)
        {e.printStackTrace()}

    }

    lateinit var dialog: Dialog


}