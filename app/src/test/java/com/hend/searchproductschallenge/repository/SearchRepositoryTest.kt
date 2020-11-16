package com.hend.searchproductschallenge.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.hend.searchproductschallenge.MainCoroutinesRule
import com.hend.searchproductschallenge.network.NetworkHelper
import com.hend.searchproductschallenge.network.api.searchproducts.SearchProductsService
import com.hend.searchproductschallenge.utils.MockUtil
import com.hend.searchproductschallenge.utils.SearchResult
import com.hend.searchproductschallenge.utils.SuccessResult
import com.nhaarman.mockitokotlin2.mock
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.IOException

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class SearchRepositoryTest {

    @Rule
    @JvmField
    val instantExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutinesRule = MainCoroutinesRule()

    lateinit var mockWebServer: MockWebServer

    private lateinit var service: SearchProductsService
    private lateinit var searchRepository: SearchRepository
    private val networkHelper : NetworkHelper = mock()


    @Throws(IOException::class)
    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        mockWebServer.start()
        service = createService(SearchProductsService::class.java)

        searchRepository = SearchRepository(networkHelper, service)
    }

    @Throws(IOException::class)
    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    private fun createService(clazz: Class<SearchProductsService>): SearchProductsService {
        return Retrofit.Builder()
            .baseUrl(mockWebServer.url("search"))
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(clazz)
    }

    @FlowPreview
    @Test
    fun testInstantSearch() = runBlockingTest {

        val queriesList = mutableListOf<String>()

        val response = searchRepository.getInternalSearchResult(1)

        /** start collecting flows in a new coroutine **/
        val job = launch {

            /** collect the flow to trigger the debounce behavior **/
            response.launchIn(this)

            /** make sure we're actually sending all queries through **/
            searchRepository.queryChannel.mapLatest { query ->
                queriesList.add(query)
            }.launchIn(this)

            /** actually trigger the debounce delay **/
            advanceTimeBy(100)

            response.collect { value: SearchResult ->
                val responseBody = requireNotNull((value as SuccessResult).result)
                MatcherAssert.assertThat(responseBody[0].productName, CoreMatchers.`is`(MockUtil.mockSearchProductsResponse().products[0].productName))
            }
        }

        /** need to cancel all the coroutines launched for collection **/
        job.cancel()
    }
}