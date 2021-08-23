package com.asharya.agromart.ui.fragments.pricelist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.asharya.agromart.repository.PostRepository

class PriceListViewModelFactory(val repository: PostRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return PriceListViewModel(repository) as T
    }
}