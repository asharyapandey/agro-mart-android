package com.asharya.agromart.model

data class User(
    val phoneNumber: String? = null,
    val fullName: String? = null,
    val password: String? = null,
    val permissionLevel: Int = 1,
    val image: String? = null
)
