package com.asharya.agromart.ui.fragments.bid

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.asharya.agromart.repository.BidRepository
import com.asharya.agromart.repository.PostRepository

class BidViewModelFactory(val repository: BidRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return BidViewModel(repository) as T
    }
}