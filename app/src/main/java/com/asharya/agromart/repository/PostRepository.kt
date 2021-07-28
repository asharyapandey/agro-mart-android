package com.asharya.agromart.repository

import com.asharya.agromart.api.PostAPI
import com.asharya.agromart.api.ServiceBuilder
import com.asharya.agromart.model.AddPost
import com.asharya.agromart.model.Upload
import okhttp3.MultipartBody
import okhttp3.RequestBody

class PostRepository {
    private val postApi = ServiceBuilder.buildService(PostAPI::class.java)

    // add post
    suspend fun addPost(
        address: RequestBody,
        description: RequestBody,
        price: RequestBody,
        name: RequestBody,
        product: RequestBody,
        image: MultipartBody.Part,
    ) = postApi.addPost(
        "Bearer ${ServiceBuilder.token!!}",
        address,
        description,
        price,
        product,
        name,
        image
    )

    suspend fun getProducts() = postApi.getCategories("Bearer ${ServiceBuilder.token!!}")
    suspend fun upload(image: String) =
        postApi.upload("Bearer ${ServiceBuilder.token!!}", Upload(image))


}