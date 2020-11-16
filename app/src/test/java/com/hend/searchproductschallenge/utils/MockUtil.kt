package com.hend.searchproductschallenge.utils

import com.hend.searchproductschallenge.network.response.Product
import com.hend.searchproductschallenge.network.response.SearchProductsResponse

object MockUtil {

    // Mock response
    fun mockSearchProductsResponse() =
        SearchProductsResponse(
            currentPage = 1,
            pageSize = 24,
            totalResults = 70,
            pageCount = 3,
            products = listOf(mockProduct())
        )

    // Mock Product
    private fun mockProduct() = Product(
        productId = 3131,
        productName = "Apple",
        availabilityState = 2,
        salesPriceIncVat = 30.2,
        productImage = "",
        nextDayDelivery = true
    )

}
