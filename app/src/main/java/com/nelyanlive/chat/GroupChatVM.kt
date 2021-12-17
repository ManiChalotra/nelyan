package com.nelyanlive.chat

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
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
import com.nelyanlive.AppUtils.hideKeyboard
import com.nelyanlive.R
import com.nelyanlive.fullscreen.FullScreen
import com.nelyanlive.modals.postDetails.Activityimage
import com.nelyanlive.ui.Chat1Activity
import com.nelyanlive.utils.from_admin_image_base_URl
import com.nelyanlive.utils.socketBaseUrl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.java_websocket.util.Base64
import org.json.JSONObject
import java.nio.charset.StandardCharsets
import java.util.*

class GroupChatVM : ViewModel() {
    lateinit var socket: Socket
    lateinit var ctx: Context
    lateinit var rvChat: RecyclerView
    var userId = ""
    var groupName = ""
    var groupId = ""
    var notifyStatus = "0"
    var replayerId = ""
    var selectedImage = ""
    var message: ObservableField<String> = ObservableField("")
    var replymessage: ObservableField<String> = ObservableField("")
    var replymessageUserName: ObservableField<String> = ObservableField("")
    var searchVisible: ObservableField<String> = ObservableField("")
    var serchempty: ObservableField<String> = ObservableField("")
    var senderName: ObservableField<String> = ObservableField("")
    var senderImage: ObservableField<String> = ObservableField("")
    var noDataMessage: ObservableField<String> = ObservableField("")

    val groupChatAdapter by lazy {
        RecyclerAdapterChat<ChatData>(
            R.layout.chat_text_left,
            R.layout.chat_text_right,
            R.layout.chat_image_right,
            R.layout.chat_image_left
        )
    }

    val listChat by lazy { ArrayList<ChatData>() }

    init {
        groupChatAdapter.setOnItemClick(object : RecyclerAdapterChat.OnItemClick {
            override fun onClick(view: View, position: Int, type: String) {
                when (type) {
                    "delete" -> {
                        dailogDelete(
                            view.context,
                            listChat[position].id,
                            listChat[position].groupId,
                            position
                        )
                    }
                    "flag" -> {
                        showDailog(
                            view.context,
                            listChat[position].senderId,
                            listChat[position].id,
                            listChat[position].groupId
                        )
                    }
                    "replyclick" -> {
                        for (i in 0 until listChat.size) {
                            Log.e(
                                "checkpozz",
                                "----" + listChat[position].parentId + "-----" + (listChat[i].id) + "---" + listChat.size
                            )
                            if (listChat[position].parentId.equals(listChat[i].id)) {
                                try {
                                    rvChat.getLayoutManager()!!.scrollToPosition(i)
                                } catch (e: Exception) {

                                }
                            }
                        }
                    }
                    "arrayreply" -> {
                        replymessage.set("'" + listChat[position].message + "'")
                        replymessageUserName.set(listChat[position].senderName)
                        replayerId = listChat[position].id

                        //                        var filterPopUp: PopupWindow? = null
                        //                        val v: View? = LayoutInflater.from(ctx).inflate(R.layout.res_reply, null)
                        //                        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
                        //                        // Creating the PopupWindow
                        //                        filterPopUp = PopupWindow(view.context)
                        //                        filterPopUp.setContentView(v)
                        //                        filterPopUp.setOutsideTouchable(true)
                        //                        filterPopUp.setWidth(200)
                        //                        filterPopUp.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT)
                        //                        val llReply = v!!.findViewById<LinearLayout>(R.id.llReply)
                        //                        llReply.setOnClickListener {
                        //                            replymessage.set("'" + listChat[position].message + "'")
                        //                            replymessageUserName.set(listChat[position].senderName)
                        //                            replayerId = listChat[position].id
                        //                            filterPopUp.dismiss()
                        //                        }
                        //
                        //                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        //                            filterPopUp.showAsDropDown(view, -0, 0, Gravity.RIGHT)
                        //                        }
                        //                        filterPopUp.setFocusable(true)
                    }
                    "chat" -> {
                        disconnectSocket()
                        view.context.startActivity(
                            Intent(view.context, Chat1Activity::class.java).putExtra(
                                "senderID", listChat[position].senderId
                            ).putExtra("senderName", listChat[position].senderName)
                                .putExtra("senderImage", listChat[position].senderImage)
                                .putExtra("userId", userId)
                        )

                    }
                    "fullscreen" -> {
                        val listActivityimage = ArrayList<Activityimage>()
                        listActivityimage.clear()
                        var totalimages = 0
                        var findpoz = 0
                        for (i in 0 until listChat.size) {
                            if (listChat[i].messageType.equals("1")) // Image
                            {
                                totalimages++
                                Log.e("checkimage", "===" + listChat[i].message)
                                listActivityimage.add(
                                    Activityimage(
                                        0,
                                        listChat[i].id.toInt(),
                                        from_admin_image_base_URl + listChat[i].message.toString(),
                                        1
                                    )
                                )
                            }
                            if (listChat[i].message.equals(listChat[position].message)) // Image
                            {
                                findpoz = totalimages
                            }
                        }

                        (view.context as Activity).startActivity(
                            Intent(view.context, FullScreen::class.java).putExtra(
                                "image", findpoz.toString()
                            ).putExtra("imagearry", listActivityimage)
                        )
                    }
                }
            }
        })
    }

    fun serchList(text: String) {
        var listfilter = ArrayList<ChatData>()
        if (text.isNotEmpty()) {
            for (i in 0 until listChat.size) {
                if (listChat.get(i).message.toLowerCase()
                        .contains(text.toLowerCase()) || listChat.get(i).description.toLowerCase()
                        .contains(text.toLowerCase())
                ) {
                    listfilter.add(listChat.get(i))
                }
            }
            noDataMessage.set("")
            //  val a = listChat.filter { text.equals(it.message, true) || text.equals(it.description, true) }
            groupChatAdapter.addItems(listfilter)
            groupChatAdapter.notifyDataSetChanged()
        } else {
            groupChatAdapter.addItems(listChat)
            groupChatAdapter.notifyDataSetChanged()
            rvChat.getLayoutManager()!!.scrollToPosition(listChat.size - 1)
        }
    }

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
            groupChatAdapter.removeAtPosition(position) // groupChatAdapter.notifyItemRemoved(position)
            groupChatAdapter.notifyItemRangeChanged(position, listChat.size)

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

            if (etReport.text.toString().trim().isEmpty()) {
                Toast.makeText(ctx, "please enter message", Toast.LENGTH_SHORT).show()
            } else {
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

    fun onClick(view: View, s: String) {
        when (s) {
            "ivSend" -> {
                if (message.get()!!.trim().isEmpty()) {
                    Toast.makeText(
                        view.context,
                        view.context.getString(R.string.please_enter_message),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        view.context,
                        "enter message-----${message.get().toString().trim()}----",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            "replyclose" -> {
                replymessage.set("")
            }
            "searchoption" -> {
                searchVisible.set("true")
                serchempty.set("")
                // view.requestFocus()
                val imm =
                    view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
            }
            "Serchclose" -> {
                searchVisible.set("")
                serchempty.set("")
                groupChatAdapter.addItems(listChat)
                groupChatAdapter.notifyDataSetChanged()
                rvChat.getLayoutManager()!!.scrollToPosition(listChat.size - 1)
                hideKeyboard(view.context as Activity)
            }
        }
    }

    fun sendChatMessage(msgStg: String) {

        val json = JSONObject()
        try { //encoding
            val ps: String = msgStg
            val tmp = Base64.encodeBytes(ps.toByteArray())

            json.put("senderId", userId)
            json.put("groupName", groupName)
            json.put("messageType", 0)
            json.put("message", tmp)

            Log.e("socket", "=======$json")

            socket.emit("send_group_message", json)
            message.set("")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun sendReplyChatMessage() {

        val json = JSONObject()
        try { //encoding
            val ps: String = message.get().toString()
            val tmp = Base64.encodeBytes(ps.toByteArray())

            json.put("senderId", userId)
            json.put("groupName", groupName)
            json.put("messageType", 0)
            json.put("message", tmp)
            json.put("parentId", replayerId)
            Log.e("socket", "=======$json")

            socket.emit("send_group_message", json)
            message.set("")
        } catch (e: Exception) {
            e.printStackTrace()
        }
        replymessage.set("")
    }

    fun sendChatImageMessage(str: String) {

        val json = JSONObject()
        try { //encoding
            val messagetext: String =
                message.get()
                    .toString() // val messagetext = Base64.encodeBytes(messagemj.toByteArray())

            val ps: String = str
            val tmp = Base64.encodeBytes(ps.toByteArray())
            json.put("senderId", userId)
            json.put("groupName", groupName)
            json.put("messageType", 1)

            if (messagetext.isNotEmpty()) { // message with image
                json.put("message", tmp)
                json.put("description", messagetext)
                Log.e("send_group_message", "==With Image=====$json")
            } else {
                json.put("message", tmp)
                json.put("description", "")
                Log.e("send_group_message", "=======$json")
            }
            socket.emit("send_group_message", json)
            message.set("")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun updateLocation(lat: String, long: String, city: String) {

        val json = JSONObject()
        try {
            json.put("latitude", lat)
            json.put("longitude", long)
            json.put("city", city)
            json.put("userId", userId)

            Log.e("socket", "=======$json")
            socket.emit("update_location_user", json)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /* fun sendMessage(view: View, rv: RecyclerView)
     {
       rvChat = rv

       if (message.get().toString().trim().isEmpty())
       {
         Toast.makeText(view.context, view.context.getString(R.string.please_enter_message), Toast.LENGTH_SHORT).show()
       } else
       {

         if (selectedImage.isNotEmpty())
         {
           sendChatImageMessage(selectedImage)
           selectedImage=""
         }
         else
         {
           sendChatMessage()
           selectedImage=""
         }
       }
     }*/

    fun connectSocket(context: Context) {

        Log.e("socket", "connectSocket connectSocket")
        ctx = context
        try {
            socket = IO.socket(socketBaseUrl)
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

        } catch (e: Exception) {
            e.printStackTrace()
        }

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
        connectUser() //getUserChat()
    }

    private val onDisconnect = Emitter.Listener { Log.e("socket", "run: disconnect") }

    private val onConnectError = Emitter.Listener { Log.e("socket", "run: onConnectError") }

    private val onConnectTimeout = Emitter.Listener { Log.e("socket", "run: onConnectTimeout") }

    private val connectListener = Emitter.Listener {
        Log.e("socket", "chat   JOIN")
        Log.e("socket", it[0].toString())
    }

    private val reportUser = Emitter.Listener {

        Log.e("socket", "chat    reportUser")
        Log.e("socket", it[0].toString())

        GlobalScope.launch {

            withContext(Dispatchers.Main) {
                Toast.makeText(ctx, "Message reported successfully", Toast.LENGTH_SHORT).show()

            }
        }
    }
    private val updatedLocation = Emitter.Listener {

        Log.e("socket", "chat    reportUser")
        Log.e("socket", it[0].toString())

    }


    private val newMessage = Emitter.Listener {

        Log.e("socketmychat", "chat    newMessage")
        Log.e("socket", it[0].toString())

        try {

            val json = JSONObject(it[0].toString())
            Log.e("socket===", json.toString())

            val listData: ArrayList<ChatData> = ArrayList()

            //decoding
            val lineSep = System.getProperty("line.separator")
            val ps2: String = json.getString("message")
            val tmp2: ByteArray = Base64.decode(ps2)
            val val2 = String(tmp2, StandardCharsets.UTF_8)
            val val3 = val2.replace("<br />".toRegex(), lineSep!!)

            val lineSepparentmessage = System.getProperty("line.separator")
            val ps2parentmessage: String = json.getString("parentmessage")
            val tmp2parentmessage: ByteArray = Base64.decode(ps2parentmessage)
            val val2parentmessage = String(tmp2parentmessage, StandardCharsets.UTF_8)
            val parentmessage =
                val2parentmessage.replace("<br />".toRegex(), lineSepparentmessage!!)

            listData.add(
                ChatData(
                    json.getString("id"),
                    json.getString("senderId"),
                    json.getString("receiverId"),
                    "",
                    json.getString("groupId"),
                    val3,
                    json.getString("readStatus"),
                    json.getString("messageType"),
                    json.getString("deletedId"),
                    json.getString("created"),
                    json.getString("updated"),
                    "",
                    json.getString("recieverImage"),
                    json.getString("senderName"),
                    json.getString("senderImage"),
                    userId,
                    "1",
                    false,
                    json.getString("description"),
                    json.getString("parentId"),
                    parentmessage, json.getString("parentmessageType")

                )
            )
            groupId = json.getString("groupId")

            GlobalScope.launch {

                withContext(Dispatchers.Main) {

                    listChat.addAll(listData)
                    groupChatAdapter.addAtPosition(listChat, listChat.size - 1)

                    rvChat.scrollToPosition(listChat.size - 1)
                    Log.e("socket=gsdfgsdfg==chat", listChat.size.toString())
                    if (listChat.isEmpty()) noDataMessage.set("No chat found") else {
                        noDataMessage.set("")
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private val deleteData = Emitter.Listener {

        Log.e("socket", "chat    deleteData")
        Log.e("socket", it[0].toString())

        //{"messageId":"1403"}

        val json = JSONObject(it[0].toString())
        GlobalScope.launch {

            withContext(Dispatchers.Main) {
                for (i in 0 until listChat.size) {
                    if (listChat[i].id == json.getString("messageId")) {
                        listChat.removeAt(i)
                        groupChatAdapter.removeAtPosition(i)
                        groupChatAdapter.notifyItemRangeChanged(i, listChat.size)
                    }
                    Log.d(GroupChatVM::class.java.name, "GroupChatVM_deleteData   ")
                }
            }
        }
    }

    private fun connectUser() {
        val json = JSONObject()
        try {
            Log.e("socket=connectUser", userId)
            json.put("userId", userId)
            socket.emit("connect_user", json)

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    lateinit var dialog: Dialog
}