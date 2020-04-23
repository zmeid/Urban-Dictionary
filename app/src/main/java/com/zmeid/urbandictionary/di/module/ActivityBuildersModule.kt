package com.zmeid.urbandictionary.di.module

import com.zmeid.urbandictionary.view.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Defines activities where dependencies are going to be injected.
 */
@Module
abstract class ActivityBuildersModule {
    @ContributesAndroidInjector(modules = [ViewModelsModule::class])
    abstract fun contributeMainActivity(): MainActivity
}