package com.zmeid.urbandictionary.repository.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.zmeid.urbandictionary.model.Urban

/**
 * [AppDatabase] is the database abstract class used by room framework. It holds entities, database version and dao classes.
 *
 * New dao classes should be added here as abstract functions.
 *
 * If any structural changes were made, database version should be increased and migration policy should be added to room database builder. @see (https://developer.android.com/training/data-storage/room/migrating-db-versions)
 */
@Database(entities = [Urban::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun urbanDao(): UrbanDao
}