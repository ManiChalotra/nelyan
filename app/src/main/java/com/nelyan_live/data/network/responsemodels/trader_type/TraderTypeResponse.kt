package com.nelyan_live.data.network.responsemodels.trader_type

data class TraderTypeResponse(
    var code: Int,
    var `data`: List<Data>,
    var msg: String,
    var success: Int
) {
    data class Data(
        var createdAt: Int,
        var id: Int,
        var name: String,
        var updatedAt: Int
    ){
        override fun toString(): String {
            return name
        }
    }


}