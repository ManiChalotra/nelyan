package com.nelyanlive.chat

abstract class AbstractModel {
    var adapterPosition: Int = -1
    var onItemClick: RecyclerAdapter.OnItemClick? = null
    var onItemClickChat: RecyclerAdapterChat.OnItemClick? = null
}