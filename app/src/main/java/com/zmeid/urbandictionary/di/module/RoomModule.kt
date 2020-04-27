package com.zmeid.urbandictionary.di.module

import android.app.Application
import androidx.room.Room
import com.zmeid.urbandictionary.repository.room.AppDatabase
import com.zmeid.urbandictionary.repository.room.UrbanDao
import com.zmeid.urbandictionary.util.DATABASE_NAME
import dagger.Module
import dagger.Provides

import javax.inject.Singleton

@Module
class RoomModule {
    @Singleton
    @Provides
    fun providesRoomDatabase(appContext: Application): AppDatabase = Room.databaseBuilder(
        appContext,
        AppDatabase::class.java, DATABASE_NAME
    ).build()

    @Singleton
    @Provides
    fun providesUrbanDao(appDatabase: AppDatabase): UrbanDao = appDatabase.urbanDao()
}