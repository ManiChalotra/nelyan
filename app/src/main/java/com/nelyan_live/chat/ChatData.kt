package com.nelyan_live.chat

data class ChatData(
    var id: String = "",
    var senderId: String = "",
    var receiverId: String = "",
    var chatConstantId: String = "",
    var groupId: String = "",
    var message: String = "",
    var readStatus: String = "",
    var messageType: String = "",
    var deletedId: String = "",
    var created: String = "",
    var updated: String = "",
    var recieverName: String = "",
    var recieverImage: String = "",
    var senderName: String = "",
    var senderImage: String = "",
    var myID: String = "",
    var isFlag: String = "",
    var dateShown: Boolean = false


) : AbstractModel()