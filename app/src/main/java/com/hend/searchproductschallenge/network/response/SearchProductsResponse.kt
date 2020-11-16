package com.hend.searchproductschallenge.network.response

/**
 * API Response data classes
 */
data class SearchProductsResponse(
    val currentPage: Int,
    val pageSize: Int,
    val totalResults: Int,
    val pageCount: Int,
    val products: List<Product>
)

data class Product(
    val productId: Int, val productName: String,
    val availabilityState: Int, val salesPriceIncVat: Double,
    val productImage: String, val nextDayDelivery: Boolean
)


