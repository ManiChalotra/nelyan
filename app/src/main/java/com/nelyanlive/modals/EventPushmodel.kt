package com.nelyanlive.modals

data class EventPushmodel(
    var success: Int = 0, // 1
    var code: Int = 0, // 200
    var msg: String = "", //  Event push Status updated !!
    var `data`: Data = Data()
) {

    class Data
}