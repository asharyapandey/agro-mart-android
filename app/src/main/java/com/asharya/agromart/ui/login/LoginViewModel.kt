package com.asharya.agromart.ui.login

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asharya.agromart.repository.UserRepository
import com.asharya.agromart.uitls.InformationType
import com.asharya.divinex.api.ServiceBuilder
import kotlinx.coroutines.launch

class LoginViewModel(
    private val userRepository: UserRepository,
    private val myPreferences: SharedPreferences
): ViewModel() {
    private val _isLoggedIn = MutableLiveData<InformationType>()
    val isLoggedIn: LiveData<InformationType>
        get() = _isLoggedIn

    fun login(phoneNumber: String, password: String) {
        viewModelScope.launch {
            try {
                val response = userRepository.loginUser(phoneNumber, password)
                if(response.success == true) {
                    ServiceBuilder.token = response.token
                    saveSharedPref(phoneNumber, password)
                    _isLoggedIn.value = InformationType(true, response.message!!)
                }
            } catch (ex: Exception) {
                Log.e("LoginViewModel", ex.toString())
                _isLoggedIn.value = InformationType(false, "Invalid Credentials")
            }
        }
    }
    private fun saveSharedPref(phoneNumber: String, password: String) {
        val editor = myPreferences.edit()
        editor.putString("phoneNumber", phoneNumber)
        editor.putString("password", password)
        editor.apply()
    }

}