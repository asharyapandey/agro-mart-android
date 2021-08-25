package com.asharya.agromart.ui.fragments.profile

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.asharya.agromart.R
import com.asharya.agromart.api.ServiceBuilder
import com.asharya.agromart.databinding.AddBidDialogBinding
import com.asharya.agromart.databinding.ChangePasswordDialogBinding
import com.asharya.agromart.databinding.EditProfileDialogBinding
import com.asharya.agromart.databinding.FragmentProfileBinding
import com.asharya.agromart.model.Bid
import com.asharya.agromart.repository.UserRepository
import com.asharya.agromart.ui.login.LoginActivity
import com.asharya.agromart.uitls.Resource
import com.bumptech.glide.Glide
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class ProfileFragment : Fragment(R.layout.fragment_profile) {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var viewModel: ProfileViewModel
    private var fullName = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProfileBinding.bind(view)

        viewModel = ViewModelProvider(
            this,
            ProfileViewModelFactory(UserRepository())
        ).get(ProfileViewModel::class.java)

        // getting Data
        viewModel.getProfile()

        viewModel.profile.observe(viewLifecycleOwner) { response ->
            if (response is Resource.Success) {
                response.data?.let { data ->
                    binding.tvFullName.text = data.result!!.fullName
                    fullName = data.result.fullName!!
                    var imagePath = ServiceBuilder.loadImagePath() + data.result.image
                    imagePath = imagePath.replace("\\", "/")
                    Glide.with(this).load(imagePath).into(binding.civProfileImage)
                }
            } else if (response is Resource.Error) {
                Toast.makeText(context, "${response.message}", Toast.LENGTH_SHORT).show()
            } else if (response is Resource.Loading) {
                Toast.makeText(context, "Loading Profile Data", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.editProfile.observe(viewLifecycleOwner) { response ->
            if (response is Resource.Success) {
                response.data?.let { data ->
                    viewModel.getProfile()
                }
            } else if (response is Resource.Error) {
                Toast.makeText(context, "${response.message}", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.addProfilePicture.observe(viewLifecycleOwner) { response ->
            if (response is Resource.Success) {
                response.data?.let { data ->
                    viewModel.getProfile()
                }
            } else if (response is Resource.Error) {
                Toast.makeText(context, "${response.message}", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.changePassword.observe(viewLifecycleOwner) { response ->
            if (response is Resource.Success) {
                response.data?.let { data ->
                    Toast.makeText(context, "${data.message}", Toast.LENGTH_SHORT).show()
                }
            } else if (response is Resource.Error) {
                Toast.makeText(context, "${response.message}", Toast.LENGTH_SHORT).show()
            }
        }
        binding.cvPosts.setOnClickListener {
            val action = ProfileFragmentDirections.actionProfileFragmentToUserPostFragment()
            findNavController().navigate(action)
        }

        binding.cvKalimatiPrice.setOnClickListener {
            val action = ProfileFragmentDirections.actionProfileFragmentToPriceListFragment()
            findNavController().navigate(action)
        }

        binding.cvEditProfile.setOnClickListener {
            showEditProfileDialog()
        }
        binding.cvChangePassword.setOnClickListener {
            showChangePasswordDialog()
        }

        binding.civProfileImage.setOnClickListener {
            loadPopUpMenu()
        }

        binding.cvLogout.setOnClickListener {
            ServiceBuilder.token = null
            ServiceBuilder.userID = null
            val sharedPreferences = requireContext().getSharedPreferences("MyPref", 0)
            val editor = sharedPreferences.edit()
            editor.putString("username", "")
            editor.putString("password", "")
            editor.apply()
            startActivity(Intent(requireContext(), LoginActivity::class.java))
        }
    }


    private fun showEditProfileDialog() {
        val dialogBinding = EditProfileDialogBinding.inflate(LayoutInflater.from(context))
        val dialog = AlertDialog.Builder(context).setView(dialogBinding.root)
            .setCancelable(true)
            .create()

        dialogBinding.etFullName.setText(fullName)

        dialogBinding.btnUpdateProfile.setOnClickListener {
            // validation
            when {
                TextUtils.isEmpty(dialogBinding.etFullName.text) -> {
                    dialogBinding.etFullName.error = "Full Name is Required"
                    dialogBinding.etFullName.requestFocus()
                    return@setOnClickListener
                }
            }
            // addding
            val newFullName = dialogBinding.etFullName.text.toString().trim()
            if (fullName == newFullName) {
                Toast.makeText(context, "Changes are required to update.", Toast.LENGTH_SHORT)
                    .show()
            } else {
                viewModel.editProfile(newFullName)
                dialog.cancel()
            }
        }
        dialog.show()
    }

    private fun showChangePasswordDialog() {
        val dialogBinding = ChangePasswordDialogBinding.inflate(LayoutInflater.from(context))
        val dialog = AlertDialog.Builder(context).setView(dialogBinding.root)
            .setCancelable(true)
            .create()

        dialogBinding.btnChangePassword.setOnClickListener {
            // validation
            when {
                TextUtils.isEmpty(dialogBinding.etCurrentPassword.text) -> {
                    dialogBinding.etCurrentPassword.error = "Please Enter Your Current Password."
                    dialogBinding.etCurrentPassword.requestFocus()
                    return@setOnClickListener
                }
                TextUtils.isEmpty(dialogBinding.etNewPassword.text) -> {
                    dialogBinding.etNewPassword.error = "Please add a new password."
                    dialogBinding.etNewPassword.requestFocus()
                    return@setOnClickListener
                }
                TextUtils.isEmpty(dialogBinding.etConfirmNewPassword.text) -> {
                    dialogBinding.etConfirmNewPassword.error = "Please confirm your password."
                    dialogBinding.etConfirmNewPassword.requestFocus()
                    return@setOnClickListener
                }
            }
            // addding
            val current = dialogBinding.etCurrentPassword.text.toString().trim()
            val new = dialogBinding.etConfirmNewPassword.text.toString().trim()
            val confrimNew = dialogBinding.etNewPassword.text.toString().trim()
            if (new == confrimNew) {
                viewModel.changePassword(current, new)
                dialog.cancel()
            } else {
                dialogBinding.etConfirmNewPassword.error = "Password Doesn't Match."
                dialogBinding.etConfirmNewPassword.requestFocus()
            }
        }
        dialog.show()
    }

    private fun loadPopUpMenu() {
        val popupMenu = PopupMenu(context, binding.civProfileImage)
        popupMenu.menuInflater.inflate(R.menu.gallery_camera, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.menuCamera -> openCamera()
                R.id.menuGallery -> openGallery()
            }
            true
        }
        popupMenu.show()
    }

    private var REQUEST_GALLERY_CODE = 0
    private var REQUEST_CAMERA_CODE = 1
    private var imageUrl: String? = null
    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_GALLERY_CODE)
    }

    private fun openCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, REQUEST_CAMERA_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_GALLERY_CODE && data != null) {
                val selectedImage = data.data
                val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
                val contentResolver = context?.contentResolver
                val cursor =
                    contentResolver?.query(selectedImage!!, filePathColumn, null, null, null)
                cursor!!.moveToFirst()
                val columnIndex = cursor.getColumnIndex(filePathColumn[0])
                imageUrl = cursor.getString(columnIndex)
                binding.civProfileImage.setImageBitmap(BitmapFactory.decodeFile(imageUrl))
                imageUrl?.let{imageUrl ->
                    viewModel.addProfilePicture(imageUrl)
                }
                cursor.close()
            } else if (requestCode == REQUEST_CAMERA_CODE && data != null) {
                val imageBitmap = data.extras?.get("data") as Bitmap
                val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
                val file = bitmapToFile(imageBitmap, "$timeStamp.jpg")
                imageUrl = file!!.absolutePath
                imageUrl?.let{imageUrl ->
                    viewModel.addProfilePicture(imageUrl)
                }
                binding.civProfileImage.setImageBitmap(BitmapFactory.decodeFile(imageUrl))
            }
        }

    }

    private fun bitmapToFile(
        bitmap: Bitmap,
        fileNameToSave: String
    ): File? {
        var file: File? = null
        return try {
            file = File(
                context?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                    .toString() + File.separator + fileNameToSave
            )
            file.createNewFile()
            //Convert bitmap to byte array
            val bos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos) // YOU can also save it in JPEG
            val bitMapData = bos.toByteArray()
            //write the bytes in file
            val fos = FileOutputStream(file)
            fos.write(bitMapData)
            fos.flush()
            fos.close()
            file
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            file // it will return null
        }
    }
}