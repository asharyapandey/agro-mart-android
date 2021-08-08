package com.asharya.agromart.response

import com.asharya.agromart.model.Bid

data class BidResponse(
    val success: Boolean? = null,
    val message: String? = null,
    val developerMessage: String? = null,
    val result: Bid? = null,
)
