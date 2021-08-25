package com.asharya.agromart.ui.fragments.profile

import android.util.Log
import android.webkit.MimeTypeMap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asharya.agromart.repository.UserRepository
import com.asharya.agromart.response.PostResponse
import com.asharya.agromart.response.UserResponse
import com.asharya.agromart.uitls.Resource
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.lang.Exception

class ProfileViewModel(private val repository: UserRepository) : ViewModel() {

    private val _profile = MutableLiveData<Resource<UserResponse>>()
    val profile: LiveData<Resource<UserResponse>>
        get() = _profile

    private val _editProfile = MutableLiveData<Resource<UserResponse>>()
    val editProfile: LiveData<Resource<UserResponse>>
        get() = _editProfile

    private val _changePassword = MutableLiveData<Resource<UserResponse>>()
    val changePassword: LiveData<Resource<UserResponse>>
        get() = _changePassword

    private val _addProfilePicture = MutableLiveData<Resource<UserResponse>>()
    val addProfilePicture: LiveData<Resource<UserResponse>>
        get() = _addProfilePicture


    fun getProfile() {
        viewModelScope.launch {
            _profile.value = Resource.Loading()
            try {
                val response = repository.getProfile()
                if (response.isSuccessful) {
                    response.body()?.let { resultResponse ->
                        _profile.value = Resource.Success(
                            data = resultResponse,
                            message = resultResponse.message
                        )
                    }
                } else {
                    response.body()?.let { resultResponse ->
                        _profile.value =
                            Resource.Error(data = resultResponse, message = resultResponse.message)
                    }
                }
            } catch (ex: Exception) {
                _profile.value = Resource.Error(message = ex.toString())
                Log.e("PROFILEVIEWMODEL", ex.toString())
            }
        }
    }

    fun editProfile(fullName: String) {
        viewModelScope.launch {
            _profile.value = Resource.Loading()
            try {
                val response = repository.updateProfile(fullName)
                if (response.isSuccessful) {
                    response.body()?.let { resultResponse ->
                        _profile.value = Resource.Success(
                            data = resultResponse,
                            message = resultResponse.message
                        )
                    }
                } else {
                    response.body()?.let { resultResponse ->
                        _profile.value =
                            Resource.Error(data = resultResponse, message = resultResponse.message)
                    }
                }
            } catch (ex: Exception) {
                _profile.value = Resource.Error(message = ex.toString())
                Log.e("PROFILEVIEWMODEL", ex.toString())
            }
        }
    }

    fun changePassword(oldPassword: String, newPassword: String) {
        viewModelScope.launch {
            _profile.value = Resource.Loading()
            try {
                val response = repository.changePassword(oldPassword, newPassword)
                if (response.isSuccessful) {
                    response.body()?.let { resultResponse ->
                        _profile.value = Resource.Success(
                            data = resultResponse,
                            message = resultResponse.message
                        )
                    }
                } else {
                    response.body()?.let { resultResponse ->
                        _profile.value =
                            Resource.Error(data = resultResponse, message = resultResponse.message)
                    }
                }
            } catch (ex: Exception) {
                _profile.value = Resource.Error(message = ex.toString())
                Log.e("PROFILEVIEWMODEL", ex.toString())
            }
        }
    }

    fun addProfilePicture(image: String) {
        viewModelScope.launch {
            _profile.value = Resource.Loading()
            try {
                val file = File(image)
                val extension = MimeTypeMap.getFileExtensionFromUrl(image)
                val mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)

                val reqFile = file.asRequestBody(mimeType?.toMediaType())
                val body = MultipartBody.Part.createFormData("image", file.name, reqFile)
                val response = repository.addProfilePicture(body)
                if (response.isSuccessful) {
                    response.body()?.let { resultResponse ->
                        _profile.value = Resource.Success(
                            data = resultResponse,
                            message = resultResponse.message
                        )
                    }
                } else {
                    response.body()?.let { resultResponse ->
                        _profile.value =
                            Resource.Error(data = resultResponse, message = resultResponse.message)
                    }
                }
            } catch (ex: Exception) {
                _profile.value = Resource.Error(message = ex.toString())
                Log.e("PROFILEVIEWMODEL", ex.toString())
            }
        }
    }
}