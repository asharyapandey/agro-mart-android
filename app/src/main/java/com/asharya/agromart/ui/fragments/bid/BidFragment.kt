package com.asharya.agromart.ui.fragments.bid

import android.app.AlertDialog
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.PopupMenu
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

        viewModel = ViewModelProvider(
            this,
            BidViewModelFactory(BidRepository())
        ).get(BidViewModel::class.java)
        val adapter = BidAdapter(requireContext(), this)
        binding.rvBid.adapter = adapter

        viewModel.getBids(args.postID)

        viewModel.bids.observe(viewLifecycleOwner) { response ->
            if (response is Resource.Success) {
                response.data?.let { data ->
                    adapter.submitList(data.result!!)
                    binding.tvPrice.text = "Rs. ${data.maxBid}"
                    // disabling button if the bid is accepted
                    if (data.isPostBidAccepted == true) {
                        binding.btnAddBid.isEnabled = false
                    }
                }
            } else if (response is Resource.Error) {
                Toast.makeText(context, "${response.message}", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.bidAdded.observe(viewLifecycleOwner) { response ->
            if (response is Resource.Success) {
                response.data?.let { data ->
                    Toast.makeText(context, data.message, Toast.LENGTH_SHORT).show()
                    viewModel.getBids(args.postID)
                    binding.rvBid.scrollToPosition(0)
                }
            } else if (response is Resource.Error) {
                Toast.makeText(context, "${response.message}", Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.bidStatus.observe(viewLifecycleOwner) { response ->
            if (response is Resource.Success) {
                response.data?.let { data ->
                    Toast.makeText(context, data.message, Toast.LENGTH_SHORT).show()
                    viewModel.getBids(args.postID)
                }
            } else if (response is Resource.Error) {
                Toast.makeText(context, "${response.message}", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.bidEdit.observe(viewLifecycleOwner) { response ->
            if (response is Resource.Success) {
                response.data?.let { data ->
                    Toast.makeText(context, data.message, Toast.LENGTH_SHORT).show()
                    viewModel.getBids(args.postID)
                }
            } else if (response is Resource.Error) {
                Toast.makeText(context, "${response.message}", Toast.LENGTH_SHORT).show()

            }
        }

        viewModel.bidDelete.observe(viewLifecycleOwner) { response ->
            if (response is Resource.Success) {
                response.data?.let { data ->
                    Toast.makeText(context, data.message, Toast.LENGTH_SHORT).show()
                    viewModel.getBids(args.postID)
                }
            } else if (response is Resource.Error) {
                Log.e("BidFragment", response.message!!)
                Toast.makeText(context, "${response.message}", Toast.LENGTH_SHORT).show()
            }
        }
        // add bid click
        binding.btnAddBid.setOnClickListener {
            showAddBidDialog()
        }
    }

    fun showAddBidDialog(bid: Bid = Bid(), isEdit: Boolean = false) {
        val dialogBinding = AddBidDialogBinding.inflate(LayoutInflater.from(context))
        val dialog = AlertDialog.Builder(context).setView(dialogBinding.root)
            .setCancelable(true)
            .create()

        if (isEdit) {
            dialogBinding.etRemarks.setText(bid.remarks)
            dialogBinding.etAmountOffered.setText(bid.amountOffered.toString())
            dialogBinding.etAddress.setText(bid.address)
            dialogBinding.btnDialogAddBid.text = "Edit Bid"
        }

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
            val remarks = dialogBinding.etRemarks.text.toString().trim()
            val amountOffered = dialogBinding.etAmountOffered.text.toString().trim()
            if(isEdit) {
                viewModel.editBid(bid._id!!, address, remarks, amountOffered)
            } else {
                viewModel.addBid(args.postID, address, remarks, amountOffered)
            }
            dialog.cancel()
        }
        dialog.show()
    }

    override fun acceptClick(bid: Bid, position: Int) {
        val builder = androidx.appcompat.app.AlertDialog.Builder(requireContext())

        val alertDialog = builder.apply {
            setTitle("Accepted this Bid?")
            setMessage("This bid will be accepted and it can't be undone.")
            setIcon(R.drawable.ic_done)

            setPositiveButton("Yes") { _, _ ->
                viewModel.changeBidStatus(bid._id!!, "ACCEPTED")
            }

            setNegativeButton("No") { _, _ ->

            }
                .create()
        }
        alertDialog.show()
    }

    override fun rejectClick(bid: Bid, position: Int) {
        val builder = androidx.appcompat.app.AlertDialog.Builder(requireContext())

        val alertDialog = builder.apply {
            setTitle("Reject this Bid?")
            setMessage("This bid will be rejected until the bidder changes it .")
            setIcon(R.drawable.ic_alert)

            setPositiveButton("Yes") { _, _ ->
                viewModel.changeBidStatus(bid._id!!, "REJECTED")
            }

            setNegativeButton("No") { _, _ ->

            }
                .create()
        }
        alertDialog.show()
    }

    override fun moreClick(bid: Bid, view: View) {
        val popupMenu = PopupMenu(context, view)
        popupMenu.menuInflater.inflate(R.menu.edit_delete, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.menuDelete -> delete(bid)
                R.id.menuEdit -> showAddBidDialog(bid, true)
            }
            true
        }
        popupMenu.show()
    }

    private fun delete(bid: Bid) {
        val builder = androidx.appcompat.app.AlertDialog.Builder(requireContext())

        val alertDialog = builder.apply {
            setTitle("Are You sure you want to delete this bid?")
            setMessage("This bid will be deleted forever and it cant be undone.")
            setIcon(R.drawable.ic_alert)

            setPositiveButton("Yes") { _, _ ->
                Toast.makeText(context, "watman", Toast.LENGTH_SHORT).show()
                viewModel.deleteBid(bid._id!!)
            }

            setNegativeButton("No") { _, _ ->

            }
                .create()
        }
        alertDialog.show()
    }

}
