package com.nelyan_live.modals.activity_type

data class ActivityTypeResponse(
    val code: Int,
    val data: List<Data>,
    val msg: String,
    val success: Int
){

data class Data(
      /*  val created: Int,*/
        val id: Int,
        val name: String,
       /* val updated: Int*/
)
}