package com.asharya.agromart.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.asharya.agromart.api.ServiceBuilder
import com.asharya.agromart.databinding.FragmentSearchBinding
import com.asharya.agromart.databinding.PostItemBinding
import com.asharya.agromart.databinding.PostLayoutBinding
import com.asharya.agromart.model.GetPost
import com.bumptech.glide.Glide

class PostItemAdapter(val context: Context, val listener: PostItemClickListener) :
    RecyclerView.Adapter<PostItemAdapter.PostViewHolder>() {
    private var postList = emptyList<GetPost>()

    inner class PostViewHolder(val binding: PostItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    interface PostItemClickListener {
        fun btnMoreClicked(post: GetPost, position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        return PostViewHolder(
            PostItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = postList[position]

        var postImagePath = ServiceBuilder.loadImagePath() + post.image
        postImagePath = postImagePath.replace("\\", "/")
        holder.binding.apply {
            Glide.with(context).load(postImagePath).into(ivPostImage)
            tvAddress.text = "From ${post.address}"
            tvName.text = post.name
            btnMore.setOnClickListener {
                listener.btnMoreClicked(post, position)
            }
        }
    }

    override fun getItemCount(): Int = postList.size

    fun submitList(newPostList: List<GetPost>) {
        val oldPostList = postList
        val diffResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(
            UserItemDiffCallback(
                oldPostList,
                newPostList
            )
        )
        postList = newPostList
        diffResult.dispatchUpdatesTo(this)
    }

    class UserItemDiffCallback(
        var oldPostList: List<GetPost>,
        var newPostList: List<GetPost>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int {
            return oldPostList.size
        }

        override fun getNewListSize(): Int {
            return newPostList.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return (oldPostList[oldItemPosition]._id == newPostList[newItemPosition]._id)
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return (oldPostList[oldItemPosition] == newPostList[newItemPosition])
        }
    }

}