package com.zmeid.urbandictionary

import com.zmeid.urbandictionary.model.Urban
import com.zmeid.urbandictionary.model.UrbanApiResponseModel
import com.zmeid.urbandictionary.repository.UrbanRepository
import com.zmeid.urbandictionary.repository.room.UrbanDao
import com.zmeid.urbandictionary.repository.webservice.UrbanService
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

/**
 * Contains unit tests for [UrbanRepository].
 */
@RunWith(MockitoJUnitRunner::class)
class UrbanRepositoryUnitTest {

    @Mock lateinit var urbanService: UrbanService
    @Mock lateinit var urbanDao: UrbanDao
    @Mock lateinit var urbanApiResponseModel: UrbanApiResponseModel
    @Mock lateinit var urbanListFromDatabase: List<Urban>
    private val word = "Test"

    /**
     * Initialize mocking behaviours.
     */
    @Before
    fun init() {
        MockitoAnnotations.initMocks(this)
        runBlocking {
            Mockito.`when`(urbanService.getDefinition(word)).thenReturn(urbanApiResponseModel)
            Mockito.`when`(urbanDao.getDefinition(word)).thenReturn(urbanListFromDatabase)
        }
    }

    /**
     * Tests [UrbanRepository.getDefinitions]. If forceRefresh is true, urban list from [urbanApiResponseModel] should be returned. Otherwise, [urbanListFromDatabase] should be returned.
     */
    @Test
    fun testGetDefinitions() {
        val urbanRepository = UrbanRepository(urbanService, urbanDao)
        runBlocking {
            var urbanList = urbanRepository.getDefinitions(word, false)
            assertEquals(urbanList, urbanListFromDatabase)

            urbanList = urbanRepository.getDefinitions(word, true)
            assertEquals(urbanList, urbanApiResponseModel.urbanList)
        }
    }
}