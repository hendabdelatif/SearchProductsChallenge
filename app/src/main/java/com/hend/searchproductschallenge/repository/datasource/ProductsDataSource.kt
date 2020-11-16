package com.hend.searchproductschallenge.repository.datasource

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.hend.searchproductschallenge.network.response.Product
import com.hend.searchproductschallenge.repository.SearchRepository
import com.hend.searchproductschallenge.utils.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

/**
 * DataSource and PagedList configuration
 */
@ExperimentalCoroutinesApi
class ProductsDataSource(
    coroutineContext: CoroutineContext,
    private val searchRepository: SearchRepository
) : PageKeyedDataSource<Int, Product>() {

    private val scope = CoroutineScope(coroutineContext)

    private val _searchResult = MutableLiveData<SearchResult>()
    val searchResult: LiveData<SearchResult>
        get() = _searchResult


    @FlowPreview
    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Product>
    ) {
        scope.launch {

            searchRepository.getInternalSearchResult(FIRST_PAGE).collect {
                when (it) {
                    is SuccessResult -> {
                        _searchResult.postValue(it)
                        try {
                            callback.onResult(
                                it.result, null,
                                FIRST_PAGE
                            )
                        } catch (e: IllegalStateException) {
                            e.printStackTrace()
                        }
                    }
                    else -> _searchResult.postValue(it)
                }
            }
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Product>) {
        // Nothing to implement
    }

    @FlowPreview
    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Product>) {
        scope.launch {
            searchRepository.getInternalSearchResult(params.key + 1).collect {
                when (it) {
                    is SuccessResult -> {
                        _searchResult.postValue(it)
                        try {
                            callback.onResult(
                                it.result, params.key + 1
                            )
                        } catch (e: IllegalStateException) {
                            e.printStackTrace()
                        }
                    }
                    else -> _searchResult.postValue(it)
                }
            }
        }
    }

    companion object {
        private const val FIRST_PAGE = 1
    }


}