package com.asharya.agromart.api

import com.asharya.agromart.model.User
import com.asharya.agromart.response.AddPostResponse
import com.asharya.agromart.response.LoginResponse
import com.asharya.agromart.response.UserResponse
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
        @Field("phoneNumber") username : String,
        @Field("password") password : String
    ) : Response<LoginResponse>

    @Multipart
    @POST("user/image")
    suspend fun addProfilePicture(
        @Header("authorization") token: String,
        @Part image: MultipartBody.Part,
    ) : Response<UserResponse>

    @FormUrlEncoded
    @POST("user/password")
    suspend fun changePassword(
        @Header("authorization") token: String,
        @Field("oldPassword") oldPassword: String,
        @Field("newPassword") newPassword: String
    ) : Response<UserResponse>

    @FormUrlEncoded
    @POST("user/profile")
    suspend fun updateProfile(
        @Header("authorization") token: String,
        @Field("fullName") fullName: String,
    ) : Response<UserResponse>

    @GET("user/profile")
    suspend fun getProfile(
        @Header("authorization") token: String,
    ) : Response<UserResponse>
}