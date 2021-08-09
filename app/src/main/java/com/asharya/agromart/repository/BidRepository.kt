package com.asharya.agromart.repository

import com.asharya.agromart.api.BidAPI
import com.asharya.agromart.api.ServiceBuilder
import okhttp3.MultipartBody
import okhttp3.RequestBody

class BidRepository {
    private val bidApi = ServiceBuilder.buildService(BidAPI::class.java)

    // add bid
    suspend fun addBid(
        postID: String,
        address: String,
        remarks: String,
        amountOffered: String
    ) = bidApi.addBid(
        "Bearer ${ServiceBuilder.token!!}",
        postID,
        amountOffered,
        address,
        remarks
    )

    suspend fun editBid(
        bidID: String,
        address: String,
        remarks: String,
        amountOffered: String
    ) = bidApi.editBid(
        "Bearer ${ServiceBuilder.token!!}",
        bidID,
        amountOffered,
        address,
        remarks
    )

    suspend fun getBids(postID: String) =
        bidApi.bids("Bearer ${ServiceBuilder.token!!}", postID, null, null)

    // change bid stauts
    suspend fun changeBidStatus(bidID: String, status: String) =
        bidApi.changeBidStatus("Bearer ${ServiceBuilder.token!!}", bidID, status)

    suspend fun deleteBid(bidID: String) =
        bidApi.deleteBid("Bearer ${ServiceBuilder.token!!}", bidID)


}