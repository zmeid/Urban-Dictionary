package com.zmeid.urbandictionary.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zmeid.urbandictionary.model.UrbanApiResponseModel
import com.zmeid.urbandictionary.repository.UrbanRepository
import com.zmeid.urbandictionary.util.ApiResponseWrapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainActivityViewModel @Inject constructor(private val urbanRepository: UrbanRepository) : ViewModel() {

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
                urbanDefinitionResultMutable.postValue(ApiResponseWrapper.success(urbanRepository.getDefinition(word)))
            } catch (e: Exception) {
                urbanDefinitionResultMutable.postValue(ApiResponseWrapper.error(exception = e))
            }
        }
    }
}