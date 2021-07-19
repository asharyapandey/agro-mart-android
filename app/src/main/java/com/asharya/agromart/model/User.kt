package com.asharya.agromart.model

data class User(
    val phoneNumber: String,
    val fullName: String,
    val password: String,
    val permissionLevel: Int,
    val image: String? = null
)
