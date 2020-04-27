package com.zmeid.urbandictionary.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class Urban(
    @PrimaryKey @SerializedName("defid") val id: Int,
    var tag: String,
    @SerializedName("word") val word: String,
    @SerializedName("definition") val definition: String,
    @SerializedName("example") val example: String,
    @SerializedName("author") val author: String,
    @SerializedName("thumbs_up") val thumbsUp: Int,
    @SerializedName("thumbs_down") val thumbsDown: Int
// TODO add date and URLs?
)