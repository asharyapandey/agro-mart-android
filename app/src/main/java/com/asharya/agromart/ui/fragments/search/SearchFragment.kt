package com.asharya.agromart.ui.fragments.search

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.asharya.agromart.R
import com.asharya.agromart.databinding.FragmentHomeBinding
import com.asharya.agromart.databinding.FragmentSearchBinding

class SearchFragment : Fragment(R.layout.fragment_search) {
    private lateinit var binding:FragmentSearchBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSearchBinding.bind(view)
    }
}