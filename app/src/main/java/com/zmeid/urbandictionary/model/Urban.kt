package com.zmeid.urbandictionary.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class Urban(
    @PrimaryKey @SerializedName("defid") val id: Int,
    var tag: String, // We need to insert words to database with a tag. Tag is needed in order to query definitions and simulate api search algorithm. Word variable is not enough. For Ex: try searching "e-", "E&E" will also be in results.
    @SerializedName("word") val word: String,
    @SerializedName("definition") val definition: String,
    @SerializedName("example") val example: String,
    @SerializedName("author") val author: String,
    @SerializedName("thumbs_up") val thumbsUp: Int,
    @SerializedName("thumbs_down") val thumbsDown: Int,
    var soundUrl: String?
) {
    @Ignore // @Ignore is not accepted by room when used in constructor
    @SerializedName("sound_urls")
    var soundUrls: List<String>? = null
}