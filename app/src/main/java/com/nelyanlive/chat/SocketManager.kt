package com.nelyanlive.chat

import android.util.Log
import com.github.nkzawa.emitter.Emitter
import com.github.nkzawa.socketio.client.IO
import org.json.JSONObject

object SocketManager {


    var socket = IO.socket("http://3.13.214.27:1052")

    fun connectSocket(event:String,fn:Emitter.Listener)
    {

        Log.e("socket","========0====$event=")

        if(socket.connected())
        {
            Log.e("socket","========1=====")
        }

        socket.on(event, fn)
       if(!socket.connected())
       {
        socket.connect()
       }
    }


    fun emitData(event:String,json:JSONObject)
    {
        socket.emit(event,json)

        Log.e("socket","===emit=====0====$event=")
        Log.e("socket","===emit=====0====$json=")
    }

}