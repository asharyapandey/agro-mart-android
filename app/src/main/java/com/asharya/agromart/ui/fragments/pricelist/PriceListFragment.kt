package com.asharya.agromart.ui.fragments.pricelist

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.asharya.agromart.R
import com.asharya.agromart.adapters.PriceAdapter
import com.asharya.agromart.databinding.FragmentPriceListBinding
import com.asharya.agromart.repository.PostRepository
import com.asharya.agromart.uitls.Resource
import java.util.*

class PriceListFragment : Fragment(R.layout.fragment_price_list) {
    private lateinit var binding:FragmentPriceListBinding
    private lateinit var viewModel: PriceListViewModel
    private val productsIdMap = mutableMapOf<String, String>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPriceListBinding.bind(view)
        viewModel = ViewModelProvider(
            this,
            PriceListViewModelFactory(PostRepository())
        ).get(PriceListViewModel::class.java)

        viewModel.getProducts()
        val adapter = PriceAdapter()
        binding.rvPriceList.adapter = adapter

        viewModel.products.observe(viewLifecycleOwner) {response ->
            if(response is Resource.Success) {
                response.data?.let { data ->
                    adapter.submitList(data.result)
                }
            } else {
                Toast.makeText(context, "${response.message}", Toast.LENGTH_SHORT).show()
            }
        }


    }
}