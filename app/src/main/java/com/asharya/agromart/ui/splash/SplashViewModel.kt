package com.asharya.agromart.ui.splash

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asharya.agromart.repository.UserRepository
import com.asharya.agromart.uitls.InformationType
import com.asharya.divinex.api.ServiceBuilder
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.Exception

class SplashViewModel(
    private val userRepository: UserRepository,
    private val myPref: SharedPreferences
) : ViewModel() {

    private val _isLoggedIn = MutableLiveData<InformationType>()
    val isLoggedIn: LiveData<InformationType>
        get() = _isLoggedIn


    fun login() {
        viewModelScope.launch {
            try {
                val phoneNumber = myPref.getString("phoneNumber", "")
                val password = myPref.getString("password", "")
                if (phoneNumber == "" || password == "") {
                    delay(500)
                    _isLoggedIn.value = InformationType(false, "")
                } else {
                    val response = userRepository.loginUser(phoneNumber!!, password!!)
                    if (response.success == true) {
                        // current user details
                        ServiceBuilder.token = response.token
//                        ServiceBuilder.currentUser = response.user
                        // saving to shared pref
                        saveSharedPref(phoneNumber, password)
                        // updating the ui
                        _isLoggedIn.value = InformationType(true, response.message!!)
                    }
                }
            } catch (ex: Exception) {
                Log.e("LoginViewModel", ex.toString())
                _isLoggedIn.value = InformationType(false, "Couldn't Log You In.")
            }
        }
    }

    private fun saveSharedPref(username: String, password: String) {
        val editor = myPref.edit()
        editor.putString("username", username)
        editor.putString("password", password)
        editor.apply()
    }

    override fun onCleared() {
        super.onCleared()
    }
}