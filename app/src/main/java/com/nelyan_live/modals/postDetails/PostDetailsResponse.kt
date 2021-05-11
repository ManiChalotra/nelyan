package com.nelyan_live.modals.postDetails

data class PostDetailsResponse(
        val code: Int,
        val data: PostDetailsResponeData,
        val msg: String,
        val success: Int
)