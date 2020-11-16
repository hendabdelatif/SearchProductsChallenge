package com.hend.searchproductschallenge.utils

import com.hend.searchproductschallenge.network.response.Product

/**
 * Base class to represent common status results
 */
sealed class SearchResult
/**
 * Shows data successfully on the screen.
 */
class SuccessResult(val result: List<Product>) : SearchResult()

/**
 * Shows no data found
 */
object EmptyResult : SearchResult()

/**
 * Shows the end of total number of results
 */
object EndOfResult : SearchResult()

/**
 * Search box is empty
 */
object EmptyQuery : SearchResult()

/**
 * Shows error while searching
 */
class ErrorResult(val e: Throwable) : SearchResult()

/**
 * Shows error in network connection
 */
object NetworkError : SearchResult()
