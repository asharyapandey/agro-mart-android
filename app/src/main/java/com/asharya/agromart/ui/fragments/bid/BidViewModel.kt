package com.asharya.agromart.ui.fragments.bid

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asharya.agromart.model.AddPost
import com.asharya.agromart.repository.BidRepository
import com.asharya.agromart.repository.PostRepository
import com.asharya.agromart.response.BidResponse
import com.asharya.agromart.response.BidsResponse
import com.asharya.agromart.uitls.Resource
import kotlinx.coroutines.launch
import java.lang.Exception

class BidViewModel(private val bidRepository: BidRepository) : ViewModel() {
    private val _bidAdded = MutableLiveData<Resource<BidResponse>>()
    val bidAdded: LiveData<Resource<BidResponse>>
        get() = _bidAdded

    private val _bids = MutableLiveData<Resource<BidsResponse>>()
    val bids: LiveData<Resource<BidsResponse>>
        get() = _bids

    fun addBid(postID: String, address: String, remarks: String, amountOffered: String) {
        viewModelScope.launch {
            _bidAdded.postValue(Resource.Loading())
            try {
                val response = bidRepository.addBid(
                    postID,
                    address,
                    remarks,
                    amountOffered
                )
                if (response.isSuccessful) {
                    response.body()?.let { resultResponse ->
                        _bidAdded.postValue(Resource.Success(resultResponse))
                    }
                } else {
                    response.body()?.let { resultResponse ->
                        _bidAdded.postValue(
                            Resource.Error(
                                message = resultResponse.message!!,
                                data = resultResponse
                            )
                        )
                    }
                }

            } catch (Ex: Exception) {
                _bidAdded.postValue(Resource.Error(message = Ex.toString()))
            }
        }
    }

    fun getBids(postID: String) {
        viewModelScope.launch {
            _bidAdded.postValue(Resource.Loading())
            try {
                val response = bidRepository.getBids(postID)
                if (response.isSuccessful) {
                    response.body()?.let { resultResponse ->
                        _bids.postValue(Resource.Success(resultResponse))
                    }
                } else {
                    response.body()?.let { resultResponse ->
                        _bids.postValue(
                            Resource.Error(
                                message = resultResponse.message!!,
                                data = resultResponse
                            )
                        )
                    }
                }
            } catch (Ex: Exception) {
                _bids.postValue(Resource.Error(message = "Couldn't Add Book"))
            }
        }
    }
}