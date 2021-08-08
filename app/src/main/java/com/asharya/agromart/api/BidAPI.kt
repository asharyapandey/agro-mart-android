package com.asharya.agromart.api

import com.asharya.agromart.response.*
import retrofit2.Response
import retrofit2.http.*

interface BidAPI {

    @GET("bid")
    suspend fun bids(
        @Header("authorization") token: String,
        @Query("postID") postID: String?,
        @Query("userID") userID: String?,
        @Query("belongsTo") belongsTo: String?
    ): Response<BidsResponse>

    @FormUrlEncoded
    @POST("bid/{id}")
    suspend fun addBid(
        @Header("authorization") token: String,
        @Path("id") postID: String,
//        @Body amountOffered: String,
//        @Body address: String,
//        @Body remarks: String
        @Field("amountOffered") amountOffered: String,
        @Field("address") address: String,
        @Field("remarks") remarks: String
    ): Response<BidResponse>

}