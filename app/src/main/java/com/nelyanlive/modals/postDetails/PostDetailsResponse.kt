package com.nelyanlive.modals.postDetails

data class PostDetailsResponse(
        val code: Int,
        val data: PostDetailsResponeData,
        val msg: String,
        val success: Int
)