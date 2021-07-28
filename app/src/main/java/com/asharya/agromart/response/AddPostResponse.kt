package com.asharya.agromart.response

import com.asharya.agromart.model.AddPost

data class AddPostResponse(
    val success: Boolean? = null,
    val message: String? = null,
    val developerMessage: String? = null,
    val result: AddPost? = null,
)
