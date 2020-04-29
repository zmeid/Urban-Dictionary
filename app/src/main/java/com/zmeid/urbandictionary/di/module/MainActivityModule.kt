package com.zmeid.urbandictionary.di.module

import android.app.Activity
import com.zmeid.urbandictionary.view.ui.MainActivity
import dagger.Binds
import dagger.Module

/**
 * This module provides Activity to be used as context. For ex in [UtilsModule].
 */
@Module
abstract class MainActivityModule {
    @Binds
    abstract fun providesActivity(activity: MainActivity): Activity
}