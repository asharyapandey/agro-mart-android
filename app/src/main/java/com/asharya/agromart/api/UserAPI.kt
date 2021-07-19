package com.asharya.agromart.api

import com.asharya.agromart.model.User
import com.asharya.agromart.response.LoginResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface UserAPI {

    // Registering User
    @POST("user/register")
    suspend fun registerUser(
        @Body user: User
    ) : Response<LoginResponse>

    // Login
    @FormUrlEncoded
    @POST("user/login")
    suspend fun loginUser(
        @Field("username") username : String,
        @Field("password") password : String
    ) : Response<LoginResponse>

}