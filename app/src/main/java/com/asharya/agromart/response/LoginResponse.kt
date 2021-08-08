package com.asharya.agromart.response

data class LoginResponse (
    val success : Boolean? = null,
    val message: String? = null,
    val token : String? = null,
    val userID: String? = null,
)