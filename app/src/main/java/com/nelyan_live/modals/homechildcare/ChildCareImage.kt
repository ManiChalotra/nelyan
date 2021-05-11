package com.nelyan_live.modals.homechildcare

import java.io.Serializable

data class ChildCareImage(
    val createdAt: String,
    val id: Int,
    val image: String,
    val postId: Int,
    val updatedAt: String
) :Serializable