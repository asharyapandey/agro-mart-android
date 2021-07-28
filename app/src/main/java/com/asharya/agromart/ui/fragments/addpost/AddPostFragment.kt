package com.asharya.agromart.ui.fragments.addpost

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.TextUtils
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.PopupMenu
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.asharya.agromart.R
import com.asharya.agromart.databinding.FragmentAddPostBinding
import com.asharya.agromart.model.AddPost
import com.asharya.agromart.repository.PostRepository
import com.asharya.agromart.uitls.Resource
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class AddPostFragment : Fragment(R.layout.fragment_add_post) {
    private lateinit var binding: FragmentAddPostBinding
    private lateinit var viewModel: AddPostViewModel
    private val productsIdMap = mutableMapOf<String, String>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAddPostBinding.bind(view)
        viewModel = ViewModelProvider(
            this,
            AddPostViewModelFactory(PostRepository())
        ).get(AddPostViewModel::class.java)

        viewModel.getProducts()

        viewModel.products.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success ->  {
                    response.data?.let { productResponse ->
                        for (data in productResponse.result) {
                            productsIdMap[data.productName] = data._id
                        }
                        readySpinner(productsIdMap.keys.toTypedArray())
                    }
                }
                else -> Toast.makeText(context, "Loading", Toast.LENGTH_SHORT).show()
            }

        })


        viewModel.postAdded.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    Toast.makeText(context, response.data!!.message, Toast.LENGTH_SHORT).show()
                }
                is Resource.Error -> {
                    Toast.makeText(context, response.message, Toast.LENGTH_SHORT).show()
                }
                is Resource.Loading -> {
                }
                else -> Toast.makeText(context, "Something Went Wrong", Toast.LENGTH_SHORT).show()
            }
        })

        binding.btnAddPost.setOnClickListener {
            if(validate()) {
                val product = productsIdMap[binding.etProduct.text.toString()]
                val productName = binding.etProductName.text.toString().trim()
                val price = binding.etPrice.text.toString().trim()
                val address = binding.etAddress.text.toString().trim()
                val description = binding.etDescription.text.toString().trim()


                val post = AddPost(address, description,price, imageUrl!!, productName, product!!)
                viewModel.addPost(post)
            }
        }


        binding.ivPostImage.setOnClickListener {
            loadPopUpMenu()
        }

    }

    private fun loadPopUpMenu() {
        val popupMenu = PopupMenu(context, binding.ivPostImage)
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

    private fun validate(): Boolean {
        when {
            TextUtils.isEmpty(binding.etProduct.text) -> {
                binding.etProduct.error = "Please Select a Product Type"
                binding.etProduct.requestFocus()
                return false
            }
            TextUtils.isEmpty(binding.etProductName.text) -> {
                binding.etProductName.error = "Please add your product name"
                binding.etProductName.requestFocus()
                return false
            }
            TextUtils.isEmpty(binding.etAddress.text) -> {
                binding.etAddress.error = "Please add your address"
                binding.etAddress.requestFocus()
                return false
            }
            TextUtils.isEmpty(binding.etPrice.text) -> {
                binding.etPrice.error = "Please add your price"
                binding.etPrice.requestFocus()
                return false
            }
            TextUtils.isEmpty(binding.etDescription.text) -> {
                binding.etDescription.error = "Please add your products description"
                binding.etDescription.requestFocus()
                return false
            }
            imageUrl == null -> {
                Toast.makeText(context, "Please Select an Image", Toast.LENGTH_SHORT).show()
                return false
            }
        }
        return true
    }
    private fun readySpinner(products: Array<String>) {
        binding.etProduct.setAdapter(
            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_list_item_1,
                products
            )
        )
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
                binding.ivPostImage.setImageBitmap(BitmapFactory.decodeFile(imageUrl))
                cursor.close()
            } else if (requestCode == REQUEST_CAMERA_CODE && data != null) {
                val imageBitmap = data.extras?.get("data") as Bitmap
                val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
                val file = bitmapToFile(imageBitmap, "$timeStamp.jpg")
                imageUrl = file!!.absolutePath
                binding.ivPostImage.setImageBitmap(BitmapFactory.decodeFile(imageUrl))
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