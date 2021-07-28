package com.asharya.agromart.ui.fragments.addpost

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.asharya.agromart.repository.PostRepository

class AddPostViewModelFactory(val repository: PostRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AddPostViewModel(repository) as T
    }
}