package com.zmeid.urbandictionary.di.component

import com.zmeid.urbandictionary.di.module.RetrofitModule
import com.zmeid.urbandictionary.repository.UrbanRepository
import dagger.Component
import javax.inject.Singleton

/**
 * The interface which enables dependency injection for UrbanRepository.
 */
@Singleton
@Component(modules = [RetrofitModule::class])
interface RepositoryComponent {
    fun getUrbanRepository(): UrbanRepository
}