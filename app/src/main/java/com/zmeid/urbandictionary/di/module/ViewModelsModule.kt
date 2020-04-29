package com.zmeid.urbandictionary.di.module

import androidx.lifecycle.ViewModel
import com.zmeid.urbandictionary.di.ViewModelKey
import com.zmeid.urbandictionary.viewmodel.MainActivityViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Enables dependency injection for viewModels. New viewmodels should be added here with related activity.
 */
@Module
abstract class ViewModelsModule {
    @Binds
    @IntoMap
    @ViewModelKey(MainActivityViewModel::class)
    abstract fun bindMainActivityViewModel(mainActivityViewModel: MainActivityViewModel): ViewModel
}