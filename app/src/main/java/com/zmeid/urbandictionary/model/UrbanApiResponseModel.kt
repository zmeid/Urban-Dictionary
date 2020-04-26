package com.zmeid.urbandictionary.model

import com.google.gson.annotations.SerializedName

data class UrbanApiResponseModel(@SerializedName("list") val urbanList: List<Urban>)