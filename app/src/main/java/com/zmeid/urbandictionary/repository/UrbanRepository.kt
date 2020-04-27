package com.zmeid.urbandictionary.repository

import com.zmeid.urbandictionary.model.Urban
import com.zmeid.urbandictionary.repository.room.UrbanDao
import com.zmeid.urbandictionary.repository.webservice.UrbanService
import javax.inject.Inject

class UrbanRepository @Inject constructor(private val urbanService: UrbanService, private val urbanDao: UrbanDao) {

    private suspend fun insertDefinitionsToDatabase(urbanList: List<Urban>, searchedWord: String) {
        urbanList.map { urban -> urban.tag = searchedWord }
        urbanDao.insertAll(urbanList)
    }

    suspend fun getDefinitions(word: String): List<Urban> {
        var urbanList = urbanDao.getDefinition(word)

        if (urbanList.isEmpty()) {
            // if database has no definitions, fetch the API and insert result to database.
            urbanList = urbanService.getDefinition(word).urbanList
            insertDefinitionsToDatabase(urbanList, word)
        }
        return urbanList
    }
}