package com.zmeid.urbandictionary.di.module

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.zmeid.urbandictionary.util.DialogUtils
import com.zmeid.urbandictionary.util.ErrorMessageGenerator
import com.zmeid.urbandictionary.util.SOUND_PLAYER_APP_NAME
import dagger.Module
import dagger.Provides

/**
 * Provides utility objects.
 */
@Module
class UtilsModule {
    @Provides
    fun providesLayoutManager(context: Activity) = LinearLayoutManager(context)

    @Provides
    fun providesApiErrorMessageGenerator(context: Activity) = ErrorMessageGenerator(context)

    @Provides
    fun providesSharedPref(appContext: Application): SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(appContext)

    @Provides
    fun providesDialogUtils(activity: Activity, sharedPreferences: SharedPreferences) =
        DialogUtils(activity, sharedPreferences)

    @Provides
    fun providesShareTextIntent() = Intent().apply {
        action = Intent.ACTION_SEND
        type = "text/plain"
    }

    @Provides
    fun providesDataSourceFactory(context: Activity) =
        DefaultDataSourceFactory(context, Util.getUserAgent(context, SOUND_PLAYER_APP_NAME))

    @Provides
    fun providesSimpleExoPlayer(context: Activity) = SimpleExoPlayer.Builder(context).build()
}