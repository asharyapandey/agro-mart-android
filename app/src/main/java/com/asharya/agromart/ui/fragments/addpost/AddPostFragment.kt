package com.asharya.agromart.ui.fragments.addpost

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.asharya.agromart.R
import com.asharya.agromart.databinding.FragmentAddPostBinding
import com.asharya.agromart.databinding.FragmentHomeBinding

class AddPostFragment : Fragment(R.layout.fragment_add_post) {
    private lateinit var binding: FragmentAddPostBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAddPostBinding.bind(view)
    }
}