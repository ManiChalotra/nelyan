package com.nelyan_live.modals.myFavourite

data class FavouritePost(
    val PostData: PostData,
    val createdAt: String,
    val id: Int,
    val postId: Int,
    val postType: Int,
    val updatedAt: String,
    val userId: Int
)