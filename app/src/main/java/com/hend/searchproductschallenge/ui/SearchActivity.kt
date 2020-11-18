package com.hend.searchproductschallenge.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagedList
import com.hend.searchproductschallenge.R
import com.hend.searchproductschallenge.databinding.ActivitySearchBinding
import com.hend.searchproductschallenge.network.response.Product
import com.hend.searchproductschallenge.ui.adapter.ProductsAdapter
import com.hend.searchproductschallenge.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

/**
 * Activity to show list of products filtered after instance search
 */
@AndroidEntryPoint
@ExperimentalCoroutinesApi
class SearchActivity : AppCompatActivity() {

    private val viewModel by lazy {
        ViewModelProvider(this)[SearchViewModel::class.java]
    }

    private lateinit var binding: ActivitySearchBinding
    private val productsAdapter = ProductsAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initViewBinding()
        observeViewModel()
    }

    private fun initViewBinding() {
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.txtErrorMessage.setText(R.string.start_search_products)
        binding.txtSearch.doAfterTextChanged { editable ->
            lifecycleScope.launch {
                binding.progressBar.visibility = View.VISIBLE
                binding.txtErrorMessage.visibility = View.GONE
                viewModel.queryChannel.emit(editable.toString())
            }
        }
    }

    private fun observeViewModel() {
        viewModel.getProductsList().observe(this, {
            handleSearchResult(it)
        })
    }

    private fun handleSearchResult(productsList: PagedList<Product>) {
        viewModel.getSearchResult()
            .observe(this, { state ->
                when (state) {
                    is SuccessResult -> {
                        binding.rvSearch.visibility = View.VISIBLE
                        binding.progressBar.visibility = View.GONE
                        binding.txtErrorMessage.visibility = View.GONE

                        binding.rvSearch.adapter = productsAdapter
                        productsAdapter.submitList(productsList)

                    }
                    is ErrorResult -> {
                        binding.rvSearch.visibility = View.GONE
                        binding.progressBar.visibility = View.GONE
                        binding.txtErrorMessage.visibility = View.VISIBLE
                        binding.txtErrorMessage.setText(R.string.search_error)
                    }
                    is EmptyResult -> {
                        binding.rvSearch.visibility = View.GONE
                        binding.progressBar.visibility = View.GONE
                        binding.txtErrorMessage.visibility = View.VISIBLE
                        binding.txtErrorMessage.setText(R.string.empty_result)
                    }
                    is EndOfResult -> {
                        binding.rvSearch.visibility = View.VISIBLE
                        binding.progressBar.visibility = View.GONE
                        binding.txtErrorMessage.visibility = View.GONE
                    }
                    is EmptyQuery -> {
                        binding.rvSearch.visibility = View.GONE
                        binding.progressBar.visibility = View.GONE
                        binding.txtErrorMessage.visibility = View.VISIBLE
                        binding.txtErrorMessage.setText(R.string.not_enough_characters)
                    }
                    is NetworkError -> {
                        binding.rvSearch.visibility = View.GONE
                        binding.progressBar.visibility = View.GONE
                        binding.txtErrorMessage.visibility = View.VISIBLE
                        binding.txtErrorMessage.setText(R.string.connection_error)
                    }
                }

            })

    }


}