package com.nelyanlive.chat

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.github.nkzawa.emitter.Emitter
import com.github.nkzawa.socketio.client.IO
import com.github.nkzawa.socketio.client.Socket
import com.nelyanlive.R
import com.nelyanlive.ui.Chat1Activity
import com.nelyanlive.utils.socketBaseUrl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.java_websocket.util.Base64
import org.json.JSONObject
import java.nio.charset.StandardCharsets

class MessagesVM : ViewModel() {

    lateinit var socket: Socket
    lateinit var ctx: Context

    var userId = ""
    var noDataMessage: ObservableField<String> = ObservableField("")

    val membersAdapter by lazy { RecyclerAdapter<ChatListResponse>(R.layout.list_message) }
    val listMembers by lazy { ArrayList<ChatListResponse>() }

    init {

        membersAdapter.setOnItemClick(object : RecyclerAdapter.OnItemClick {
            override fun onClick(view: View, position: Int, type: String) {

                when (type) {
                    "chat" -> {
                        disconnectSocket()
                        view.context.startActivity(Intent(view.context, Chat1Activity::class.java)
                                        .putExtra("senderID", listMembers[position].user_id)
                                        .putExtra("senderName", listMembers[position].userName)
                                        .putExtra("senderImage", listMembers[position].userImage)
                                        .putExtra("userId", userId)
                        )
                    }
                    "delete" -> {
                        delDialog(listMembers[position].user_id)
                    }
                }
            }
        })
    }

    fun onClick(view: View, s: String) {
        when (s) {
            "noData" -> {

            }
        }
    }

    fun connectSocket(context: Context) {

        ctx = context

        Log.e("socket", "connectSocket")

        try {
            socket = IO.socket(socketBaseUrl)
            socket.on(Socket.EVENT_CONNECT, onConnect)
            socket.on(Socket.EVENT_DISCONNECT, onDisconnect)
            socket.on(Socket.EVENT_CONNECT_ERROR, onConnectError)
            socket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectTimeout)

            // socket.on("order accept", onOrderAccept)
            socket.on("connect_user", connectListener)
            socket.on("connect_listener", connectListener)
            socket.on("chat_listing", chatList)
            socket.on("chat_message", chatList)
            socket.on("send_message", newMessage)
            socket.on("new_message", newMessage)
            socket.on("delete_chat_listing", deleteChat)
            socket.on("chat_list_data", deleteChat)
            socket.on("seen_unseen_msg", seenMessages)

            if (!socket.connected()) {
                socket.connect()
            } else {
                connectUser()
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun disconnectSocket() {
        socket.disconnect()
        Log.e("socket", "disconnectSocket")

        socket.off(Socket.EVENT_CONNECT, onConnect)
        socket.off(Socket.EVENT_DISCONNECT, onDisconnect)
        socket.off(Socket.EVENT_CONNECT_ERROR, onConnectError)
        socket.off(Socket.EVENT_CONNECT_TIMEOUT, onConnectTimeout)

        socket.off("connect_user", connectListener)
        socket.off("connect_listener", connectListener)
        socket.off("chat_listing", chatList)
        socket.off("chat_message", chatList)
        socket.off("send_message", newMessage)
        socket.off("new_message", newMessage)
        socket.off("delete_chat_listing", deleteChat)
        socket.off("chat_list_data", deleteChat)
        socket.off("seen_unseen_msg", seenMessages)

    }

    private val onConnect = Emitter.Listener {
        Log.e("socket", "call: onConnect")
        connectUser()

    }


    private val onDisconnect = Emitter.Listener { Log.e("socket", "run: disconnect") }


    private val onConnectError = Emitter.Listener {

        Log.e("socket", "run: onConnectError")
        // connectSocket(ctx)
    }

    private val onConnectTimeout = Emitter.Listener {

        Log.e("socket", "run: onConnectTimeout")
        //  connectSocket(ctx)

    }

    private val connectListener = Emitter.Listener {
        Log.e("socket", "JOIN")
        Log.e("socket", it[0].toString())
        getUserList()
    }

    private val newMessage = Emitter.Listener {

        Log.e("socket", "chat    newMessage")
        Log.e("socket", it[0].toString())

        try {

            getUserList()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private val seenMessages = Emitter.Listener {

        Log.e("socket", "chat    seenMessages")
        Log.e("socket", it[0].toString())

        try {

            getUserList()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private val deleteChat = Emitter.Listener {

        Log.e("socket", "chat    deleteChat")
        Log.e("socket", it[0].toString())

        try {
            getUserList()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun delDialog(id: String) {
        val dialog = Dialog(ctx)
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.setContentView(R.layout.alert_chat_delete)
        dialog.setCancelable(true)
        val tvNo: TextView = dialog.findViewById(R.id.tvNo)
        val tvYes: TextView = dialog.findViewById(R.id.tvYes)
        tvNo.setOnClickListener {
            dialog.dismiss()
        }

        tvYes.setOnClickListener {
            dialog.dismiss()

            deleteChatMessages(id)
        }

        dialog.show()

    }

    private fun deleteChatMessages(id: String) {
        val json = JSONObject()
        try {

            json.put("userId", userId)
            json.put("user2Id", id)
            socket.emit("delete_chat_listing", json)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    private val chatList = Emitter.Listener {

        Log.e("socket", "chatList")
        Log.e("socket", "====${it[0]}===")
        Log.e("socket", it[0].toString())

        try {
            val obj = JSONObject("{\"body\":${it[0]}}")
            Log.e("socket===", obj.toString())

            val listData: ArrayList<ChatListResponse> = ArrayList()
            val jsonArray = obj.getJSONArray("body")

            for (i in 0 until jsonArray.length()) {
                val json = jsonArray.getJSONObject(i)

                //decoding
                val lineSep = System.getProperty("line.separator")
                val ps2: String = json.getString("lastMessage")
                val tmp2: ByteArray = Base64.decode(ps2)
                val val2 = String(tmp2, StandardCharsets.UTF_8)
                val val3 = val2.replace("<br />".toRegex(), lineSep!!)


                listData.add(ChatListResponse(
                        json.getString("id"),
                        json.getString("senderId"),
                        json.getString("receiverId"),
                        json.getString("groupId"),
                        json.getString("lastMessageId"),
                        json.getString("deletedId"),
                        json.getString("created"),
                        json.getString("updated"),
                        json.getString("user_id"),
                        if (json.getString("messageType") == "1") {
                            "image"
                        } else {
                            val3
                        },
                        json.getString("userName"),
                        json.getString("userImage"),
                        json.getString("created_at"),
                        json.getString("messageType"),
                        "",
                        json.getString("isOnline"),
                        json.getString("unreadcount"),
                        json.getString("readStatus")

                ))
            }


            GlobalScope.launch {

                withContext(Dispatchers.Main) {
                    listMembers.clear()
                    listMembers.addAll(listData)
                    membersAdapter.addItems(listMembers)
                    Log.e("socket=sdffd==", listMembers.size.toString())

                    if (listMembers.isEmpty()) noDataMessage.set("No messages found") else noDataMessage.set("")

                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun connectUser() {
        val json = JSONObject()
        try {

            json.put("userId", userId)
            socket.emit("connect_user", json)

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun getUserList() {
        val json = JSONObject()
        try {
            Log.e("socket", "getUserList")

            json.put("userId", userId)

            Log.e("socket", "====$json")

            socket.emit("chat_listing", json)

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}