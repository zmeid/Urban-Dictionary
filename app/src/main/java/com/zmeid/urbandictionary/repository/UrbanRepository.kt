package com.zmeid.urbandictionary.repository

import com.zmeid.urbandictionary.model.Urban
import com.zmeid.urbandictionary.repository.room.UrbanDao
import com.zmeid.urbandictionary.repository.webservice.UrbanService
import javax.inject.Inject

/**
 * [UrbanRepository] is responsible to supply data to [com.zmeid.urbandictionary.viewmodel.MainActivityViewModel]. It decides where to get the data from. If the word definition results are already in database, it returns them. If not, API call is made and results are inserted to database.
 *
 * [UrbanService] and [UrbanDao] are injected here.
 */
class UrbanRepository @Inject constructor(private val urbanService: UrbanService, private val urbanDao: UrbanDao) {

    /**
     * Gets the definitions from database if available. Otherwise, makes the API call, inserts the definitions to database and returns results to viewmodel.
     */
    suspend fun getDefinitions(word: String, forceRefresh: Boolean): List<Urban> {

        if (forceRefresh) return getDefinitionsFromApiAndInsertToDatabase(word)

        val urbanList = urbanDao.getDefinition(word)

        return if (urbanList.isEmpty()) getDefinitionsFromApiAndInsertToDatabase(word) else urbanList
    }

    private suspend fun getDefinitionsFromApiAndInsertToDatabase(word: String): List<Urban> {
        val urbanList = urbanService.getDefinition(word).urbanList
        insertDefinitionsToDatabase(urbanList, word)
        return urbanList
    }

    private suspend fun insertDefinitionsToDatabase(urbanList: List<Urban>, searchedWord: String) {
        urbanList.map { urban ->
            urban.tag = searchedWord
            if (!urban.soundUrls.isNullOrEmpty()) urban.soundUrl =
                urban.soundUrls!![0] // Only first URL to be inserted to room and to be played sound
        }
        urbanDao.insertAll(urbanList)
    }
}