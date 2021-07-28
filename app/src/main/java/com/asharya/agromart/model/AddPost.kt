package com.asharya.agromart.model

data class AddPost(
    val address: String,
    val description: String,
    val farmerPrice: String,
    var image: String,
    val name: String,
    val product: String
)