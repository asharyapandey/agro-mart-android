package com.asharya.agromart.ui.register

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.RadioButton
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.asharya.agromart.databinding.ActivityRegisterBinding
import com.asharya.agromart.repository.UserRepository

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var viewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // viewModel
        viewModel = ViewModelProvider(this, RegisterViewModelFactory(UserRepository())).get(
            RegisterViewModel::class.java
        )

        viewModel.isRegistered.observe(this, Observer { isRegistered ->
            if (isRegistered) {
                Toast.makeText(this, "User Registered", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Something went wrong please try again", Toast.LENGTH_SHORT)
                    .show()
            }
        })

        // button click
        binding.btnRegister.setOnClickListener {
            if (validate()) {
                val password = binding.etPassword.text.toString()
                val confirmPassword = binding.etConfirmPassword.text.toString()
                val email = binding.etEmail.text.toString()
                val fullName = binding.etFullName.text.toString()
                val rdoID = binding.rgPermission.checkedRadioButtonId
                val checkedRadioButton: RadioButton = findViewById(rdoID)
                val permissionLevel = if (checkedRadioButton.text.toString() == "Farmer") 1 else 2

                if (password == confirmPassword) {
                    viewModel.registerUser(email, password, email, permissionLevel)
                } else {
                    binding.etConfirmPassword.error = "Passwords do not match"
                    binding.etConfirmPassword.requestFocus()
                }
            }
        }
    }

    private fun validate(): Boolean {
        when {
            TextUtils.isEmpty(binding.etEmail.text) -> {
                binding.etEmail.error = "Please enter a Email"
                binding.etEmail.requestFocus()
                return false
            }
            TextUtils.isEmpty(binding.etFullName.text) -> {
                binding.etFullName.error = "Please enter Your Full Name"
                binding.etFullName.requestFocus()
                return false
            }
            TextUtils.isEmpty(binding.etPassword.text) -> {
                binding.etPassword.error = "Please enter a Password"
                binding.etPassword.requestFocus()
                return false
            }
            TextUtils.isEmpty(binding.etConfirmPassword.text) -> {
                binding.etConfirmPassword.error = "Please confirm the Password"
                binding.etConfirmPassword.requestFocus()
                return false
            }
            (binding.rgPermission.checkedRadioButtonId <= 0) -> {
                Toast.makeText(this, "Please select if you are a farmer", Toast.LENGTH_SHORT).show()
                binding.rbConsumer.requestFocus()
                return false
            }
        }
        return true
    }
}