package com.asharya.agromart.api

import com.asharya.agromart.model.AddPost
import com.asharya.agromart.model.Upload
import com.asharya.agromart.response.ProductResponse
import com.asharya.agromart.response.AddPostResponse
import com.asharya.agromart.response.PostResponse
import com.asharya.agromart.response.UploadResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface PostAPI {

    // Adding Post
    @Multipart
    @POST("post")
    suspend fun addPost(
        @Header("authorization") token: String,
        @Part ("address") address: RequestBody,
        @Part ("description") desc: RequestBody,
        @Part ("farmerPrice") price: RequestBody,
        @Part ("product") product: RequestBody,
        @Part ("name") name: RequestBody,
        @Part image: MultipartBody.Part,
    ) : Response<AddPostResponse>

    @GET("products")
    suspend fun getCategories(
        @Header("authorization") token: String
    ): Response<ProductResponse>

    @GET("posts")
    suspend fun posts(
        @Header("authorization") token: String,
        @Query("searchTerm") searchTerm: String?,
        @Query("userID") userID: String? = null,
    ): Response<PostResponse>
}