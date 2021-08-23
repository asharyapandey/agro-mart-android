package com.asharya.agromart.ui.fragments.profile

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.asharya.agromart.R
import com.asharya.agromart.databinding.FragmentHomeBinding
import com.asharya.agromart.databinding.FragmentProfileBinding

class ProfileFragment : Fragment(R.layout.fragment_profile) {
    private lateinit var binding:FragmentProfileBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProfileBinding.bind(view)

        binding.cvPosts.setOnClickListener {
            val action = ProfileFragmentDirections.actionProfileFragmentToUserPostFragment()
            findNavController().navigate(action)
        }

        binding.cvKalimatiPrice.setOnClickListener {
            val action = ProfileFragmentDirections.actionProfileFragmentToPriceListFragment()
            findNavController().navigate(action)
        }
    }
}