package com.zmeid.urbandictionary.di.module

import android.app.Activity
import androidx.recyclerview.widget.LinearLayoutManager
import com.zmeid.urbandictionary.util.ApiErrorMessageGenerator
import dagger.Module
import dagger.Provides

@Module
class UtilsModule {
    @Provides
    fun providesLayoutManager(context: Activity) = LinearLayoutManager(context)

    @Provides
    fun providesApiErrorMessageGenerator(context: Activity) = ApiErrorMessageGenerator(context)
}