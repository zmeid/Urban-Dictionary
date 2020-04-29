package com.zmeid.urbandictionary.model

import com.google.gson.annotations.SerializedName
import com.zmeid.urbandictionary.util.SN_LIST

/**
 *  Urban API returns definitions as list with [SN_LIST] tag. [UrbanApiResponseModel] is used to hold and map list of [Urban]s.
 */
data class UrbanApiResponseModel(@SerializedName(SN_LIST) val urbanList: List<Urban>)