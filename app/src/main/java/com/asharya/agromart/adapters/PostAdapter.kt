package com.asharya.agromart.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.asharya.agromart.api.ServiceBuilder
import com.asharya.agromart.databinding.FragmentSearchBinding
import com.asharya.agromart.databinding.PostLayoutBinding
import com.asharya.agromart.model.GetPost
import com.bumptech.glide.Glide

class PostAdapter(val context: Context, val listener: PostClickListener) :
    RecyclerView.Adapter<PostAdapter.PostViewHolder>() {
    private var postList = emptyList<GetPost>()

    inner class PostViewHolder(val binding: PostLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)

    interface PostClickListener {
        fun bidClicked(post: GetPost, position: Int)
        fun ibMoreClicked(post: GetPost, view: View)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        return PostViewHolder(
            PostLayoutBinding.inflate(
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
            Glide.with(context).load(postImagePath).into(ivImage)
            tvProductName.text = post.productName
            tvAddress.text = "From ${post.address}"
            tvDescription.text = "${post.description}"
            tvUsername.text = "${post.user.fullName}"
            var profileImagePath = ServiceBuilder.loadImagePath() + post.user.image
            profileImagePath = profileImagePath.replace("\\", "/")
            Glide.with(context).load(profileImagePath).into(civUserImage)
            tvPrice.text = "Rs. ${post.farmerPrice}/${post.unit}"
            tvKalimatiPrice.text = "Rs. ${post.kalimatiPrice}/${post.unit}"
            chipCategory.text = post.category

            btnBid.text = "Bid (${post.totalBids})"

            btnBid.setOnClickListener {
                listener.bidClicked(post, position)
            }

            if (post.user._id == ServiceBuilder.userID!!) {
                ibMore.setOnClickListener {
                    listener.ibMoreClicked(post, it)
                }
            } else {
                ibMore.visibility = View.GONE
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