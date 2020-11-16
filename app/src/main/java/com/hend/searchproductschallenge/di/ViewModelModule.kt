package com.hend.searchproductschallenge.di

import com.hend.searchproductschallenge.repository.SearchRepository
import com.hend.searchproductschallenge.ui.SearchViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * Module that provides instances of ViewModels
 */
@ExperimentalCoroutinesApi
@Module
@InstallIn(ActivityRetainedComponent::class)
object ViewModelModule {

    @Provides
    @ActivityRetainedScoped
    fun provideSearchViewModel(searchRepository: SearchRepository) : SearchViewModel {
        return SearchViewModel(searchRepository)
    }

}
