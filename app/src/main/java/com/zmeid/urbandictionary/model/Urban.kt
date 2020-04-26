package com.zmeid.urbandictionary.model

import com.google.gson.annotations.SerializedName

data class Urban(
    @SerializedName("word") val word: String,
    @SerializedName("definition") val definition: String,
    @SerializedName("example") val example: String,
    @SerializedName("author") val author: String,
    @SerializedName("thumbs_up") val thumbsUp: Int,
    @SerializedName("thumbs_down") val thumbsDown: Int
// TODO add date and URLs?
)