package com.asharya.agromart.ui.fragments.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asharya.agromart.repository.PostRepository
import com.asharya.agromart.response.PostResponse
import com.asharya.agromart.uitls.Resource
import kotlinx.coroutines.launch
import java.lang.Exception

class SearchViewModel(private val repository: PostRepository): ViewModel() {

    private val _posts = MutableLiveData<Resource<PostResponse>>()
    val posts: LiveData<Resource<PostResponse>>
        get() = _posts

    fun getPosts(searchTerm: String) {
        viewModelScope.launch {
            _posts.value = Resource.Loading()
            try {
                val response = repository.posts(searchTerm)
                if(response.isSuccessful) {
                    response.body()?.let { resultResponse ->
                        _posts.value = Resource.Success(data = resultResponse, message = resultResponse.message)
                    }
                } else {
                    response.body()?.let { resultResponse ->
                        _posts.value = Resource.Error(data = resultResponse, message = resultResponse.message)
                    }

                }
            } catch(ex: Exception) {
                _posts.value = Resource.Error(message = ex.toString())
                Log.e("HOMEVIEWMODEL", ex.toString())
            }
        }
    }
}