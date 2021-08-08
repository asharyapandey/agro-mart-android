package com.asharya.agromart.model

data class Bid(
    val _id: String? = null,
    val address: String? = null,
    val amountOffered: Int = 0,
    val belongsTo: User? = null,
    val createdAt: String? = null,
    val isPostBidAccepted: Boolean = false,
    val post: String? = null,
    val remarks: String? = null,
    val status: String? = null,
    val updatedAt: String? = null,
    val userID: User? = null
)