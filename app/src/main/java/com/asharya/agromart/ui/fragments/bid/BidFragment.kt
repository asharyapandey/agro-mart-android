package com.asharya.agromart.ui.fragments.bid

import android.app.AlertDialog
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.asharya.agromart.R
import com.asharya.agromart.adapters.BidAdapter
import com.asharya.agromart.databinding.AddBidDialogBinding
import com.asharya.agromart.databinding.FragmentBidBinding
import com.asharya.agromart.model.Bid
import com.asharya.agromart.repository.BidRepository
import com.asharya.agromart.uitls.Resource

class BidFragment : Fragment(R.layout.fragment_bid), BidAdapter.BidClickListener {
    private lateinit var binding: FragmentBidBinding
    private val args by navArgs<BidFragmentArgs>()
    private lateinit var viewModel: BidViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentBidBinding.bind(view)

        viewModel = ViewModelProvider(this, BidViewModelFactory(BidRepository())).get(BidViewModel::class.java)
        val adapter = BidAdapter(requireContext(), this)
        binding.rvBid.adapter = adapter

        viewModel.getBids(args.postID)

        viewModel.bids.observe(viewLifecycleOwner) { response ->
            if(response is Resource.Success) {
                response.data?.let { data ->
                    binding.rvBid.scrollToPosition(0)
                    adapter.submitList(data.result!!)
                    binding.tvPrice.text = "Rs. ${data.maxBid}"
                }
            } else if (response is Resource.Error) {
                Toast.makeText(context, "${response.message}", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.bidAdded.observe(viewLifecycleOwner) { response ->
            if(response is Resource.Success) {
                response.data?.let { data ->
                    Toast.makeText(context, data.message, Toast.LENGTH_SHORT).show()
                    viewModel.getBids(args.postID)
                }
            } else if (response is Resource.Error) {
                Toast.makeText(context, "${response.message}", Toast.LENGTH_SHORT).show()
            }
        }

        // add bid click
        binding.btnAddBid.setOnClickListener {
            showAddBidDialog()
        }
    }

    fun showAddBidDialog() {
        val dialogBinding = AddBidDialogBinding.inflate(LayoutInflater.from(context))
        val dialog = AlertDialog.Builder(context).setView(dialogBinding.root)
            .setCancelable(true)
            .create()

        dialogBinding.btnDialogAddBid.setOnClickListener {
            // validation
            when {
                TextUtils.isEmpty(dialogBinding.etAmountOffered.text) -> {
                    dialogBinding.etAmountOffered.error = "Please add an offer amount."
                    dialogBinding.etAmountOffered.requestFocus()
                    return@setOnClickListener
                }
                TextUtils.isEmpty(dialogBinding.etAddress.text) -> {
                    dialogBinding.etAddress.error = "Please add you address."
                    dialogBinding.etAddress.requestFocus()
                    return@setOnClickListener
                }
                TextUtils.isEmpty(dialogBinding.etRemarks.text) -> {
                    dialogBinding.etRemarks.error = "Please add an remark for your offer."
                    dialogBinding.etRemarks.requestFocus()
                    return@setOnClickListener
                }
            }
            // addding
            val address = dialogBinding.etAddress.text.toString().trim()
            val remarks= dialogBinding.etRemarks.text.toString().trim()
            val amountOffered= dialogBinding.etAmountOffered.text.toString().trim()
            viewModel.addBid(args.postID, address, remarks, amountOffered)
            dialog.cancel()
        }
        dialog.show()
    }

    override fun acceptClick(bid: Bid, position: Int) {
        Toast.makeText(context, "accept Clicked", Toast.LENGTH_SHORT).show()
    }

    override fun rejectClick(bid: Bid, position: Int) {
        Toast.makeText(context, "reject Clicked", Toast.LENGTH_SHORT).show()
    }

    override fun moreClick(bid: Bid, view: View) {
        TODO("Not yet implemented")
    }

}