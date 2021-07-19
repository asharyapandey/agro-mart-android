package com.asharya.agromart.ui.register

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asharya.agromart.model.User
import com.asharya.agromart.repository.UserRepository
import kotlinx.coroutines.launch
import java.lang.Exception

class RegisterViewModel(
    private val userRepository: UserRepository
): ViewModel() {
    private val TAG = "RegisterViewModel"
    private var _isRegistered = MutableLiveData<Boolean>()
    val isRegistered: LiveData<Boolean>
    get() = _isRegistered

    fun registerUser(phoneNumber: String, password:String, fullName: String, permissionLevel: Int) {
        val user = User(phoneNumber= phoneNumber, password=password, fullName = fullName, permissionLevel = permissionLevel)
        try {
            viewModelScope.launch {
                val response = userRepository.registerUser(user)
                if(response.success == true) {
                    _isRegistered.value = true
                }
            }
        } catch (ex: Exception) {
            Log.e(TAG, ex.toString())
            _isRegistered.value = false
        }
    }
}