package com.asharya.agromart.model

data class Product(
    val _id: String,
    val category: Category,
    val createdAt: String,
    val isArchived: Boolean,
    val kalimatiPrice: Int,
    val productName: String,
    val slug: String,
    val unit: Unit,
    val updatedAt: String
)


