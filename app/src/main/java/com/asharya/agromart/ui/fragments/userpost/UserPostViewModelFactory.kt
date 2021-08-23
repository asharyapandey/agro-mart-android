package com.asharya.agromart.ui.fragments.userpost

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.asharya.agromart.repository.PostRepository

class UserPostViewModelFactory(val repository: PostRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return UserPostViewModel(repository) as T
    }
}