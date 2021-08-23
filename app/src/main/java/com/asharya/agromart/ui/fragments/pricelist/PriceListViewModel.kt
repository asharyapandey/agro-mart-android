package com.asharya.agromart.ui.fragments.pricelist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asharya.agromart.repository.PostRepository
import com.asharya.agromart.response.ProductResponse
import com.asharya.agromart.uitls.Resource
import kotlinx.coroutines.launch
import java.lang.Exception

class PriceListViewModel(val postRepository: PostRepository) : ViewModel() {

    private val _products = MutableLiveData<Resource<ProductResponse>>()
    val products: LiveData<Resource<ProductResponse>>
        get() = _products

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