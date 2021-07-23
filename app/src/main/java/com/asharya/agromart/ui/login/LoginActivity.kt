package com.asharya.agromart.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.asharya.agromart.databinding.ActivityLoginBinding
import com.asharya.agromart.repository.UserRepository
import com.asharya.agromart.ui.DashboardActivity
import com.asharya.agromart.ui.register.RegisterActivity
import com.google.android.material.snackbar.Snackbar

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPref = getSharedPreferences("MyPref", MODE_PRIVATE)
        viewModel = ViewModelProvider(this, LoginViewModelFactory(UserRepository(), sharedPref)).get(LoginViewModel::class.java)

        binding.btnLogin.setOnClickListener {
            if(validate()) {
                val phoneNumber = binding.etPhoneNumber.text.toString().trim()
                val password= binding.etPassword.text.toString().trim()
                viewModel.login(phoneNumber, password)
            }
        }
        // observing
        viewModel.isLoggedIn.observe(this, Observer { result ->
            if(result.status) {
                startActivity(Intent(this, DashboardActivity::class.java))
                finish()
            } else {
                val snackbar = Snackbar.make(binding.clLogin, result.message, Snackbar.LENGTH_LONG)
                snackbar.setAction("OK", View.OnClickListener {
                    snackbar.dismiss()
                })
                snackbar.show()
            }

        })

        binding.tvLoginHere.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }


    }

    private fun validate(): Boolean {
        when {
            TextUtils.isEmpty(binding.etPhoneNumber.text) -> {
                binding.etPhoneNumber.error = "Please Enter Your Phone Number"
                binding.etPhoneNumber.requestFocus()
                return false
            }
            TextUtils.isEmpty(binding.etPassword.text) -> {
                binding.etPassword.error = "Please Enter Your Password"
                binding.etPassword.requestFocus()
                return false
            }
        }
        return true
    }

}