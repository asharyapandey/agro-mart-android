package com.asharya.agromart.ui.fragments.addpost

import android.util.Log
import android.webkit.MimeTypeMap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asharya.agromart.model.AddPost
import com.asharya.agromart.repository.PostRepository
import com.asharya.agromart.response.ProductResponse
import com.asharya.agromart.response.AddPostResponse
import com.asharya.agromart.uitls.Resource
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.lang.Exception
import kotlin.math.log

class AddPostViewModel(val postRepository: PostRepository) : ViewModel() {
    private val _postAdded = MutableLiveData<Resource<AddPostResponse>>()
    val postAdded: LiveData<Resource<AddPostResponse>>
        get() = _postAdded

    private val _products = MutableLiveData<Resource<ProductResponse>>()
    val products: LiveData<Resource<ProductResponse>>
        get() = _products

    fun addPost(post: AddPost) {
        viewModelScope.launch {
            try {
                val file = File(post.image)
                val extension = MimeTypeMap.getFileExtensionFromUrl(post.image)
                val mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)

                val reqFile = file.asRequestBody(mimeType?.toMediaType())
                val body = MultipartBody.Part.createFormData("image", file.name, reqFile)
                val address = post.address.toRequestBody("text/plain".toMediaType())
                val desc = post.description.toRequestBody("text/plain".toMediaType())
                val price = post.farmerPrice.toRequestBody("text/plain".toMediaType())
                val product = post.product.toRequestBody("text/plain".toMediaType())
                val name = post.name.toRequestBody("text/plain".toMediaType())
                val response = postRepository.addPost(
                    address,
                    desc,
                    price,
                    name,
                    product,
                    body
                )
                Log.e("ADDPOSTVIEWMODEL", "status code -------- ${response.code()}")
                if (response.isSuccessful) {

                    response.body()?.let { resultResponse ->
                        _postAdded.postValue(Resource.Success(resultResponse))
                    }
                } else {
                    response.body()?.let { resultResponse ->
                        _postAdded.postValue(
                            Resource.Error(
                                message = resultResponse.message!!,
                                data = resultResponse
                            )
                        )
                    }
                }

            } catch (Ex: Exception) {
                _postAdded.postValue(Resource.Error(message = Ex.toString()))
            }
        }
    }

    fun getProducts() {
        viewModelScope.launch {
            _products.postValue(Resource.Loading())
            try {
                val response = postRepository.getProducts()
                if (response.isSuccessful) {
                    response.body()?.let { resultResponse ->
                        _products.postValue(Resource.Success(resultResponse))
                    }
                } else {
                    response.body()?.let { resultResponse ->
                        _products.postValue(
                            Resource.Error(
                                message = resultResponse.message!!,
                                data = resultResponse
                            )
                        )
                    }
                }
            } catch (Ex: Exception) {
                _products.postValue(Resource.Error(message = "Couldn't Add Book"))
            }
        }
    }
}