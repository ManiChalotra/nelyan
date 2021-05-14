package com.nelyan_live.chat

import com.nelyan_live.chat.AbstractModel

class ChatListResponse (
    var id: String = "",
    var senderId: String = "",
    var receiverId: String = "",
    var groupId: String = "",
    var lastMessageId: String = "",
    var deletedId: String = "",
    var created: String = "",
    var updated: String = "",
    var user_id: String = "",
    var lastMessage: String = "",
    var userName: String = "",
    var userImage: String = "",
    var created_at: String = "",
    var messageType: String = "",
    var reportcount: String = "",
    var isOnline: String = "",
    var unreadcount: String = "",
    var blockCount: String = ""
    ) : AbstractModel()