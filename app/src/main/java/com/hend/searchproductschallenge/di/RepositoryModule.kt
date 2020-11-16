package com.hend.searchproductschallenge.di

import com.hend.searchproductschallenge.network.NetworkHelper
import com.hend.searchproductschallenge.network.api.searchproducts.SearchProductsService
import com.hend.searchproductschallenge.repository.SearchRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * Module that provides an instance of the Repository
 */
@ExperimentalCoroutinesApi
@Module
@InstallIn(ActivityRetainedComponent::class)
object RepositoryModule {

    @Provides
    @ActivityRetainedScoped
    fun provideCategoriesListRepository(networkHelper: NetworkHelper,
                                        searchProductsService: SearchProductsService
    ): SearchRepository {
        return SearchRepository(networkHelper, searchProductsService)
    }
}
