package com.nelyan_live.chat

abstract class AbstractModel {
    var adapterPosition: Int = -1
    var onItemClick: RecyclerAdapter.OnItemClick? = null
}