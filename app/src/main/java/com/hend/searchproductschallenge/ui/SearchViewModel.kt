package com.hend.searchproductschallenge.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.hend.searchproductschallenge.network.response.Product
import com.hend.searchproductschallenge.repository.SearchRepository
import com.hend.searchproductschallenge.repository.datasource.ProductsDataSource
import com.hend.searchproductschallenge.repository.datasource.ProductsDataSourceFactory
import com.hend.searchproductschallenge.utils.PAGE_SIZE
import com.hend.searchproductschallenge.utils.SearchResult
import com.hend.searchproductschallenge.utils.cancelIfActive
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

/**
 * ViewModel to get filtered products after search in a paging list from data source
 */
@ExperimentalCoroutinesApi
class SearchViewModel @ViewModelInject constructor(
    searchRepository: SearchRepository
) : ViewModel() {

    /** Coroutine's background job **/
    private val job = Job()
    private val coroutineContext: CoroutineContext = job + Dispatchers.IO

    private var productsLiveData: LiveData<PagedList<Product>>
    private var productsDataSourceFactory: ProductsDataSourceFactory

    internal val queryChannel = searchRepository.queryChannel

    init {
        val config = PagedList.Config.Builder()
            .setPageSize(PAGE_SIZE)
            .setEnablePlaceholders(false)
            .build()

        productsDataSourceFactory = ProductsDataSourceFactory(coroutineContext, searchRepository)
        productsLiveData = LivePagedListBuilder(productsDataSourceFactory, config).build()

    }

    fun getProductsList(): LiveData<PagedList<Product>> = productsLiveData

    fun getSearchResult(): LiveData<SearchResult> = Transformations.switchMap(
        productsDataSourceFactory.dataSourceLiveData,
        ProductsDataSource::searchResult
    )

    /** Clear our job when the linked activity is destroyed to avoid memory leaks **/
    override fun onCleared() {
        super.onCleared()
        job.cancelIfActive()
    }
}