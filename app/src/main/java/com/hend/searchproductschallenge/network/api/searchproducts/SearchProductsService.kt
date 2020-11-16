package com.hend.searchproductschallenge.network.api.searchproducts

import com.hend.searchproductschallenge.network.response.SearchProductsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Service API to search products list
 */
interface SearchProductsService {

    @GET("search")
    suspend fun searchProductsList(
        @Query("query") query: String,
        @Query("page") page: Int
    ): Response<SearchProductsResponse>
}