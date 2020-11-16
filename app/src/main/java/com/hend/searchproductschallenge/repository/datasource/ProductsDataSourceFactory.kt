package com.hend.searchproductschallenge.repository.datasource

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.hend.searchproductschallenge.network.response.Product
import com.hend.searchproductschallenge.repository.SearchRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlin.coroutines.CoroutineContext


/**
 * Retrieving the data using the DataSource and PagedList configuration
 */
@ExperimentalCoroutinesApi
class ProductsDataSourceFactory(
    private val coroutineContext: CoroutineContext,
    private val searchRepository: SearchRepository
) : DataSource.Factory<Int, Product>() {

    val dataSourceLiveData = MutableLiveData<ProductsDataSource>()

    override fun create(): DataSource<Int, Product> {
        val dataSource = ProductsDataSource(coroutineContext, searchRepository)
        dataSourceLiveData.postValue(dataSource)
        return dataSource
    }
}

