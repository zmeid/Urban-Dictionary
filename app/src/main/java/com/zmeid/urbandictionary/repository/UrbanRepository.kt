package com.zmeid.urbandictionary.repository

import com.zmeid.urbandictionary.repository.webservice.UrbanService
import javax.inject.Inject

class UrbanRepository @Inject constructor(private val urbanService: UrbanService) {
    suspend fun getDefinition(word: String) = urbanService.getDefinition(word)
}