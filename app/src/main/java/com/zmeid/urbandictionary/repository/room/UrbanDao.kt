package com.zmeid.urbandictionary.repository.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.zmeid.urbandictionary.model.Urban

/**
 * [UrbanDao] is data access object which holds database queries.
 */
@Dao
interface UrbanDao {
    /**
     * We insert all definitions coming from API to the database. Since we have force refresh option with swap-to-refresh, it can create conflicts when we try to insert definitions which are already in the database. In this case, we replace old rows with new ones with [OnConflictStrategy.REPLACE] conflict strategy.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(urbanList: List<Urban>)

    @Query("SELECT * FROM urban where tag = :word")
    suspend fun getDefinition(word: String): List<Urban>

    @Query("DELETE FROM urban")
    suspend fun deleteAll()
}