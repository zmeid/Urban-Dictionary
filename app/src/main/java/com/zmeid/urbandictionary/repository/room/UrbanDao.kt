package com.zmeid.urbandictionary.repository.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.zmeid.urbandictionary.model.Urban

@Dao
interface UrbanDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE) // in case of force reload, replace
    suspend fun insertAll(urbanList: List<Urban>)

    @Query("SELECT * FROM urban where tag = :word")
    suspend fun getDefinition(word: String): List<Urban>

    @Query("DELETE FROM urban")
    suspend fun deleteAll()
}