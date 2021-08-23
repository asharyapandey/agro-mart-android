package com.asharya.agromart.ui.fragments.userpost

import android.os.Bundle
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.asharya.agromart.R
import com.asharya.agromart.adapters.PostAdapter
import com.asharya.agromart.databinding.FragmentUserPostBinding
import com.asharya.agromart.model.GetPost
import com.asharya.agromart.repository.PostRepository
import com.asharya.agromart.uitls.Resource

class UserPostFragment : Fragment(R.layout.fragment_user_post), PostAdapter.PostClickListener {
    private lateinit var binding: FragmentUserPostBinding
    private lateinit var viewModel: UserPostViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentUserPostBinding.bind(view)

        viewModel = ViewModelProvider(this, UserPostViewModelFactory(PostRepository())).get(UserPostViewModel::class.java)

        val adapter = PostAdapter(requireContext(),this )
        binding.userPostsViewPager.adapter = adapter
        viewModel.getPosts()

        viewModel.posts.observe(viewLifecycleOwner) { response ->
            if(response is Resource.Success) {
                response.data?.let { data ->
                    adapter.submitList(data.result)
                }
            } else if (response is Resource.Error) {
                Toast.makeText(context, "${response.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun bidClicked(post: GetPost, position: Int) {
        val action = UserPostFragmentDirections.actionUserPostFragmentToBidFragment(post._id)
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