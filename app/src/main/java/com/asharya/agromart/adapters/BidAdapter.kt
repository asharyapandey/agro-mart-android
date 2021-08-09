package com.asharya.agromart.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.asharya.agromart.api.ServiceBuilder
import com.asharya.agromart.databinding.BidItemBinding
import com.asharya.agromart.model.Bid
import com.asharya.agromart.model.GetPost
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class BidAdapter(val context: Context, private val listener: BidClickListener) :
    RecyclerView.Adapter<BidAdapter.BidViewHolder>() {
    private var bidList = emptyList<Bid>()

    inner class BidViewHolder(val binding: BidItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    interface BidClickListener {
        fun acceptClick(bid: Bid, position: Int)
        fun rejectClick(bid: Bid, position: Int)
        fun moreClick(bid: Bid, view: View)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BidViewHolder {
        return BidViewHolder(
            BidItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: BidViewHolder, position: Int) {
        val bid = bidList[position]

        holder.binding.apply {
            tvTitle.text = "${bid.userID?.fullName} placed a bid of Rs.${bid.amountOffered}."
            tvAddress.text = bid.address
            tvRemarks.text = bid.remarks

            // showing accept & reject button when status is PENDING and user is seeing his post
            // status is shown when other users are seeing it
            tvStatus.text = "Status: ${bid.status}"
            if (bid.isPostBidAccepted) {
                btnAccept.visibility = View.GONE
                btnReject.visibility = View.GONE
                tvStatus.visibility = View.VISIBLE
            } else {
                if (bid.belongsTo?._id == ServiceBuilder.userID && bid.status == "PENDING") {
                    btnAccept.visibility = View.VISIBLE
                    btnReject.visibility = View.VISIBLE
                    tvStatus.visibility = View.GONE
                } else {
                    tvStatus.visibility = View.VISIBLE
                    btnAccept.visibility = View.GONE
                    btnReject.visibility = View.GONE
                }
            }


            // formatting date
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            val date = LocalDate.parse(bid.createdAt, formatter)
            tvDateTime.text = date.toString()

            // click listeners for button
            btnAccept.setOnClickListener {
                listener.acceptClick(bid, position)
            }

            btnReject.setOnClickListener {
                listener.rejectClick(bid, position)
            }

            // showing more icon if the bid belongs to that user and post is not accepted
            // TODO: Add Check to see if the bid is already accepted if so don't let them edit or delete
            if (bid.userID?._id!! == ServiceBuilder.userID) {
                ibMore.visibility = View.VISIBLE
                ibMore.setOnClickListener {
                    listener.moreClick(bid, it)
                }
            } else {
                ibMore.visibility = View.GONE
            }


        }
    }

    override fun getItemCount(): Int = bidList.size

    fun submitList(newBidList: List<Bid>) {
        val oldBidList = bidList
        val diffResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(
            UserItemDiffCallback(
                oldBidList,
                newBidList
            )
        )
        bidList = newBidList
        diffResult.dispatchUpdatesTo(this)
    }

    class UserItemDiffCallback(
        var oldBidList: List<Bid>,
        var newBidList: List<Bid>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int {
            return oldBidList.size
        }

        override fun getNewListSize(): Int {
            return newBidList.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return (oldBidList[oldItemPosition]._id == newBidList[newItemPosition]._id)
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return (oldBidList[oldItemPosition] == newBidList[newItemPosition])
        }
    }

}