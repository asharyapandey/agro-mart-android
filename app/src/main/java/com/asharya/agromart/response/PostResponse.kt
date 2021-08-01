package com.asharya.agromart.response

import com.asharya.agromart.model.GetPost

data class PostResponse(
    val developerMessage: String,
    val message: String,
    val page: Int,
    val result: List<GetPost>,
    val success: Boolean,
    val totalCount: Int
)