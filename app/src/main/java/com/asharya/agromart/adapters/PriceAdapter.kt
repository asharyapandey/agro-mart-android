package com.asharya.agromart.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.asharya.agromart.databinding.PriceItemBinding
import com.asharya.agromart.model.Product

class PriceAdapter() :
    RecyclerView.Adapter<PriceAdapter.PostViewHolder>() {
    private var productList = emptyList<Product>()

    inner class PostViewHolder(val binding: PriceItemBinding) :
        RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        return PostViewHolder(
            PriceItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val product= productList[position]

        holder.binding.apply {
            tvPriceItem.text = "${product.productName} is at Rs. ${product.kalimatiPrice}/ ${product.unit.displayName}"
        }
    }

    override fun getItemCount(): Int = productList.size

    fun submitList(newProductList: List<Product>) {
        val oldProductList = productList
        val diffResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(
            UserItemDiffCallback(
                oldProductList,
                newProductList
            )
        )
        productList = newProductList
        diffResult.dispatchUpdatesTo(this)
    }

    class UserItemDiffCallback(
        var oldProductList: List<Product>,
        var newProductList: List<Product>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int {
            return oldProductList.size
        }

        override fun getNewListSize(): Int {
            return newProductList.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return (oldProductList[oldItemPosition]._id == newProductList[newItemPosition]._id)
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return (oldProductList[oldItemPosition] == newProductList[newItemPosition])
        }
    }

}