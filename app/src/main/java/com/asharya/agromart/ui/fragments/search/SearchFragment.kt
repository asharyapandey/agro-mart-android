package com.asharya.agromart.ui.fragments.search

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.asharya.agromart.R
import com.asharya.agromart.adapters.PostItemAdapter
import com.asharya.agromart.databinding.FragmentSearchBinding
import com.asharya.agromart.model.GetPost
import com.asharya.agromart.repository.PostRepository
import com.asharya.agromart.uitls.Resource

class SearchFragment : Fragment(R.layout.fragment_search), PostItemAdapter.PostItemClickListener {
    private lateinit var binding:FragmentSearchBinding
    private lateinit var viewModel: SearchViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSearchBinding.bind(view)

        viewModel = ViewModelProvider(this, SearchViewModelFactory(PostRepository())).get(
            SearchViewModel::class.java)

        val adapter  = PostItemAdapter(requireContext(), this)
        binding.rvSearch.adapter = adapter

        viewModel.posts.observe(viewLifecycleOwner) { response ->
            if(response is Resource.Success) {
                response.data?.let { data ->
                    adapter.submitList(data.result)
                }
            } else if (response is Resource.Error) {
                Toast.makeText(context, "${response.message}", Toast.LENGTH_SHORT).show()
            }
        }

        binding.etSearchTearm.addTextChangedListener { text ->
            if(text.toString() == "") {
                adapter.submitList(emptyList())
                return@addTextChangedListener
            }
            viewModel.getPosts(text.toString())
        }
    }

    override fun btnMoreClicked(post: GetPost, position: Int) {
        Toast.makeText(context, "watman", Toast.LENGTH_SHORT).show()
    }
}