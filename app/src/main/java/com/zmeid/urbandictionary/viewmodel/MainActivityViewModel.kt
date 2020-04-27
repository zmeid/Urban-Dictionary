package com.zmeid.urbandictionary.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zmeid.urbandictionary.model.Urban
import com.zmeid.urbandictionary.model.UrbanApiResponseModel
import com.zmeid.urbandictionary.repository.UrbanRepository
import com.zmeid.urbandictionary.util.ApiResponseWrapper
import com.zmeid.urbandictionary.util.SHARED_PREF_SHOULD_SORT_BY_THUMBS_UP
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainActivityViewModel @Inject constructor(
    private val urbanRepository: UrbanRepository,
    private val sharedPreferences: SharedPreferences
) : ViewModel() {

    private var urbanList: List<Urban> = emptyList()

    private var shouldSortByThumbsUp = sharedPreferences.getBoolean(SHARED_PREF_SHOULD_SORT_BY_THUMBS_UP, true)

    private val urbanLastSearchWordMutable = MutableLiveData<String>()
    val urbanLastSearchWord: LiveData<String> = urbanLastSearchWordMutable

    private val urbanDefinitionResultMutable: MutableLiveData<ApiResponseWrapper<UrbanApiResponseModel>> =
        MutableLiveData()
    val urbanDefinitionResult: LiveData<ApiResponseWrapper<UrbanApiResponseModel>> = urbanDefinitionResultMutable

    /**
     * This is the method used by activity. If [forceRefresh] is true, TODO devam et
     */
    fun searchDefinition(word: String, forceRefresh: Boolean) {
        if (forceRefresh || word != urbanLastSearchWordMutable.value) {
            getDefinitionFromApi(word)
            urbanLastSearchWordMutable.value = word
        }
    }

    private fun getDefinitionFromApi(word: String) {
        viewModelScope.launch(Dispatchers.IO) {
            urbanDefinitionResultMutable.postValue(ApiResponseWrapper.loading())
            try {
                urbanList = urbanRepository.getDefinition(word).urbanList
                sortDefinitionResults(shouldSortByThumbsUp)
            } catch (e: Exception) {
                urbanDefinitionResultMutable.postValue(ApiResponseWrapper.error(exception = e))
            }
        }
    }

    fun sortDefinitionResults(shouldSortByThumbsUp: Boolean) {
        viewModelScope.launch {
            updateSortingPreference(shouldSortByThumbsUp)
            if (urbanList.isNotEmpty()) {
                urbanDefinitionResultMutable.postValue(ApiResponseWrapper.loading())
                urbanList = if (shouldSortByThumbsUp) {
                    urbanList.sortedByDescending { urban -> urban.thumbsUp }
                } else {
                    urbanList.sortedByDescending { urban -> urban.thumbsDown }
                }
                urbanDefinitionResultMutable.postValue(ApiResponseWrapper.success(UrbanApiResponseModel(urbanList)))
            }
        }
    }

    private fun updateSortingPreference(shouldSortByThumbsUp: Boolean) {
        this.shouldSortByThumbsUp = shouldSortByThumbsUp
        sharedPreferences.edit().putBoolean(SHARED_PREF_SHOULD_SORT_BY_THUMBS_UP, this.shouldSortByThumbsUp).apply()
    }
}