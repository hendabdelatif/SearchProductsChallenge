package com.hend.searchproductschallenge.repository

import androidx.annotation.WorkerThread
import com.hend.searchproductschallenge.network.NetworkHelper
import com.hend.searchproductschallenge.network.api.searchproducts.SearchProductsService
import com.hend.searchproductschallenge.utils.*
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.mapLatest
import javax.inject.Inject

/**
 * Used to get products list from API service by instance search
 */
@ExperimentalCoroutinesApi
class SearchRepository @Inject constructor(
    private val networkHelper: NetworkHelper,
    private val searchProductsService: SearchProductsService
) {

    internal val queryChannel = MutableStateFlow("")

    @FlowPreview
    @WorkerThread
    suspend fun getInternalSearchResult(page: Int) =
        queryChannel
            .debounce(SEARCH_DELAY_MS)
            .mapLatest {
                try {
                    if (networkHelper.isNetworkConnected()) {
                        if (it.length >= MIN_QUERY_LENGTH) {
                            val searchResult =
                                searchProductsService.searchProductsList(it, page)

                            if (searchResult.isSuccessful && searchResult.body() != null) {
                                    if(page <= searchResult.body()!!.currentPage) {
                                        SuccessResult(searchResult.body()!!.products)
                                    }else{
                                        EndOfResult
                                    }
                            } else
                                EmptyResult
                        } else {
                            EmptyQuery
                        }
                    } else {
                        NetworkError
                    }
                } catch (e: Throwable) {
                    if (e is CancellationException) {
                        throw e
                    } else {
                        ErrorResult(e)
                    }
                }
            }
            .flowOn(Dispatchers.IO)

    companion object {
        const val SEARCH_DELAY_MS = 100L
        const val MIN_QUERY_LENGTH = 3
    }
}