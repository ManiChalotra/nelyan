package com.nelyanlive.chat

data class GroupDataResponse(
    var success: Int = 0,
    var code: Int = 0,
    var msg: String = "",
    var `data`: List<ChatData> = listOf()
)
