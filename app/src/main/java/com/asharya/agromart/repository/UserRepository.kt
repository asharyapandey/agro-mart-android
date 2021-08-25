package com.asharya.agromart.repository

import com.asharya.agromart.api.ApiRequest
import com.asharya.agromart.api.UserAPI
import com.asharya.agromart.model.User
import com.asharya.agromart.response.LoginResponse
import com.asharya.agromart.api.ServiceBuilder
import okhttp3.MultipartBody
import okhttp3.RequestBody

class UserRepository : ApiRequest() {
    private val userApi = ServiceBuilder.buildService(UserAPI::class.java)

    // register user
    suspend fun registerUser(user: User): LoginResponse {
        return apiRequest {
            userApi.registerUser(user)
        }
    }

    // register user
    suspend fun loginUser(phoneNumber: String, password: String): LoginResponse {
        return apiRequest {
            userApi.loginUser(phoneNumber, password)
        }
    }

    suspend fun changePassword(oldPassword: String, newPassword: String) =
        userApi.changePassword("Bearer ${ServiceBuilder.token!!}", oldPassword, newPassword)

    suspend fun updateProfile(fullName: String) =
        userApi.updateProfile("Bearer ${ServiceBuilder.token!!}", fullName)

    suspend fun getProfile() =
        userApi.getProfile("Bearer ${ServiceBuilder.token!!}")

    suspend fun addProfilePicture(
        image: MultipartBody.Part,
    ) = userApi.addProfilePicture(
        "Bearer ${ServiceBuilder.token!!}",
        image
    )
}