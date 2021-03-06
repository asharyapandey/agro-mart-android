package com.asharya.agromart.ui.fragments.home

import android.os.Bundle
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.asharya.agromart.R
import com.asharya.agromart.adapters.PostAdapter
import com.asharya.agromart.databinding.FragmentHomeBinding
import com.asharya.agromart.model.GetPost
import com.asharya.agromart.repository.PostRepository
import com.asharya.agromart.uitls.Resource

class HomeFragment : Fragment(R.layout.fragment_home), PostAdapter.PostClickListener {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: HomeViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)

        viewModel = ViewModelProvider(
            this,
            HomeViewModelFactory(PostRepository())
        ).get(HomeViewModel::class.java)

        val adapter = PostAdapter(requireContext(), this)
        binding.postsViewPager.adapter = adapter
        viewModel.getPosts()

        viewModel.posts.observe(viewLifecycleOwner) { response ->
            if (response is Resource.Success) {
                response.data?.let { data ->
                    adapter.submitList(data.result)
                }
            } else if (response is Resource.Error) {
                Toast.makeText(context, "${response.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun bidClicked(post: GetPost, position: Int) {
        val action = HomeFragmentDirections.actionHomeFragmentToBidFragment(post._id)
        findNavController().navigate(action)
    }

    override fun ibMoreClicked(post: GetPost, view: View) {
        val popupMenu = PopupMenu(context, view)
        popupMenu.menuInflater.inflate(R.menu.edit_delete, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.menuDelete -> delete(post)
                R.id.menuEdit -> editPost(post)
            }
            true
        }
        popupMenu.show()
    }

    private fun delete(post: GetPost) {

    }

    private fun editPost(post: GetPost) {

    }
}