package com.zmeid.urbandictionary.di.module

import androidx.lifecycle.ViewModelProvider
import com.zmeid.urbandictionary.viewmodel.factory.ViewModelProviderFactory
import dagger.Binds
import dagger.Module

/**
 * Generates dependency injection for ViewModelProviderFactory class.
 */
@Module(includes = [ViewModelsModule::class])
abstract class ViewModuleFactoryModule {
    @Binds
    abstract fun bindViewModelFactory(viewModelProviderFactory: ViewModelProviderFactory): ViewModelProvider.Factory
}