package com.nelyan_live.chat

data class GroupDataResponse(
    var success: Int = 0,
    var code: Int = 0,
    var msg: String = "",
    var `data`: List<ChatData> = listOf()
)
