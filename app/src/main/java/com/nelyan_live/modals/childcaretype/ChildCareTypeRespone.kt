package com.nelyan_live.modals.childcaretype

data class ChildCareTypeRespone(
    val code: Int,
    val data: List<Data>,
    val msg: String,
    val success: Int
){
    data class Data(
            val id: Int,
            val categoryName: String,
          //  val createdAt: String,
           // val updatedAt: String
    )
}