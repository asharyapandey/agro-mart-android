package com.asharya.agromart.response

import com.asharya.agromart.model.Bid

data class BidsResponse(
    val success: Boolean? = null,
    val message: String? = null,
    val developerMessage: String? = null,
    val result: List<Bid>? = null,
    val totalCount: Int = 0,
    val maxBid: Int = 0,
)
