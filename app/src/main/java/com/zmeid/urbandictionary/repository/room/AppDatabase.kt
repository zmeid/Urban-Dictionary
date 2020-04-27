package com.zmeid.urbandictionary.repository.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.zmeid.urbandictionary.model.Urban

@Database(entities = [Urban::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun urbanDao(): UrbanDao
}