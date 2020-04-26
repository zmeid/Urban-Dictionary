package com.zmeid.urbandictionary.repository.webservice

import com.zmeid.urbandictionary.model.UrbanApiResponseModel
import com.zmeid.urbandictionary.util.DEFINE_QUERY_PARAM_TERM
import com.zmeid.urbandictionary.util.GET_PATH_DEFINE
import retrofit2.http.GET
import retrofit2.http.Query

interface UrbanService {
    @GET(GET_PATH_DEFINE)
    suspend fun getDefinition(@Query(DEFINE_QUERY_PARAM_TERM) term: String): UrbanApiResponseModel
}