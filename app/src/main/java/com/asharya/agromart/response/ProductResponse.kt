package com.asharya.agromart.response

import com.asharya.agromart.model.Product

data class ProductResponse(
    val developerMessage: String,
    val message: String,
    val result: List<Product>,
    val success: Boolean
)