package com.asharya.agromart.response

import com.asharya.agromart.model.User

data class UserResponse(
    val success: Boolean? = null,
    val message: String,
    val developerMessage: String,
    val result: User? = null,
)
