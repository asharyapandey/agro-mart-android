package com.asharya.agromart.model

data class Product(
    val _id: String,
    val category: String,
    val createdAt: String,
    val isArchived: Boolean,
    val kalimatiPrice: Int,
    val productName: String,
    val slug: String,
    val unit: String,
    val updatedAt: String
)