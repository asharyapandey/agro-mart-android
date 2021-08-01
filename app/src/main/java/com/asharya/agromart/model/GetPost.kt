package com.asharya.agromart.model

data class GetPost(
    val _id: String,
    val address: String,
    val category: String,
    val createdAt: String,
    val description: String,
    val farmerPrice: Float,
    val image: String,
    val kalimatiPrice: Int,
    val name: String,
    val productName: String,
    val unit: String,
    val user: User
)