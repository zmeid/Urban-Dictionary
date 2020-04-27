package com.zmeid.urbandictionary.di.module

import android.app.Activity
import android.app.Application
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.zmeid.urbandictionary.util.ApiErrorMessageGenerator
import com.zmeid.urbandictionary.util.DialogUtils
import dagger.Module
import dagger.Provides

@Module
class UtilsModule {
    @Provides
    fun providesLayoutManager(context: Activity) = LinearLayoutManager(context)

    @Provides
    fun providesApiErrorMessageGenerator(context: Activity) = ApiErrorMessageGenerator(context)

    @Provides
    fun providesSharedPref(context: Application): SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(context)

    @Provides
    fun providesDialogUtils(activity: Activity, sharedPreferences: SharedPreferences) =
        DialogUtils(activity, sharedPreferences)
}